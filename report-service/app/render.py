"""Рендер SummaryReport в PDF."""

from decimal import ROUND_HALF_UP, Decimal
from io import BytesIO

from reportlab.lib import colors
from reportlab.lib.enums import TA_RIGHT
from reportlab.lib.pagesizes import A4, landscape
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib.units import mm
from reportlab.platypus import (
    KeepTogether,
    PageBreak,
    Paragraph,
    SimpleDocTemplate,
    Spacer,
    Table,
    TableStyle,
)

from .fonts import has_glyph, register_fonts
from .models import SummaryReport

FONT, FONT_BOLD = register_fonts()

NBSP = " "
MINUS = "−" if has_glyph(FONT, 0x2212) else "-"
# Если шрифт не знает знака рубля, пишем словом, а не квадратом.
CURRENCY = "₽" if has_glyph(FONT, 0x20BD) else "руб."

INK = colors.HexColor("#111111")
MUTED = colors.HexColor("#6b7280")
LINE = colors.HexColor("#e5e7eb")
ZEBRA = colors.HexColor("#fafafa")
POSITIVE = colors.HexColor("#15803d")
NEGATIVE = colors.HexColor("#b91c1c")

STATUS = {
    "OUT": ("Закончился", colors.HexColor("#b91c1c")),
    "FEW": ("Заканчивается", colors.HexColor("#b45309")),
    "ENOUGH": ("В наличии", colors.HexColor("#15803d")),
}

MONTHS = [
    "января", "февраля", "марта", "апреля", "мая", "июня",
    "июля", "августа", "сентября", "октября", "ноября", "декабря",
]


def money(value) -> str:
    if value is None:
        return "—"
    amount = Decimal(value).quantize(Decimal("0.01"), rounding=ROUND_HALF_UP)
    body = f"{abs(amount):,.2f}".replace(",", NBSP).replace(".", ",")
    return f"{MINUS if amount < 0 else ''}{body}{NBSP}{CURRENCY}"


def number(value: int) -> str:
    return f"{value:,}".replace(",", NBSP)


def ru_date(value) -> str:
    return f"{value.day} {MONTHS[value.month - 1]} {value.year}"


def ru_datetime(value) -> str:
    return f"{ru_date(value)}, {value:%H:%M}"


def _style(name: str, **kwargs) -> ParagraphStyle:
    base = {"fontName": FONT, "fontSize": 9, "leading": 12, "textColor": INK}
    base.update(kwargs)
    return ParagraphStyle(name, **base)


TITLE = _style("title", fontName=FONT_BOLD, fontSize=18, leading=22)
SUBTITLE = _style("subtitle", fontSize=10, leading=14, textColor=MUTED)
SECTION = _style("section", fontName=FONT_BOLD, fontSize=12, leading=16, spaceAfter=6)
CELL = _style("cell")
CELL_RIGHT = _style("cellRight", alignment=TA_RIGHT)
HEAD = _style("head", fontName=FONT_BOLD, fontSize=8, textColor=MUTED)
HEAD_RIGHT = _style("headRight", fontName=FONT_BOLD, fontSize=8, textColor=MUTED, alignment=TA_RIGHT)
FOOT = _style("foot", fontSize=8, textColor=MUTED)


def hex_of(color) -> str:
    """reportlab отдаёт hexval() как 0xrrggbb, а разметке абзаца нужен #rrggbb."""
    return "#" + color.hexval()[2:]


def _sign_color(value: Decimal):
    if value > 0:
        return POSITIVE
    if value < 0:
        return NEGATIVE
    return INK


