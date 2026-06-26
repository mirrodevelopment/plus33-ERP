package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.AllocationStatus;
import com.plus33.erp.sales.entity.InventoryAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryAllocationRepository extends JpaRepository<InventoryAllocation, Long>, JpaSpecificationExecutor<InventoryAllocation> {

    List<InventoryAllocation> findByPickListId(Long pickListId);

    List<InventoryAllocation> findByPickListIdAndAllocationStatus(Long pickListId, AllocationStatus status);
}
