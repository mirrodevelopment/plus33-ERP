package com.plus33.erp.twin.conformance;

import org.springframework.stereotype.Service;

@Service
public class SlaPredictionService {
    public boolean predictSlaBreach(String processName, String activity) {
        // Simulates prediction based on variance duration limits
        return activity.contains("DELAY") || activity.contains("REJECT");
    }
}