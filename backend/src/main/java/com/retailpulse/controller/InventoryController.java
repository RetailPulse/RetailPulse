package com.retailpulse.controller;

import com.retailpulse.entity.Inventory;
import com.retailpulse.service.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private static final Logger logger = Logger.getLogger(Inventory.class.getName());

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<Inventory> getAllInventories() {
        logger.info("Fetching all inventories");
        return inventoryService.getAllInventory();
    }

    @GetMapping("/{id}")
    public Optional<Inventory> getInventoryById(@PathVariable Long id) {
        logger.info("Fetching inventory with id: " + id);
        return inventoryService.getInventoryById(id);
    }

    @GetMapping("/productId/{id}")
    public List<Inventory> getInventoryByProductId(@PathVariable Long id) {
        logger.info("Fetching inventory with productId: " + id);
        return inventoryService.getInventoryByProductId(id);
    }

    @GetMapping("/businessEntityId/{id}")
    public List<Inventory> getInventoryByBusinessEntityId(@PathVariable Long id) {
        logger.info("Fetching inventory with businessEntityId: " + id);
        return inventoryService.getInventoryByBusinessEntityId(id);
    }

    @GetMapping("/productId/{productId}/businessEntityId/{businessEntityId}")
    public Optional<Inventory> getInventoryByProductIdAndBusinessEntityId(@PathVariable Long productId, @PathVariable Long businessEntityId) {
        logger.info("Fetching inventory with businessEntityId (" + businessEntityId + ") and productId (" + productId + ")");
        return inventoryService.getInventoryByProductIdAndBusinessEntityId(productId, businessEntityId);
    }
}
