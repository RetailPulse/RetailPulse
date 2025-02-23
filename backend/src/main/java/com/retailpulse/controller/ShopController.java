package com.retailpulse.controller;

import com.retailpulse.entity.Shop;
import com.retailpulse.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

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

    @GetMapping("/{id}")
    public Optional<Shop> getShopById(@PathVariable Long id) {
        logger.info("Fetching shop with id: " + id);
        return shopService.getShopById(id);
    }

    @PostMapping
    public Shop createShop(@RequestBody Shop shop) {
        logger.info("Received request to create shop: " + shop);
        try {
            Shop createdShop = shopService.saveShop(shop);
            logger.info("Successfully created shop with location: " + createdShop.getLocation());
            return createdShop;
        } catch (Exception e) {
            logger.severe("Error creating shop: " + e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public Shop updateShop(@PathVariable Long id, @RequestBody Shop shop) {
        logger.info("Received request to update shop with id: " + id);
        try {
            Shop updatedShop = shopService.updateShop(id, shop);
            logger.info("Successfully updated shop with id: " + updatedShop.getId());
            return updatedShop;
        } catch (Exception e) {
            logger.severe("Error updating shop: " + e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteShop(@PathVariable Long id) {
        logger.info("Deleting shop with id: " + id);
        shopService.deleteShop(id);
    }
}
