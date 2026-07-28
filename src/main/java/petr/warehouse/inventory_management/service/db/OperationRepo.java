package petr.warehouse.inventory_management.service.db;

import org.springframework.data.jpa.repository.JpaRepository;
import petr.warehouse.inventory_management.service.db.entity.Operation;

public interface OperationRepo extends JpaRepository<Operation, Long> {
}
