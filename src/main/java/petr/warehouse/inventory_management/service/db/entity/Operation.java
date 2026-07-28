package petr.warehouse.inventory_management.service.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Operation {
    public Operation(
            String storageName,
            OperationType operationType,
            String productName,
            int amount,
            LocalDateTime operationDateTime
    ){
        this.storageName = storageName;
        this.operationType = operationType;
        this.productName = productName;
        this.amount = amount;
        this.operationDateTime = operationDateTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "storage_name")
    private String storageName;

    @Column(name = "operation_type")
    private OperationType operationType;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "amount")
    private int amount;

    @Column(name = "operation_date_time", columnDefinition = "timestamptz")
    private LocalDateTime operationDateTime;
}
