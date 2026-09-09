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
        Stats stats,
        Map<String, ProductStats> productStats
) {
    public record Stats(
            int admissionsCount, int admissionsTotal,
            int sellsCount,      int sellsTotal,
            int writeOffsCount,  int writeOffsTotal,
            int spending, int productProfit
    ) {}

    public record ProductStats(
            int admissionsCount, int admissionsTotal,
            int sellsCount,      int sellsTotal,
            int writeOffsCount,  int writeOffsTotal,
            int productSpending, int productProfit
    ) {}
}
