package petr.warehouse.inventory_management.service.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepo extends JpaRepository<Storage, Long> {
}
