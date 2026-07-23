package petr.warehouse.inventory_management.service.api;

import org.springframework.stereotype.Component;
import petr.warehouse.inventory_management.service.entity.Storage;

@Component
public class StorageMapper {
    public StorageDto toDto(Storage storage){
        if(storage == null){
            return null;
        }

        StorageDto dto = new StorageDto();

        dto.setName(storage.getName());
        dto.setStorageItemList(storage.getItems());

        return dto;
    }

    public Storage toStorage(StorageDto dto){
        if(dto == null){
            return null;
        }

        Storage storage = new Storage();

        storage.setName(dto.getName());
        storage.setItems(dto.getStorageItemList());

        return storage;
    }
}
