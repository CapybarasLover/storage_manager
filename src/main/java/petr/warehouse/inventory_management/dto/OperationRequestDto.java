package petr.warehouse.inventory_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import petr.warehouse.inventory_management.model.OperationType;

@Setter
@Getter
public class OperationRequestDto {
    @NotNull
    private OperationType operationType;

    @NotBlank
    private String productName;

    @NotNull @Positive
    private Integer count;

    @Size(max = 500)
    private String comment;
}
