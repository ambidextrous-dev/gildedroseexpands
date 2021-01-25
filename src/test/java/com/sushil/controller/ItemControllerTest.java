package com.sushil.controller;

import com.sushil.domain.Item;
import com.sushil.exception.ItemNotFoundException;
import com.sushil.service.ItemService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @InjectMocks
    ItemController itemController;

    @Mock
    private ItemService itemService;

    private String validItemName;
    private String invalidItemName;

    private Item item;
    private List<Item> listOfItems;

    @BeforeEach
    public void init() {
        item = new Item("Bread", "Fresh Baked Bread", new BigDecimal(2.50));
        validItemName = "Bread";
        invalidItemName = "invalidItem";
        listOfItems = new ArrayList<Item>();
        listOfItems.add(item);
    }

    @Test
    public void getItemTest_shouldReturnSuccessCode() {
        Mockito.when(itemService.getItem(validItemName)).thenReturn(Optional.of(item));
        ResponseEntity<Item> itemResponseEntity = itemController.getItem(validItemName);
        Assertions.assertThat(itemResponseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getItemsTest_shouldReturnSuccessCode() {
        Mockito.when(itemService.getInventoryList()).thenReturn(listOfItems);
        ResponseEntity<List<Item>> listOfItemsResponse = itemController.getListOfItems();
        Assertions.assertThat(listOfItemsResponse.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void ifItemNotExists_shouldThrowItemNotFoundException() {
        Mockito.when(itemService.getItem(invalidItemName)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(ItemNotFoundException.class, () -> {
            itemController.getItem(invalidItemName);
        });
    }


}
