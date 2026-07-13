/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : EmployeeAnalyticsService.java
 * Purpose           : Business logic service layer for Workforce Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeAnalyticsController
 * Related Service   : EmployeeAnalyticsService
 * Related Repository: EmployeeAnalyticsRepository
 * Related Entity    : EmployeeAnalytics
 * Related DTO       : N/A
 * Related Mapper    : EmployeeAnalyticsMapper
 * Related DB Table  : employee_analyticss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeeAnalyticsController, EmployeeAnalyticsServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Workforce Module. Implements EmployeeAnalyticsService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plus33.erp.dashboard.dto.DashboardScope;
import java.time.LocalDate;
import java.util.*;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeAnalyticsService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Workforce Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * EmployeeAnalyticsController
 *   --> EmployeeAnalyticsService (this)
 *   --> Validate business rules
 *   --> EmployeeAnalyticsRepository (read/write 'employee_analyticss')
 *   --> EmployeeAnalyticsMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code employee_analyticss}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class EmployeeAnalyticsService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Retrieves employee count data from the database.
     *
     * @param scope the dashboard query scope context
     * @return the numeric result value
     */
    public Long getEmployeeCount(DashboardScope scope) {
        Long storeId = scope != null ? scope.getStoreId() : null;
        Long regionId = scope != null ? scope.getRegionId() : null;

        StringBuilder sb = new StringBuilder("SELECT COUNT(e) FROM Employee e");
        if (storeId != null) {
            sb.append(" JOIN UserStore us ON e.user.id = us.user.id");
        }
        sb.append(" WHERE e.active = true");
        if (storeId != null) {
            sb.append(" AND us.store.id = :storeId");
        }
        if (regionId != null) {
            sb.append(" AND (e.user.id IN (SELECT ur.user.id FROM UserRegion ur WHERE ur.region.id = :regionId OR ur.region.parent.id = :regionId) OR " +
                      "      e.user.id IN (SELECT us.user.id FROM UserStore us JOIN Store s ON s.id = us.store.id WHERE s.region.id = :regionId OR s.region.parent.id = :regionId))");
        }

        var query = entityManager.createQuery(sb.toString());
        if (storeId != null) {
            query.setParameter("storeId", storeId);
        }
        if (regionId != null) {
            query.setParameter("regionId", regionId);
        }
        return (Long) query.getSingleResult();
    }

    /**
     * Retrieves present today count data from the database.
     *
     * @param today the today input value
     * @param scope the dashboard query scope context
     * @return the numeric result value
     */
    public Long getPresentTodayCount(LocalDate today, DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        if (regionId == null) {
            return (Long) entityManager.createQuery("SELECT COUNT(a) FROM Attendance a WHERE a.attendanceDate = :today AND a.status = 'PRESENT'")
                    .setParameter("today", today)
                    .getSingleResult();
        } else {
            return (Long) entityManager.createQuery("SELECT COUNT(a) FROM Attendance a " +
                    "WHERE a.attendanceDate = :today AND a.status = 'PRESENT' AND " +
                    "(a.employee.user.id IN (SELECT ur.user.id FROM UserRegion ur WHERE ur.region.id = :regionId OR ur.region.parent.id = :regionId) OR " +
                    " a.employee.user.id IN (SELECT us.user.id FROM UserStore us JOIN Store s ON s.id = us.store.id WHERE s.region.id = :regionId OR s.region.parent.id = :regionId))")
                    .setParameter("today", today)
                    .setParameter("regionId", regionId)
                    .getSingleResult();
        }
    }

    /**
     * Retrieves on leave count data from the database.
     *
     * @param today the today input value
     * @param scope the dashboard query scope context
     * @return the numeric result value
     */
    public Long getOnLeaveCount(LocalDate today, DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        if (regionId == null) {
            return (Long) entityManager.createQuery("SELECT COUNT(el) FROM EmployeeLeave el WHERE el.status = 'APPROVED' AND :today BETWEEN el.startDate AND el.endDate")
                    .setParameter("today", today)
                    .getSingleResult();
        } else {
            return (Long) entityManager.createQuery("SELECT COUNT(el) FROM EmployeeLeave el " +
                    "WHERE el.status = 'APPROVED' AND :today BETWEEN el.startDate AND el.endDate AND " +
                    "(el.employee.user.id IN (SELECT ur.user.id FROM UserRegion ur WHERE ur.region.id = :regionId OR ur.region.parent.id = :regionId) OR " +
                    " el.employee.user.id IN (SELECT us.user.id FROM UserStore us JOIN Store s ON s.id = us.store.id WHERE s.region.id = :regionId OR s.region.parent.id = :regionId))")
                    .setParameter("today", today)
                    .setParameter("regionId", regionId)
                    .getSingleResult();
        }
    }

    /**
     * Retrieves roles distribution data from the database.
     *
     * @param scope the dashboard query scope context
     * @return List of matching records
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getRolesDistribution(DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        if (regionId == null) {
            String jpql = "SELECT e.designation, COUNT(e) FROM Employee e GROUP BY e.designation ORDER BY COUNT(e) DESC";
            return entityManager.createQuery(jpql).getResultList();
        } else {
            String jpql = "SELECT e.designation, COUNT(e) FROM Employee e " +
                    "WHERE (e.user.id IN (SELECT ur.user.id FROM UserRegion ur WHERE ur.region.id = :regionId OR ur.region.parent.id = :regionId) OR " +
                    "       e.user.id IN (SELECT us.user.id FROM UserStore us JOIN Store s ON s.id = us.store.id WHERE s.region.id = :regionId OR s.region.parent.id = :regionId)) " +
                    "GROUP BY e.designation ORDER BY COUNT(e) DESC";
            return entityManager.createQuery(jpql)
                    .setParameter("regionId", regionId)
                    .getResultList();
        }
    }

    /**
     * Retrieves departments distribution data from the database.
     *
     * @param scope the dashboard query scope context
     * @return List of matching records
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getDepartmentsDistribution(DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        if (regionId == null) {
            String jpql = "SELECT e.department, COUNT(e) FROM Employee e GROUP BY e.department ORDER BY COUNT(e) DESC";
            return entityManager.createQuery(jpql).getResultList();
        } else {
            String jpql = "SELECT e.department, COUNT(e) FROM Employee e " +
                    "WHERE (e.user.id IN (SELECT ur.user.id FROM UserRegion ur WHERE ur.region.id = :regionId OR ur.region.parent.id = :regionId) OR " +
                    "       e.user.id IN (SELECT us.user.id FROM UserStore us JOIN Store s ON s.id = us.store.id WHERE s.region.id = :regionId OR s.region.parent.id = :regionId)) " +
                    "GROUP BY e.department ORDER BY COUNT(e) DESC";
            return entityManager.createQuery(jpql)
                    .setParameter("regionId", regionId)
                    .getResultList();
        }
    }
}