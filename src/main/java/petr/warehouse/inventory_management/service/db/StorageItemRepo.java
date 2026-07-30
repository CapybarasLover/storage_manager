package petr.warehouse.inventory_management.service.db;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import petr.warehouse.inventory_management.service.db.entity.StorageItem;

public interface StorageItemRepo extends JpaRepository<StorageItem, Long> {
    StorageItem getReferenceByItemName(String productName);
    @Transactional
    void deleteByItemNameAndStorageId(String productName, Long storageId);

    StorageItem getReferenceByItemNameAndStorageId(String productName, Long storageId);
}
