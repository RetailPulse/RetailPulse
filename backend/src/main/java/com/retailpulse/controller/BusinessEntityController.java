package com.retailpulse.controller;

import com.retailpulse.entity.BusinessEntity;
import com.retailpulse.service.BusinessEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/businessEntity")
public class BusinessEntityController {

    private static final Logger logger = Logger.getLogger(BusinessEntityController.class.getName());

    @Autowired
    BusinessEntityService businessEntityService;

    @GetMapping
    public ResponseEntity<List<BusinessEntity>> getAllBusinessEntities() {
        logger.info("Fetching all business entities");
        List<BusinessEntity> entities = businessEntityService.getAllBusinessEntities();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessEntity> getBusinessById(@PathVariable Long id) {
        logger.info("Fetching business entity with id: " + id);
        BusinessEntity entity = businessEntityService.getBusinessEntityById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Business Entity not found with id: " + id));
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<BusinessEntity> createBusinessEntity(@RequestBody BusinessEntity businessEntity) {
        logger.info("Received request to create business entity: " + businessEntity);
        try {
            BusinessEntity createdBusinessEntity = businessEntityService.saveBusinessEntity(businessEntity);
            logger.info("Successfully created business entity with location: " + createdBusinessEntity.getLocation());
            return ResponseEntity.ok(createdBusinessEntity);
        } catch (Exception e) {
            logger.severe("Error creating business entity: " + e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessEntity> updateBusinessEntity(@PathVariable Long id, @RequestBody BusinessEntity businessEntity) {
        logger.info("Received request to update business entity with id: " + id);
        try {
            BusinessEntity updatedBusinessEntity = businessEntityService.updateBusinessEntity(id, businessEntity);
            logger.info("Successfully updated business entity with id: " + updatedBusinessEntity.getId());
            return ResponseEntity.ok(updatedBusinessEntity);
        } catch (Exception e) {
            logger.severe("Error updating business entity: " + e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessEntity(@PathVariable Long id) {
        logger.info("Deleting business entity with id: " + id);
        businessEntityService.deleteBusinessEntity(id);
        return ResponseEntity.ok().build();
    }
}
