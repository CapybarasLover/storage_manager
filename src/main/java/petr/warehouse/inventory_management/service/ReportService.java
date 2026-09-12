package petr.warehouse.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petr.warehouse.inventory_management.dto.StorageItemDto;
import petr.warehouse.inventory_management.dto.SummaryReportDto;
import petr.warehouse.inventory_management.mapper.StorageItemMapper;
import petr.warehouse.inventory_management.model.OperationType;
import petr.warehouse.inventory_management.repository.OperationRepo;
import petr.warehouse.inventory_management.repository.StorageItemRepo;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired OperationRepo operationRepo;
    @Autowired StorageItemRepo storageItemRepo;

    public SummaryReportDto createNewReport(String storageName, LocalDate dateFrom, LocalDate dateTo) {
        ZoneId zone = ZoneId.of("Europe/Moscow");
        Instant from = dateFrom.atStartOfDay(zone).toInstant();
        Instant to = dateTo.plusDays(1).atStartOfDay(zone).toInstant();

        //TODO добавить работу с ценой операций и выручку (плюс возврат потраченных средств spending)
        List<Object> rawRows = operationRepo.groupOperationsForReport(storageName, from, to);

        if (rawRows.isEmpty()) {
            throw new RuntimeException("No operations found for the given period");
        }

        Map<String, SummaryReportDto.ProductStats> productStats = new HashMap<>();
        int totalAdmCount = 0;
        int totalAdmTotal = 0;
        int totalSellCount = 0;
        int totalSellTotal = 0;
        int totalWoCount  = 0;
        int totalWoTotal  = 0;


        for (Object row : rawRows) {
            Object[] cols = (Object[]) row;
            String productName = (String) cols[0];
            OperationType opType = (OperationType) cols[1];
            int ops = ((Long) cols[2]).intValue();
            int total = ((Long) cols[3]).intValue();

            SummaryReportDto.ProductStats prev = productStats.getOrDefault(
                    productName, new SummaryReportDto.ProductStats(
                            0,
                            0,
                            0,
                            0,
                            0,
                            0
                    ));
            SummaryReportDto.ProductStats updated = switch (opType) {
                case ADMISSION -> new SummaryReportDto.ProductStats(
                        prev.admissionsCount() + ops, prev.admissionsTotal() + total,
                        prev.sellsCount(), prev.sellsTotal(),
                        prev.writeOffsCount(), prev.writeOffsTotal());
                case SELL -> new SummaryReportDto.ProductStats(
                        prev.admissionsCount(), prev.admissionsTotal(),
                        prev.sellsCount() + ops, prev.sellsTotal() + total,
                        prev.writeOffsCount(), prev.writeOffsTotal());
                case WRITE_OFF -> new SummaryReportDto.ProductStats(
                        prev.admissionsCount(), prev.admissionsTotal(),
                        prev.sellsCount(), prev.sellsTotal(),
                        prev.writeOffsCount() + ops, prev.writeOffsTotal() + total);
            };
            productStats.put(productName, updated);

            switch (opType) {
                case ADMISSION -> { totalAdmCount  += ops; totalAdmTotal  += total; }
                case SELL      -> { totalSellCount += ops; totalSellTotal += total; }
                case WRITE_OFF -> { totalWoCount   += ops; totalWoTotal   += total; }
            }
        }

        SummaryReportDto.Stats stats = new SummaryReportDto.Stats(
                totalAdmCount,  totalAdmTotal,
                totalSellCount, totalSellTotal,
                totalWoCount,   totalWoTotal
        );

        List<StorageItemDto> currentStock = storageItemRepo
                .findAllByStorage_Name(storageName)
                .stream()
                .map(StorageItemMapper::toDto)
                .toList();

        return new SummaryReportDto(storageName, dateFrom, dateTo, Instant.now(), currentStock, stats, productStats);
    }
}
