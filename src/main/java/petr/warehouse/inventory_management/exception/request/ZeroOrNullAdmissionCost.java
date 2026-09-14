package petr.warehouse.inventory_management.exception.request;

public class ZeroOrNullAdmissionCost extends RuntimeException {
    public ZeroOrNullAdmissionCost(String message) {
      super(message);
    }
}
