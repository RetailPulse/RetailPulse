package com.retailpulse.service;

import com.retailpulse.DTO.InventoryTransactionProductDto;
import com.retailpulse.entity.Inventory;
import com.retailpulse.entity.InventoryTransaction;
import com.retailpulse.entity.Product;
import com.retailpulse.repository.InventoryTransactionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryTransactionService {
    @Autowired
    private InventoryTransactionRepository inventoryTransactionRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BusinessEntityService businessEntityService;

    public List<InventoryTransactionProductDto> getAllInventoryTransactionWithProduct() {
        return inventoryTransactionRepository.findAllWithProduct();
    }

    public InventoryTransaction saveInventoryTransaction(@NotNull InventoryTransaction inventoryTransaction) {
        long productId = inventoryTransaction.getProductId();
        long sourceId = inventoryTransaction.getSource();
        long destinationId = inventoryTransaction.getDestination();
        int quantity = inventoryTransaction.getQuantity();
        double costPricePerUnit = inventoryTransaction.getCostPricePerUnit();

        // Validate input Product
        Optional<Product> product = productService.getProductById(productId);
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product not found for product id: " + productId);
        }
        if (!product.get().isActive()) {
            throw new IllegalArgumentException("Product deleted for product id: " + productId);
        }
        // Validate input source & destination
        if (sourceId == destinationId) {
            throw new IllegalArgumentException("Source and Destination cannot be the same");
        }
        // Validate input quantity
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity cannot be negative or zero");
        }
        // Validate input cost price per unit
        if (costPricePerUnit < 0) {
            throw new IllegalArgumentException("Cost price per unit cannot be negative");
        }

        boolean isSourceExternal = businessEntityService.isExternalBusinessEntity(sourceId);
        // Source External: No need to validate source inventory
        if (!isSourceExternal) {
            // Validate source inventory
            Optional<Inventory> sourceInventory = inventoryService.getInventoryByProductIdAndBusinessEntityId(productId, sourceId);
            if (sourceInventory.isEmpty()) {
                throw new IllegalArgumentException("Source inventory not found for product id: " 
                        + productId + " and source id: " + sourceId);
            }
            if (sourceInventory.get().getQuantity() < quantity) {
                throw new IllegalArgumentException("Not enough quantity in source inventory for product id: "
                        + productId + " and source id: " + sourceId + ". Available: "
                        + sourceInventory.get().getQuantity() + ", required: " + quantity);
            }

            // Update source inventory: deduct the quantity
            Inventory existingSourceInventory = sourceInventory.get();
            existingSourceInventory.setQuantity(existingSourceInventory.getQuantity() - quantity);
            inventoryService.updateInventory(existingSourceInventory.getId(), existingSourceInventory);
        }

        boolean isDestinationExternal = businessEntityService.isExternalBusinessEntity(destinationId);
            // Destination External: No need to deduct destination inventory
            if (!isDestinationExternal) {
                        // Update or create destination inventory 
            Optional<Inventory> destinationInventory = inventoryService.getInventoryByProductIdAndBusinessEntityId(productId, destinationId);
            if (destinationInventory.isEmpty()) {
                // Create new inventory for destination since it does not exist.
                Inventory newDestinationInventory = new Inventory();
                newDestinationInventory.setProductId(productId);
                newDestinationInventory.setBusinessEntityId(destinationId);
                newDestinationInventory.setQuantity(quantity);
                newDestinationInventory.setTotalCostPrice(costPricePerUnit * quantity);
                destinationInventory = Optional.of(inventoryService.saveInventory(newDestinationInventory));
            } else {
                // Update existing destination inventory by adding the quantity.
                Inventory existingDestinationInventory = destinationInventory.get();
                existingDestinationInventory.setQuantity(existingDestinationInventory.getQuantity() + quantity);
                existingDestinationInventory.setTotalCostPrice(existingDestinationInventory.getTotalCostPrice() + (costPricePerUnit * quantity));
                inventoryService.updateInventory(existingDestinationInventory.getId(), existingDestinationInventory);
            }
        }

        // Proceed with saving the transaction
        return inventoryTransactionRepository.save(inventoryTransaction);
    }
}