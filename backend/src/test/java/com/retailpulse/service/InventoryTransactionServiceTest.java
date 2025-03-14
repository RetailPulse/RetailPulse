package com.retailpulse.service;

import com.retailpulse.DTO.InventoryTransactionProductDto;
import com.retailpulse.entity.*;
import com.retailpulse.repository.InventoryTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryTransactionServiceTest {

    @Mock
    private InventoryTransactionRepository mockInventoryTransactionRepository;

    @Mock
    private InventoryService mockInventoryService;

    @Mock
    private ProductService mockProductService;

    @Mock
    private BusinessEntityService mockBusinessEntityService;

    @InjectMocks
    private InventoryTransactionService inventoryTransactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllInventoryTransactionWithProduct() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setDescription("Product A");

        InventoryTransaction inventoryTransaction = new InventoryTransaction();
        inventoryTransaction.setProductId(1L);

        InventoryTransactionProductDto dto1 = new InventoryTransactionProductDto(inventoryTransaction, product);

        List<InventoryTransactionProductDto> mockDtos = Collections.singletonList(dto1);

        when(mockInventoryTransactionRepository.findAllWithProduct()).thenReturn(mockDtos);

        // Act
        List<InventoryTransactionProductDto> result = inventoryTransactionService.getAllInventoryTransactionWithProduct();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product A", result.get(0).getProduct().getDescription());

        verify(mockInventoryTransactionRepository, times(1)).findAllWithProduct();
        verifyNoMoreInteractions(mockInventoryTransactionRepository);
    }

    @Test
    void testSaveInventoryTransaction_Successful() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(1L);
        transaction.setSource(101L);
        transaction.setDestination(201L);
        transaction.setQuantity(10);
        transaction.setCostPricePerUnit(5.0);

        Product product = new Product();
        product.setId(1L);
        product.setActive(true);

        Inventory sourceInventory = new Inventory();
        sourceInventory.setId(1L);
        sourceInventory.setProductId(1L);
        sourceInventory.setBusinessEntityId(101L);
        sourceInventory.setQuantity(20);
        sourceInventory.setTotalCostPrice(100.0);

        Inventory updatedSourceInventory = new Inventory();
        updatedSourceInventory.setId(1L);
        updatedSourceInventory.setProductId(1L);
        updatedSourceInventory.setBusinessEntityId(101L);
        updatedSourceInventory.setQuantity(10);
        updatedSourceInventory.setTotalCostPrice(50.0);

        Inventory destinationInventory = new Inventory();
        destinationInventory.setId(2L);
        destinationInventory.setProductId(1L);
        destinationInventory.setBusinessEntityId(201L);
        destinationInventory.setQuantity(30);
        destinationInventory.setTotalCostPrice(150.0);

        Inventory updatedDestinationInventory = new Inventory();
        updatedDestinationInventory.setId(2L);
        updatedDestinationInventory.setProductId(1L);
        updatedDestinationInventory.setBusinessEntityId(201L);
        updatedDestinationInventory.setQuantity(40);
        updatedDestinationInventory.setTotalCostPrice(200.0);

        when(mockProductService.getProductById(1L)).thenReturn(Optional.of(product));
        when(mockBusinessEntityService.isExternalBusinessEntity(101L)).thenReturn(false);
        when(mockBusinessEntityService.isExternalBusinessEntity(201L)).thenReturn(false);
        when(mockInventoryService.getInventoryByProductIdAndBusinessEntityId(1L, 101L)).thenReturn(Optional.of(sourceInventory));
        when(mockInventoryService.getInventoryByProductIdAndBusinessEntityId(1L, 201L)).thenReturn(Optional.of(destinationInventory));
        when(mockInventoryService.updateInventory(1L, updatedSourceInventory)).thenReturn(updatedSourceInventory);
        when(mockInventoryService.updateInventory(2L, updatedDestinationInventory)).thenReturn(updatedDestinationInventory);
        when(mockInventoryTransactionRepository.save(transaction)).thenReturn(transaction);

        // Act
        InventoryTransaction result = inventoryTransactionService.saveInventoryTransaction(transaction);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals(101L, result.getSource());
        assertEquals(201L, result.getDestination());
        assertEquals(10, result.getQuantity());
        assertEquals(5.0, result.getCostPricePerUnit(), 0.01);

        verify(mockProductService, times(1)).getProductById(1L);
        verify(mockBusinessEntityService, times(1)).isExternalBusinessEntity(101L);
        verify(mockBusinessEntityService, times(1)).isExternalBusinessEntity(201L);
        verify(mockInventoryService, times(1)).getInventoryByProductIdAndBusinessEntityId(1L, 101L);
        verify(mockInventoryService, times(1)).getInventoryByProductIdAndBusinessEntityId(1L, 201L);
        verify(mockInventoryService, times(1)).updateInventory(1L, updatedSourceInventory);
        verify(mockInventoryService, times(1)).updateInventory(2L, updatedDestinationInventory);
        verify(mockInventoryTransactionRepository, times(1)).save(transaction);
        verifyNoMoreInteractions(mockProductService, mockBusinessEntityService, mockInventoryService, mockInventoryTransactionRepository);
    }

    @Test
    void testSaveInventoryTransaction_InsufficientSourceQuantity() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(1L);
        transaction.setSource(101L);
        transaction.setDestination(201L);
        transaction.setQuantity(30);
        transaction.setCostPricePerUnit(5.0);

        Product product = new Product();
        product.setId(1L);
        product.setActive(true);

        Inventory sourceInventory = new Inventory();
        sourceInventory.setId(1L);
        sourceInventory.setProductId(1L);
        sourceInventory.setBusinessEntityId(101L);
        sourceInventory.setQuantity(20);
        sourceInventory.setTotalCostPrice(100.0);

        when(mockProductService.getProductById(1L)).thenReturn(Optional.of(product));
        when(mockBusinessEntityService.isExternalBusinessEntity(101L)).thenReturn(false);
        when(mockInventoryService.getInventoryByProductIdAndBusinessEntityId(1L, 101L)).thenReturn(Optional.of(sourceInventory));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventoryTransactionService.saveInventoryTransaction(transaction);
        });

        assertEquals(
                "Not enough quantity in source inventory for product id: 1 and source id: 101. Available: 20, required: 30",
                exception.getMessage()
        );

        verify(mockProductService, times(1)).getProductById(1L);
        verify(mockBusinessEntityService, times(1)).isExternalBusinessEntity(101L);
        verify(mockInventoryService, times(1)).getInventoryByProductIdAndBusinessEntityId(1L, 101L);
        verifyNoMoreInteractions(mockProductService, mockBusinessEntityService, mockInventoryService);
    }

    @Test
    void testSaveInventoryTransaction_InvalidProduct() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(999L);
        transaction.setSource(101L);
        transaction.setDestination(201L);
        transaction.setQuantity(10);
        transaction.setCostPricePerUnit(5.0);

        when(mockProductService.getProductById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventoryTransactionService.saveInventoryTransaction(transaction);
        });

        assertEquals("Product not found for product id: 999", exception.getMessage());

        verify(mockProductService, times(1)).getProductById(999L);
        verifyNoMoreInteractions(mockProductService);
    }

    @Test
    void testSaveInventoryTransaction_SourceSameAsDestination() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(1L);
        transaction.setSource(101L);
        transaction.setDestination(101L);
        transaction.setQuantity(10);
        transaction.setCostPricePerUnit(5.0);

        Product product = new Product();
        product.setId(1L);
        product.setActive(true);

        when(mockProductService.getProductById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventoryTransactionService.saveInventoryTransaction(transaction);
        });

        assertEquals("Source and Destination cannot be the same", exception.getMessage());

        verify(mockProductService, times(1)).getProductById(1L);
        verifyNoMoreInteractions(mockProductService);
    }

    @Test
    void testSaveInventoryTransaction_NegativeQuantity() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(1L);
        transaction.setSource(101L);
        transaction.setDestination(201L);
        transaction.setQuantity(-5);
        transaction.setCostPricePerUnit(5.0);

        Product product = new Product();
        product.setId(1L);
        product.setActive(true);

        when(mockProductService.getProductById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventoryTransactionService.saveInventoryTransaction(transaction);
        });

        assertEquals("Quantity cannot be negative or zero", exception.getMessage());

        verify(mockProductService, times(1)).getProductById(1L);
        verifyNoMoreInteractions(mockProductService);
    }
}