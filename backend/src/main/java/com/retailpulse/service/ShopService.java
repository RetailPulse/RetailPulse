package com.retailpulse.service;

import com.retailpulse.entity.Shop;
import com.retailpulse.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    @Autowired
    ShopRepository shopRepository;

    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    public Shop saveShop(Shop shop) {
        return shopRepository.save(shop);
    }
}
