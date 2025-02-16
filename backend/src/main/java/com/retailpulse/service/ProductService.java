package com.retailpulse.service;

import com.retailpulse.entity.Product;
import com.retailpulse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        product.setDescription(productDetails.getDescription());
        product.setCategory(productDetails.getCategory());
        product.setSubcategory(productDetails.getSubcategory());
        product.setBrand(productDetails.getBrand());
        product.setOrigin(productDetails.getOrigin());
        product.setUom(productDetails.getUom());
        product.setVendorCode(productDetails.getVendorCode());
        product.setBarcode(productDetails.getBarcode());
        product.setRrp(productDetails.getRrp());

        // Do not update isActive field, this is used for soft delete
        // product.setIsActive(productDetails.isActive());

        return productRepository.save(product);
    }

    public Product deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setActive(false);
        return productRepository.save(product);
    }
}
