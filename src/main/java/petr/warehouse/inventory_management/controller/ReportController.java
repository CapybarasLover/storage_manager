package petr.warehouse.inventory_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import petr.warehouse.inventory_management.dto.SummaryReportDto;
import petr.warehouse.inventory_management.service.ReportPdfClient;
import petr.warehouse.inventory_management.service.ReportService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;
    private final ReportPdfClient reportPdfClient;

    @Autowired
    public ReportController(ReportService reportService, ReportPdfClient reportPdfClient){
        this.reportService = reportService;
        this.reportPdfClient = reportPdfClient;
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryReportDto> getReportSummary(
            @RequestParam String storageName,
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo
    ){
        return ResponseEntity.ok(reportService.createNewReport(storageName, dateFrom, dateTo));
    }

    //Тот же отчёт, но отрисованный в PDF отдельным python-сервисом.
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getReportPdf(
            @RequestParam String storageName,
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo
    ){
        SummaryReportDto summary = reportService.createNewReport(storageName, dateFrom, dateTo);
        byte[] pdf = reportPdfClient.render(summary);

        //Имя склада кириллическое, поэтому filename* в кодировке UTF-8.
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename("report-%s-%s-%s.pdf".formatted(storageName, dateFrom, dateTo), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }
}
