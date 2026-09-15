package petr.warehouse.inventory_management.exception.data;

public class StorageNotFoundException extends RuntimeException {
    public StorageNotFoundException(String message, Long id) {
        super(message + ": Storage id: " + id);
    }

    public StorageNotFoundException(String message, String name) {
        super(message + ": Storage name: " + name);
    }
}
