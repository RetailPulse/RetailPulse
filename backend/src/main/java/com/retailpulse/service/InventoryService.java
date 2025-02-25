package com.retailpulse.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retailpulse.entity.Inventory;
import com.retailpulse.repository.InventoryRepository;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
    }

    public Inventory getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with product id: " + productId));
    }
    
    public List<Inventory> getInventoryByBusinessEntityId(Long businessEntityId) {
        return inventoryRepository.findByBusinessEntityId(businessEntityId);
    }

    public Optional<Inventory> getInventoryByProductIdAndBusinessEntityId(Long productId, Long businessEntityId) {
        return inventoryRepository.findByProductIdAndBusinessEntityId(productId, businessEntityId);

    }

    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Inventory updateInventory(Long id, @NotNull Inventory inventoryDetails) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));

        // Update fields from the incoming details if provided
        updateField(inventoryDetails.getProductId(), inventory::setProductId);
        updateField(inventoryDetails.getBusinessEntityId(), inventory::setBusinessEntityId);

        if (inventoryDetails.getQuantity() >= 0) {
            updateField(inventoryDetails.getQuantity(), inventory::setQuantity);
        }

        if (inventoryDetails.getTotalCostPrice() >= 0) {
            updateField(inventoryDetails.getTotalCostPrice(), inventory::setTotalCostPrice);
        }
        return inventoryRepository.save(inventory);
    }

    // Generic helper method for updating fields
    private <T> void updateField(T newValue, Consumer<T> updater) {
        if(newValue == null) {
            return;
        }
        if (newValue instanceof String && ((String) newValue).isEmpty()) {
            return;
        }
        updater.accept(newValue);
    }

    public Inventory deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));

        inventoryRepository.delete(inventory);
        return inventory;
    }
}
