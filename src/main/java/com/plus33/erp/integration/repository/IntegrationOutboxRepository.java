package com.plus33.erp.integration.repository;

import com.plus33.erp.integration.entity.IntegrationOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrationOutboxRepository extends JpaRepository<IntegrationOutbox, Long> {
}