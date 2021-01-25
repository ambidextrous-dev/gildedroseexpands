package com.sushil.controller;

import com.sushil.domain.Item;
import com.sushil.exception.ExceptionMessages;
import com.sushil.exception.ItemNotFoundException;
import com.sushil.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Item>> getListOfItems() {
        return ResponseEntity.ok(itemService.getInventoryList());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/item/{itemName}")
    public ResponseEntity<Item> getItem(@PathVariable String itemName) {
        Optional<Item> item = itemService.getItem(itemName);

        if (item.isPresent()) {
            return ResponseEntity.ok().body(item.get());
        } else {
            throw new ItemNotFoundException(ExceptionMessages.ITEM_NOT_FOUND_EXCEPTION_MESSAGE_DETAIL);
        }

    }


}
