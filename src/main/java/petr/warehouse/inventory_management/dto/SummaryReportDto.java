package petr.warehouse.inventory_management.dto;

import lombok.Getter;
import lombok.Setter;
import petr.warehouse.inventory_management.service.ReportService;

import java.time.Instant;

@Getter
@Setter
public class SummaryReportDto {
    private Instant generatedDate;
    private Long storageId;
    private String storageName;
    private Instant dateFrom;
    private Instant dateTo;
}
