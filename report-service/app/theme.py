"""Палитры отчёта.

Повторяют цветовой язык интерфейса: светофор остатков и знак результата
означают в PDF то же самое, что на экране.
"""

from dataclasses import dataclass
from typing import Literal

from reportlab.lib import colors
from reportlab.lib.colors import Color

Theme = Literal["light", "dark"]


@dataclass(frozen=True)
class Palette:
    page: Color
    card: Color
    ink: Color
    muted: Color
    line: Color
    zebra: Color
    positive: Color
    negative: Color
    status_out: Color
    status_few: Color
    status_enough: Color

    def status(self, code: str) -> Color:
        return {
            "OUT": self.status_out,
            "FEW": self.status_few,
            "ENOUGH": self.status_enough,
        }[code]

    def sign(self, value) -> Color:
        if value > 0:
            return self.positive
        if value < 0:
            return self.negative
        return self.ink


LIGHT = Palette(
    page=colors.HexColor("#ffffff"),
    card=colors.HexColor("#ffffff"),
    ink=colors.HexColor("#111111"),
    muted=colors.HexColor("#6b7280"),
    line=colors.HexColor("#e5e7eb"),
    zebra=colors.HexColor("#fafafa"),
    positive=colors.HexColor("#15803d"),
    negative=colors.HexColor("#b91c1c"),
    status_out=colors.HexColor("#b91c1c"),
    status_few=colors.HexColor("#b45309"),
    status_enough=colors.HexColor("#15803d"),
)

DARK = Palette(
    page=colors.HexColor("#161616"),
    card=colors.HexColor("#1f1f1f"),
    ink=colors.HexColor("#f5f5f5"),
    muted=colors.HexColor("#a1a1aa"),
    line=colors.HexColor("#3a3a3a"),
    zebra=colors.HexColor("#1c1c1c"),
    positive=colors.HexColor("#4ade80"),
    negative=colors.HexColor("#f87171"),
    status_out=colors.HexColor("#f87171"),
    status_few=colors.HexColor("#fbbf24"),
    status_enough=colors.HexColor("#4ade80"),
)

PALETTES: dict[str, Palette] = {"light": LIGHT, "dark": DARK}


def palette_for(theme: str | None) -> Palette:
    return PALETTES.get((theme or "light").lower(), LIGHT)
