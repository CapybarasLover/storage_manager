package petr.warehouse.inventory_management.service.api;

import petr.warehouse.inventory_management.service.entity.StorageItem;

public class StorageItemMapper {
    static public StorageItem toStorageItem(StorageItemDto dto){
        StorageItem storageItem = new StorageItem();

        storageItem.setStorage(dto.getStorage());
        storageItem.setItemCount(dto.getCount());
        storageItem.setItemName(dto.getName());
        storageItem.setItemStatus(dto.getStatus());

        return storageItem;
    }

    static public StorageItemDto toDto(StorageItem storageItem){
        StorageItemDto dto = new StorageItemDto();

        dto.setStorage(storageItem.getStorage());
        dto.setCount(storageItem.getItemCount());
        dto.setName(storageItem.getItemName());
        dto.setStatus(storageItem.getItemStatus());

        return dto;
    }
}
