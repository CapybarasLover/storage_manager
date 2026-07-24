package petr.warehouse.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import petr.warehouse.inventory_management.service.api.StorageDto;

@RestController()
@RequestMapping("/srorage")
public class InventoryController {
    @Autowired
    StorageManagerService storageService;

    @GetMapping("/{id}")
    StorageDto getStorage(@PathVariable String id){
        return storageService.getStorageById(Long.parseLong(id));
    }
}
