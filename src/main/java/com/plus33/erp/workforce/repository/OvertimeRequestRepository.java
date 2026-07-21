package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.OvertimeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OvertimeRequestRepository extends JpaRepository<OvertimeRequest, Long> {
    List<OvertimeRequest> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);
    List<OvertimeRequest> findByStoreIdAndStatusOrderByCreatedAtDesc(Long storeId, String status);
    List<OvertimeRequest> findByEmployeeIdAndStatusOrderByCreatedAtDesc(Long employeeId, String status);
}
