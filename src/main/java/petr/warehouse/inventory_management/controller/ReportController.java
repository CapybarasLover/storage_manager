package petr.warehouse.inventory_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import petr.warehouse.inventory_management.dto.SummaryReportDto;
import petr.warehouse.inventory_management.service.ReportService;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    ReportService reportService;

    @GetMapping("/summary")
    public ResponseEntity<SummaryReportDto> getReportSummary(
            @RequestParam String storageName,
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo
    ){
        return ResponseEntity.ok(reportService.createNewReport(storageName, dateFrom, dateTo));
    }
}
