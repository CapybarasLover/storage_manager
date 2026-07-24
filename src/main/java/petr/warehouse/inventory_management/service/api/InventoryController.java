package petr.warehouse.inventory_management.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import petr.warehouse.inventory_management.service.StorageManagerService;

@RestController()
@RequestMapping("/srorage")
public class InventoryController {
    @Autowired
    StorageManagerService storageService;

    //Получить данные склада по id
    @GetMapping("/{id}")
    StorageDto getStorage(@PathVariable String id){
        return storageService.getStorageById(Long.parseLong(id));
    }

    //Добавить новый продукт
    @PostMapping("/{id}/newproduct")
    String postNewProduct(@PathVariable String id){
        return null;
    }

    //Обновить продукт
    @PutMapping("/{id}/{productName}")
    String updateProduct(@PathVariable String id, @PathVariable String productName){
        return null;
    }
}
