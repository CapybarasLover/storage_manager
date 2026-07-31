package petr.warehouse.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petr.warehouse.inventory_management.service.api.StorageDto;
import petr.warehouse.inventory_management.service.api.StorageMapper;
import petr.warehouse.inventory_management.service.db.StorageItemRepo;
import petr.warehouse.inventory_management.service.db.entity.Storage;
import petr.warehouse.inventory_management.service.db.StorageRepo;
import petr.warehouse.inventory_management.service.db.entity.StorageItem;

import java.util.Optional;
import java.util.OptionalInt;

@Service
public class StorageManagerService {
    @Autowired
    StorageRepo storageRepo;

    @Autowired
    StorageItemRepo itemRepo;

    @Autowired
    StorageMapper storageMapper;

    @Autowired
    OperationService operationService;

    public StorageDto getStorageById(Long id){
        Optional<Storage> storageOptional = storageRepo.findById(id);

        Storage storage;

        if(storageOptional.isPresent()){
            storage = storageOptional.get();
        }
        else {
            storage = null;
        }

        return storageMapper.toDto(storage);
    }

    public String createStorage(String storageName) {
        Storage storage = new Storage();
        storage.setName(storageName);

        try{
            storageRepo.save(storage);
        } catch (Exception e){
            return "Error: couldn't create new storage " + e.getMessage();
        }
        return "Storage " + storageName + " created!";
    }

    public String addProduct(Long storageId, String itemName) {
        StorageItem newItem = new StorageItem(itemName, storageRepo.getReferenceById(storageId));
        try{
            itemRepo.save(newItem);
        } catch (Exception e){
            return "Error: couldn't create new item " + e.getMessage();
        }
        return "Item " + itemName + " created!";
    }

    public String executeOperation(Long storageId, String operationType, String productName, Integer count) {
        if(operationType.equals("поступление")){
            return operationService.opAdmission(storageId, productName, count);
        } else if(operationType.equals("продажа")){
            return operationService.opSell(storageId, productName, count);
        } else if(operationType.equals("списание")){
            return operationService.opWrightOff(storageId, productName, count);
        } else {
            return "Неизвестная операция!";
        }
    }

    public String deleteProduct(Long storageId, String productName) {
        try{
            itemRepo.deleteByItemNameAndStorageId(productName, storageId);
        } catch (Exception e) {
            return "Невозможно удалить продукт " + productName + ": " + e.getMessage();
        }

        return "Продукт " + productName + " удален!";
    }
}