def _kpi_card(label: str, value: str, hint: str, value_color=INK) -> Table:
    inner = Table(
        [
            [Paragraph(label.upper(), _style("kpiLabel", fontName=FONT_BOLD, fontSize=7, textColor=MUTED))],
            [Paragraph(value, _style("kpiValue", fontName=FONT_BOLD, fontSize=15, leading=19, textColor=value_color))],
            [Paragraph(hint, _style("kpiHint", fontSize=7, leading=9, textColor=MUTED))],
        ],
        colWidths=[76 * mm],
    )
    inner.setStyle(
        TableStyle(
            [
                ("LEFTPADDING", (0, 0), (-1, -1), 8),
                ("RIGHTPADDING", (0, 0), (-1, -1), 8),
                ("TOPPADDING", (0, 0), (0, 0), 8),
                ("BOTTOMPADDING", (0, -1), (0, -1), 8),
                ("TOPPADDING", (0, 1), (0, -1), 2),
                ("BOTTOMPADDING", (0, 0), (0, -2), 2),
                ("BOX", (0, 0), (-1, -1), 0.6, LINE),
                ("VALIGN", (0, 0), (-1, -1), "TOP"),
            ]
        )
    )
    return inner


def _cards_row(cards: list[Table], width: float) -> Table:
    row = Table([cards], colWidths=[width / len(cards)] * len(cards))
    row.setStyle(
        TableStyle(
            [
                ("LEFTPADDING", (0, 0), (-1, -1), 0),
                ("RIGHTPADDING", (0, 0), (-1, -1), 4),
                ("TOPPADDING", (0, 0), (-1, -1), 0),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 0),
                ("VALIGN", (0, 0), (-1, -1), "TOP"),
            ]
        )
    )
    return row


def _data_table(header: list, rows: list[list], col_widths: list[float], right_from: int) -> Table:
    table = Table([header] + rows, colWidths=col_widths, repeatRows=1)
    style = [
        ("FONTNAME", (0, 0), (-1, -1), FONT),
        ("FONTSIZE", (0, 0), (-1, -1), 9),
        ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
        ("ALIGN", (right_from, 0), (-1, -1), "RIGHT"),
        ("LINEBELOW", (0, 0), (-1, 0), 0.6, LINE),
        ("LINEBELOW", (0, 1), (-1, -2), 0.3, LINE),
        ("TOPPADDING", (0, 0), (-1, -1), 5),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 5),
        ("LEFTPADDING", (0, 0), (-1, -1), 4),
        ("RIGHTPADDING", (0, 0), (-1, -1), 4),
    ]
    for index in range(1, len(rows) + 1):
        if index % 2 == 0:
            style.append(("BACKGROUND", (0, index), (-1, index), ZEBRA))
    table.setStyle(TableStyle(style))
    return table


def _ops_cell(ops: int, total: int) -> Paragraph:
    if not ops:
        return Paragraph("—", CELL_RIGHT)
    return Paragraph(
        f'{number(ops)} <font color="#6b7280">/ {number(total)}{NBSP}шт.</font>', CELL_RIGHT
    )


def _footer(canvas, doc):
    canvas.saveState()
    canvas.setFont(FONT, 8)
    canvas.setFillColor(MUTED)
    canvas.drawRightString(doc.pagesize[0] - 15 * mm, 10 * mm, f"стр. {doc.page}")
    canvas.drawString(15 * mm, 10 * mm, "Складской учёт — сводный отчёт")
    canvas.restoreState()


