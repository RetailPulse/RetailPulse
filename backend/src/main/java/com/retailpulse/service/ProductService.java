package com.retailpulse.service;

import com.retailpulse.entity.Product;
import com.retailpulse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class ProductService {

    @Autowired
    private SKUGeneratorService skuGeneratorService;

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductBySKU(String sku) {
        return productRepository.findBySku(sku);
    }

    public Product saveProduct(Product product) {
        // Generate SKU before saving
        String generatedSKU = skuGeneratorService.generateSKU();
        product.setSku(generatedSKU);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (!product.isActive()) {
            throw new RuntimeException("Cannot update a deleted product with id: " + id);
        }

        updateField(productDetails.getDescription(), product::setDescription);
        updateField(productDetails.getCategory(), product::setCategory);
        updateField(productDetails.getSubcategory(), product::setSubcategory);
        updateField(productDetails.getBrand(), product::setBrand);
        updateField(productDetails.getOrigin(), product::setOrigin);
        updateField(productDetails.getUom(), product::setUom);
        updateField(productDetails.getVendorCode(), product::setVendorCode);
        updateField(productDetails.getBarcode(), product::setBarcode);
         if (productDetails.getRrp() >= 0) {
            product.setRrp(productDetails.getRrp());
         }

        // Do not update isActive field, this is used for soft delete
        // product.setIsActive(productDetails.isActive());

        return productRepository.save(product);
    }

    private void updateField(String newValue, Consumer<String> updater) {
        if (newValue != null && !newValue.isEmpty()) {
            updater.accept(newValue);
        }
    }

    public Product deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setActive(false);
        return productRepository.save(product);
    }
}
