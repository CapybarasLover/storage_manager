package petr.warehouse.inventory_management.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import petr.warehouse.inventory_management.exception.DataExceptions.IllegalSellOrWriteOffCount;
import petr.warehouse.inventory_management.exception.DataExceptions.ProductNotFoundException;
import petr.warehouse.inventory_management.exception.DataExceptions.StorageNotFoundException;

import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ProblemDetail handleStorageNotFound(StorageNotFoundException e, WebRequest webRequest){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "Хранилище не найдено");
        System.out.println(webRequest);
        return problemDetail;
    }

    @ExceptionHandler
    public ProblemDetail handleProductNotFound(ProductNotFoundException e, WebRequest webRequest){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "Товар не найден");
        System.out.println(webRequest);
        return problemDetail;
    }

    @ExceptionHandler
    public ProblemDetail handleProductIllegalOperation(IllegalSellOrWriteOffCount e, WebRequest webRequest){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "На складе недостаточно товара!");
        System.out.println(webRequest);
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation failed");
        problemDetail.setProperty("errors", ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
                .toList());
        return handleExceptionInternal(ex, problemDetail, headers, HttpStatus.BAD_REQUEST, request);
    }
}
