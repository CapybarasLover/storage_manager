package petr.warehouse.inventory_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import petr.warehouse.inventory_management.dto.OperationDto;
import petr.warehouse.inventory_management.filter.OperationFilter;
import petr.warehouse.inventory_management.model.OperationType;
import petr.warehouse.inventory_management.service.OperationService;
import petr.warehouse.inventory_management.service.StorageManagerService;
import petr.warehouse.inventory_management.dto.StorageDto;

@RestController()
@RequestMapping("storage")
public class InventoryController {
    @Autowired
    StorageManagerService storageService;

    @Autowired
    OperationService operationService;

    //Получить данные склада по id
    @GetMapping("/{id}")
    ResponseEntity<StorageDto> getStorage(@PathVariable(name = "id") Long storageId){
        return ResponseEntity.ok(storageService.getStorageById(storageId));
    }

    //Добавить склад
    @PostMapping("/newStorage")
    String postNewStorage(@RequestParam(name = "name") String storageName){
        return storageService.createStorage(storageName);
    }

    //Добавить новый продукт
    @PostMapping("/{id}/newProduct")
    String postNewProduct(@PathVariable(name = "id") Long storageId, @RequestParam(name = "name") String productName){
        return storageService.addProduct(storageId, productName);
    }

    //Удалить продукт
    @DeleteMapping("/{id}/deleteProduct")
    String deleteProduct(@PathVariable(name = "id") Long storageId, @RequestParam(name = "name") String productName){
        return storageService.deleteProduct(storageId, productName);
    }

    //Изменить продукт
    @PatchMapping("/{id}/operation")
    String operationType(@PathVariable(name = "id") Long storageId,
                         @RequestParam(name="type") OperationType operationType,
                         @RequestParam(name="product") String productName,
                         @RequestParam(name="count") Integer count,
                         @RequestParam(name="comment") String comment){
        return storageService.executeOperation(storageId, operationType, productName, count, comment);
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
