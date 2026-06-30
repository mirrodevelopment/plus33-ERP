package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;

public class WorkCenterDto {
    private Long id;
    private Long companyId;
    private String code;
    private String name;
    private String description;
    private String department;
    private String workCenterType;
    private BigDecimal machineCapacity;
    private BigDecimal laborCapacity;
    private BigDecimal hourlyMachineRate;
    private BigDecimal hourlyLaborRate;
    private BigDecimal hourlyOverheadRate;
    private BigDecimal capacityHoursPerDay;
    private String overheadRateType;
    private BigDecimal queueTimeHours;
    private BigDecimal moveTimeHours;
    private BigDecimal efficiencyFactor;
    private Long glAccountId;
    private Boolean active;

    public WorkCenterDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getWorkCenterType() { return workCenterType; }
    public void setWorkCenterType(String workCenterType) { this.workCenterType = workCenterType; }
    public BigDecimal getMachineCapacity() { return machineCapacity; }
    public void setMachineCapacity(BigDecimal machineCapacity) { this.machineCapacity = machineCapacity; }
    public BigDecimal getLaborCapacity() { return laborCapacity; }
    public void setLaborCapacity(BigDecimal laborCapacity) { this.laborCapacity = laborCapacity; }
    public BigDecimal getHourlyMachineRate() { return hourlyMachineRate; }
    public void setHourlyMachineRate(BigDecimal hourlyMachineRate) { this.hourlyMachineRate = hourlyMachineRate; }
    public BigDecimal getHourlyLaborRate() { return hourlyLaborRate; }
    public void setHourlyLaborRate(BigDecimal hourlyLaborRate) { this.hourlyLaborRate = hourlyLaborRate; }
    public BigDecimal getHourlyOverheadRate() { return hourlyOverheadRate; }
    public void setHourlyOverheadRate(BigDecimal hourlyOverheadRate) { this.hourlyOverheadRate = hourlyOverheadRate; }
    public BigDecimal getCapacityHoursPerDay() { return capacityHoursPerDay; }
    public void setCapacityHoursPerDay(BigDecimal capacityHoursPerDay) { this.capacityHoursPerDay = capacityHoursPerDay; }
    public String getOverheadRateType() { return overheadRateType; }
    public void setOverheadRateType(String overheadRateType) { this.overheadRateType = overheadRateType; }
    public BigDecimal getQueueTimeHours() { return queueTimeHours; }
    public void setQueueTimeHours(BigDecimal queueTimeHours) { this.queueTimeHours = queueTimeHours; }
    public BigDecimal getMoveTimeHours() { return moveTimeHours; }
    public void setMoveTimeHours(BigDecimal moveTimeHours) { this.moveTimeHours = moveTimeHours; }
    public BigDecimal getEfficiencyFactor() { return efficiencyFactor; }
    public void setEfficiencyFactor(BigDecimal efficiencyFactor) { this.efficiencyFactor = efficiencyFactor; }
    public Long getGlAccountId() { return glAccountId; }
    public void setGlAccountId(Long glAccountId) { this.glAccountId = glAccountId; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
