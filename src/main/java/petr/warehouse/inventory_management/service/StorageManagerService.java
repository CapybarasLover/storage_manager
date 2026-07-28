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
            return "Error: couldn't create new storage";
        }
        return "Storage " + storageName + " created!";
    }

    public String addProduct(String storageId, String itemName) {
        StorageItem newItem = new StorageItem(itemName, storageRepo.getReferenceById(Long.parseLong(storageId)));
        try{
            itemRepo.save(newItem);
        } catch (Exception e){
            return "Error: couldn't create new item";
        }
        return "Item " + itemName + " created!";
    }

    public String executeOperation(String operationType, String productName, String count) {
        if(operationType.equals("поступление")){
            operationService.opAdmission(productName, count);
        } else if(operationType.equals("продажа")){
            operationService.opSell(productName, count);
        } else if(operationType.equals("списание")){
            operationService.opWrightOff(productName, count);
        }

        return "операция выполнена!";
    }
}
