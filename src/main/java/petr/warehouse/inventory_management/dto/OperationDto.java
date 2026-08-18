package petr.warehouse.inventory_management.dto;

import lombok.Getter;
import lombok.Setter;
import petr.warehouse.inventory_management.model.OperationType;

import java.time.LocalDateTime;

@Getter
@Setter
public class OperationDto {
    private String storageName;
    private OperationType operationType;
    private String productName;
    private int amount;
    private LocalDateTime operationDateTime;
    private String comment;
}
