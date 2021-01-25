package com.sushil.service;

import com.sushil.domain.Inventory;
import com.sushil.domain.OrderRequest;
import com.sushil.domain.OrderResponse;
import com.sushil.exception.ExceptionMessages;
import com.sushil.exception.ItemNotFoundException;
import com.sushil.exception.PaymentException;
import com.sushil.repository.InventoryRepository;
import com.sushil.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class OrderService {

    private InventoryRepository inventoryRepository;
    private OrderRepository orderRepository;
    private SurgePricingService surgePricingService;
    private ItemService itemService;

    @Autowired
    public OrderService(InventoryRepository inventoryRepository, OrderRepository orderRepository,
                        SurgePricingService surgePricingService, ItemService itemService) {
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
        this.surgePricingService = surgePricingService;
        this.itemService = itemService;
    }

    @Transactional
    public Optional<OrderResponse> purchaseItem(OrderRequest orderRequest) {
        Optional<Inventory> itemInventory = inventoryRepository.findById(orderRequest.getItemName());

        if (!itemInventory.isPresent()) {
            throw new ItemNotFoundException(ExceptionMessages.ITEM_NOT_FOUND_EXCEPTION_MESSAGE_DETAIL);
        }

        //check if requested number of quantity is less than the quantity in inventory
        if (itemInventory.isPresent() && (itemInventory.get().getQuantity() >= orderRequest.getQuantity())) {
            int updatedQuantity = itemInventory.get().getQuantity() - orderRequest.getQuantity();

            BigDecimal itemPrice = surgePricingService.getLatestPrice(itemService.getItemPrice(orderRequest.getItemName()));

            if (processPayment(itemPrice)) {
                inventoryRepository.updateItemQuantity(orderRequest.getItemName(), updatedQuantity);
                OrderResponse orderResponse = new OrderResponse(orderRequest.getItemName(), orderRequest.getQuantity());
                orderRepository.save(orderResponse);
                return Optional.of(orderResponse);
            } else {
                throw new PaymentException(ExceptionMessages.INVALID_PAYMENT_EXCEPTION_MESSAGE_DETAIL);
            }

        } else {
            return Optional.empty();
        }
    }


    private boolean processPayment(BigDecimal amount) {
        //Payment Processing Logic to be added as a future enhancement
        return true;
    }


}
