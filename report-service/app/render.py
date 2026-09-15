"""Рендер SummaryReport в PDF."""

from decimal import ROUND_HALF_UP, Decimal
from io import BytesIO
from types import SimpleNamespace

from reportlab.lib.enums import TA_RIGHT
from reportlab.lib.pagesizes import A4, landscape
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib.units import mm
from reportlab.platypus import (
    KeepTogether,
    Paragraph,
    SimpleDocTemplate,
    Spacer,
    Table,
    TableStyle,
)

from .fonts import has_glyph, register_fonts
from .models import SummaryReport
from .theme import Palette, palette_for

FONT, FONT_BOLD = register_fonts()

NBSP = " "
MINUS = "−" if has_glyph(FONT, 0x2212) else "-"
# Если шрифт не знает знака рубля, пишем словом, а не квадратом.
CURRENCY = "₽" if has_glyph(FONT, 0x20BD) else "руб."

STATUS_LABEL = {
    "OUT": "Закончился",
    "FEW": "Заканчивается",
    "ENOUGH": "В наличии",
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


def hex_of(color) -> str:
    """reportlab отдаёт hexval() как 0xrrggbb, а разметке абзаца нужен #rrggbb."""
    return "#" + color.hexval()[2:]


def _styles(palette: Palette) -> SimpleNamespace:
    def style(name: str, **kwargs) -> ParagraphStyle:
        base = {"fontName": FONT, "fontSize": 9, "leading": 12, "textColor": palette.ink}
        base.update(kwargs)
        return ParagraphStyle(name, **base)

    return SimpleNamespace(
        title=style("title", fontName=FONT_BOLD, fontSize=18, leading=22),
        subtitle=style("subtitle", fontSize=10, leading=14, textColor=palette.muted),
        section=style("section", fontName=FONT_BOLD, fontSize=12, leading=16, spaceAfter=6),
        cell=style("cell"),
        cell_right=style("cellRight", alignment=TA_RIGHT),
        head=style("head", fontName=FONT_BOLD, fontSize=8, textColor=palette.muted),
        head_right=style("headRight", fontName=FONT_BOLD, fontSize=8, textColor=palette.muted, alignment=TA_RIGHT),
        empty=style("empty", textColor=palette.muted),
        kpi_label=style("kpiLabel", fontName=FONT_BOLD, fontSize=7, textColor=palette.muted),
        kpi_hint=style("kpiHint", fontSize=7, leading=9, textColor=palette.muted),
    )


def _kpi_card(palette: Palette, s: SimpleNamespace, label: str, value: str, hint: str, value_color=None) -> Table:
    value_style = ParagraphStyle(
        "kpiValue",
        fontName=FONT_BOLD,
        fontSize=15,
        leading=19,
        textColor=value_color or palette.ink,
    )
    inner = Table(
        [
            [Paragraph(label.upper(), s.kpi_label)],
            [Paragraph(value, value_style)],
            [Paragraph(hint, s.kpi_hint)],
        ],
        colWidths=[76 * mm],
    )
    inner.setStyle(
        TableStyle(
            [
                ("BACKGROUND", (0, 0), (-1, -1), palette.card),
                ("LEFTPADDING", (0, 0), (-1, -1), 8),
                ("RIGHTPADDING", (0, 0), (-1, -1), 8),
                ("TOPPADDING", (0, 0), (0, 0), 8),
                ("BOTTOMPADDING", (0, -1), (0, -1), 8),
                ("TOPPADDING", (0, 1), (0, -1), 2),
                ("BOTTOMPADDING", (0, 0), (0, -2), 2),
                ("BOX", (0, 0), (-1, -1), 0.6, palette.line),
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


def _data_table(palette: Palette, header: list, rows: list[list], col_widths: list[float], right_from: int) -> Table:
    table = Table([header] + rows, colWidths=col_widths, repeatRows=1)
    style = [
        ("FONTNAME", (0, 0), (-1, -1), FONT),
        ("FONTSIZE", (0, 0), (-1, -1), 9),
        ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
        ("ALIGN", (right_from, 0), (-1, -1), "RIGHT"),
        ("LINEBELOW", (0, 0), (-1, 0), 0.6, palette.line),
        ("LINEBELOW", (0, 1), (-1, -2), 0.3, palette.line),
        ("TOPPADDING", (0, 0), (-1, -1), 5),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 5),
        ("LEFTPADDING", (0, 0), (-1, -1), 4),
        ("RIGHTPADDING", (0, 0), (-1, -1), 4),
    ]
    for index in range(1, len(rows) + 1):
        if index % 2 == 0:
            style.append(("BACKGROUND", (0, index), (-1, index), palette.zebra))
    table.setStyle(TableStyle(style))
    return table


def _ops_cell(palette: Palette, s: SimpleNamespace, ops: int, total: int) -> Paragraph:
    if not ops:
        return Paragraph("—", s.cell_right)
    return Paragraph(
        f'{number(ops)} <font color="{hex_of(palette.muted)}">/ {number(total)}{NBSP}шт.</font>',
        s.cell_right,
    )


def _page_decorator(palette: Palette):
    def decorate(canvas, doc):
        width, height = doc.pagesize
        # Фон рисуется в начале страницы, поэтому содержимое ложится поверх.
        canvas.saveState()
        canvas.setFillColor(palette.page)
        canvas.rect(0, 0, width, height, stroke=0, fill=1)
        canvas.setFont(FONT, 8)
        canvas.setFillColor(palette.muted)
        canvas.drawRightString(width - 15 * mm, 10 * mm, f"стр. {doc.page}")
        canvas.drawString(15 * mm, 10 * mm, "Складской учёт — сводный отчёт")
        canvas.restoreState()

    return decorate


def render_report(report: SummaryReport, theme: str = "light") -> bytes:
    palette = palette_for(theme)
    s = _styles(palette)

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

    story.append(Paragraph(f"Отчёт по складу «{report.storageName}»", s.title))
    story.append(
        Paragraph(
            f"Период: {ru_date(report.dateFrom)} — {ru_date(report.dateTo)}"
            f"{NBSP}·{NBSP}сформирован {ru_datetime(report.generatedAt)}",
            s.subtitle,
        )
    )
    story.append(Spacer(1, 8 * mm))

    story.append(
        _cards_row(
            [
                _kpi_card(palette, s, "Закупки", money(stats.spending), "Сумма поступлений за период"),
                _kpi_card(palette, s, "Выручка", money(stats.revenue), "Сумма продаж за период"),
                _kpi_card(
                    palette, s, "Результат", money(stats.profit),
                    "Продажи − Закупки за период", palette.sign(stats.profit),
                ),
            ],
            width,
        )
    )
    story.append(Spacer(1, 4 * mm))
    story.append(
        _cards_row(
            [
                _kpi_card(palette, s, "Поступления", number(stats.admissionsCount), f"{number(stats.admissionsTotal)} шт. принято"),
                _kpi_card(palette, s, "Продажи", number(stats.sellsCount), f"{number(stats.sellsTotal)} шт. продано"),
                _kpi_card(palette, s, "Списания", number(stats.writeOffsCount), f"{number(stats.writeOffsTotal)} шт. списано"),
            ],
            width,
        )
    )
    story.append(Spacer(1, 9 * mm))

    story.append(Paragraph("По товарам", s.section))
    if report.productStats:
        header = [
            Paragraph("Товар", s.head),
            Paragraph("Поступления", s.head_right),
            Paragraph("Продажи", s.head_right),
            Paragraph("Списания", s.head_right),
            Paragraph("Затраты", s.head_right),
            Paragraph("Выручка", s.head_right),
            Paragraph("Результат", s.head_right),
        ]
        rows = []
        ordered = sorted(report.productStats.items(), key=lambda kv: kv[1].productRevenue, reverse=True)
        for name, product in ordered:
            rows.append(
                [
                    Paragraph(name, s.cell),
                    _ops_cell(palette, s, product.admissionsCount, product.admissionsTotal),
                    _ops_cell(palette, s, product.sellsCount, product.sellsTotal),
                    _ops_cell(palette, s, product.writeOffsCount, product.writeOffsTotal),
                    Paragraph(money(product.productSpending), s.cell_right),
                    Paragraph(money(product.productRevenue), s.cell_right),
                    Paragraph(
                        f'<font color="{hex_of(palette.sign(product.productProfit))}">'
                        f"{money(product.productProfit)}</font>",
                        s.cell_right,
                    ),
                ]
            )
        shares = [0.22, 0.13, 0.13, 0.13, 0.13, 0.13, 0.13]
        story.append(_data_table(palette, header, rows, [width * share for share in shares], 1))
    else:
        story.append(Paragraph("За период операций не было.", s.empty))

    story.append(Spacer(1, 9 * mm))

    stock_block = [Paragraph("Остатки на момент формирования", s.section)]
    if report.currentStock:
        header = [
            Paragraph("Товар", s.head),
            Paragraph("Остаток", s.head_right),
            Paragraph("Статус", s.head),
            Paragraph("Цена за ед.", s.head_right),
            Paragraph("Стоимость остатка", s.head_right),
        ]
        rows = []
        for item in sorted(report.currentStock, key=lambda i: i.name):
            total = None if item.cost is None else item.cost * item.count
            rows.append(
                [
                    Paragraph(item.name, s.cell),
                    Paragraph(number(item.count), s.cell_right),
                    Paragraph(
                        f'<font color="{hex_of(palette.status(item.status))}">'
                        f"{STATUS_LABEL[item.status]}</font>",
                        s.cell,
                    ),
                    Paragraph(money(item.cost), s.cell_right),
                    Paragraph(money(total), s.cell_right),
                ]
            )
        shares = [0.34, 0.14, 0.18, 0.17, 0.17]
        stock_block.append(_data_table(palette, header, rows, [width * share for share in shares], 1))
    else:
        stock_block.append(Paragraph("На складе нет позиций.", s.empty))
    story.append(KeepTogether(stock_block))

    decorate = _page_decorator(palette)
    doc.build(story, onFirstPage=decorate, onLaterPages=decorate)
    return buffer.getvalue()
