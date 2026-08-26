package petr.warehouse.inventory_management.exception.DataExceptions;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String message, Long name) {
        super(message);
    }
}
