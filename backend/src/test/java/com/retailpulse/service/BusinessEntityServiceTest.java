package com.retailpulse.service;

import com.retailpulse.entity.BusinessEntity;
import com.retailpulse.repository.BusinessEntityRepository;
import com.retailpulse.repository.InventoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BusinessEntityServiceTest {

    @Mock
    private BusinessEntityRepository businessEntityRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private BusinessEntityService businessEntityService;

    @Mock
    private InventoryService inventoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBusinessEntity_Success() {
        BusinessEntity businessEntity1 = new BusinessEntity();
        businessEntity1.setId(1L);
        businessEntity1.setName("Shop 1");
        businessEntity1.setLocation("Location 1");
        businessEntity1.setType("Shop");
        businessEntity1.setExternal(false);

        BusinessEntity businessEntity2 = new BusinessEntity();
        businessEntity2.setId(2L);
        businessEntity2.setName("Warehouse");
        businessEntity2.setLocation("Location 2");
        businessEntity2.setType("Warehouse");
        businessEntity2.setExternal(true);

        List<BusinessEntity> businessEntities = Arrays.asList(businessEntity1, businessEntity2);
        when(businessEntityRepository.findAll()).thenReturn(businessEntities);

        List<BusinessEntity> result = businessEntityService.getAllBusinessEntities();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(businessEntityRepository, times(1)).findAll();
    }

    @Test
    void testGetBusinessEntityById_Success() {
        // Mock BusinessEntity object
        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity.setId(1L);
        // When findById is called with 1L, then return the Business Entity object
        when(businessEntityRepository.findById(1L)).thenReturn(Optional.of(businessEntity));

        Optional<BusinessEntity> result = businessEntityService.getBusinessEntityById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testSaveBusinessEntity_Success() {
        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity.setId(1L);
        businessEntity.setName("Shop 1");
        businessEntity.setLocation("Location 1");
        businessEntity.setType("Shop");
        businessEntity.setExternal(false);

        when(businessEntityRepository.save(any(BusinessEntity.class))).thenAnswer(invocation -> {
            BusinessEntity savedBusinessEntity = invocation.getArgument(0);
            savedBusinessEntity.setId(1L); // Simulate the database setting the ID
            return savedBusinessEntity;
        });

        BusinessEntity result = businessEntityService.saveBusinessEntity(businessEntity);
        assertEquals(businessEntity.getId(), result.getId());
        verify(businessEntityRepository, times(1)).save(businessEntity);
    }

    @Test
    public void testUpdateBusinessEntity_Success() {
        Long businessId = 1L;
        BusinessEntity existingEntity = new BusinessEntity();
        existingEntity.setId(businessId);
        existingEntity.setName("Old Name");
        existingEntity.setLocation("Old Location");
        existingEntity.setType("Old Type");
        existingEntity.setExternal(false);
        existingEntity.setActive(true);

        BusinessEntity updatedEntityDetails = new BusinessEntity();
        updatedEntityDetails.setName("New Name");
        updatedEntityDetails.setLocation("New Location");
        updatedEntityDetails.setType("New Type");
        updatedEntityDetails.setExternal(true);

        when(businessEntityRepository.findById(businessId)).thenReturn(Optional.of(existingEntity));
        when(businessEntityRepository.save(any(BusinessEntity.class))).thenReturn(existingEntity);

        BusinessEntity result = businessEntityService.updateBusinessEntity(businessId, updatedEntityDetails);

        assertEquals("New Name", result.getName());
        assertEquals("New Location", result.getLocation());
        assertEquals("New Type", result.getType());
        assertTrue(result.isExternal());
        verify(businessEntityRepository, times(1)).findById(businessId);
        verify(businessEntityRepository, times(1)).save(existingEntity);
    }

    @Test
    public void testUpdateBusinessEntity_NotFound() {
        Long businessId = 1L;
        BusinessEntity updatedEntityDetails = new BusinessEntity();
        updatedEntityDetails.setName("New Name");
        updatedEntityDetails.setLocation("New Location");
        updatedEntityDetails.setType("New Type");
        updatedEntityDetails.setExternal(false);

        when(businessEntityRepository.findById(businessId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            businessEntityService.updateBusinessEntity(businessId, updatedEntityDetails);
        });

        assertEquals("Business Entity not found with id: " + businessId, exception.getMessage());
        verify(businessEntityRepository, times(1)).findById(businessId);
        verify(businessEntityRepository, never()).save(any(BusinessEntity.class));
    }

    @Test
    public void testUpdateBusinessEntity_DeletedEntity() {
        Long businessId = 1L;
        BusinessEntity existingEntity = new BusinessEntity();
        existingEntity.setId(businessId);
        existingEntity.setName("Old Name");
        existingEntity.setLocation("Old Location");
        existingEntity.setType("Old Type");
        existingEntity.setExternal(false);
        existingEntity.setActive(false);

        BusinessEntity updatedEntityDetails = new BusinessEntity();
        updatedEntityDetails.setName("New Name");
        updatedEntityDetails.setLocation("New Location");
        updatedEntityDetails.setType("New Location");
        updatedEntityDetails.setExternal(false);

        when(businessEntityRepository.findById(businessId)).thenReturn(Optional.of(existingEntity));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            businessEntityService.updateBusinessEntity(businessId, updatedEntityDetails);
        });

        assertEquals("Cannot update a deleted business entity with id: " + businessId, exception.getMessage());
        verify(businessEntityRepository, times(1)).findById(businessId);
        verify(businessEntityRepository, never()).save(any(BusinessEntity.class));
    }

    @Test
    public void testUpdateBusinessEntity_BusinessEntityNotFound() {
        Long businessEntityId = 1L;
        BusinessEntity updatedBusinessEntityDetails = new BusinessEntity();
        updatedBusinessEntityDetails.setName("New Name");
        updatedBusinessEntityDetails.setLocation("New Location");
        updatedBusinessEntityDetails.setType("New Type");
        updatedBusinessEntityDetails.setExternal(false);

        when(businessEntityRepository.findById(businessEntityId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            businessEntityService.updateBusinessEntity(businessEntityId, updatedBusinessEntityDetails);
        });

        assertEquals("Business Entity not found with id: " + businessEntityId, exception.getMessage());
        verify(businessEntityRepository, times(1)).findById(businessEntityId);
        verify(businessEntityRepository, never()).save(any(BusinessEntity.class));
    }

    @Test
    void testUpdateBusinessEntityDoesNotChangeIsActive_Success() {
        // Arrange: existing business entity with isActive = true
        BusinessEntity existingBusinessEntity = new BusinessEntity();
        existingBusinessEntity.setId(1L);
        existingBusinessEntity.setName("Old Name");
        existingBusinessEntity.setLocation("Old Location");
        existingBusinessEntity.setType("Old Type");
        existingBusinessEntity.setExternal(false);
        existingBusinessEntity.setActive(true);

        // Arrange: update details with new values and an attempt to change isActive to false
        BusinessEntity updatedBusinessEntity = new BusinessEntity();
        updatedBusinessEntity.setName("New Name");
        updatedBusinessEntity.setLocation("New Location");
        updatedBusinessEntity.setType("New Type");
        updatedBusinessEntity.setExternal(true);
        updatedBusinessEntity.setActive(false); // This should be ignored

        when(businessEntityRepository.findById(1L)).thenReturn(Optional.of(existingBusinessEntity));
        when(businessEntityRepository.save(any(BusinessEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act: update the business entity
        BusinessEntity result = businessEntityService.updateBusinessEntity(1L, updatedBusinessEntity);

        // Assert: the fields are updated except isActive, which remains true
        assertEquals("New Name", result.getName());
        assertEquals("New Location", result.getLocation());
        assertEquals("New Type", result.getType());
        assertTrue(result.isExternal());
        assertTrue(result.isActive(), "BusinessEntity should remain active");
    }

    @Test
    public void testDeleteBusinessEntity_Success() {
        Long businessId = 1L;
        BusinessEntity existingEntity = new BusinessEntity();
        existingEntity.setId(businessId);
        existingEntity.setName("Old Name");
        existingEntity.setLocation("Old Location");
        existingEntity.setType("Old Type");
        existingEntity.setExternal(false);
        existingEntity.setActive(true);

        when(businessEntityRepository.findById(businessId)).thenReturn(Optional.of(existingEntity));
        when(businessEntityRepository.save(any(BusinessEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Fix: Return an empty list to simulate no associated inventory exists.
        when(inventoryService.getInventoryByBusinessEntityId(businessId))
                .thenReturn(Collections.emptyList());

        BusinessEntity deletedEntity = businessEntityService.deleteBusinessEntity(businessId);

        // Validate that the entity is now marked as deleted (active = false)
        assertFalse(deletedEntity.isActive());
        verify(businessEntityRepository, times(1)).findById(businessId);
        verify(businessEntityRepository, times(1)).save(existingEntity);
    }

}
