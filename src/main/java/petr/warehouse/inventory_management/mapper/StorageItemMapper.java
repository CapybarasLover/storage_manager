package petr.warehouse.inventory_management.mapper;

import org.springframework.stereotype.Component;
import petr.warehouse.inventory_management.dto.StorageItemDto;
import petr.warehouse.inventory_management.model.StorageItem;

@Component
public class StorageItemMapper {
    static public StorageItem toStorageItem(StorageItemDto dto){
        if(dto == null){
            return null;
        }
        StorageItem storageItem = new StorageItem();

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

        dto.setCount(storageItem.getItemCount());
        dto.setName(storageItem.getItemName());
        dto.setStatus(storageItem.getItemStatus());

        return dto;
    }
}
