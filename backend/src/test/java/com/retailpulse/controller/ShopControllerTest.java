 package com.retailpulse.controller;

 import com.retailpulse.entity.Shop;
 import com.retailpulse.service.ShopService;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.mockito.MockitoAnnotations;
 import org.springframework.http.MediaType;
 import org.springframework.test.web.servlet.MockMvc;
 import org.springframework.test.web.servlet.setup.MockMvcBuilders;

 import java.util.Arrays;
 import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 public class ShopControllerTest {

     @Mock
     private ShopService shopService;

     @InjectMocks
     private ShopController shopController;

     private MockMvc mockMvc;

     @BeforeEach
     void setUp() {
         MockitoAnnotations.openMocks(this);
         mockMvc = MockMvcBuilders.standaloneSetup(shopController).build();
     }

     @Test
     void testGetAllShops() throws Exception {
         Shop shop1 = new Shop();
         shop1.setId(1L);
         shop1.setName("Shop 1");
         shop1.setLocation("Location 1");

         Shop shop2 = new Shop();
         shop2.setId(2L);
         shop2.setName("Shop 2");
         shop2.setLocation("Location 2");

         List<Shop> shops = Arrays.asList(shop1, shop2);
         when(shopService.getAllShops()).thenReturn(shops);

         mockMvc.perform(get("/api/shops"))
                 .andExpect(status().isOk())
                 .andExpect(content().json("[{'id': 1, 'name': 'Shop 1', 'location': 'Location 1'}, {'id': 2, 'name': 'Shop 2', 'location': 'Location 2'}]"));

         verify(shopService, times(1)).getAllShops();
     }

     @Test
     void testCreateShop() throws Exception {
         Shop shop = new Shop();
         shop.setId(1L);
         shop.setName("Shop 1");
         shop.setLocation("Location 1");

         when(shopService.saveShop(any(Shop.class))).thenReturn(shop);

         mockMvc.perform(post("/api/shops")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content("{\"name\": \"Shop 1\", \"location\": \"Location 1\"}"))
                 .andExpect(status().isOk())
                 .andExpect(content().json("{'id': 1, 'name': 'Shop 1', 'location': 'Location 1'}"));

         verify(shopService, times(1)).saveShop(any(Shop.class));
     }
 }
