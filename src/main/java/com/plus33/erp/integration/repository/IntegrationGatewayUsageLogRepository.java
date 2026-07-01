package com.plus33.erp.integration.repository;

import com.plus33.erp.integration.entity.IntegrationGatewayUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrationGatewayUsageLogRepository extends JpaRepository<IntegrationGatewayUsageLog, Long> {
}