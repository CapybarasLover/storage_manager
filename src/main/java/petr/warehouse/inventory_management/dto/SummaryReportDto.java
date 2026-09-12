package petr.warehouse.inventory_management.dto;

import java.math.BigDecimal;
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
            BigDecimal spending, BigDecimal revenue,
            BigDecimal profit
    ) {}

    public record ProductStats(
            int admissionsCount, int admissionsTotal,
            int sellsCount,      int sellsTotal,
            int writeOffsCount,  int writeOffsTotal,
            BigDecimal productSpending, BigDecimal productRevenue,
            BigDecimal productProfit
    ) {}
}
