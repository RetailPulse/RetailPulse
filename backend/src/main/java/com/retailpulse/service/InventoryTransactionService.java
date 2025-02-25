package com.retailpulse.service;

import com.retailpulse.DTO.InventoryTransactionProductDto;
import com.retailpulse.entity.Inventory;
import com.retailpulse.entity.InventoryTransaction;
import com.retailpulse.repository.InventoryTransactionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryTransactionService {
    @Autowired
    private InventoryTransactionRepository inventoryTransactionRepository;

    @Autowired
    private InventoryService inventoryService;

    public List<InventoryTransactionProductDto> getAllInventoryTransactionWithProduct() {
        return inventoryTransactionRepository.findAllWithProduct();
    }

    public InventoryTransaction saveInventoryTransaction(@NotNull InventoryTransaction inventoryTransaction) {
        long productId = inventoryTransaction.getProductId();
        long sourceId = inventoryTransaction.getSource();
        long destinationId = inventoryTransaction.getDestination();
        int quantity = inventoryTransaction.getQuantity();
        double costPricePerUnit = inventoryTransaction.getCostPricePerUnit();

        // Validate input
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (costPricePerUnit < 0) {
            throw new IllegalArgumentException("Cost price per unit cannot be negative");
        }

        // Validate source inventory
        Inventory sourceInventory = inventoryService.getInventoryByProductIdAndBusinessEntityId(productId, sourceId);
        if (sourceInventory == null) {
            throw new IllegalArgumentException("Source inventory not found for product id: " 
                    + productId + " and source id: " + sourceId);
        }
        if (sourceInventory.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough quantity in source inventory for product id: "
                    + productId + " and source id: " + sourceId + ". Available: "
                    + sourceInventory.getQuantity() + ", required: " + quantity);
        }

        // Update or create destination inventory 
        Inventory destinationInventory = inventoryService.getInventoryByProductIdAndBusinessEntityId(productId, destinationId);
        if (destinationInventory == null) {
            // Create new inventory for destination since it does not exist.
            Inventory newDestinationInventory = new Inventory();
            newDestinationInventory.setProductId(productId);
            newDestinationInventory.setBusinessEntityId(destinationId);
            newDestinationInventory.setQuantity(quantity);
            destinationInventory = inventoryService.saveInventory(newDestinationInventory);
        } else {
            // Update existing destination inventory by adding the quantity.
            int updatedDestinationQuantity = destinationInventory.getQuantity() + quantity;
            Inventory updatedDestinationInventory = new Inventory();
            updatedDestinationInventory.setQuantity(updatedDestinationQuantity);
            // Update additional fields as needed.
            inventoryService.updateInventory(destinationInventory.getId(), updatedDestinationInventory);
        }

        // Update source inventory: deduct the quantity
        int updatedQuantity = sourceInventory.getQuantity() - quantity;
        Inventory updatedSourceInventory = new Inventory();
        updatedSourceInventory.setQuantity(updatedQuantity);
        // Update additional fields as needed.
        inventoryService.updateInventory(sourceInventory.getId(), updatedSourceInventory);

        // Proceed with saving the transaction
        return inventoryTransactionRepository.save(inventoryTransaction);
    }
}