package petr.warehouse.inventory_management.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petr.warehouse.inventory_management.model.StorageItem;

import java.util.Optional;

@Repository
public interface StorageItemRepo extends JpaRepository<StorageItem, Long> {
    StorageItem getReferenceByItemName(String productName);

    Optional<StorageItem> findByItemNameAndStorageId(String productName, Long storageId);

    @Transactional
    void deleteByItemNameAndStorageId(String productName, Long storageId);

    @Transactional
    int deleteByIdAndStorageId(Long Id, Long storageId);
}
