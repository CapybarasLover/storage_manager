package petr.warehouse.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petr.warehouse.inventory_management.dto.StorageItemDto;
import petr.warehouse.inventory_management.dto.SummaryReportDto;
import petr.warehouse.inventory_management.mapper.StorageItemMapper;
import petr.warehouse.inventory_management.model.StorageItem;
import petr.warehouse.inventory_management.repository.OperationRepo;
import petr.warehouse.inventory_management.repository.StorageItemRepo;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    OperationRepo operationRepo;

    @Autowired
    StorageItemRepo storageItemRepo;

    @Autowired
    StorageItemMapper itemMapper;

    //TODO Завершить метод
    public SummaryReportDto createNewReport(String storageName, LocalDate dateFrom, LocalDate dateTo) {
        SummaryReportDto reportDto = null;

        ZoneId zone = ZoneId.of("Europe/Moscow"); // совпадает с конфигом приложения

        Instant from = dateFrom.atStartOfDay(zone).toInstant();
        Instant to   = dateTo.plusDays(1).atStartOfDay(zone).toInstant();

        List<Object> listOfGroupedOperations = operationRepo.groupOperationsForReport(storageName, from, to);
        if (listOfGroupedOperations.isEmpty()) {
            //TODO Поменять на кастомное исключение!
            throw new RuntimeException("No such operation");
        }

        List<StorageItem> items = storageItemRepo.findAllByStorage_Name(storageName);
        List<StorageItemDto> currentStock = items.stream().map(StorageItemMapper::toDto).toList();

//        SummaryReportDto.Aggregates aggregates = new SummaryReportDto.Aggregates();

        Instant generatedTime = Instant.now();

//        reportDto = new SummaryReportDto(
//                storageName,
//                dateFrom,
//                dateTo,
//                generatedTime,
//                currentStock,
//
//                );
        return reportDto;
    }
}
