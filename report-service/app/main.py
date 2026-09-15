"""Микросервис формирования PDF по сводному отчёту склада.

Наружу не торчит: его дёргает Java-бэкенд, который сам собирает
SummaryReportDto и отдаёт готовый файл клиенту.
"""

import logging

from fastapi import FastAPI, Request
from fastapi.encoders import jsonable_encoder
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse, Response

from .models import SummaryReport
from .render import FONT, render_report

logger = logging.getLogger("report-service")

app = FastAPI(
    title="Warehouse report renderer",
    description="Принимает SummaryReportDto и возвращает PDF.",
    version="1.0.0",
)


@app.exception_handler(RequestValidationError)
async def on_validation_error(request: Request, exc: RequestValidationError) -> JSONResponse:
    """Без этого причина 422 видна только клиенту, а не в логах сервиса."""
    logger.warning("Некорректное тело запроса: %s", exc.errors())
    return JSONResponse(status_code=422, content={"detail": jsonable_encoder(exc.errors())})


@app.get("/health")
def health() -> dict:
    return {"status": "ok", "font": FONT}


@app.post(
    "/render",
    responses={200: {"content": {"application/pdf": {}}, "description": "PDF-отчёт"}},
)
def render(report: SummaryReport) -> Response:
    pdf = render_report(report)
    logger.info(
        "Отчёт «%s» за %s—%s: %d позиций, %d байт",
        report.storageName,
        report.dateFrom,
        report.dateTo,
        len(report.productStats),
        len(pdf),
    )
    return Response(content=pdf, media_type="application/pdf")
