package petr.warehouse.inventory_management.service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Operations {
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
