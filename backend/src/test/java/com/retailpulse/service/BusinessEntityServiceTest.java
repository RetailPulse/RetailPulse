package com.retailpulse.service;

import com.retailpulse.entity.BusinessEntity;
import com.retailpulse.entity.Inventory;
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
        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity.setId(1L);
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
            savedBusinessEntity.setId(1L);
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

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            businessEntityService.updateBusinessEntity(businessId, updatedEntityDetails)
        );

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

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            businessEntityService.updateBusinessEntity(businessId, updatedEntityDetails)
        );

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

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            businessEntityService.updateBusinessEntity(businessEntityId, updatedBusinessEntityDetails)
        );

        assertEquals("Business Entity not found with id: " + businessEntityId, exception.getMessage());
        verify(businessEntityRepository, times(1)).findById(businessEntityId);
        verify(businessEntityRepository, never()).save(any(BusinessEntity.class));
    }

    @Test
    void testUpdateBusinessEntityDoesNotChangeIsActive_Success() {
        BusinessEntity existingBusinessEntity = new BusinessEntity();
        existingBusinessEntity.setId(1L);
        existingBusinessEntity.setName("Old Name");
        existingBusinessEntity.setLocation("Old Location");
        existingBusinessEntity.setType("Old Type");
        existingBusinessEntity.setExternal(false);
        existingBusinessEntity.setActive(true);

        BusinessEntity updatedBusinessEntity = new BusinessEntity();
        updatedBusinessEntity.setName("New Name");
        updatedBusinessEntity.setLocation("New Location");
        updatedBusinessEntity.setType("New Type");
        updatedBusinessEntity.setExternal(true);
        updatedBusinessEntity.setActive(false); // This should be ignored

        when(businessEntityRepository.findById(1L)).thenReturn(Optional.of(existingBusinessEntity));
        when(businessEntityRepository.save(any(BusinessEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BusinessEntity result = businessEntityService.updateBusinessEntity(1L, updatedBusinessEntity);
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

        when(inventoryService.getInventoryByBusinessEntityId(businessId))
                .thenReturn(Collections.emptyList());

        BusinessEntity deletedEntity = businessEntityService.deleteBusinessEntity(businessId);
        assertFalse(deletedEntity.isActive());
        verify(businessEntityRepository, times(1)).findById(businessId);
        verify(businessEntityRepository, times(1)).save(existingEntity);
    }

    @Test
    void testIsExternalBusinessEntity_Success() {
        Long businessId = 1L;
        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity.setId(businessId);
        businessEntity.setExternal(true);

        when(businessEntityRepository.findById(businessId)).thenReturn(Optional.of(businessEntity));
        boolean isExternal = businessEntityService.isExternalBusinessEntity(businessId);
        assertTrue(isExternal);
        verify(businessEntityRepository, times(1)).findById(businessId);
    }

    @Test
    void testDeleteBusinessEntity_FailsDueToAssociatedInventory() {
        Long businessId = 1L;
        BusinessEntity existingEntity = new BusinessEntity();
        existingEntity.setId(businessId);
        existingEntity.setName("Test Entity");
        existingEntity.setLocation("Test Location");
        existingEntity.setType("Shop");
        existingEntity.setExternal(false);
        existingEntity.setActive(true);

        when(businessEntityRepository.findById(businessId)).thenReturn(Optional.of(existingEntity));

        Inventory inventory = new Inventory();
        inventory.setQuantity(10);
        when(inventoryService.getInventoryByBusinessEntityId(businessId))
                .thenReturn(Collections.singletonList(inventory));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            businessEntityService.deleteBusinessEntity(businessId)
        );
        assertEquals("Cannot delete Business Entity with id " + businessId + " as it has associated products in the inventory.", exception.getMessage());
        verify(businessEntityRepository, times(1)).findById(businessId);
    }

    @Test
    void testUpdateBusinessEntity_IgnoreEmptyStringField() {
        Long businessId = 1L;
        BusinessEntity existingEntity = new BusinessEntity();
        existingEntity.setId(businessId);
        existingEntity.setName("Original Name");
        existingEntity.setLocation("Original Location");
        existingEntity.setType("Original Type");
        existingEntity.setExternal(false);
        existingEntity.setActive(true);

        BusinessEntity updateDetails = new BusinessEntity();
        updateDetails.setName(""); // Should be ignored
        updateDetails.setLocation("Updated Location");
        updateDetails.setType("Updated Type");
        updateDetails.setExternal(true);

        when(businessEntityRepository.findById(businessId)).thenReturn(Optional.of(existingEntity));
        when(businessEntityRepository.save(any(BusinessEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BusinessEntity result = businessEntityService.updateBusinessEntity(businessId, updateDetails);
        assertEquals("Original Name", result.getName());
        assertEquals("Updated Location", result.getLocation());
        assertEquals("Updated Type", result.getType());
        assertTrue(result.isExternal());
    }
}
