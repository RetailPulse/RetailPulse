package com.retailpulse.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String sku;

    private String description;
    private String category;
    private String subcategory;
    private String brand;
    private String origin;
    private String uom;
    private String vendorCode;
    private String barcode;
    private double rrp; // Recommended Retail Price
    private boolean isActive = true;
}
