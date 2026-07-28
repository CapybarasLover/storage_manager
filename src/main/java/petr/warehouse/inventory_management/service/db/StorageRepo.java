package petr.warehouse.inventory_management.service.db;

import org.springframework.data.jpa.repository.JpaRepository;
import petr.warehouse.inventory_management.service.db.entity.Storage;

import java.util.Optional;

public interface StorageRepo extends JpaRepository<Storage, Long> {
    Optional<Storage> findById(Long id);
}
