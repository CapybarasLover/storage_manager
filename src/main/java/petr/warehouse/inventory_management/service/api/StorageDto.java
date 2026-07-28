package petr.warehouse.inventory_management.service.api;

import lombok.Getter;
import lombok.Setter;
import petr.warehouse.inventory_management.service.db.entity.StorageItem;

import java.util.List;

@Getter
@Setter
public class StorageDto {
    private String name;
    private List<StorageItemDto> storageItemListDto;
}
