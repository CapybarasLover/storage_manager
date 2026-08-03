package petr.warehouse.inventory_management.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StorageDto {
    private String name;
    private List<StorageItemDto> storageItemListDto;
}
