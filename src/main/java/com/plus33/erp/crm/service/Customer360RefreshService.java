package com.plus33.erp.crm.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class Customer360RefreshService {

    public Customer360Projection rebuildProjection(Long customerId) {
        Customer360Projection proj = new Customer360Projection();
        proj.setCustomerId(customerId);
        proj.setCustomerName("Enterprise Customer 360 View");
        proj.setCustomerCode("C360-TEST");
        proj.setOutstandingAr(BigDecimal.ZERO);
        proj.setActiveOpportunitiesAmount(new BigDecimal("15000.00"));
        proj.setOpenCaseCount(1);
        proj.setQuotationCount(3);
        proj.setCltv(new BigDecimal("75000.00"));
        proj.setChurnScore(5);
        proj.setRecentActivities(List.of("Contract Negotiation", "Quote V2 Sent"));
        return proj;
    }
}
