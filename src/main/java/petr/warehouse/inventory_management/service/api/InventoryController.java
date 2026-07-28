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
    StorageDto getStorage(@PathVariable String id){
        return storageService.getStorageById(Long.parseLong(id));
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
    String deleteProduct(@PathVariable String id, @RequestParam String productName){
        return null;
    }

    //Изменить продукт
    @PatchMapping("/{id}/operation")
    String operationType(@PathVariable String id,
                         @RequestParam(name="type") String operationType,
                         @RequestParam(name="product") String productName,
                         @RequestParam(name="count") String count){
        return storageService.executeOperation(operationType, productName, count);
    }
}
