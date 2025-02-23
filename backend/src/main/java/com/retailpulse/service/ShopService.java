package com.retailpulse.service;

import com.retailpulse.entity.Shop;
import com.retailpulse.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class ShopService {

    @Autowired
    ShopRepository shopRepository;

    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    public Optional<Shop> getShopById(Long id) {
        return shopRepository.findById(id);
    }

    public Shop saveShop(Shop shop) {
        return shopRepository.save(shop);
    }

    public Shop updateShop(Long id, Shop shopDetails) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found with id: " + id));

        if (!shop.isActive()) {
            throw new RuntimeException("Cannot update a deleted shop with id: " + id);
        }

        updateField(shopDetails.getName(), shopDetails::setName);
        updateField(shopDetails.getLocation(), shopDetails::setLocation);
        // Do not update isActive field, this is used for soft delete
        // shop.setIsActive(shopDetails.isActive());

        return shopRepository.save(shop);
    }

    private void updateField(String newValue, Consumer<String> updater) {
        if (newValue != null && !newValue.isEmpty()) {
            updater.accept(newValue);
        }
    }

    public Shop deleteShop(Long id) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found with id: " + id));

        shop.setActive(false);
        return shopRepository.save(shop);
    }
}
