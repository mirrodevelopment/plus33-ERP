/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.service
 * File              : Customer360Projection.java
 * Purpose           : Component of Crm Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: Customer360ProjectionController
 * Related Service   : Customer360ProjectionService, Customer360ProjectionServiceImpl
 * Related Repository: Customer360ProjectionRepository
 * Related Entity    : Customer360Projection
 * Related DTO       : N/A
 * Related Mapper    : Customer360ProjectionMapper
 * Related DB Table  : customer360_projections
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Crm Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Crm Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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
    /**
     * Retrieves customer id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCustomerId() { return customerId; }
    /**
     * Performs the setCustomerId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setCustomerId(Long id) { this.customerId = id; }
    /**
     * Retrieves customer name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCustomerName() { return customerName; }
    /**
     * Performs the setCustomerName operation in this module.
     *
     * @param name the name input value
     */
    public void setCustomerName(String name) { this.customerName = name; }
    /**
     * Retrieves customer code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCustomerCode() { return customerCode; }
    /**
     * Performs the setCustomerCode operation in this module.
     *
     * @param code the code input value
     */
    public void setCustomerCode(String code) { this.customerCode = code; }
    /**
     * Retrieves outstanding ar data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getOutstandingAr() { return outstandingAr; }
    /**
     * Performs the setOutstandingAr operation in this module.
     *
     * @param ar the ar input value
     */
    public void setOutstandingAr(BigDecimal ar) { this.outstandingAr = ar; }
    /**
     * Retrieves active opportunities amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActiveOpportunitiesAmount() { return activeOpportunitiesAmount; }
    /**
     * Performs the setActiveOpportunitiesAmount operation in this module.
     *
     * @param amt the amt input value
     */
    public void setActiveOpportunitiesAmount(BigDecimal amt) { this.activeOpportunitiesAmount = amt; }
    /**
     * Retrieves open case count data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getOpenCaseCount() { return openCaseCount; }
    /**
     * Performs the setOpenCaseCount operation in this module.
     *
     * @param count the count input value
     */
    public void setOpenCaseCount(int count) { this.openCaseCount = count; }
    /**
     * Retrieves quotation count data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getQuotationCount() { return quotationCount; }
    /**
     * Performs the setQuotationCount operation in this module.
     *
     * @param count the count input value
     */
    public void setQuotationCount(int count) { this.quotationCount = count; }
    /**
     * Retrieves cltv data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCltv() { return cltv; }
    /**
     * Performs the setCltv operation in this module.
     *
     * @param cltv the cltv input value
     */
    public void setCltv(BigDecimal cltv) { this.cltv = cltv; }
    /**
     * Retrieves churn score data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getChurnScore() { return churnScore; }
    /**
     * Performs the setChurnScore operation in this module.
     *
     * @param score the score input value
     */
    public void setChurnScore(int score) { this.churnScore = score; }
    /**
     * Retrieves recent activities data from the database.
     *
     * @return List of matching records the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<String> getRecentActivities() { return recentActivities; }
    /**
     * Performs the setRecentActivities operation in this module.
     *
     * @param activities the activities input value
     */
    public void setRecentActivities(List<String> activities) { this.recentActivities = activities; }
}
