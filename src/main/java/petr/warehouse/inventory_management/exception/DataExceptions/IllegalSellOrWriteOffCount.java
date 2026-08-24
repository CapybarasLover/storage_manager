package petr.warehouse.inventory_management.exception.DataExceptions;

public class IllegalSellOrWriteOffCount extends RuntimeException {
    public IllegalSellOrWriteOffCount(String message, String itemName, int operationCount) {
        super(message + "Item name: " + itemName + " Operation count: " + operationCount);
    }
}
