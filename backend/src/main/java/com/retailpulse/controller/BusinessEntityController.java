package com.retailpulse.controller;

import com.retailpulse.entity.BusinessEntity;
import com.retailpulse.service.BusinessEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/businessEntity")
public class BusinessEntityController {

    private static final Logger logger = Logger.getLogger(BusinessEntityController.class.getName());

    @Autowired
    BusinessEntityService businessEntityService;

    @GetMapping
    public List<BusinessEntity> getAllBusinessEntities() {
        logger.info("Fetching all business entities");
        return businessEntityService.getAllBusinessEntities();
    }

    @GetMapping("/{id}")
    public Optional<BusinessEntity> getBusinessById(@PathVariable Long id) {
        logger.info("Fetching business entity with id: " + id);
        return businessEntityService.getBusinessEntityById(id);
    }

    @PostMapping
    public BusinessEntity createBusinessEntity(@RequestBody BusinessEntity businessEntity) {
        logger.info("Received request to create business entity: " + businessEntity);
        try {
            BusinessEntity createdBusinessEntity = businessEntityService.saveBusinessEntity(businessEntity);
            logger.info("Successfully created business entity with location: " + createdBusinessEntity.getLocation());
            return createdBusinessEntity;
        } catch (Exception e) {
            logger.severe("Error creating business entity: " + e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public BusinessEntity udpateBusinessEntity(@PathVariable Long id, @RequestBody BusinessEntity businessEntity) {
        logger.info("Received request to update business entity with id: " + id);
        try {
            BusinessEntity updatedBusinessEntity = businessEntityService.updateBusinessEntity(id, businessEntity);
            logger.info("Successfully updated business entity with id: " + updatedBusinessEntity.getId());
            return updatedBusinessEntity;
        } catch (Exception e) {
            logger.severe("Error updating business entity: " + e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBusinessEntity(@PathVariable Long id) {
        logger.info("Deleting business entity with id: " + id);
        businessEntityService.deleteBusinessEntity(id);
    }
}
