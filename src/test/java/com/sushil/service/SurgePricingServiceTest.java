package com.sushil.service;

import com.sushil.config.SurgePricingConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class SurgePricingServiceTest {

    @InjectMocks
    private SurgePricingService surgePricingService;

    @Mock
    private SurgePricingConfig surgePricingConfig;

    private int thresholdCount;
    private BigDecimal multiplierPercent;
    private BigDecimal hundredValue;
    private String itemName;
    private BigDecimal itemBasePrice;

    @BeforeEach
    public void init() {
        itemName = "validItemName";
        thresholdCount = 3;
        itemBasePrice = new BigDecimal(100);
        multiplierPercent = new BigDecimal(10);
        hundredValue = new BigDecimal(100);
    }

    @Test
    public void shouldAddNewItemsToVisitCounter() {
        Mockito.when(surgePricingConfig.getCountThreshold()).thenReturn(thresholdCount);

        surgePricingService.processSurge(itemName);
        Assertions.assertTrue(surgePricingService.getItemViewCounter().containsKey(itemName));
    }

    @Test
    public void shouldUpdateExistingItemsInVisitCounter() {
        Mockito.when(surgePricingConfig.getCountThreshold()).thenReturn(thresholdCount);

        surgePricingService.processSurge(itemName);
        surgePricingService.processSurge(itemName);

        Assertions.assertEquals(surgePricingService.getItemViewCounter().get(itemName), 2);
    }

    @Test
    public void shouldPriceSurge() {
        Mockito.when(surgePricingConfig.getCountThreshold()).thenReturn(thresholdCount);

        surgePricingService.processSurge(itemName);
        surgePricingService.processSurge(itemName);
        surgePricingService.processSurge(itemName);
        surgePricingService.processSurge(itemName);

        Assertions.assertTrue(surgePricingService.isSurged());
    }

    @Test
    public void shouldNotPriceSurge() {
        Mockito.when(surgePricingConfig.getCountThreshold()).thenReturn(thresholdCount);

        surgePricingService.processSurge(itemName);
        surgePricingService.processSurge(itemName);

        Assertions.assertFalse(surgePricingService.isSurged());
    }

    @Test
    public void shouldProvideLatestSurgedPricee() {
        Mockito.when(surgePricingConfig.getPriceMultiplierPercent()).thenReturn(multiplierPercent);
        Mockito.when(surgePricingConfig.getHundredValue()).thenReturn(hundredValue);

        surgePricingService.setSurged(true);
        BigDecimal latestPrice = surgePricingService.getLatestPrice(itemBasePrice);
        Assertions.assertEquals(latestPrice, new BigDecimal(110));
    }


}
