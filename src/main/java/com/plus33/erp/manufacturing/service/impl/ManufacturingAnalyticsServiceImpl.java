/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service.impl
 * File              : ManufacturingAnalyticsServiceImpl.java
 * Purpose           : Business logic service layer for Manufacturing Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingAnalyticsController
 * Related Service   : ManufacturingAnalyticsServiceImpl
 * Related Repository: ManufacturingAnalyticsRepository
 * Related Entity    : ManufacturingAnalytics
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingAnalyticsMapper
 * Related DB Table  : manufacturing_analyticss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ManufacturingAnalyticsController, ManufacturingAnalyticsServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Manufacturing Module. Implements ManufacturingAnalyticsService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.manufacturing.service.impl;

import com.plus33.erp.manufacturing.service.ManufacturingAnalyticsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingAnalyticsServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.service.impl}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Manufacturing Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ManufacturingAnalyticsController
 *   --> ManufacturingAnalyticsServiceImpl (this)
 *   --> Validate business rules
 *   --> ManufacturingAnalyticsRepository (read/write 'manufacturing_analyticss')
 *   --> ManufacturingAnalyticsMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code manufacturing_analyticss}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class ManufacturingAnalyticsServiceImpl implements ManufacturingAnalyticsService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Retrieves production dashboard data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getProductionDashboard(Long companyId) {
        String sql = "SELECT status, order_count, total_target_qty, total_completed_qty, total_scrapped_qty, total_actual_cost, total_standard_cost, planning_month FROM mv_production_dashboard WHERE company_id = :companyId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("companyId", companyId);
        return mapResultList(query.getResultList(), new String[]{"status", "order_count", "total_target_qty", "total_completed_qty", "total_scrapped_qty", "total_actual_cost", "total_standard_cost", "planning_month"});
    }

    /**
     * Retrieves wip valuation data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getWipValuation(Long companyId) {
        String sql = "SELECT product_id, wip_orders, total_material_cost, total_labor_cost, total_machine_cost, total_overhead_cost, total_wip_balance, total_variance FROM mv_wip_valuation WHERE company_id = :companyId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("companyId", companyId);
        return mapResultList(query.getResultList(), new String[]{"product_id", "wip_orders", "total_material_cost", "total_labor_cost", "total_machine_cost", "total_overhead_cost", "total_wip_balance", "total_variance"});
    }

    /**
     * Retrieves oee summary data from the database.
     *
     * @param workCenterId the workCenterId input value
     * @return List of matching records the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getOeeSummary(Long workCenterId) {
        String sql = "SELECT week_start, total_run_hours, total_planned_hours, completed_qty, scrapped_qty, availability_pct, first_pass_yield_pct FROM mv_oee_summary WHERE work_center_id = :workCenterId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("workCenterId", workCenterId);
        return mapResultList(query.getResultList(), new String[]{"week_start", "total_run_hours", "total_planned_hours", "completed_qty", "scrapped_qty", "availability_pct", "first_pass_yield_pct"});
    }

    /**
     * Retrieves manufacturing variances data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getManufacturingVariances(Long companyId) {
        String sql = "SELECT product_id, posting_month, order_count, material_variance, labor_variance, machine_variance, overhead_variance, total_variance FROM mv_manufacturing_variances WHERE company_id = :companyId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("companyId", companyId);
        return mapResultList(query.getResultList(), new String[]{"product_id", "posting_month", "order_count", "material_variance", "labor_variance", "machine_variance", "overhead_variance", "total_variance"});
    }

    private Boolean viewsExist = null;

    private boolean doMaterializedViewsExist() {
        if (viewsExist != null) {
            return viewsExist;
        }
        try {
            String sql = "SELECT COUNT(*) FROM pg_class WHERE relname = 'mv_production_dashboard'";
            Number count = (Number) entityManager.createNativeQuery(sql).getSingleResult();
            viewsExist = count != null && count.intValue() > 0;
        } catch (Exception e) {
            viewsExist = false;
        }
        return viewsExist;
    }

    /**
     * Performs the refreshMaterializedViews operation in this module.
     *
     */
    @Override
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void refreshMaterializedViews() {
        if (!doMaterializedViewsExist()) {
            return;
        }
        try {
            try {
                entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW CONCURRENTLY mv_production_dashboard").executeUpdate();
                entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW CONCURRENTLY mv_wip_valuation").executeUpdate();
            } catch (Exception e) {
                // Fallback to standard refresh if concurrent is not supported or indexing is incomplete in the environment
                entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW mv_production_dashboard").executeUpdate();
                entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW mv_wip_valuation").executeUpdate();
            }
            // Non-concurrent refresh for views lacking unique indexes
            entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW mv_oee_summary").executeUpdate();
            entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW mv_manufacturing_variances").executeUpdate();
        } catch (Exception e) {
            // Log warning but do not fail the parent transaction
            // (e.g. if materialized views are not created/supported in the current environment)
            System.err.println("Failed to refresh materialized views: " + e.getMessage());
        }
    }

    private List<Map<String, Object>> mapResultList(List<Object> results, String[] keys) {
        List<Map<String, Object>> mappedResults = new ArrayList<>();
        for (Object result : results) {
            Map<String, Object> map = new HashMap<>();
            if (result instanceof Object[]) {
                Object[] row = (Object[]) result;
                for (int i = 0; i < Math.min(row.length, keys.length); i++) {
                    map.put(keys[i], row[i]);
                }
            } else {
                if (keys.length > 0) {
                    map.put(keys[0], result);
                }
            }
            mappedResults.add(map);
        }
        return mappedResults;
    }
}