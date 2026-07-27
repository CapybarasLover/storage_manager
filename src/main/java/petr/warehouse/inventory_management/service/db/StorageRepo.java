package petr.warehouse.inventory_management.service.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageRepo extends JpaRepository<Storage, Long> {
    Optional<Storage> findById(Long id);
}
