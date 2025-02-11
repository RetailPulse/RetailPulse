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

 import static org.junit.jupiter.api.Assertions.assertEquals;
 import static org.mockito.Mockito.*;

 public class ShopServiceTest {

     @Mock
     private ShopRepository shopRepository;

     @InjectMocks
     private ShopService shopService;

     @BeforeEach
     void setUp() {
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
         verify(shopRepository, times(1)).findAll();
     }

     @Test
     void testSaveShop() {
         Shop shop = new Shop();
         shop.setId(1L);
         shop.setName("Shop 1");
         shop.setLocation("Location 1");

         when(shopRepository.save(shop)).thenReturn(shop);

         Shop result = shopService.saveShop(shop);
         assertEquals(shop.getId(), result.getId());
         verify(shopRepository, times(1)).save(shop);
     }
 }
