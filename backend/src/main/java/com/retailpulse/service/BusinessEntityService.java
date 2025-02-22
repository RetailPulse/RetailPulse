package com.retailpulse.service;

import com.retailpulse.entity.BusinessEntity;
import com.retailpulse.repository.BusinessEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class BusinessEntityService {

    @Autowired
    BusinessEntityRepository businessEntityRepository;

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

        // Do not update isActive field, this is used for soft delete
        // businessEntity.setActive(businessEntityDetails.isActive());
        return businessEntityRepository.save(businessEntity);
    }

    // Helper method for updating fields
    private void updateField(String newValue, Consumer<String> updater) {
        if (newValue != null && !newValue.isEmpty()) {
            updater.accept(newValue);
        }
    }

    public BusinessEntity deleteBusinessEntity(Long id) {
        BusinessEntity businessEntity = businessEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business Entity not found with id: " + id));

        businessEntity.setActive(false);
        return businessEntityRepository.save(businessEntity);
    }
}
