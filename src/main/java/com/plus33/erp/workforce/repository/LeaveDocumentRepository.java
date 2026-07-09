package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.LeaveDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveDocumentRepository extends JpaRepository<LeaveDocument, Long> {

    List<LeaveDocument> findByLeaveId(Long leaveId);

    boolean existsByLeaveId(Long leaveId);

    boolean existsByLeaveIdAndVerifiedTrue(Long leaveId);
}
