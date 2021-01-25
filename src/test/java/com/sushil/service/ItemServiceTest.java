package com.sushil.service;

import com.sushil.domain.Item;
import com.sushil.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private SurgePricingService surgePricingService;

    @InjectMocks
    private ItemService itemService;

    private BigDecimal mockPrice;
    private String validItemName;
    private String invalidItemName;
    private Item validItem;

    @BeforeEach
    public void init() {
        mockPrice = new BigDecimal(10);
        validItemName = "Valid Item";
        invalidItemName = "Invalid Item";
        validItem = new Item(validItemName, "a valid item for testing", mockPrice);
    }

    @Test
    public void shouldBeAbleToGetItemForValidItem() {
        Mockito.when(itemRepository.findById(validItemName)).thenReturn(Optional.of(validItem));
        Mockito.when(surgePricingService.getLatestPrice(Mockito.any())).thenReturn(mockPrice);

        Optional<Item> validItem = itemService.getItem(validItemName);
        Assertions.assertEquals(validItem.get().getName(), validItemName);
    }

    @Test
    public void shouldNotGetItemForInvalidItem() {
        Mockito.when(itemRepository.findById(invalidItemName)).thenReturn(Optional.empty());
        Assertions.assertFalse(itemService.getItem(invalidItemName).isPresent());
    }


}
