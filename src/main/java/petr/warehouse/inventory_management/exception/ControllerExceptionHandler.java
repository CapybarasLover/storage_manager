package petr.warehouse.inventory_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import petr.warehouse.inventory_management.exception.DataExceptions.ProductNotFoundException;
import petr.warehouse.inventory_management.exception.DataExceptions.StorageNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<?> handleStorageNotFound(StorageNotFoundException e, WebRequest webRequest){
        System.out.println(webRequest);
        return new ResponseEntity<>("404: " + "Хранилище не найдено", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleProductNotFound(ProductNotFoundException e, WebRequest webRequest){
        System.out.println(webRequest);
        return new ResponseEntity<>("404: " + "Товар не найден", HttpStatus.NOT_FOUND);
    }
}
