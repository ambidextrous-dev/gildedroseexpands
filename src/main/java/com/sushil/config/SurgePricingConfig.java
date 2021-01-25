package com.sushil.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class SurgePricingConfig {

    @Value("${pricesurge.countThreshold}")
    private int countThreshold;

    @Value("${pricesurge.priceMultiplierPercent}")
    private BigDecimal priceMultiplierPercent;

    private final BigDecimal hundredValue = new BigDecimal(100);


    public int getCountThreshold() {
        return countThreshold;
    }

    public void setCountThreshold(int countThreshold) {
        this.countThreshold = countThreshold;
    }

    public BigDecimal getPriceMultiplierPercent() {
        return priceMultiplierPercent;
    }

    public void setPriceMultiplierPercent(BigDecimal priceMultiplierPercent) {
        this.priceMultiplierPercent = priceMultiplierPercent;
    }

    public BigDecimal getHundredValue() {
        return hundredValue;
    }
}
