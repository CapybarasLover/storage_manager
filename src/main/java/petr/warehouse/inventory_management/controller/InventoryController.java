package petr.warehouse.inventory_management.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import petr.warehouse.inventory_management.dto.OperationDto;
import petr.warehouse.inventory_management.dto.OperationRequestDto;
import petr.warehouse.inventory_management.filter.OperationFilter;
import petr.warehouse.inventory_management.model.OperationType;
import petr.warehouse.inventory_management.service.OperationService;
import petr.warehouse.inventory_management.service.StorageManagerService;
import petr.warehouse.inventory_management.dto.StorageDto;

import java.net.URI;

@RestController()
@RequestMapping("storage")
public class InventoryController {
    @Autowired
    StorageManagerService storageService;

    @Autowired
    OperationService operationService;

    //Получить данные склада по id
    @GetMapping("/{storageId}")
    ResponseEntity<StorageDto> getStorage(@PathVariable Long storageId){
        return ResponseEntity.ok(storageService.getStorageById(storageId));
    }

    //Добавить склад
    @PostMapping
    ResponseEntity<Void> postNewStorage(@RequestParam String name){
        Long storageId = storageService.createStorage(name);
        URI location = URI.create("/storage/" + storageId);
        return ResponseEntity.created(location).build();
    }

    //Добавить новый продукт
    @PostMapping("/{storageId}/products")
    ResponseEntity<Void> postNewProduct(@PathVariable Long storageId, @RequestParam String productName){
        storageService.addProduct(storageId, productName);
        URI location = URI.create("/storage/" + storageId + "/products/" + productName);
        return ResponseEntity.created(location).build();
    }

    //Удалить продукт
    @DeleteMapping("/{storageId}/products/{productId}")
    ResponseEntity<?> deleteProduct(@PathVariable Long storageId, @PathVariable Long productId){
        storageService.deleteProduct(storageId, productId);
        return ResponseEntity.noContent().build();
    }

    //Изменить продукт
    @PatchMapping("/{storageId}/operation")
    public ResponseEntity<Void> executeOperation(
            @PathVariable Long storageId,
            @Valid @RequestBody OperationRequestDto requestBody
    ){
        storageService.executeOperation(storageId, requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @GetMapping("/operations")
//    public Page<OperationDto> getOperations(
//            OperationFilter filter,
//            Pageable pageable
//    ) {
//        return operationService.getOperations(
//                filter,
//                pageable
//        );
//    }
}
