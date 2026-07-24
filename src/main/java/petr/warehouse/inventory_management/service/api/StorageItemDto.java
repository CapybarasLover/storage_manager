package petr.warehouse.inventory_management.service.api;

import lombok.Getter;
import lombok.Setter;
import petr.warehouse.inventory_management.service.entity.ItemStatus;
import petr.warehouse.inventory_management.service.entity.Storage;

@Getter
@Setter
public class StorageItemDto {
    private String name;
//    private Storage storage;
    private int count;
    private ItemStatus status;
}
