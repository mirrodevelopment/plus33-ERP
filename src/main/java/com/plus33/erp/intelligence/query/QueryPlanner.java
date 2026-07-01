package com.plus33.erp.intelligence.query;

import org.springframework.stereotype.Service;

@Service
public class QueryPlanner {
    public String planQuery(String queryText) {
        return "{\"plan\": \"traverse graph for " + queryText + "\"}";
    }
}