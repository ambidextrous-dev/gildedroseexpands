package com.sushil.service;

import com.sushil.domain.Inventory;
import com.sushil.domain.OrderRequest;
import com.sushil.domain.OrderResponse;
import com.sushil.exception.ItemNotFoundException;
import com.sushil.repository.InventoryRepository;
import com.sushil.repository.OrderRepository;
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
public class OrderServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SurgePricingService surgePricingService;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private OrderService orderService;

    private Inventory itemInventory;
    private String itemName;
    private OrderRequest validOrderRequest;
    private OrderRequest invalidOrderRequest;
    private OrderRequest invalidItemOrderRequest;
    private BigDecimal mockPrice;


    @BeforeEach
    public void init() {
        mockPrice = new BigDecimal(11);
        itemInventory = new Inventory("testItem", 10);
        itemName = itemInventory.getName();
        validOrderRequest = new OrderRequest("testItem", 3);
        invalidOrderRequest = new OrderRequest("testItem", 15);
        invalidItemOrderRequest = new OrderRequest("invalidItem", 15);

    }

    @Test
    public void shouldBeAbleToPurchaseValidItem() {
        Mockito.when(itemService.getItemPrice(itemName)).thenReturn(new BigDecimal(10));
        Mockito.when(surgePricingService.getLatestPrice(Mockito.any())).thenReturn(mockPrice);
        Mockito.when(inventoryRepository.findById(itemName)).thenReturn(Optional.of(itemInventory));

        Optional<OrderResponse> orderResponse = orderService.purchaseItem(validOrderRequest);
        Assertions.assertNotNull(orderResponse.get().getOrderId());
    }

    @Test
    public void shouldNotBeAbleToPurchaseItemWithInvalidQuantity() {
        Mockito.when(inventoryRepository.findById(itemName)).thenReturn(Optional.of(itemInventory));

        Optional<OrderResponse> orderResponse = orderService.purchaseItem(invalidOrderRequest);
        Assertions.assertFalse(orderResponse.isPresent());
    }

    @Test
    public void ifItemNotExists_shouldThrowItemNotFoundException() {
        Mockito.when(inventoryRepository.findById(invalidItemOrderRequest.getItemName())).thenReturn(Optional.empty());

        Assertions.assertThrows(ItemNotFoundException.class, () -> {
            orderService.purchaseItem(invalidItemOrderRequest);
        });
    }

}
