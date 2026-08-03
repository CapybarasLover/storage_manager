package petr.warehouse.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public String opAdmission(Long storageId, String productName, Integer count){
        StorageItem item = itemRepo.getReferenceByItemNameAndStorageId(productName, storageId);
        item.addCount(count);

        Operation operation = new Operation(item.getStorage().getName(),
                OperationType.ADMISSION,
                productName,
                count,
                LocalDateTime.now()
                );
        opRepo.save(operation);
        return "Операция выполнена!";
    }

    public String opSell(Long storageId, String productName, Integer count) {
        StorageItem item = itemRepo.getReferenceByItemNameAndStorageId(productName, storageId);
        item.minusCount(count);
        itemRepo.save(item);
        Operation operation = new Operation(item.getStorage().getName(),
                OperationType.SELL,
                productName,
                count,
                LocalDateTime.now()
        );
        opRepo.save(operation);
        return "Операция выполнена!";
    }

    public String opWrightOff(Long storageId, String productName, Integer count) {
        StorageItem item = itemRepo.getReferenceByItemNameAndStorageId(productName, storageId);
        item.minusCount(count);
        itemRepo.save(item);
        Operation operation = new Operation(item.getStorage().getName(),
                OperationType.WRIGHT_OFF,
                productName,
                count,
                LocalDateTime.now()
        );
        opRepo.save(operation);
        return "Операция выполнена!";
    }
}
