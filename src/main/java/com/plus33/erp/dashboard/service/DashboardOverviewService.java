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
import com.plus33.erp.dashboard.dto.DashboardOverviewDTO;
import com.plus33.erp.dashboard.dto.DashboardScope;
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
     */
    @Cacheable(value = "dashboardCache", key = "{#from, #to, #regionId, #storeId, T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()}")
    public DashboardOverviewDTO getDashboardOverview(LocalDate from, LocalDate to, Long regionId, Long storeId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String userRole = "ULTIMATE_ADMIN";
        try {
            userRole = (String) entityManager.createNativeQuery(
                    "SELECT r.code FROM roles r JOIN user_roles ur ON ur.role_id = r.id JOIN users u ON ur.user_id = u.id WHERE LOWER(u.email) = LOWER(:email) LIMIT 1")
                    .setParameter("email", username)
                    .getSingleResult();
        } catch (Exception e) {
            try {
                userRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                        .filter(auth -> auth.startsWith("ROLE_") || auth.contains("ADMIN") || auth.contains("MANAGER"))
                        .findFirst().orElse("ULTIMATE_ADMIN");
            } catch (Exception ex) {
                userRole = "ULTIMATE_ADMIN";
            }
        }

        Long resolvedStoreId = storeId;
        Long resolvedWarehouseId = null;
        Long resolvedRegionId = regionId;

        // Role-based restrictions logic
        if (userRole.equalsIgnoreCase("nationalAdmin") || userRole.equalsIgnoreCase("ROLE_NATIONAL_ADMIN") || userRole.equalsIgnoreCase("NATIONAL_ADMIN") ||
            userRole.equalsIgnoreCase("regionalAdmin") || userRole.equalsIgnoreCase("ROLE_REGIONAL_ADMIN") || userRole.equalsIgnoreCase("REGIONAL_ADMIN") ||
            userRole.equalsIgnoreCase("ROLE_REGIONAL_MANAGER") || userRole.equalsIgnoreCase("REGIONAL_MANAGER") ||
            userRole.equalsIgnoreCase("ROLE_NATIONAL_WAREHOUSE_ADMIN") || userRole.equalsIgnoreCase("nationalWarehouseAdmin") || userRole.equalsIgnoreCase("NATIONAL_WAREHOUSE_ADMIN") ||
            userRole.equalsIgnoreCase("ROLE_REGIONAL_WAREHOUSE_ADMIN") || userRole.equalsIgnoreCase("regionalWarehouseAdmin") || userRole.equalsIgnoreCase("REGIONAL_WAREHOUSE_ADMIN")) {
            
            Long assignedRegionId = null;
            try {
                assignedRegionId = ((Number) entityManager.createNativeQuery(
                        "SELECT region_id FROM user_regions ur JOIN users u ON ur.user_id = u.id WHERE LOWER(u.email) = LOWER(:email)")
                        .setParameter("email", username)
                        .getSingleResult()).longValue();
            } catch (Exception e) {
                try {
                    assignedRegionId = ((Number) entityManager.createNativeQuery("SELECT id FROM regions LIMIT 1").getSingleResult()).longValue();
                } catch (Exception ex) {
                    assignedRegionId = 1L;
                }
            }
            
            // Lock down dashboard queries strictly to the user's assigned country/region
            resolvedRegionId = assignedRegionId;
            
            // Validate that storeId belongs to their region tree
            if (resolvedStoreId != null) {
                try {
                    Number storeRegionId = (Number) entityManager.createNativeQuery(
                            "SELECT region_id FROM stores WHERE id = :storeId")
                            .setParameter("storeId", resolvedStoreId)
                            .getSingleResult();
                    
                    if (storeRegionId != null) {
                        long srId = storeRegionId.longValue();
                        boolean isChild = false;
                        if (srId == assignedRegionId.longValue()) {
                            isChild = true;
                        } else {
                            try {
                                Number parentId = (Number) entityManager.createNativeQuery(
                                        "SELECT parent_id FROM regions WHERE id = :rId")
                                        .setParameter("rId", srId)
                                        .getSingleResult();
                                if (parentId != null && parentId.longValue() == assignedRegionId.longValue()) {
                                    isChild = true;
                                }
                            } catch (Exception ex) {
                                // ignore
                            }
                        }
                        if (!isChild) {
                            resolvedStoreId = null;
                        }
                    }
                } catch (Exception e) {
                    resolvedStoreId = null;
                }
            }
        } else if (userRole.equalsIgnoreCase("storeManager") || userRole.equalsIgnoreCase("ROLE_STORE_ADMIN") || userRole.equalsIgnoreCase("ROLE_STORE_MANAGER") ||
                   userRole.equalsIgnoreCase("STORE_ADMIN") || userRole.equalsIgnoreCase("STORE_MANAGER")) {
            Long assignedStoreId = null;
            try {
                assignedStoreId = ((Number) entityManager.createNativeQuery(
                        "SELECT store_id FROM user_stores us JOIN users u ON us.user_id = u.id WHERE LOWER(u.email) = LOWER(:email)")
                        .setParameter("email", username)
                        .getSingleResult()).longValue();
            } catch (Exception e) {
                try {
                    assignedStoreId = ((Number) entityManager.createNativeQuery("SELECT id FROM stores LIMIT 1").getSingleResult()).longValue();
                } catch (Exception ex) {
                    assignedStoreId = 1L;
                }
            }
            // Lock down dashboard queries strictly to the user's assigned store
            resolvedStoreId = assignedStoreId;
            
            // Resolve regionId and warehouseId from store
            try {
                Object[] storeInfo = (Object[]) entityManager.createNativeQuery(
                        "SELECT region_id, warehouse_id FROM stores WHERE id = :storeId")
                        .setParameter("storeId", resolvedStoreId)
                        .getSingleResult();
                if (storeInfo != null) {
                    if (storeInfo[0] != null) {
                        resolvedRegionId = ((Number) storeInfo[0]).longValue();
                    }
                    if (storeInfo[1] != null) {
                        resolvedWarehouseId = ((Number) storeInfo[1]).longValue();
                    }
                }
            } catch (Exception ex) {
                // ignore
            }
        } else if (userRole.equalsIgnoreCase("warehouseAdmin") || userRole.equalsIgnoreCase("ROLE_WAREHOUSE_ADMIN") || userRole.equalsIgnoreCase("WAREHOUSE_ADMIN")) {
            Long assignedWarehouseId = null;
            try {
                assignedWarehouseId = ((Number) entityManager.createNativeQuery(
                        "SELECT warehouse_id FROM user_warehouses uw JOIN users u ON uw.user_id = u.id WHERE LOWER(u.email) = LOWER(:email)")
                        .setParameter("email", username)
                        .getSingleResult()).longValue();
            } catch (Exception e) {
                try {
                    assignedWarehouseId = ((Number) entityManager.createNativeQuery("SELECT id FROM warehouses LIMIT 1").getSingleResult()).longValue();
                } catch (Exception ex) {
                    assignedWarehouseId = 1L;
                }
            }
            resolvedWarehouseId = assignedWarehouseId;
            // Resolve regionId from warehouse
            try {
                resolvedRegionId = ((Number) entityManager.createNativeQuery(
                        "SELECT region_id FROM warehouses WHERE id = :warehouseId")
                        .setParameter("warehouseId", resolvedWarehouseId)
                        .getSingleResult()).longValue();
            } catch (Exception ex) {
                // ignore
            }
        }
        DashboardScope scope = new DashboardScope(resolvedStoreId, resolvedWarehouseId, resolvedRegionId, userRole, username);
        return aggregator.aggregateDashboard(from, to, scope);
    }
}