package petr.warehouse.inventory_management.mapper;

import org.springframework.stereotype.Component;
import petr.warehouse.inventory_management.dto.StorageDto;
import petr.warehouse.inventory_management.dto.StorageItemDto;
import petr.warehouse.inventory_management.model.Storage;
import petr.warehouse.inventory_management.model.StorageItem;

import java.util.ArrayList;
import java.util.List;

@Component
public class StorageMapper {
    public StorageDto toDto(Storage storage){
        if(storage == null){
            return null;
        }

        StorageDto dto = new StorageDto();

        dto.setName(storage.getName());
        List<StorageItemDto> itemListDto = new ArrayList<>();

        List<StorageItem> storageItemList = storage.getItems();

        for(var item : storageItemList){
            itemListDto.add(StorageItemMapper.toDto(item));
        }
        dto.setStorageItemListDto(itemListDto);

        return dto;
    }

    public Storage toStorage(StorageDto dto){
        if(dto == null){
            return null;
        }

        Storage storage = new Storage();

        storage.setName(dto.getName());

        List<StorageItemDto> itemListDto = dto.getStorageItemListDto();

        List<StorageItem> storageItemList = new ArrayList<>();

        for(var dtoItem : itemListDto){
            storageItemList.add(StorageItemMapper.toStorageItem(dtoItem));
        }

        storage.setItems(storageItemList);

        return storage;
    }
}
