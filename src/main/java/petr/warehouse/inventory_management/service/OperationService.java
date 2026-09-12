package petr.warehouse.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import petr.warehouse.inventory_management.dto.OperationDto;
import petr.warehouse.inventory_management.dto.OperationRequestDto;
import petr.warehouse.inventory_management.exception.dataExceptions.IllegalSellOrWriteOffCount;
import petr.warehouse.inventory_management.exception.dataExceptions.ProductNotFoundException;
import petr.warehouse.inventory_management.exception.requestException.ZeroOrNullAdmissionCost;
import petr.warehouse.inventory_management.filter.OperationFilter;
import petr.warehouse.inventory_management.mapper.OperationMapper;
import petr.warehouse.inventory_management.repository.OperationRepo;
import petr.warehouse.inventory_management.repository.StorageItemRepo;
import petr.warehouse.inventory_management.model.Operation;
import petr.warehouse.inventory_management.model.StorageItem;
import petr.warehouse.inventory_management.repository.specification.OperationSpecifications;

import java.math.BigDecimal;
import java.time.Instant;

//Класс для работы с операциями
@Service
@Transactional
public class OperationService {
    @Autowired
    OperationRepo opRepo;

    @Autowired
    StorageItemRepo itemRepo;

    @Autowired
    OperationMapper operationMapper;

    public void executeOperation(Long storageId, OperationRequestDto requestBody){
        StorageItem item = itemRepo.findByItemNameAndStorageId(requestBody.getProductName(), storageId)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Товар не найден!", storageId, requestBody.getProductName()));

        switch (requestBody.getOperationType()){
            case ADMISSION -> {
                if(requestBody.getOperationCost().equals(BigDecimal.ZERO) || requestBody.getOperationCost() == null){
                    throw new ZeroOrNullAdmissionCost("Пустое или нулевое значение цены поступления!");
                }

                item.addCount(requestBody.getCount());
                itemRepo.save(item);
                Operation admissionOperation = Operation.createAdmissionOperation(
                        item.getStorage().getName(),
                        requestBody.getOperationType(),
                        requestBody.getProductName(),
                        requestBody.getCount(),
                        Instant.now(),
                        requestBody.getComment(),
                        requestBody.getOperationCost()
                );

                opRepo.save(admissionOperation);
            }
            case SELL, WRITE_OFF -> {
                if(requestBody.getCount() > item.getItemCount()){
                    throw new IllegalSellOrWriteOffCount(
                            "Invalid argument.",
                            requestBody.getProductName(),
                            requestBody.getCount()
                    );
                }

                item.minusCount(requestBody.getCount());
                itemRepo.save(item);

                Operation sellOrWriteOffOperation = Operation.createSellOrWriteOffOperation(
                        item.getStorage().getName(),
                        requestBody.getOperationType(),
                        requestBody.getProductName(),
                        requestBody.getCount(),
                        Instant.now(),
                        requestBody.getComment(),
                        countOperationCost(requestBody.getCount(), item.getCost())
                );

                opRepo.save(sellOrWriteOffOperation);
            }
        }
    }

    public Page<OperationDto> getOperations(OperationFilter filter, Pageable pageable){
        Specification<Operation> specification = Specification
                .allOf(OperationSpecifications.hasStorageName(filter.getStorageName()))
                .and(OperationSpecifications.hasOperationType(filter.getOperationType()))
                .and(OperationSpecifications.hasProductName(filter.getProductName()))
                .and(OperationSpecifications.inDateRange(filter.getDateFrom(), filter.getDateTo()));

        return opRepo.findAll(specification, pageable).map(operation -> operationMapper.toDto(operation));
    }

    private BigDecimal countOperationCost(int unitsSold, BigDecimal unitCost){
        return unitCost.multiply(unitCost);
    }
}
