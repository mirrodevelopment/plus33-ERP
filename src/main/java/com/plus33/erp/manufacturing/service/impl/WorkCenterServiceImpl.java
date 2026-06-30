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

@Service
@Transactional
public class WorkCenterServiceImpl implements WorkCenterService {

    private final WorkCenterRepository workCenterRepository;

    public WorkCenterServiceImpl(WorkCenterRepository workCenterRepository) {
        this.workCenterRepository = workCenterRepository;
    }

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

    @Override
    @Transactional(readOnly = true)
    public WorkCenterDto getWorkCenterById(Long id) {
        WorkCenter wc = workCenterRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Work Center not found with ID: " + id));
        return mapToDto(wc);
    }

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
