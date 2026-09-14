package petr.warehouse.inventory_management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Operation {
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

    @Column(name = "operation_date_time", columnDefinition = "timestamptz")
    private Instant operationDateTime;

    //Если это поступление - то стоимость операции это стоимость ВСЕГО поступления,
    //Если это продажа - стоимость ВСЕЙ продажи
    @Column(name = "operation_cost")
    private BigDecimal operationCost;

    @Column(name = "is_cancelled")
    private Boolean isCancelled;

    @Column(name = "cancels_operation_id")
    private Long cancelsOperationId;

    @Column(name = "comment")
    private String comment;

    private Operation(
            String storageName,
            OperationType operationType,
            String productName,
            int amount,
            Instant operationDateTime,
            String comment,
            BigDecimal operationCost
    ){
        this.storageName = storageName;
        this.operationType = operationType;
        this.productName = productName;
        this.amount = amount;
        this.operationDateTime = operationDateTime;
        this.operationCost = operationCost;
        this.comment = comment;
        this.isCancelled = false;
        this.cancelsOperationId = null;
    }

    public static Operation createAdmissionOperation(
            String storageName,
            OperationType operationType,
            String productName,
            int amount,
            Instant operationDateTime,
            String comment,
            BigDecimal admissionCost
    ){
        return new Operation(
                storageName,
                operationType,
                productName,
                amount,
                operationDateTime,
                comment,
                admissionCost
        );
    }

    public static Operation createSellOrWriteOffOperation(
            String storageName,
            OperationType operationType,
            String productName,
            int amount,
            Instant operationDateTime,
            String comment,
            BigDecimal sellCost
    ){
        return new Operation(
                storageName,
                operationType,
                productName,
                amount,
                operationDateTime,
                comment,
                sellCost
        );
    }
}
