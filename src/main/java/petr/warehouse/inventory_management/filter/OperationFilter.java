package petr.warehouse.inventory_management.filter;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import petr.warehouse.inventory_management.model.OperationType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class OperationFilter{
    private String storageName;
    private OperationType operationType;
    private String productName;
    @PastOrPresent
    LocalDate dateFrom;
    @PastOrPresent
    LocalDate dateTo;

    @AssertTrue(message = "dateFrom must be <= dateTo")
    public boolean isDateRangeValid() {
        return dateFrom == null || dateTo == null || !dateFrom.isAfter(dateTo);
    }
}