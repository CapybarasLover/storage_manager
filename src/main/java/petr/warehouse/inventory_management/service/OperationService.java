package petr.warehouse.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petr.warehouse.inventory_management.service.db.OperationRepo;
import petr.warehouse.inventory_management.service.db.StorageItemRepo;
import petr.warehouse.inventory_management.service.db.entity.Operation;
import petr.warehouse.inventory_management.service.db.entity.OperationType;
import petr.warehouse.inventory_management.service.db.entity.StorageItem;

import java.time.LocalDateTime;

//Класс для работы с операциями
@Service
public class OperationService {
    @Autowired
    OperationRepo opRepo;

    @Autowired
    StorageItemRepo itemRepo;

    public String opAdmission(String productName, String count){
        Integer countInt = Integer.parseInt(count);

        try{
            StorageItem item = itemRepo.getReferenceByItemName(productName);
            item.addCount(countInt);
            itemRepo.save(item);
            Operation operation = new Operation(item.getStorage().getName(),
                    OperationType.ADMISSION,
                    productName,
                    countInt,
                    LocalDateTime.now()
                    );
            opRepo.save(operation);
            return "Операция выполнена!";
        } catch (Exception e){
            return "Не удалось выполнить операцию!";
        }
    }

    public String opSell(String productName, String count) {
        Integer countInt = Integer.parseInt(count);

        try{
            StorageItem item = itemRepo.getReferenceByItemName(productName);
            item.minusCount(countInt);
            itemRepo.save(item);
            Operation operation = new Operation(item.getStorage().getName(),
                    OperationType.SELL,
                    productName,
                    countInt,
                    LocalDateTime.now()
            );
            opRepo.save(operation);
            return "Операция выполнена!";
        } catch (Exception e) {
            return "Не удалось выполнить операцию!";
        }
    }

    public String opWrightOff(String productName, String count) {
        Integer countInt = Integer.parseInt(count);

        try{
            StorageItem item = itemRepo.getReferenceByItemName(productName);
            item.minusCount(countInt);
            itemRepo.save(item);
            Operation operation = new Operation(item.getStorage().getName(),
                    OperationType.WRIGHT_OFF,
                    productName,
                    countInt,
                    LocalDateTime.now()
            );
            opRepo.save(operation);
            return "Операция выполнена!";
        } catch (Exception e) {
            return "Не удалось выполнить операцию!";
        }
    }
}
