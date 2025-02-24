package com.retailpulse.repository;

import com.retailpulse.DTO.InventoryTransactionProductDto;
import com.retailpulse.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, UUID> {
    
    @Query("SELECT new com.retailpulse.DTO.InventoryTransactionProductDto(it, p) FROM InventoryTransaction it JOIN Product p ON it.productId = p.id")
    List<InventoryTransactionProductDto> findAllWithProduct();


}
