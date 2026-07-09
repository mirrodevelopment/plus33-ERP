/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : MrpRunRequest.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MrpRunController
 * Related Service   : MrpRunService, MrpRunServiceImpl
 * Related Repository: MrpRunRepository
 * Related Entity    : MrpRun
 * Related DTO       : MrpRunRequest
 * Related Mapper    : MrpRunMapper
 * Related DB Table  : mrp_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MrpRunController, MrpRunService, MrpRunServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.*;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code MrpRunRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class MrpRunRequest {
    @NotNull private Long companyId;
    private Integer planningHorizonDays = 90;
    private Boolean includeForecasts = true;
    private Boolean includeSalesOrders = true;
    private Boolean includeSafetyStock = true;
    private Long executedBy;
    private String notes;
    private String runType = "REGENERATIVE";
    private List<DemandItem> demandItems;

    public MrpRunRequest() {}

    /**
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves planning horizon days data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPlanningHorizonDays() { return planningHorizonDays; }
    /**
     * Performs the setPlanningHorizonDays operation in this module.
     *
     * @param planningHorizonDays the planningHorizonDays input value
     */
    public void setPlanningHorizonDays(Integer planningHorizonDays) { this.planningHorizonDays = planningHorizonDays; }
    /**
     * Retrieves include forecasts data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIncludeForecasts() { return includeForecasts; }
    /**
     * Performs the setIncludeForecasts operation in this module.
     *
     * @param includeForecasts the includeForecasts input value
     */
    public void setIncludeForecasts(Boolean includeForecasts) { this.includeForecasts = includeForecasts; }
    /**
     * Retrieves include sales orders data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIncludeSalesOrders() { return includeSalesOrders; }
    /**
     * Performs the setIncludeSalesOrders operation in this module.
     *
     * @param includeSalesOrders the includeSalesOrders input value
     */
    public void setIncludeSalesOrders(Boolean includeSalesOrders) { this.includeSalesOrders = includeSalesOrders; }
    /**
     * Retrieves include safety stock data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIncludeSafetyStock() { return includeSafetyStock; }
    /**
     * Performs the setIncludeSafetyStock operation in this module.
     *
     * @param includeSafetyStock the includeSafetyStock input value
     */
    public void setIncludeSafetyStock(Boolean includeSafetyStock) { this.includeSafetyStock = includeSafetyStock; }
    /**
     * Retrieves executed by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getExecutedBy() { return executedBy; }
    /**
     * Performs the setExecutedBy operation in this module.
     *
     * @param executedBy the executedBy input value
     */
    public void setExecutedBy(Long executedBy) { this.executedBy = executedBy; }
    /**
     * Retrieves notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNotes() { return notes; }
    /**
     * Performs the setNotes operation in this module.
     *
     * @param notes the notes input value
     */
    public void setNotes(String notes) { this.notes = notes; }
    /**
     * Retrieves run type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRunType() { return runType; }
    /**
     * Performs the setRunType operation in this module.
     *
     * @param runType the runType input value
     */
    public void setRunType(String runType) { this.runType = runType; }
    /**
     * Retrieves demand items data from the database.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<DemandItem> getDemandItems() { return demandItems; }
    /**
     * Performs the setDemandItems operation in this module.
     *
     * @param demandItems the demandItems input value
     */
    public void setDemandItems(List<DemandItem> demandItems) { this.demandItems = demandItems; }

    public static class DemandItem {
        @NotNull private Long productId;
        @NotNull @Positive private BigDecimal quantity;
        @NotNull private LocalDate dueDate;
        private String sourceType; // SALES_ORDER, FORECAST
        private Long sourceReferenceId;

        public DemandItem() {}

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public BigDecimal getQuantity() { return quantity; }
        public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
        public LocalDate getDueDate() { return dueDate; }
        public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
        public String getSourceType() { return sourceType; }
        public void setSourceType(String sourceType) { this.sourceType = sourceType; }
        public Long getSourceReferenceId() { return sourceReferenceId; }
        public void setSourceReferenceId(Long sourceReferenceId) { this.sourceReferenceId = sourceReferenceId; }
    }
}
