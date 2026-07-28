package petr.warehouse.inventory_management.service.db.entity;


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
    public StorageItem(String itemName, Storage storage){
        this.itemName = itemName;
        this.storage = storage;
        itemCount = 0;
        this.itemStatus = ItemStatus.OUT;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @Column(name = "item", unique = true)
    private String itemName;

    @Column(name = "count")
    private Integer itemCount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    public void addCount(Integer itemCount){
        this.itemCount += itemCount;
        changeStatus();
    }

    private void changeStatus(){
        if(itemCount == 0){
            itemStatus = ItemStatus.OUT;
        } else if(itemCount < 10){
            itemStatus = ItemStatus.FEW;
        }
        else {
            itemStatus = ItemStatus.ENOUGH;
        }
    }

    //TODO написать похожую функцию для проверки статуса, либо для блокировки операции при нехватке продукции.
}
