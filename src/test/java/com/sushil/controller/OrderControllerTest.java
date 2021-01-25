package com.sushil.controller;

import com.sushil.domain.OrderRequest;
import com.sushil.domain.OrderResponse;
import com.sushil.service.OrderService;
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

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private OrderRequest orderRequest;
    private OrderResponse orderResponse;

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void init() {
        orderRequest = new OrderRequest("Cookies", 10);
        orderResponse = new OrderResponse("Cookies", 10);
    }


    @Test
    public void shouldReturnSuccessStatusCode() {
        Mockito.when(orderService.purchaseItem(orderRequest)).thenReturn(Optional.of(orderResponse));
        ResponseEntity<OrderResponse> orderResponseEntity = orderController.purchaseItem(orderRequest);
        Assertions.assertThat(orderResponseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

    }

}
