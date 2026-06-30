package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.*;

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

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Integer getPlanningHorizonDays() { return planningHorizonDays; }
    public void setPlanningHorizonDays(Integer planningHorizonDays) { this.planningHorizonDays = planningHorizonDays; }
    public Boolean getIncludeForecasts() { return includeForecasts; }
    public void setIncludeForecasts(Boolean includeForecasts) { this.includeForecasts = includeForecasts; }
    public Boolean getIncludeSalesOrders() { return includeSalesOrders; }
    public void setIncludeSalesOrders(Boolean includeSalesOrders) { this.includeSalesOrders = includeSalesOrders; }
    public Boolean getIncludeSafetyStock() { return includeSafetyStock; }
    public void setIncludeSafetyStock(Boolean includeSafetyStock) { this.includeSafetyStock = includeSafetyStock; }
    public Long getExecutedBy() { return executedBy; }
    public void setExecutedBy(Long executedBy) { this.executedBy = executedBy; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getRunType() { return runType; }
    public void setRunType(String runType) { this.runType = runType; }
    public List<DemandItem> getDemandItems() { return demandItems; }
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
