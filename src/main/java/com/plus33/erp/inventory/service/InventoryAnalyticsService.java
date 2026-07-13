/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.service
 * File              : InventoryAnalyticsService.java
 * Purpose           : Business logic service layer for Inventory Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAnalyticsController
 * Related Service   : InventoryAnalyticsService
 * Related Repository: InventoryAnalyticsRepository
 * Related Entity    : InventoryAnalytics
 * Related DTO       : N/A
 * Related Mapper    : InventoryAnalyticsMapper
 * Related DB Table  : inventory_analyticss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryAnalyticsController, InventoryAnalyticsServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Inventory Module. Implements InventoryAnalyticsService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.inventory.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plus33.erp.dashboard.dto.DashboardScope;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryAnalyticsService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Inventory Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * InventoryAnalyticsController
 *   --> InventoryAnalyticsService (this)
 *   --> Validate business rules
 *   --> InventoryAnalyticsRepository (read/write 'inventory_analyticss')
 *   --> InventoryAnalyticsMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code inventory_analyticss}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class InventoryAnalyticsService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Retrieves inventory value data from the database.
     *
     * @param scope the dashboard query scope context
     * @return the BigDecimal result
     */
    public BigDecimal getInventoryValue(DashboardScope scope) {
        Long storeId = scope != null ? scope.getStoreId() : null;
        Long warehouseId = scope != null ? scope.getWarehouseId() : null;
        Long regionId = scope != null ? scope.getRegionId() : null;

        StringBuilder sb = new StringBuilder("SELECT COALESCE(SUM(ls.quantity * COALESCE(ls.unitCost, 0)), 0) FROM LocationStock ls");
        sb.append(" JOIN Warehouse w ON w.id = ls.location.zone.warehouseId");
        if (storeId != null) {
            sb.append(" JOIN Store s ON s.warehouse.id = w.id");
        }
        sb.append(" WHERE 1=1");
        if (storeId != null) {
            sb.append(" AND s.id = :storeId");
        }
        if (warehouseId != null) {
            sb.append(" AND ls.location.zone.warehouseId = :warehouseId");
        }
        if (regionId != null) {
            sb.append(" AND (w.region.id = :regionId OR w.region.parent.id = :regionId)");
        }

        var query = entityManager.createQuery(sb.toString());
        if (storeId != null) {
            query.setParameter("storeId", storeId);
        }
        if (warehouseId != null) {
            query.setParameter("warehouseId", warehouseId);
        }
        if (regionId != null) {
            query.setParameter("regionId", regionId);
        }
        return (BigDecimal) query.getSingleResult();
    }

    /**
     * Retrieves stock in hand items data from the database.
     *
     * @param scope the dashboard query scope context
     * @return the numeric result value
     */
    public Long getStockInHandItems(DashboardScope scope) {
        Long storeId = scope != null ? scope.getStoreId() : null;
        Long warehouseId = scope != null ? scope.getWarehouseId() : null;
        Long regionId = scope != null ? scope.getRegionId() : null;

        StringBuilder sb = new StringBuilder("SELECT COALESCE(SUM(ls.quantity), 0) FROM LocationStock ls");
        sb.append(" JOIN Warehouse w ON w.id = ls.location.zone.warehouseId");
        if (storeId != null) {
            sb.append(" JOIN Store s ON s.warehouse.id = w.id");
        }
        sb.append(" WHERE 1=1");
        if (storeId != null) {
            sb.append(" AND s.id = :storeId");
        }
        if (warehouseId != null) {
            sb.append(" AND ls.location.zone.warehouseId = :warehouseId");
        }
        if (regionId != null) {
            sb.append(" AND (w.region.id = :regionId OR w.region.parent.id = :regionId)");
        }

        var query = entityManager.createQuery(sb.toString());
        if (storeId != null) {
            query.setParameter("storeId", storeId);
        }
        if (warehouseId != null) {
            query.setParameter("warehouseId", warehouseId);
        }
        if (regionId != null) {
            query.setParameter("regionId", regionId);
        }
        Number res = (Number) query.getSingleResult();
        return res != null ? res.longValue() : 0L;
    }

    /**
     * Retrieves low stock items count data from the database.
     *
     * @param scope the dashboard query scope context
     * @return the numeric result value
     */
    public Long getLowStockItemsCount(DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        if (regionId == null) {
            String jpql = "SELECT COUNT(p) FROM Product p WHERE p.reorderLevel > 0 AND " +
                    "(SELECT COALESCE(SUM(ls.quantity), 0) FROM LocationStock ls WHERE ls.productId = p.id) < p.reorderLevel";
            return (Long) entityManager.createQuery(jpql).getSingleResult();
        } else {
            String jpql = "SELECT COUNT(p) FROM Product p WHERE p.reorderLevel > 0 AND " +
                    "(SELECT COALESCE(SUM(ls.quantity), 0) FROM LocationStock ls " +
                    " JOIN Warehouse w ON w.id = ls.location.zone.warehouseId " +
                    " WHERE ls.productId = p.id AND (w.region.id = :regionId OR w.region.parent.id = :regionId)) < p.reorderLevel";
            return (Long) entityManager.createQuery(jpql)
                    .setParameter("regionId", regionId)
                    .getSingleResult();
        }
    }

    /**
     * Retrieves out of stock items count data from the database.
     *
     * @param scope the dashboard query scope context
     * @return the numeric result value
     */
    public Long getOutOfStockItemsCount(DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        if (regionId == null) {
            String jpql = "SELECT COUNT(p) FROM Product p WHERE " +
                    "(SELECT COALESCE(SUM(ls.quantity), 0) FROM LocationStock ls WHERE ls.productId = p.id) <= 0";
            return (Long) entityManager.createQuery(jpql).getSingleResult();
        } else {
            String jpql = "SELECT COUNT(p) FROM Product p WHERE " +
                    "(SELECT COALESCE(SUM(ls.quantity), 0) FROM LocationStock ls " +
                    " JOIN Warehouse w ON w.id = ls.location.zone.warehouseId " +
                    " WHERE ls.productId = p.id AND (w.region.id = :regionId OR w.region.parent.id = :regionId)) <= 0";
            return (Long) entityManager.createQuery(jpql)
                    .setParameter("regionId", regionId)
                    .getSingleResult();
        }
    }

    /**
     * Retrieves category distribution data from the database.
     *
     * @param scope the dashboard query scope context
     * @return List of matching records
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getCategoryDistribution(DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        String jpql;
        if (regionId == null) {
            jpql = "SELECT pc.name, COALESCE(SUM(ls.quantity), 0) " +
                    "FROM LocationStock ls " +
                    "JOIN Product p ON ls.productId = p.id " +
                    "JOIN ProductCategory pc ON p.category.id = pc.id " +
                    "GROUP BY pc.name " +
                    "ORDER BY SUM(ls.quantity) DESC";
            return entityManager.createQuery(jpql).getResultList();
        } else {
            jpql = "SELECT pc.name, COALESCE(SUM(ls.quantity), 0) " +
                    "FROM LocationStock ls " +
                    "JOIN Warehouse w ON w.id = ls.location.zone.warehouseId " +
                    "JOIN Product p ON ls.productId = p.id " +
                    "JOIN ProductCategory pc ON p.category.id = pc.id " +
                    "WHERE (w.region.id = :regionId OR w.region.parent.id = :regionId) " +
                    "GROUP BY pc.name " +
                    "ORDER BY SUM(ls.quantity) DESC";
            return entityManager.createQuery(jpql)
                    .setParameter("regionId", regionId)
                    .getResultList();
        }
    }

    /**
     * Retrieves expiry alerts data from the database.
     *
     * @param scope the dashboard query scope context
     * @return the result map value
     */
    public Map<String, Long> getExpiryAlerts(DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        LocalDate today = LocalDate.now();
        LocalDate in30 = today.plusDays(30);
        LocalDate in60 = today.plusDays(60);
        LocalDate in90 = today.plusDays(90);

        String baseJpql = "SELECT COUNT(ls) FROM LocationStock ls " +
                "JOIN Warehouse w ON w.id = ls.location.zone.warehouseId ";
        String whereClause = "WHERE ls.expiryDate BETWEEN :today AND :targetDate ";
        if (regionId != null) {
            whereClause += "AND (w.region.id = :regionId OR w.region.parent.id = :regionId) ";
        }

        var query30 = entityManager.createQuery(baseJpql + whereClause)
                .setParameter("today", today)
                .setParameter("targetDate", in30);
        var query60 = entityManager.createQuery(baseJpql + whereClause)
                .setParameter("today", today)
                .setParameter("targetDate", in60);
        var query90 = entityManager.createQuery(baseJpql + whereClause)
                .setParameter("today", today)
                .setParameter("targetDate", in90);

        if (regionId != null) {
            query30.setParameter("regionId", regionId);
            query60.setParameter("regionId", regionId);
            query90.setParameter("regionId", regionId);
        }

        Long count30 = (Long) query30.getSingleResult();
        Long count60 = (Long) query60.getSingleResult();
        Long count90 = (Long) query90.getSingleResult();

        Map<String, Long> res = new HashMap<>();
        res.put("within30", count30);
        res.put("within60", count60);
        res.put("within90", count90);
        return res;
    }
}