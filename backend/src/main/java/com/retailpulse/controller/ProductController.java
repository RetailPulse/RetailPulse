package com.retailpulse.controller;

import com.retailpulse.entity.Product;
import com.retailpulse.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = Logger.getLogger(ProductController.class.getName());

    @Autowired
    ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        logger.info("Fetching product with id: " + id);
        return productService.getProductById(id);
    }

    @GetMapping("/sku/{sku}")
    public Optional<Product> getProductBySKU(@PathVariable String sku) {
        logger.info("Fetching product with sku: " + sku);
        return productService.getProductBySKU(sku);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        logger.info("Received request to create product: " + product);
        try {
            Product createdProduct = productService.saveProduct(product);
            logger.info("Successfully created product with sku: " + createdProduct.getSku());
            return createdProduct;
        } catch (Exception e) {
            logger.severe("Error creating product: " + e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        logger.info("Received request to update product with id: " + id);
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            logger.info("Successfully updated product with id: " + updatedProduct.getId());
            return updatedProduct;
        } catch (Exception e) {
            logger.severe("Error updating product: " + e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        logger.info("Deleting product with id: " + id);
        productService.softDeleteProduct(id);
    }

    @PutMapping("/reverseSoftDelete/{id}")
    public Product reverseSoftDeleteProduct(@PathVariable Long id) {
        logger.info("Reverse soft delete of product with id: " + id);
        return productService.reverseSoftDelete(id);
    }
}
