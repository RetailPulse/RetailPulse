package com.retailpulse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Acts like a transactionId

    private Long productId; // ProductId - Unique Identifier for Product
    private int quantity;
    private double costPricePerUnit;
    /*
     * Supplier
     * Central InventoryService
     * Shop(s)
     */
    private Long source; // BusinessEntityId - InventoryService coming from
    private Long destination; // BusinessEntityId - InventoryService going to

    @CreationTimestamp
    // Automatically set when the entity is persisted
    private Instant insertedAt; // Using Instant to make sure follow application.yml config on timezone

    // @ManyToOne
    // @JoinColumn(name = "product_id", referencedColumnName = "id")
    // private Product product;
}
