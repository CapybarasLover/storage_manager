package petr.warehouse.inventory_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import petr.warehouse.inventory_management.service.StorageManagerService;
import petr.warehouse.inventory_management.dto.StorageDto;

@RestController()
@RequestMapping("storage")
public class InventoryController {
    @Autowired
    StorageManagerService storageService;

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
                         @RequestParam(name="type") String operationType,
                         @RequestParam(name="product") String productName,
                         @RequestParam(name="count") Integer count){
        return storageService.executeOperation(storageId, operationType, productName, count);
    }
}
