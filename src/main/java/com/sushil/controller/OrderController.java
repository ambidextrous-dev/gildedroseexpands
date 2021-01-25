package com.sushil.controller;

import com.sushil.domain.OrderRequest;
import com.sushil.domain.OrderResponse;
import com.sushil.exception.ExceptionMessages;
import com.sushil.exception.InsufficientQuantityException;
import com.sushil.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(method = RequestMethod.POST,
            path = "/buy",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> purchaseItem(@Valid @RequestBody OrderRequest orderRequest) {
        Optional<OrderResponse> orderResponse = orderService.purchaseItem(orderRequest);

        if (orderResponse.isPresent()) {
            return ResponseEntity.ok().body(orderResponse.get());
        } else {
            throw new InsufficientQuantityException(ExceptionMessages.INSUFFICIENT_QUANTITY_EXCEPTION_MESSAGE_DETAIL);
        }
    }

}
