/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiDashboardSubscriptionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiDashboardSubscriptionController
 * Related Service   : BiDashboardSubscriptionService, BiDashboardSubscriptionServiceImpl
 * Related Repository: BiDashboardSubscriptionRepository
 * Related Entity    : BiDashboardSubscription
 * Related DTO       : N/A
 * Related Mapper    : BiDashboardSubscriptionMapper
 * Related DB Table  : bi_dashboard_subscriptions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiDashboardSubscriptionService, BiDashboardSubscriptionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_dashboard_subscriptions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiDashboardSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiDashboardSubscriptionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bi_dashboard_subscriptions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_dashboard_subscriptions}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BiDashboardSubscriptionRepository extends JpaRepository<BiDashboardSubscription, Long> {
}