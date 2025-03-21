package com.retailpulse.service;

import com.retailpulse.entity.BusinessEntity;
import com.retailpulse.entity.Inventory;
import com.retailpulse.repository.BusinessEntityRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class BusinessEntityService {

    @Autowired
    BusinessEntityRepository businessEntityRepository;

    @Autowired
    InventoryService inventoryService;

    public List<BusinessEntity> getAllBusinessEntities() {
        return businessEntityRepository.findAll();
    }

    public Optional<BusinessEntity> getBusinessEntityById(Long id) {
        return businessEntityRepository.findById(id);
    }

    public BusinessEntity saveBusinessEntity(BusinessEntity businessEntity) {
        return businessEntityRepository.save(businessEntity);
    }

    public BusinessEntity updateBusinessEntity(Long id, BusinessEntity businessEntityDetails) {
        BusinessEntity businessEntity = businessEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business Entity not found with id: " + id));

        if (!businessEntity.isActive()) {
            throw new RuntimeException("Cannot update a deleted business entity with id: " + id);
        }

        // Update fields from the incoming details if provided
        updateField(businessEntityDetails.getName(), businessEntity::setName);
        updateField(businessEntityDetails.getLocation(), businessEntity::setLocation);
        updateField(businessEntityDetails.getType(), businessEntity::setType);
        updateField(businessEntityDetails.isExternal(), businessEntity::setExternal);

        // Do not update isActive field, this is used for soft delete
        // businessEntity.setActive(businessEntityDetails.isActive());
        return businessEntityRepository.save(businessEntity);
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

    public BusinessEntity deleteBusinessEntity(Long id) {
        BusinessEntity businessEntity = businessEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business Entity not found with id: " + id));

        if (!businessEntity.isActive()) {
            throw new IllegalArgumentException("Business Entity with id " + id + " is already deleted.");
        }

        // Check if Inventory has products; If yes, cannot delete
        if (hasProductsInInventory(businessEntity)) {
            throw new IllegalArgumentException("Cannot delete Business Entity with id " + id + " as it has associated products in the inventory.");
        }

        businessEntity.setActive(false);
        return businessEntityRepository.save(businessEntity);
    }

    private boolean hasProductsInInventory(@NotNull BusinessEntity businessEntity) {
        List<Inventory> inventories = inventoryService.getInventoryByBusinessEntityId(businessEntity.getId());
        return inventories.stream().anyMatch(inventory -> inventory.getQuantity() > 0);
    }

    public boolean isExternalBusinessEntity(Long id) {
        BusinessEntity businessEntity = businessEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business Entity not found with id: " + id));
        return businessEntity.isExternal();
    }
}
