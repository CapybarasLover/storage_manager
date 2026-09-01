package petr.warehouse.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petr.warehouse.inventory_management.dto.OperationRequestDto;
import petr.warehouse.inventory_management.dto.StorageDto;
import petr.warehouse.inventory_management.dto.StorageInfoDto;
import petr.warehouse.inventory_management.exception.DataExceptions.ProductNotFoundException;
import petr.warehouse.inventory_management.exception.DataExceptions.StorageNotFoundException;
import petr.warehouse.inventory_management.mapper.StorageMapper;
import petr.warehouse.inventory_management.model.OperationType;
import petr.warehouse.inventory_management.repository.StorageItemRepo;
import petr.warehouse.inventory_management.model.Storage;
import petr.warehouse.inventory_management.repository.StorageRepo;
import petr.warehouse.inventory_management.model.StorageItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    public StorageDto getStorageById(Long storageId){
        Optional<Storage> storageOptional = storageRepo.findById(storageId);

        Storage storage = storageOptional.orElseThrow(
                () -> new StorageNotFoundException(
                        "404: Такой склад не найден!", storageId));

        return storageMapper.toDto(storage);
    }

    public Long createStorage(String storageName) {
        Storage storage = new Storage();
        storage.setName(storageName);

        storageRepo.save(storage);

        return storage.getId();
    }

    public String addProduct(Long storageId, String itemName) {
        Optional<Storage> storageOptional = storageRepo.findById(storageId);

        Storage storage = storageOptional.orElseThrow(
                () -> new StorageNotFoundException(
                        "404: Не удалось добавить продукт на склад тк такой склад не найден!"
                        , storageId));

        StorageItem newItem = new StorageItem(itemName, storage);
        itemRepo.save(newItem);

        return "Item " + itemName + " created!";
    }

    public String deleteProduct(Long storageId, Long productId) {
        int count = itemRepo.deleteByIdAndStorageId(productId, storageId);

        if(count > 0){
            return "Товар удален!";
        } else {
            throw new ProductNotFoundException("Товар не найден!", storageId, productId);
        }
    }

    public List<StorageInfoDto> getAllStorages() {
        List<StorageInfoDto> storageInfoDtos = new ArrayList<>();
        List<Storage> storageList = storageRepo.findAll();

        if(storageList.isEmpty()){
            return storageInfoDtos;
        }

        storageInfoDtos = storageList.stream().map(storge -> storageMapper.toInfoDto(storge)).toList();

        return storageInfoDtos;
    }
}
