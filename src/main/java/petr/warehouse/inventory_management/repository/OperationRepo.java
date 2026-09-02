package petr.warehouse.inventory_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import petr.warehouse.inventory_management.model.Operation;

@Repository
public interface OperationRepo extends JpaRepository<Operation, Long>, JpaSpecificationExecutor<Operation> {
}