def render_report(report: SummaryReport) -> bytes:
    buffer = BytesIO()
    doc = SimpleDocTemplate(
        buffer,
        pagesize=landscape(A4),
        leftMargin=15 * mm,
        rightMargin=15 * mm,
        topMargin=14 * mm,
        bottomMargin=16 * mm,
        title=f"Отчёт — {report.storageName}",
        author="Складской учёт",
    )
    width = doc.width
    stats = report.storageStats
    story = []

    story.append(Paragraph(f"Отчёт по складу «{report.storageName}»", TITLE))
    story.append(
        Paragraph(
            f"Период: {ru_date(report.dateFrom)} — {ru_date(report.dateTo)}"
            f"{NBSP}·{NBSP}сформирован {ru_datetime(report.generatedAt)}",
            SUBTITLE,
        )
    )
    story.append(Spacer(1, 8 * mm))

    story.append(
        _cards_row(
            [
                _kpi_card("Закупки", money(stats.spending), "Сумма поступлений за период"),
                _kpi_card("Выручка", money(stats.revenue), "Сумма продаж за период"),
                _kpi_card(
                    "Результат",
                    money(stats.profit),
                    "Продажи − Закупки за период",
                    _sign_color(stats.profit),
                ),
            ],
            width,
        )
    )
    story.append(Spacer(1, 4 * mm))
    story.append(
        _cards_row(
            [
                _kpi_card("Поступления", number(stats.admissionsCount), f"{number(stats.admissionsTotal)} шт. принято"),
                _kpi_card("Продажи", number(stats.sellsCount), f"{number(stats.sellsTotal)} шт. продано"),
                _kpi_card("Списания", number(stats.writeOffsCount), f"{number(stats.writeOffsTotal)} шт. списано"),
            ],
            width,
        )
    )
    story.append(Spacer(1, 9 * mm))

    story.append(Paragraph("По товарам", SECTION))
    if report.productStats:
        header = [
            Paragraph("Товар", HEAD),
            Paragraph("Поступления", HEAD_RIGHT),
            Paragraph("Продажи", HEAD_RIGHT),
            Paragraph("Списания", HEAD_RIGHT),
            Paragraph("Затраты", HEAD_RIGHT),
            Paragraph("Выручка", HEAD_RIGHT),
            Paragraph("Результат", HEAD_RIGHT),
        ]
        rows = []
        ordered = sorted(report.productStats.items(), key=lambda kv: kv[1].productRevenue, reverse=True)
        for name, product in ordered:
            rows.append(
                [
                    Paragraph(name, CELL),
                    _ops_cell(product.admissionsCount, product.admissionsTotal),
                    _ops_cell(product.sellsCount, product.sellsTotal),
                    _ops_cell(product.writeOffsCount, product.writeOffsTotal),
                    Paragraph(money(product.productSpending), CELL_RIGHT),
                    Paragraph(money(product.productRevenue), CELL_RIGHT),
                    Paragraph(
                        f'<font color="{hex_of(_sign_color(product.productProfit))}">'
                        f"{money(product.productProfit)}</font>",
                        CELL_RIGHT,
                    ),
                ]
            )
        shares = [0.22, 0.13, 0.13, 0.13, 0.13, 0.13, 0.13]
        story.append(_data_table(header, rows, [width * s for s in shares], 1))
    else:
        story.append(Paragraph("За период операций не было.", _style("empty", textColor=MUTED)))

    story.append(Spacer(1, 9 * mm))

    stock_block = [Paragraph("Остатки на момент формирования", SECTION)]
    if report.currentStock:
        header = [
            Paragraph("Товар", HEAD),
            Paragraph("Остаток", HEAD_RIGHT),
            Paragraph("Статус", HEAD),
            Paragraph("Цена за ед.", HEAD_RIGHT),
            Paragraph("Стоимость остатка", HEAD_RIGHT),
        ]
        rows = []
        for item in sorted(report.currentStock, key=lambda i: i.name):
            label, color = STATUS[item.status]
            total = None if item.cost is None else item.cost * item.count
            rows.append(
                [
                    Paragraph(item.name, CELL),
                    Paragraph(number(item.count), CELL_RIGHT),
                    Paragraph(f'<font color="{hex_of(color)}">{label}</font>', CELL),
                    Paragraph(money(item.cost), CELL_RIGHT),
                    Paragraph(money(total), CELL_RIGHT),
                ]
            )
        shares = [0.34, 0.14, 0.18, 0.17, 0.17]
        stock_block.append(_data_table(header, rows, [width * s for s in shares], 1))
    else:
        stock_block.append(Paragraph("На складе нет позиций.", _style("empty2", textColor=MUTED)))
    story.append(KeepTogether(stock_block))

    doc.build(story, onFirstPage=_footer, onLaterPages=_footer)
    return buffer.getvalue()


__all__ = ["render_report", "FONT", "PageBreak"]
