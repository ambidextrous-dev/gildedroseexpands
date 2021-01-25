package com.sushil.integration;

import com.sushil.Application;
import com.sushil.config.SurgePricingConfig;
import com.sushil.domain.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SurgePricingConfig surgePricingConfig;

    @Sql(scripts = {"classpath:data.sql"})
    @Test
    public void testGetInventoryItemsApi_Success() {
        ResponseEntity<List<Item>> listOfItems = restTemplate
                .exchange("/v1/items", HttpMethod.GET, null, new ParameterizedTypeReference<List<Item>>() {
                });

        Assertions.assertEquals(listOfItems.getBody().size(), 3);
        Assertions.assertEquals(listOfItems.getStatusCode(), HttpStatus.OK);
    }

    @Sql(scripts = {"classpath:data.sql"})
    @Test
    public void testGetItemApi_Success() {
        ResponseEntity<Item> itemResponse = restTemplate.getForEntity("/v1/item/Bread", Item.class);
        Assertions.assertEquals(itemResponse.getBody().getName(), "Bread");
        Assertions.assertEquals(itemResponse.getStatusCode(), HttpStatus.OK);
    }

    @Sql(scripts = {"classpath:data.sql"})
    @Test
    public void testGetItemApi_Invalid() {
        ResponseEntity<Item> itemResponse = restTemplate.getForEntity("/v1/item/Cheesecake", Item.class);
        Assertions.assertEquals(itemResponse.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Sql(scripts = {"classpath:data.sql"})
    @Test
    public void testGetItemApi_Valid_PriceShouldSurgeAfterCountThreshold() {
        surgePricingConfig.setCountThreshold(3);

        ResponseEntity<Item> itemResponse1 = restTemplate.getForEntity("/v1/item/Bread", Item.class);
        restTemplate.getForEntity("/v1/item/Bread", Item.class);
        restTemplate.getForEntity("/v1/item/Bread", Item.class);
        ResponseEntity<Item> itemResponse2 = restTemplate.getForEntity("/v1/item/Bread", Item.class);

        //this should return -1 as first BigDecimal should be less than second
        int comparePrices = itemResponse1.getBody().getPrice().compareTo(itemResponse2.getBody().getPrice());

        //verify if the result from comparing two BigDecimal prices is as expected
        Assertions.assertEquals(comparePrices, -1);
    }

    @Sql(scripts = {"classpath:data.sql"})
    @Test
    public void testGetItemApi_Valid_PriceShouldNotSurgeBeforeCountThreshold() {
        surgePricingConfig.setCountThreshold(3);

        ResponseEntity<Item> itemResponse1 = restTemplate.getForEntity("/v1/item/Bread", Item.class);
        ResponseEntity<Item> itemResponse2 = restTemplate.getForEntity("/v1/item/Bread", Item.class);

        Assertions.assertEquals(itemResponse1.getBody().getPrice(), itemResponse2.getBody().getPrice());
    }


}
