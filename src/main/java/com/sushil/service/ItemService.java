package com.sushil.service;

import com.sushil.domain.Item;
import com.sushil.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private ItemRepository itemRepository;
    private SurgePricingService surgePricingService;

    @Autowired
    public ItemService(ItemRepository itemRepository, SurgePricingService surgePricingService) {
        this.itemRepository = itemRepository;
        this.surgePricingService = surgePricingService;
    }

    public List<Item> getInventoryList() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItem(String itemName) {
        Optional<Item> item = itemRepository.findById(itemName);

        if (item.isPresent()) {
            //Update Surge Counter with every visit by the customer
            surgePricingService.processSurge(itemName);
            item.get().setPrice(surgePricingService.getLatestPrice(item.get().getPrice()));
            return item;
        } else {
            return Optional.empty();
        }

    }

    public BigDecimal getItemPrice(String itemName) {
        return itemRepository.findById(itemName).get().getPrice();
    }


}
