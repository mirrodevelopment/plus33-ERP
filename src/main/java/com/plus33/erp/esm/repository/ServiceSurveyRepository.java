package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.ServiceSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ServiceSurveyRepository extends JpaRepository<ServiceSurvey, Long> {
    Optional<ServiceSurvey> findByWorkOrderId(Long workOrderId);
}
