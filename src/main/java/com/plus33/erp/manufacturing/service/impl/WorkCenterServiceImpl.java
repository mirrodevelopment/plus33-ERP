/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service.impl
 * File              : WorkCenterServiceImpl.java
 * Purpose           : Business logic service layer for Manufacturing Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WorkCenterController
 * Related Service   : WorkCenterServiceImpl
 * Related Repository: WorkCenterRepository
 * Related Entity    : WorkCenter
 * Related DTO       : CreateWorkCenterRequest, mapToDto, WorkCenterDto
 * Related Mapper    : WorkCenterMapper
 * Related DB Table  : work_centers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WorkCenterController, WorkCenterServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Manufacturing Module. Implements WorkCenterService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.manufacturing.service.impl;

import com.plus33.erp.manufacturing.dto.*;
import com.plus33.erp.manufacturing.entity.*;
import com.plus33.erp.manufacturing.repository.WorkCenterRepository;
import com.plus33.erp.manufacturing.service.WorkCenterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code WorkCenterServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.service.impl}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Manufacturing Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * WorkCenterController
 *   --> WorkCenterServiceImpl (this)
 *   --> Validate business rules
 *   --> WorkCenterRepository (read/write 'work_centers')
 *   --> WorkCenterMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code work_centers}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class WorkCenterServiceImpl implements WorkCenterService {

    private final WorkCenterRepository workCenterRepository;

    public WorkCenterServiceImpl(WorkCenterRepository workCenterRepository) {
        this.workCenterRepository = workCenterRepository;
    }

    /**
     * Creates a new work center and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the WorkCenterDto result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Creates a new work center and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the WorkCenterDto result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public WorkCenterDto createWorkCenter(CreateWorkCenterRequest request) {
        if (workCenterRepository.existsByCompanyIdAndCode(request.getCompanyId(), request.getCode())) {
            throw new IllegalArgumentException("Work Center code already exists: " + request.getCode());
        }

        WorkCenter wc = new WorkCenter();
        wc.setCompanyId(request.getCompanyId());
        wc.setCode(request.getCode());
        wc.setName(request.getName());
        wc.setDescription(request.getDescription());
        wc.setDepartment(request.getDepartment());
        wc.setWorkCenterType(request.getWorkCenterType() != null ? request.getWorkCenterType() : "MACHINE");
        wc.setMachineCapacity(request.getMachineCapacity() != null ? request.getMachineCapacity() : BigDecimal.ONE);
        wc.setLaborCapacity(request.getLaborCapacity() != null ? request.getLaborCapacity() : BigDecimal.ONE);
        wc.setHourlyMachineRate(request.getHourlyMachineRate() != null ? request.getHourlyMachineRate() : BigDecimal.ZERO);
        wc.setHourlyLaborRate(request.getHourlyLaborRate());
        wc.setOverheadRate(request.getHourlyOverheadRate());
        wc.setOverheadRateType(request.getOverheadRateType() != null ? request.getOverheadRateType() : "MACHINE_HOUR");
        wc.setQueueTimeHours(request.getQueueTimeHours() != null ? request.getQueueTimeHours() : BigDecimal.ZERO);
        wc.setMoveTimeHours(request.getMoveTimeHours() != null ? request.getMoveTimeHours() : BigDecimal.ZERO);
        wc.setEfficiencyFactor(request.getEfficiencyFactor() != null ? request.getEfficiencyFactor() : new BigDecimal("100.00"));
        wc.setGlAccountId(request.getGlAccountId());
        wc.setActive(true);

        wc = workCenterRepository.save(wc);
        return mapToDto(wc);
    }

    /**
     * Retrieves a single work center by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the WorkCenterDto result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public WorkCenterDto getWorkCenterById(Long id) {
        WorkCenter wc = workCenterRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Work Center not found with ID: " + id));
        return mapToDto(wc);
    }

    /**
     * Retrieves work centers by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkCenterDto> getWorkCentersByCompany(Long companyId) {
        return workCenterRepository.findByCompanyId(companyId)
                .stream().map(this::mapToDto).toList();
    }

    private WorkCenterDto mapToDto(WorkCenter wc) {
        WorkCenterDto dto = new WorkCenterDto();
        dto.setId(wc.getId());
        dto.setCompanyId(wc.getCompanyId());
        dto.setCode(wc.getCode());
        dto.setName(wc.getName());
        dto.setDescription(wc.getDescription());
        dto.setDepartment(wc.getDepartment());
        dto.setWorkCenterType(wc.getWorkCenterType());
        dto.setMachineCapacity(wc.getMachineCapacity());
        dto.setLaborCapacity(wc.getLaborCapacity());
        dto.setHourlyMachineRate(wc.getHourlyMachineRate());
        dto.setHourlyLaborRate(wc.getHourlyLaborRate());
        dto.setHourlyOverheadRate(wc.getOverheadRate());
        dto.setCapacityHoursPerDay(BigDecimal.valueOf(8)); // Default capacity hours per day
        dto.setOverheadRateType(wc.getOverheadRateType());
        dto.setQueueTimeHours(wc.getQueueTimeHours());
        dto.setMoveTimeHours(wc.getMoveTimeHours());
        dto.setEfficiencyFactor(wc.getEfficiencyFactor());
        dto.setGlAccountId(wc.getGlAccountId());
        dto.setActive(wc.getActive());
        return dto;
    }
}