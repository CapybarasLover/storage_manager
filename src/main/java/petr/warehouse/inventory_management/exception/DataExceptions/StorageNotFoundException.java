package petr.warehouse.inventory_management.exception.DataExceptions;

public class StorageNotFoundException extends RuntimeException {
    public StorageNotFoundException(String message) {
        super(message);
    }
}
