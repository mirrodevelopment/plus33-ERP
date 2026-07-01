package com.plus33.erp.project.service;

import com.plus33.erp.project.entity.ResourceAssignment;
import com.plus33.erp.project.repository.ResourceAssignmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ResourceCapacityEngine {

    private final ResourceAssignmentRepository assignmentRepository;

    public ResourceCapacityEngine(ResourceAssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Transactional(readOnly = true)
    public boolean checkOverAllocation(Long resourceId) {
        List<ResourceAssignment> assignments = assignmentRepository.findByResourceId(resourceId);
        BigDecimal totalAllocation = BigDecimal.ZERO;
        for (ResourceAssignment a : assignments) {
            totalAllocation = totalAllocation.add(a.getAllocationPercentage());
        }
        return totalAllocation.compareTo(new BigDecimal("100.00")) > 0;
    }
}
