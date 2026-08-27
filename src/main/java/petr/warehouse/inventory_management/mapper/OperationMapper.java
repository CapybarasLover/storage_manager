package petr.warehouse.inventory_management.mapper;

import org.springframework.stereotype.Component;
import petr.warehouse.inventory_management.dto.OperationDto;
import petr.warehouse.inventory_management.model.Operation;
import petr.warehouse.inventory_management.model.OperationType;

import java.time.LocalDateTime;

@Component
public class OperationMapper {
    public OperationDto toDto(Operation operation){
        OperationDto dto = new OperationDto();

        dto.setStorageName(operation.getStorageName());
        dto.setProductName(operation.getProductName());
        dto.setOperationType(operation.getOperationType());
        dto.setAmount(operation.getAmount());
        dto.setOperationDateTime(operation.getOperationDateTime());
        dto.setComment(operation.getComment());

        return dto;
    }

    public Operation toOperation(OperationDto dto){
        Operation operation = new Operation();

        operation.setStorageName(dto.getStorageName());
        operation.setProductName(dto.getProductName());
        operation.setOperationType(dto.getOperationType());
        operation.setAmount(dto.getAmount());
        operation.setOperationDateTime(dto.getOperationDateTime());
        operation.setComment(dto.getComment());

        return operation;
    }

//    private String storageName;
//    private OperationType operationType;
//    private String productName;
//    private int amount;
//    private LocalDateTime operationDateTime;
//    private String comment;
}
