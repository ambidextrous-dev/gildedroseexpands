package com.sushil.integration;

import com.sushil.Application;
import com.sushil.domain.OrderRequest;
import com.sushil.domain.OrderResponse;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.ResourceAccessException;

import java.util.Base64;

@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String userName = "john";
    private static final String password = "ontario";
    private int validQuantity = 2;
    private int invalidQuantity = 50;


    @Sql(scripts = {"classpath:data.sql"})
    @Test
    public void purchaseItemsApi_Authenticated() {
        OrderRequest orderRequest = new OrderRequest("Bread", validQuantity);

        HttpEntity<?> httpEntity = new HttpEntity<>(orderRequest, getRequestHeaders());
        ResponseEntity<OrderResponse> orderResponse = restTemplate
                .exchange("/v1/buy", HttpMethod.POST, httpEntity, OrderResponse.class);

        Assertions.assertEquals(orderResponse.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(orderResponse.getBody().getOrderId());
    }

    @Sql(scripts = {"classpath:data.sql"})
    @Test
    public void purchaseItemsApi_NotAuthenticated() {
        OrderRequest orderRequest = new OrderRequest("Bread", validQuantity);
        HttpEntity<?> httpEntity = new HttpEntity<>(orderRequest);

        Assertions.assertThrows(ResourceAccessException.class, () -> {
            ResponseEntity<OrderResponse> orderResponse = restTemplate
                    .exchange("/v1/buy", HttpMethod.POST, httpEntity, OrderResponse.class);
        });
    }

    @Sql(scripts = {"classpath:data.sql"})
    @Test
    public void purchaseItemsApi_InvalidQuantity() {
        OrderRequest orderRequest = new OrderRequest("Bread", invalidQuantity);

        HttpEntity<?> httpEntity = new HttpEntity<>(orderRequest, getRequestHeaders());
        ResponseEntity<OrderResponse> orderResponse = restTemplate
                .exchange("/v1/buy", HttpMethod.POST, httpEntity, OrderResponse.class);

        Assertions.assertEquals(orderResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }


    private HttpHeaders getRequestHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Basic " + getCredentials());
        return httpHeaders;
    }

    private String getCredentials() {
        return Base64.getEncoder().encodeToString(StringUtils.getBytesUtf8(String.format("%s:%s",
                getUserName(),
                getPassword())));
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

}
