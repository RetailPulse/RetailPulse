package com.retailpulse.service;

import com.retailpulse.entity.Shop;
import com.retailpulse.repository.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShopServiceTest {

    @Mock
    private ShopRepository shopRepository;

    @InjectMocks
    private ShopService shopService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllShops() {
        Shop shop1 = new Shop();
        shop1.setId(1L);
        shop1.setName("Shop 1");
        shop1.setLocation("Location 1");

        Shop shop2 = new Shop();
        shop2.setId(2L);
        shop2.setName("Shop 2");
        shop2.setLocation("Location 2");

        List<Shop> shops = Arrays.asList(shop1, shop2);
        when(shopRepository.findAll()).thenReturn(shops);

        List<Shop> result = shopService.getAllShops();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(shopRepository, times(1)).findAll();
    }

    @Test
    void testGetShopById() {
        // Mock Shop object
        Shop shop = new Shop();
        shop.setId(1L);
        // When findById is called with 1L, then return the Product object
        when(shopRepository.findById(1L)).thenReturn(Optional.of(shop));

        Optional<Shop> result = shopService.getShopById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testSaveShop() {
        Shop shop = new Shop();
        shop.setId(1L);
        shop.setName("Shop 1");
        shop.setLocation("Location 1");

        when(shopRepository.save(any(Shop.class))).thenAnswer(invocation -> {
            Shop savedShop = invocation.getArgument(0);
            savedShop.setId(1L); // Simulate the database setting the ID
            return savedShop;
        });

        Shop result = shopService.saveShop(shop);
        assertEquals(shop.getId(), result.getId());
        verify(shopRepository, times(1)).save(shop);
    }

    @Test
    public void testUpdateShop_Success() {
        Long shopId = 1L;
        Shop existingShop = new Shop();
        existingShop.setId(shopId);
        existingShop.setName("Old Name");
        existingShop.setLocation("Old Location");
        existingShop.setActive(true);

        Shop updatedShopDetails = new Shop();
        updatedShopDetails.setName("New Name");
        updatedShopDetails.setLocation("New Location");

        when(shopRepository.findById(shopId)).thenReturn(Optional.of(existingShop));
        when(shopRepository.save(any(Shop.class))).thenReturn(existingShop);

        Shop result = shopService.updateShop(shopId, updatedShopDetails);

        assertEquals("New Name", result.getName());
        assertEquals("New Location", result.getLocation());
        verify(shopRepository, times(1)).findById(shopId);
        verify(shopRepository, times(1)).save(existingShop);
    }

    @Test
    public void testUpdateShop_ShopNotFound() {
        Long shopId = 1L;
        Shop updatedShopDetails = new Shop();
        updatedShopDetails.setName("New Name");
        updatedShopDetails.setLocation("New Location");

        when(shopRepository.findById(shopId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            shopService.updateShop(shopId, updatedShopDetails);
        });

        assertEquals("Shop not found with id: " + shopId, exception.getMessage());
        verify(shopRepository, times(1)).findById(shopId);
        verify(shopRepository, never()).save(any(Shop.class));
    }

    @Test
    public void testUpdateShop_DeletedShop() {
        Long shopId = 1L;
        Shop existingShop = new Shop();
        existingShop.setId(shopId);
        existingShop.setName("Old Name");
        existingShop.setLocation("Old Location");
        existingShop.setActive(false);

        Shop updatedShopDetails = new Shop();
        updatedShopDetails.setName("New Name");
        updatedShopDetails.setLocation("New Location");

        when(shopRepository.findById(shopId)).thenReturn(Optional.of(existingShop));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            shopService.updateShop(shopId, updatedShopDetails);
        });

        assertEquals("Cannot update a deleted shop with id: " + shopId, exception.getMessage());
        verify(shopRepository, times(1)).findById(shopId);
        verify(shopRepository, never()).save(any(Shop.class));
    }
}
