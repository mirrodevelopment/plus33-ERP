package com.plus33.erp.crm.service;

import java.math.BigDecimal;
import java.util.List;

public class Customer360Projection {
    private Long customerId;
    private String customerName;
    private String customerCode;
    private BigDecimal outstandingAr = BigDecimal.ZERO;
    private BigDecimal activeOpportunitiesAmount = BigDecimal.ZERO;
    private int openCaseCount = 0;
    private int quotationCount = 0;
    private BigDecimal cltv = BigDecimal.ZERO;
    private int churnScore = 0;
    private List<String> recentActivities;

    // Getters and setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long id) { this.customerId = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String name) { this.customerName = name; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String code) { this.customerCode = code; }
    public BigDecimal getOutstandingAr() { return outstandingAr; }
    public void setOutstandingAr(BigDecimal ar) { this.outstandingAr = ar; }
    public BigDecimal getActiveOpportunitiesAmount() { return activeOpportunitiesAmount; }
    public void setActiveOpportunitiesAmount(BigDecimal amt) { this.activeOpportunitiesAmount = amt; }
    public int getOpenCaseCount() { return openCaseCount; }
    public void setOpenCaseCount(int count) { this.openCaseCount = count; }
    public int getQuotationCount() { return quotationCount; }
    public void setQuotationCount(int count) { this.quotationCount = count; }
    public BigDecimal getCltv() { return cltv; }
    public void setCltv(BigDecimal cltv) { this.cltv = cltv; }
    public int getChurnScore() { return churnScore; }
    public void setChurnScore(int score) { this.churnScore = score; }
    public List<String> getRecentActivities() { return recentActivities; }
    public void setRecentActivities(List<String> activities) { this.recentActivities = activities; }
}
