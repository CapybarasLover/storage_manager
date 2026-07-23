package petr.warehouse.inventory_management.service.api;

import org.springframework.stereotype.Component;
import petr.warehouse.inventory_management.service.entity.StorageItem;

@Component
public class StorageItemMapper {
    static public StorageItem toStorageItem(StorageItemDto dto){
        if(dto == null){
            return null;
        }
        StorageItem storageItem = new StorageItem();

        storageItem.setStorage(dto.getStorage());
        storageItem.setItemCount(dto.getCount());
        storageItem.setItemName(dto.getName());
        storageItem.setItemStatus(dto.getStatus());

        return storageItem;
    }

    static public StorageItemDto toDto(StorageItem storageItem){
        if(storageItem == null){
            return null;
        }

        StorageItemDto dto = new StorageItemDto();

        dto.setStorage(storageItem.getStorage());
        dto.setCount(storageItem.getItemCount());
        dto.setName(storageItem.getItemName());
        dto.setStatus(storageItem.getItemStatus());

        return dto;
    }
}
