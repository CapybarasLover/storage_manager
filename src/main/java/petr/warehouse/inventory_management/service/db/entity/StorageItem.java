package petr.warehouse.inventory_management.service.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "Item")
@Entity
public class StorageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @Column(name = "item")
    private String itemName;

    @Column(name = "count")
    private Integer itemCount;

    @Column(name = "status")

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;
}
