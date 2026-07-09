/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : PayrollApprovalWorkflow.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollApprovalWorkflowController
 * Related Service   : PayrollApprovalWorkflowService, PayrollApprovalWorkflowServiceImpl
 * Related Repository: PayrollApprovalWorkflowRepository
 * Related Entity    : PayrollApprovalWorkflow
 * Related DTO       : N/A
 * Related Mapper    : PayrollApprovalWorkflowMapper
 * Related DB Table  : payroll_approval_workflows
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollApprovalWorkflowRepository, PayrollApprovalWorkflowMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'payroll_approval_workflows'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollApprovalWorkflow}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'payroll_approval_workflows'.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_approval_workflows}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "payroll_approval_workflows")
public class PayrollApprovalWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payroll_run_id", nullable = false)
    private Long payrollRunId;

    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    @Column(name = "approver_role", nullable = false, length = 50)
    private String approverRole;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";

    private String comments;

    @Column(name = "actioned_at")
    private LocalDateTime actionedAt;

    public PayrollApprovalWorkflow() {}

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves payroll run id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPayrollRunId() { return payrollRunId; }
    /**
     * Performs the setPayrollRunId operation in this module.
     *
     * @param payrollRunId the payrollRunId input value
     */
    public void setPayrollRunId(Long payrollRunId) { this.payrollRunId = payrollRunId; }
    /**
     * Retrieves step number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getStepNumber() { return stepNumber; }
    /**
     * Performs the setStepNumber operation in this module.
     *
     * @param stepNumber the stepNumber input value
     */
    public void setStepNumber(Integer stepNumber) { this.stepNumber = stepNumber; }
    /**
     * Retrieves approver role data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getApproverRole() { return approverRole; }
    /**
     * Performs the setApproverRole operation in this module.
     *
     * @param approverRole the approverRole input value
     */
    public void setApproverRole(String approverRole) { this.approverRole = approverRole; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Retrieves comments data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getComments() { return comments; }
    /**
     * Performs the setComments operation in this module.
     *
     * @param comments the comments input value
     */
    public void setComments(String comments) { this.comments = comments; }
    /**
     * Retrieves actioned at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getActionedAt() { return actionedAt; }
    /**
     * Performs the setActionedAt operation in this module.
     *
     * @param actionedAt the actionedAt input value
     */
    public void setActionedAt(LocalDateTime actionedAt) { this.actionedAt = actionedAt; }
}