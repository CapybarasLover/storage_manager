package petr.warehouse.inventory_management.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public record SummaryReportDto(
        String storageName,
        LocalDate dateFrom,
        LocalDate dateTo,
        Instant generatedAt,
        List<StorageItemDto> currentStock,
        Aggregates aggregates,
        Map<String, ProductStats> productStats
) {
    //TODO добавить поля для каждого продукта и общее: прибыль
    public record Aggregates(
            int admissionsCount, int admissionsTotal,
            int sellsCount,      int sellsTotal,
            int writeOffsCount,  int writeOffsTotal
    ) {}

    public record ProductStats(
            int admissionsCount, int admissionsTotal,
            int sellsCount,      int sellsTotal,
            int writeOffsCount,  int writeOffsTotal
    ) {}
}
