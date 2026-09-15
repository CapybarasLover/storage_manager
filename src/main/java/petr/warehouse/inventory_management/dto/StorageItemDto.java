package petr.warehouse.inventory_management.dto;

import lombok.Getter;
import lombok.Setter;
import petr.warehouse.inventory_management.model.ItemStatus;

import java.math.BigDecimal;

@Getter
@Setter
public class StorageItemDto {
    private Long id;
    private String name;
    private int count;
    private ItemStatus status;
    private BigDecimal cost;
}
