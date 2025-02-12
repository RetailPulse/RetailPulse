package com.retailpulse.service;

import com.retailpulse.entity.Product;
import com.retailpulse.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SKUGeneratorService skuGeneratorService;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        // Mock Product object
        Product product = new Product();
        product.setId(1L);
        // When findById is called with 1L, then return the Product object
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> result = productService.getAllProducts();
        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).getId());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        // Mock Product object
        Product product = new Product();
        product.setId(1L);
        // When findById is called with 1L, then return the Product object
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        when(skuGeneratorService.generateSKU()).thenReturn("RP12345");
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            savedProduct.setId(1L); // Simulate the database setting the ID
            return savedProduct;
        });

        Product result = productService.saveProduct(product);
        assertEquals("RP12345", result.getSku());
        assertNotNull(result.getId()); // Ensure the ID is set
    }

    @Test
    void testUpdateProduct() {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setSku("12345");
        existingProduct.setDescription("Old Description");
        existingProduct.setActive(true);

        Product updatedProduct = new Product();
        updatedProduct.setDescription("New Description");

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            return savedProduct;
        });

        Product result = productService.updateProduct(1L, updatedProduct);
        assertEquals("12345", result.getSku()); // SKU should remain unchanged
        assertEquals("New Description", result.getDescription());
        assertTrue(result.isActive()); // Ensure the product remains active
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setActive(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            return savedProduct;
        });

        productService.deleteProduct(1L);

        assertFalse(product.isActive()); // Ensure the product is marked as inactive
        verify(productRepository, times(1)).save(product); // Ensure the product is saved
    }

    @Test
    void testUpdateProductDoesNotChangeIsActive() {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setSku("12345");
        existingProduct.setDescription("Old Description");
        existingProduct.setActive(true);

        Product updatedProduct = new Product();
        updatedProduct.setDescription("New Description");
        updatedProduct.setActive(false); // Attempt to change isActive

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            return savedProduct;
        });

        Product result = productService.updateProduct(1L, updatedProduct);
        assertEquals("12345", result.getSku()); // SKU should remain unchanged
        assertEquals("New Description", result.getDescription());
        assertTrue(result.isActive()); // Ensure the product remains active
    }
}
