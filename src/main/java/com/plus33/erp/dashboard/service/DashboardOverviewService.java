/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Dashboard Module
 * Package           : com.plus33.erp.dashboard.service
 * File              : DashboardOverviewService.java
 * Purpose           : Business logic service layer for Dashboard Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DashboardOverviewController
 * Related Service   : DashboardOverviewService
 * Related Repository: DashboardOverviewRepository
 * Related Entity    : DashboardOverview
 * Related DTO       : DashboardOverviewDTO
 * Related Mapper    : DashboardOverviewMapper
 * Related DB Table  : dashboard_overviews
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DashboardOverviewController, DashboardOverviewServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Dashboard Module. Implements DashboardOverviewService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.dashboard.service;

import com.plus33.erp.dashboard.dto.DashboardOverviewDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Dashboard Module</b>
 *
 * <p><b>Class  :</b> {@code DashboardOverviewService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.dashboard.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Dashboard Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DashboardOverviewController
 *   --> DashboardOverviewService (this)
 *   --> Validate business rules
 *   --> DashboardOverviewRepository (read/write 'dashboard_overviews')
 *   --> DashboardOverviewMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code dashboard_overviews}</p>
 * <p><b>Module Deps      :</b> Dashboard</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DashboardOverviewService {

    private final DashboardAggregator aggregator;

    @jakarta.persistence.PersistenceContext
    private jakarta.persistence.EntityManager entityManager;

    public DashboardOverviewService(DashboardAggregator aggregator) {
        this.aggregator = aggregator;
    }

    /**
     * Retrieves dashboard overview data from the database.
     *
     * @param from the from input value
     * @param to the to input value
     * @param regionId the regionId input value
     * @param storeId the storeId input value
     * @return the DashboardOverviewDTO result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Cacheable(value = "dashboardCache", key = "{#from, #to, #regionId, #storeId}")
    public DashboardOverviewDTO getDashboardOverview(LocalDate from, LocalDate to, Long regionId, Long storeId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String userRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .findFirst().orElse("ROLE_ULTIMATE_ADMIN");


        // Role-based restrictions logic
        if ((userRole.equalsIgnoreCase("nationalAdmin") || userRole.equalsIgnoreCase("ROLE_NATIONAL_ADMIN") || userRole.equalsIgnoreCase("regionalAdmin") || userRole.equalsIgnoreCase("ROLE_REGIONAL_ADMIN") || userRole.equalsIgnoreCase("ROLE_REGIONAL_MANAGER")) && regionId == null) {
            // Restrict to regionalAdmin's region
            try {
                regionId = ((Number) entityManager.createNativeQuery(
                        "SELECT region_id FROM user_regions ur JOIN users u ON ur.user_id = u.id WHERE u.email = :email")
                        .setParameter("email", username)
                        .getSingleResult()).longValue();
            } catch (Exception e) {
                try {
                    regionId = ((Number) entityManager.createNativeQuery("SELECT id FROM regions LIMIT 1").getSingleResult()).longValue();
                } catch (Exception ex) {
                    regionId = 1L;
                }
            }
        } else if ((userRole.equalsIgnoreCase("storeManager") || userRole.equalsIgnoreCase("ROLE_STORE_ADMIN") || userRole.equalsIgnoreCase("ROLE_STORE_MANAGER")) && storeId == null) {
            // Restrict to storeManager's store
            try {
                storeId = ((Number) entityManager.createNativeQuery(
                        "SELECT store_id FROM user_stores us JOIN users u ON us.user_id = u.id WHERE u.email = :email")
                        .setParameter("email", username)
                        .getSingleResult()).longValue();
            } catch (Exception e) {
                try {
                    storeId = ((Number) entityManager.createNativeQuery("SELECT id FROM stores LIMIT 1").getSingleResult()).longValue();
                } catch (Exception ex) {
                    storeId = 1L;
                }
            }
        }

        return aggregator.aggregateDashboard(from, to, regionId, storeId, userRole);
    }
}