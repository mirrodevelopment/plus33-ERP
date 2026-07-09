/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.mdm
 * File              : MergeDecision.java
 * Purpose           : Component of Bi Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MergeDecisionController
 * Related Service   : MergeDecisionService, MergeDecisionServiceImpl
 * Related Repository: MergeDecisionRepository
 * Related Entity    : MergeDecision
 * Related DTO       : N/A
 * Related Mapper    : MergeDecisionMapper
 * Related DB Table  : merge_decisions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Bi Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.bi.mdm;

public class MergeDecision {
    private String targetDisplayName;
    private String targetEmail;
    private String targetPhone;
    private String targetAddress;
    private String targetTaxNumber;

    /**
     * Retrieves target display name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTargetDisplayName() { return targetDisplayName; }
    /**
     * Performs the setTargetDisplayName operation in this module.
     *
     * @param targetDisplayName the targetDisplayName input value
     */
    public void setTargetDisplayName(String targetDisplayName) { this.targetDisplayName = targetDisplayName; }
    /**
     * Retrieves target email data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTargetEmail() { return targetEmail; }
    /**
     * Performs the setTargetEmail operation in this module.
     *
     * @param targetEmail the targetEmail input value
     */
    public void setTargetEmail(String targetEmail) { this.targetEmail = targetEmail; }
    /**
     * Retrieves target phone data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTargetPhone() { return targetPhone; }
    /**
     * Performs the setTargetPhone operation in this module.
     *
     * @param targetPhone the targetPhone input value
     */
    public void setTargetPhone(String targetPhone) { this.targetPhone = targetPhone; }
    /**
     * Retrieves target address data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTargetAddress() { return targetAddress; }
    /**
     * Performs the setTargetAddress operation in this module.
     *
     * @param targetAddress the targetAddress input value
     */
    public void setTargetAddress(String targetAddress) { this.targetAddress = targetAddress; }
    /**
     * Retrieves target tax number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTargetTaxNumber() { return targetTaxNumber; }
    /**
     * Performs the setTargetTaxNumber operation in this module.
     *
     * @param targetTaxNumber the targetTaxNumber input value
     */
    public void setTargetTaxNumber(String targetTaxNumber) { this.targetTaxNumber = targetTaxNumber; }
}