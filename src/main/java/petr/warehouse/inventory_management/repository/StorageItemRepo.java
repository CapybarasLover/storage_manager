package petr.warehouse.inventory_management.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petr.warehouse.inventory_management.model.StorageItem;

@Repository
public interface StorageItemRepo extends JpaRepository<StorageItem, Long> {
    StorageItem getReferenceByItemName(String productName);
    @Transactional
    void deleteByItemNameAndStorageId(String productName, Long storageId);

    StorageItem getReferenceByItemNameAndStorageId(String productName, Long storageId);
}
