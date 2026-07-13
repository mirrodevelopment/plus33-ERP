/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.service
 * File              : SalesAnalyticsService.java
 * Purpose           : Business logic service layer for Sales Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesAnalyticsController
 * Related Service   : SalesAnalyticsService
 * Related Repository: SalesAnalyticsRepository
 * Related Entity    : SalesAnalytics
 * Related DTO       : N/A
 * Related Mapper    : SalesAnalyticsMapper
 * Related DB Table  : sales_analyticss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SalesAnalyticsController, SalesAnalyticsServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Sales Module. Implements SalesAnalyticsService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.sales.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plus33.erp.dashboard.dto.DashboardScope;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code SalesAnalyticsService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Sales Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SalesAnalyticsController
 *   --> SalesAnalyticsService (this)
 *   --> Validate business rules
 *   --> SalesAnalyticsRepository (read/write 'sales_analyticss')
 *   --> SalesAnalyticsMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code sales_analyticss}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class SalesAnalyticsService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Retrieves total sales data from the database.
     *
     * @param from the from input date
     * @param to the to input date
     * @param scope the dashboard query scope context
     * @return the BigDecimal result
     */
    public BigDecimal getTotalSales(LocalDate from, LocalDate to, DashboardScope scope) {
        Long storeId = scope != null ? scope.getStoreId() : null;
        Long regionId = scope != null ? scope.getRegionId() : null;

        StringBuilder sb = new StringBuilder("SELECT COALESCE(SUM(so.totalAmount), 0) FROM SalesOrder so");
        if (storeId != null || regionId != null) {
            sb.append(" JOIN Store s ON so.store.id = s.id");
        }
        sb.append(" WHERE so.orderDate BETWEEN :from AND :to");
        if (storeId != null) {
            sb.append(" AND s.id = :storeId");
        }
        if (regionId != null) {
            sb.append(" AND (s.region.id = :regionId OR s.region.parent.id = :regionId)");
        }

        Query query = entityManager.createQuery(sb.toString())
                .setParameter("from", from)
                .setParameter("to", to);
        if (storeId != null) {
            query.setParameter("storeId", storeId);
        }
        if (regionId != null) {
            query.setParameter("regionId", regionId);
        }
        return (BigDecimal) query.getSingleResult();
    }

    /**
     * Retrieves net revenue data from the database.
     *
     * @param from the from input date
     * @param to the to input date
     * @param scope the dashboard query scope context
     * @return the BigDecimal result
     */
    public BigDecimal getNetRevenue(LocalDate from, LocalDate to, DashboardScope scope) {
        Long storeId = scope != null ? scope.getStoreId() : null;
        Long regionId = scope != null ? scope.getRegionId() : null;

        StringBuilder sb = new StringBuilder("SELECT COALESCE(SUM(ci.totalAmount - COALESCE(ci.creditedAmount, 0)), 0) FROM CustomerInvoice ci");
        if (storeId != null || regionId != null) {
            sb.append(" JOIN SalesOrder so ON ci.salesOrder.id = so.id");
            sb.append(" JOIN Store s ON so.store.id = s.id");
        }
        sb.append(" WHERE ci.invoiceDate BETWEEN :from AND :to");
        if (storeId != null) {
            sb.append(" AND s.id = :storeId");
        }
        if (regionId != null) {
            sb.append(" AND (s.region.id = :regionId OR s.region.parent.id = :regionId)");
        }

        Query query = entityManager.createQuery(sb.toString())
                .setParameter("from", from)
                .setParameter("to", to);
        if (storeId != null) {
            query.setParameter("storeId", storeId);
        }
        if (regionId != null) {
            query.setParameter("regionId", regionId);
        }
        return (BigDecimal) query.getSingleResult();
    }

    /**
     * Retrieves orders count data from the database.
     *
     * @param from the from input date
     * @param to the to input date
     * @param scope the dashboard query scope context
     * @return the numeric result value
     */
    public Long getOrdersCount(LocalDate from, LocalDate to, DashboardScope scope) {
        Long storeId = scope != null ? scope.getStoreId() : null;
        Long regionId = scope != null ? scope.getRegionId() : null;

        StringBuilder sb = new StringBuilder("SELECT COUNT(so) FROM SalesOrder so");
        if (storeId != null || regionId != null) {
            sb.append(" JOIN Store s ON so.store.id = s.id");
        }
        sb.append(" WHERE so.orderDate BETWEEN :from AND :to");
        if (storeId != null) {
            sb.append(" AND s.id = :storeId");
        }
        if (regionId != null) {
            sb.append(" AND (s.region.id = :regionId OR s.region.parent.id = :regionId)");
        }

        Query query = entityManager.createQuery(sb.toString())
                .setParameter("from", from)
                .setParameter("to", to);
        if (storeId != null) {
            query.setParameter("storeId", storeId);
        }
        if (regionId != null) {
            query.setParameter("regionId", regionId);
        }
        return (Long) query.getSingleResult();
    }

    /**
     * Retrieves customers count data from the database.
     *
     * @param from the from input date
     * @param to the to input date
     * @param scope the dashboard query scope context
     * @return the numeric result value
     */
    public Long getCustomersCount(LocalDate from, LocalDate to, DashboardScope scope) {
        Long storeId = scope != null ? scope.getStoreId() : null;
        Long regionId = scope != null ? scope.getRegionId() : null;

        if (storeId == null && regionId == null) {
            return (Long) entityManager.createQuery("SELECT COUNT(c) FROM Customer c WHERE c.createdAt <= :to")
                    .setParameter("to", to.atTime(23, 59, 59))
                    .getSingleResult();
        } else {
            StringBuilder sb = new StringBuilder("SELECT COUNT(DISTINCT c) FROM Customer c");
            sb.append(" JOIN SalesOrder so ON so.customer.id = c.id");
            sb.append(" JOIN Store s ON so.store.id = s.id");
            sb.append(" WHERE c.createdAt <= :to");
            if (storeId != null) {
                sb.append(" AND s.id = :storeId");
            }
            if (regionId != null) {
                sb.append(" AND (s.region.id = :regionId OR s.region.parent.id = :regionId)");
            }

            var query = entityManager.createQuery(sb.toString())
                    .setParameter("to", to.atTime(23, 59, 59));
            if (storeId != null) {
                query.setParameter("storeId", storeId);
            }
            if (regionId != null) {
                query.setParameter("regionId", regionId);
            }
            return (Long) query.getSingleResult();
        }
    }

    /**
     * Retrieves sales trend data from the database.
     *
     * @param from the from input date
     * @param to the to input date
     * @param scope the dashboard query scope context
     * @return List of matching records
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getSalesTrend(LocalDate from, LocalDate to, DashboardScope scope) {
        Long storeId = scope != null ? scope.getStoreId() : null;
        Long regionId = scope != null ? scope.getRegionId() : null;

        StringBuilder sb = new StringBuilder("SELECT so.orderDate, COALESCE(SUM(so.totalAmount), 0) FROM SalesOrder so");
        if (storeId != null || regionId != null) {
            sb.append(" JOIN Store s ON so.store.id = s.id");
        }
        sb.append(" WHERE so.orderDate BETWEEN :from AND :to");
        if (storeId != null) {
            sb.append(" AND s.id = :storeId");
        }
        if (regionId != null) {
            sb.append(" AND (s.region.id = :regionId OR s.region.parent.id = :regionId)");
        }
        sb.append(" GROUP BY so.orderDate ORDER BY so.orderDate ASC");

        Query query = entityManager.createQuery(sb.toString())
                .setParameter("from", from)
                .setParameter("to", to);
        if (storeId != null) {
            query.setParameter("storeId", storeId);
        }
        if (regionId != null) {
            query.setParameter("regionId", regionId);
        }
        return query.getResultList();
    }

    /**
     * Retrieves regional performance data from the database.
     *
     * @param from the from input date
     * @param to the to input date
     * @param scope the dashboard query scope context
     * @return List of matching records
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getRegionalPerformance(LocalDate from, LocalDate to, DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        String jpql;
        if (regionId == null) {
            jpql = "SELECT r.name, COALESCE(SUM(so.totalAmount), 0), COUNT(DISTINCT s.id), COUNT(DISTINCT us.user.id), COUNT(so.id) " +
                    "FROM Region r " +
                    "LEFT JOIN Region child ON child.parent = r " +
                    "LEFT JOIN Store s ON s.region.id = r.id OR s.region.id = child.id " +
                    "LEFT JOIN UserStore us ON us.store.id = s.id " +
                    "LEFT JOIN SalesOrder so ON so.store.id = s.id AND so.orderDate BETWEEN :from AND :to " +
                    "WHERE r.parent IS NULL " +
                    "GROUP BY r.name " +
                    "ORDER BY COALESCE(SUM(so.totalAmount), 0) DESC, r.name ASC";
            return entityManager.createQuery(jpql)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .getResultList();
        } else {
            jpql = "SELECT r.name, COALESCE(SUM(so.totalAmount), 0), COUNT(DISTINCT s.id), COUNT(DISTINCT us.user.id), COUNT(so.id) " +
                    "FROM Region r " +
                    "LEFT JOIN Store s ON s.region.id = r.id " +
                    "LEFT JOIN UserStore us ON us.store.id = s.id " +
                    "LEFT JOIN SalesOrder so ON so.store.id = s.id AND so.orderDate BETWEEN :from AND :to " +
                    "WHERE r.parent.id = :regionId " +
                    "GROUP BY r.name " +
                    "ORDER BY COALESCE(SUM(so.totalAmount), 0) DESC, r.name ASC";
            return entityManager.createQuery(jpql)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .setParameter("regionId", regionId)
                    .getResultList();
        }
    }

    /**
     * Retrieves sub-regional performance data from the database.
     *
     * @param from the from input date
     * @param to the to input date
     * @param scope the dashboard query scope context
     * @return List of matching records
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getSubRegionalPerformance(LocalDate from, LocalDate to, DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        String jpql;
        if (regionId == null) {
            jpql = "SELECT r.name, COALESCE(SUM(so.totalAmount), 0), COUNT(DISTINCT s.id), COUNT(DISTINCT us.user.id), COUNT(so.id) " +
                    "FROM Region r " +
                    "LEFT JOIN Store s ON s.region.id = r.id " +
                    "LEFT JOIN UserStore us ON us.store.id = s.id " +
                    "LEFT JOIN SalesOrder so ON so.store.id = s.id AND so.orderDate BETWEEN :from AND :to " +
                    "WHERE r.parent IS NOT NULL " +
                    "GROUP BY r.name " +
                    "ORDER BY COALESCE(SUM(so.totalAmount), 0) DESC, r.name ASC";
            return entityManager.createQuery(jpql)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .getResultList();
        } else {
            jpql = "SELECT r.name, COALESCE(SUM(so.totalAmount), 0), COUNT(DISTINCT s.id), COUNT(DISTINCT us.user.id), COUNT(so.id) " +
                    "FROM Region r " +
                    "LEFT JOIN Store s ON s.region.id = r.id " +
                    "LEFT JOIN UserStore us ON us.store.id = s.id " +
                    "LEFT JOIN SalesOrder so ON so.store.id = s.id AND so.orderDate BETWEEN :from AND :to " +
                    "WHERE r.parent.id = :regionId " +
                    "GROUP BY r.name " +
                    "ORDER BY COALESCE(SUM(so.totalAmount), 0) DESC, r.name ASC";
            return entityManager.createQuery(jpql)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .setParameter("regionId", regionId)
                    .getResultList();
        }
    }
}