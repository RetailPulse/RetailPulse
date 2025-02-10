package com.retailpulse.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.retailpulse.entity.Shop;
import com.retailpulse.service.ShopService;

@RestController
@RequestMapping("/api/shops")
public class ShopController {

    private static final Logger logger = Logger.getLogger(ShopController.class.getName());

    @Autowired
    ShopService shopService;

    @GetMapping
    public List<Shop> getAllShops() {
        logger.info("Fetching all shops");
        return shopService.getAllShops();
    }

    @PostMapping
    public Shop createShop(@RequestBody Shop shop) {
        logger.info("Received request to create product: " + shop);
        try {
            Shop createdShop = shopService.saveShop(shop);
            logger.info("Successfully created shop with location: " + createdShop.getLocation());
            return createdShop;
        } catch (Exception e) {
            logger.severe("Error creating product: " + e.getMessage());
            throw e;
        }
    }
}
