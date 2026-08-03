package petr.warehouse.inventory_management.dto;

import lombok.Getter;
import lombok.Setter;
import petr.warehouse.inventory_management.model.ItemStatus;

@Getter
@Setter
public class StorageItemDto {
    private String name;
    private int count;
    private ItemStatus status;
}
