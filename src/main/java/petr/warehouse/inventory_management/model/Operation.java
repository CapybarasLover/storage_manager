package petr.warehouse.inventory_management.model;

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
            LocalDateTime operationDateTime,
            String comment
    ){
        this.storageName = storageName;
        this.operationType = operationType;
        this.productName = productName;
        this.amount = amount;
        this.operationDateTime = operationDateTime;
        this.comment = comment;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "storage_name")
    private String storageName;

    @Column(name = "operation_type")
    @Enumerated(value = EnumType.STRING)
    private OperationType operationType;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "amount")
    private int amount;

    //FIXME таймштамп не по локальному времени
    @Column(name = "operation_date_time", columnDefinition = "timestamptz")
    private LocalDateTime operationDateTime;

    @Column(name = "comment")
    private String comment;
}
