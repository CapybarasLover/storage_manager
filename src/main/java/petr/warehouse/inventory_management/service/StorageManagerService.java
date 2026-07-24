package petr.warehouse.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petr.warehouse.inventory_management.service.api.StorageDto;
import petr.warehouse.inventory_management.service.api.StorageMapper;
import petr.warehouse.inventory_management.service.entity.Storage;
import petr.warehouse.inventory_management.service.entity.StorageRepo;

import java.util.Optional;

@Service
public class StorageManagerService {
    @Autowired
    StorageRepo storageRepo;

    @Autowired
    StorageMapper storageMapper;

    public StorageDto getStorageById(Long id){
        StorageDto storageDto = new StorageDto();

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
}
