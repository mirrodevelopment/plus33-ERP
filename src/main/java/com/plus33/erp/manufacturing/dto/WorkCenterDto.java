/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : WorkCenterDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WorkCenterDtoController
 * Related Service   : WorkCenterDtoService, WorkCenterDtoServiceImpl
 * Related Repository: WorkCenterDtoRepository
 * Related Entity    : WorkCenterDto
 * Related DTO       : WorkCenterDto
 * Related Mapper    : WorkCenterDtoMapper
 * Related DB Table  : work_center_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WorkCenterDtoController, WorkCenterDtoService, WorkCenterDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
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
     * Retrieves code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCode() { return code; }
    /**
     * Performs the setCode operation in this module.
     *
     * @param code the code input value
     */
    public void setCode(String code) { this.code = code; }
    /**
     * Retrieves name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getName() { return name; }
    /**
     * Performs the setName operation in this module.
     *
     * @param name the name input value
     */
    public void setName(String name) { this.name = name; }
    /**
     * Retrieves description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; }
    /**
     * Performs the setDescription operation in this module.
     *
     * @param description the description input value
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * Retrieves department data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDepartment() { return department; }
    /**
     * Performs the setDepartment operation in this module.
     *
     * @param department the department input value
     */
    public void setDepartment(String department) { this.department = department; }
    /**
     * Retrieves work center type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getWorkCenterType() { return workCenterType; }
    /**
     * Performs the setWorkCenterType operation in this module.
     *
     * @param workCenterType the workCenterType input value
     */
    public void setWorkCenterType(String workCenterType) { this.workCenterType = workCenterType; }
    /**
     * Retrieves machine capacity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMachineCapacity() { return machineCapacity; }
    /**
     * Performs the setMachineCapacity operation in this module.
     *
     * @param machineCapacity the machineCapacity input value
     */
    public void setMachineCapacity(BigDecimal machineCapacity) { this.machineCapacity = machineCapacity; }
    /**
     * Retrieves labor capacity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLaborCapacity() { return laborCapacity; }
    /**
     * Performs the setLaborCapacity operation in this module.
     *
     * @param laborCapacity the laborCapacity input value
     */
    public void setLaborCapacity(BigDecimal laborCapacity) { this.laborCapacity = laborCapacity; }
    /**
     * Retrieves hourly machine rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getHourlyMachineRate() { return hourlyMachineRate; }
    /**
     * Performs the setHourlyMachineRate operation in this module.
     *
     * @param hourlyMachineRate the hourlyMachineRate input value
     */
    public void setHourlyMachineRate(BigDecimal hourlyMachineRate) { this.hourlyMachineRate = hourlyMachineRate; }
    /**
     * Retrieves hourly labor rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getHourlyLaborRate() { return hourlyLaborRate; }
    /**
     * Performs the setHourlyLaborRate operation in this module.
     *
     * @param hourlyLaborRate the hourlyLaborRate input value
     */
    public void setHourlyLaborRate(BigDecimal hourlyLaborRate) { this.hourlyLaborRate = hourlyLaborRate; }
    /**
     * Retrieves hourly overhead rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getHourlyOverheadRate() { return hourlyOverheadRate; }
    /**
     * Performs the setHourlyOverheadRate operation in this module.
     *
     * @param hourlyOverheadRate the hourlyOverheadRate input value
     */
    public void setHourlyOverheadRate(BigDecimal hourlyOverheadRate) { this.hourlyOverheadRate = hourlyOverheadRate; }
    /**
     * Retrieves capacity hours per day data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCapacityHoursPerDay() { return capacityHoursPerDay; }
    /**
     * Performs the setCapacityHoursPerDay operation in this module.
     *
     * @param capacityHoursPerDay the capacityHoursPerDay input value
     */
    public void setCapacityHoursPerDay(BigDecimal capacityHoursPerDay) { this.capacityHoursPerDay = capacityHoursPerDay; }
    /**
     * Retrieves overhead rate type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOverheadRateType() { return overheadRateType; }
    /**
     * Performs the setOverheadRateType operation in this module.
     *
     * @param overheadRateType the overheadRateType input value
     */
    public void setOverheadRateType(String overheadRateType) { this.overheadRateType = overheadRateType; }
    /**
     * Retrieves queue time hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getQueueTimeHours() { return queueTimeHours; }
    /**
     * Performs the setQueueTimeHours operation in this module.
     *
     * @param queueTimeHours the queueTimeHours input value
     */
    public void setQueueTimeHours(BigDecimal queueTimeHours) { this.queueTimeHours = queueTimeHours; }
    /**
     * Retrieves move time hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMoveTimeHours() { return moveTimeHours; }
    /**
     * Performs the setMoveTimeHours operation in this module.
     *
     * @param moveTimeHours the moveTimeHours input value
     */
    public void setMoveTimeHours(BigDecimal moveTimeHours) { this.moveTimeHours = moveTimeHours; }
    /**
     * Retrieves efficiency factor data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEfficiencyFactor() { return efficiencyFactor; }
    /**
     * Performs the setEfficiencyFactor operation in this module.
     *
     * @param efficiencyFactor the efficiencyFactor input value
     */
    public void setEfficiencyFactor(BigDecimal efficiencyFactor) { this.efficiencyFactor = efficiencyFactor; }
    /**
     * Retrieves gl account id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getGlAccountId() { return glAccountId; }
    /**
     * Performs the setGlAccountId operation in this module.
     *
     * @param glAccountId the glAccountId input value
     */
    public void setGlAccountId(Long glAccountId) { this.glAccountId = glAccountId; }
    /**
     * Retrieves active data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(Boolean active) { this.active = active; }
}
