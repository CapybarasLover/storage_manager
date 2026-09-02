package petr.warehouse.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petr.warehouse.inventory_management.model.Operation;
import petr.warehouse.inventory_management.repository.OperationRepo;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    OperationRepo operationRepo;
}
