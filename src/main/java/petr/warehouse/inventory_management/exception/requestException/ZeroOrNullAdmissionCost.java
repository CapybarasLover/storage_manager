package petr.warehouse.inventory_management.exception.requestException;

public class ZeroOrNullAdmissionCost extends RuntimeException {
    public ZeroOrNullAdmissionCost(String message) {
      super(message);
    }
}
