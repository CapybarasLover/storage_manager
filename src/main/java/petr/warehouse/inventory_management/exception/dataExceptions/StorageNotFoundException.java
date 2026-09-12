package petr.warehouse.inventory_management.exception.dataExceptions;

public class StorageNotFoundException extends RuntimeException {
    public StorageNotFoundException(String message, Long id) {
        super(message + ": Storage id: " + id);
    }
}
