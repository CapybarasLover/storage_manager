package petr.warehouse.inventory_management.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import petr.warehouse.inventory_management.service.StorageManagerService;

@RestController()
@RequestMapping("/storage")
public class InventoryController {
    @Autowired
    StorageManagerService storageService;

    //Получить данные склада по id
    @GetMapping("/{id}")
    StorageDto getStorage(@PathVariable(name = "id") String storageId){
        return storageService.getStorageById(Long.parseLong(storageId));
    }

    //Добавить склад
    @PostMapping("/newStorage")
    String postNewStorage(@RequestParam(name = "name") String storageName){
        return storageService.createStorage(storageName);
    }

    //Добавить новый продукт
    @PostMapping("/{id}/newProduct")
    String postNewProduct(@PathVariable(name = "id") String storageId, @RequestParam(name = "name") String productName){
        return storageService.addProduct(storageId, productName);
    }

    //Удалить продукт
    @DeleteMapping("/{id}/deleteProduct")
    String deleteProduct(@PathVariable(name = "id") String storageId, @RequestParam(name = "name") String productName){
        return storageService.deleteProduct(storageId, productName);
    }

    //Изменить продукт
    @PatchMapping("/{id}/operation")
    String operationType(@PathVariable(name = "id") String storageId,
                         @RequestParam(name="type") String operationType,
                         @RequestParam(name="product") String productName,
                         @RequestParam(name="count") String count){
        return storageService.executeOperation(storageId, operationType, productName, count);
    }
}
