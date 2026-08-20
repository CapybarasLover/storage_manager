package petr.warehouse.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petr.warehouse.inventory_management.dto.OperationRequestDto;
import petr.warehouse.inventory_management.dto.StorageDto;
import petr.warehouse.inventory_management.exception.DataExceptions.StorageNotFoundException;
import petr.warehouse.inventory_management.mapper.StorageMapper;
import petr.warehouse.inventory_management.model.OperationType;
import petr.warehouse.inventory_management.repository.StorageItemRepo;
import petr.warehouse.inventory_management.model.Storage;
import petr.warehouse.inventory_management.repository.StorageRepo;
import petr.warehouse.inventory_management.model.StorageItem;

import java.util.Optional;

//TODO проверить класс на транзакции, если требуются - добавить @Transactional

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

        storage = storageOptional.orElse(null);

        if(storage == null){
            throw new StorageNotFoundException("Хранилище не найдено!");
        }

        return storageMapper.toDto(storage);
    }

    public Long createStorage(String storageName) {
        Storage storage = new Storage();
        storage.setName(storageName);

        try{
            storageRepo.save(storage);
        } catch (Exception e){
            throw new StorageNotFoundException("Хранилище не найдено!");
        }

        return storage.getId();
    }

    //DOTO Бросать ошибку а не возвращать строку
    public String addProduct(Long storageId, String itemName) {
        StorageItem newItem = new StorageItem(itemName, storageRepo.getReferenceById(storageId));
        try{
            itemRepo.save(newItem);
        } catch (Exception e){
            return "Error: couldn't create new item " + e.getMessage();
        }
        return "Item " + itemName + " created!";
    }

    public String executeOperation(Long storageId, OperationRequestDto requestBody) {
        return switch (requestBody.getOperationType()) {
            case ADMISSION -> operationService.opAdmission(storageId, requestBody);
            case SELL -> operationService.opSell(storageId, requestBody);
            case WRITE_OFF -> operationService.opWriteOff(storageId, requestBody);
        };
    }

    //DOTO Бросать ошибку а не возвращать строку
    public String deleteProduct(Long storageId, Long productId) {
        try{
            itemRepo.deleteByIdAndStorageId(productId, storageId);
        } catch (Exception e) {
            return "Невозможно удалить продукт " + productId + ": " + e.getMessage();
        }

        return "Продукт " + productId + " удален!";
    }
}
