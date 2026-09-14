package petr.warehouse.inventory_management.controller;

import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import petr.warehouse.inventory_management.dto.SummaryReportDto;
import petr.warehouse.inventory_management.filter.ReportFilter;
import petr.warehouse.inventory_management.service.ReportService;

import java.time.LocalDate;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryReportDto> getReportSummary(
            @Valid @ParameterObject ReportFilter filter
    ) {
        return ResponseEntity.ok(
                reportService.createNewReport(filter.getStorageName(), filter.getDateFrom(), filter.getDateTo())
        );
    }
}
