package petr.warehouse.inventory_management.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import petr.warehouse.inventory_management.dto.OperationRequestDto;
import petr.warehouse.inventory_management.repository.OperationRepo;
import petr.warehouse.inventory_management.repository.StorageItemRepo;
import petr.warehouse.inventory_management.model.Operation;
import petr.warehouse.inventory_management.model.OperationType;
import petr.warehouse.inventory_management.model.StorageItem;

import java.time.LocalDateTime;

//Класс для работы с операциями
@Service
@Transactional
public class OperationService {
    @Autowired
    OperationRepo opRepo;

    @Autowired
    StorageItemRepo itemRepo;

    public String opAdmission(Long storageId, OperationRequestDto requestBody){
        StorageItem item = itemRepo.getReferenceByItemNameAndStorageId(requestBody.getProductName(), storageId);
        if(item == null){
            throw new EntityNotFoundException("Не удалось обновить продукт тк не его нет в базе данных!");
        }
        item.addCount(requestBody.getCount());
        itemRepo.save(item);

        Operation operation = new Operation(item.getStorage().getName(),
                OperationType.ADMISSION,
                requestBody.getProductName(),
                requestBody.getCount(),
                LocalDateTime.now(),
                requestBody.getComment()
                );
        opRepo.save(operation);
        return "Операция выполнена!";
    }

    public String opSell(Long storageId, OperationRequestDto requestBody) {
        StorageItem item = itemRepo.getReferenceByItemNameAndStorageId(requestBody.getProductName(), storageId);
        if(item == null){
            throw new EntityNotFoundException("Не удалось обновить продукт тк не его нет в базе данных!");
        }
        //TODO проверки на отрицательность
        item.minusCount(requestBody.getCount());
        itemRepo.save(item);

        Operation operation = new Operation(item.getStorage().getName(),
                OperationType.SELL,
                requestBody.getProductName(),
                requestBody.getCount(),
                LocalDateTime.now(),
                requestBody.getComment()
        );
        opRepo.save(operation);
        return "Операция выполнена!";
    }

    public String opWriteOff(Long storageId, OperationRequestDto requestBody) {
        StorageItem item = itemRepo.getReferenceByItemNameAndStorageId(requestBody.getProductName(), storageId);
        if(item == null){
            throw new EntityNotFoundException("Не удалось обновить продукт тк не его нет в базе данных!");
        }
        //TODO проверки на отрицательность
        item.minusCount(requestBody.getCount());
        itemRepo.save(item);

        Operation operation = new Operation(item.getStorage().getName(),
                OperationType.WRITE_OFF,
                requestBody.getProductName(),
                requestBody.getCount(),
                LocalDateTime.now(),
                requestBody.getComment()
        );
        opRepo.save(operation);
        return "Операция выполнена!";
    }
}
