package com.sushil.service;

import com.sushil.config.SurgePricingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@Service
@EnableScheduling
public class SurgePricingService {
    private static ConcurrentHashMap<String, Integer> itemViewCounter;
    private boolean isSurged;
    private final SurgePricingConfig surgePricingConfig;

    @Autowired
    public SurgePricingService(SurgePricingConfig surgePricingConfig) {
        this.itemViewCounter = new ConcurrentHashMap<String, Integer>();
        this.surgePricingConfig = surgePricingConfig;
    }

    private void updateVisitCounter(String itemName) {
        if (!itemViewCounter.containsKey(itemName)) {
            itemViewCounter.put(itemName, 1);
        } else {
            itemViewCounter.put(itemName, itemViewCounter.get(itemName) + 1);
        }
    }

    @Scheduled(fixedRateString = "${pricesurge.timeThreshold}")
    private void clearVisitCounter() {
        itemViewCounter.clear();
        setSurged(false);
    }

    public void processSurge(String itemName) {
        updateVisitCounter(itemName);

        if (isPriceSurgeRequired(itemName)) {
            setSurged(true);
        }
    }

    private boolean isPriceSurgeRequired(String itemName) {
        return itemViewCounter.get(itemName) > surgePricingConfig.getCountThreshold();
    }

    public BigDecimal getLatestPrice(BigDecimal itemBasePrice) {
        //if surge pricing is in effect, return price with surge multiplier, otherwise return as it is
        if (isSurged()) {
            return itemBasePrice.add(itemBasePrice
                    .multiply(surgePricingConfig.getPriceMultiplierPercent())
                    .divide(surgePricingConfig.getHundredValue()));
        }
        return itemBasePrice;
    }

    public boolean isSurged() {
        return isSurged;
    }

    public void setSurged(boolean surged) {
        isSurged = surged;
    }

    public static ConcurrentHashMap<String, Integer> getItemViewCounter() {
        return itemViewCounter;
    }

}
