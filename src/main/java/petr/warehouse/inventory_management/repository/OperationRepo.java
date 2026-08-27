package petr.warehouse.inventory_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import petr.warehouse.inventory_management.model.Operation;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Repository
public interface OperationRepo extends JpaRepository<Operation, Long>, JpaSpecificationExecutor<Operation> {
}
