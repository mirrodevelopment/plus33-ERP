/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.service
 * File              : ComplianceAnalyticsService.java
 * Purpose           : Business logic service layer for Grc Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceAnalyticsController
 * Related Service   : ComplianceAnalyticsService
 * Related Repository: ComplianceAnalyticsRepository
 * Related Entity    : ComplianceAnalytics
 * Related DTO       : N/A
 * Related Mapper    : ComplianceAnalyticsMapper
 * Related DB Table  : compliance_analyticss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ComplianceAnalyticsController, ComplianceAnalyticsServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Grc Module. Implements ComplianceAnalyticsService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.grc.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plus33.erp.dashboard.dto.DashboardScope;
import java.time.LocalDate;
import java.util.*;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code ComplianceAnalyticsService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Grc Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ComplianceAnalyticsController
 *   --> ComplianceAnalyticsService (this)
 *   --> Validate business rules
 *   --> ComplianceAnalyticsRepository (read/write 'compliance_analyticss')
 *   --> ComplianceAnalyticsMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code compliance_analyticss}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class ComplianceAnalyticsService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Retrieves compliance score data from the database.
     *
     * @param scope the dashboard query scope context
     * @return the Double result
     */
    public Double getComplianceScore(DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        if (regionId == null) {
            Long total = (Long) entityManager.createQuery("SELECT COUNT(ctr) FROM ControlTestResult ctr WHERE ctr.result IN ('PASSED', 'FAILED')").getSingleResult();
            if (total == 0) {
                return 0.0;
            }
            Long passed = (Long) entityManager.createQuery("SELECT COUNT(ctr) FROM ControlTestResult ctr WHERE ctr.result = 'PASSED'").getSingleResult();
            return (double) passed / total * 100.0;
        } else {
            String totalJpql = "SELECT COUNT(ctr) FROM ControlTestResult ctr WHERE ctr.result IN ('PASSED', 'FAILED') AND " +
                    "(ctr.testedById IN (SELECT ur.user.id FROM UserRegion ur WHERE ur.region.id = :regionId OR ur.region.parent.id = :regionId) OR " +
                    " ctr.testedById IN (SELECT us.user.id FROM UserStore us JOIN Store s ON s.id = us.store.id WHERE s.region.id = :regionId OR s.region.parent.id = :regionId))";
            String passedJpql = "SELECT COUNT(ctr) FROM ControlTestResult ctr WHERE ctr.result = 'PASSED' AND " +
                    "(ctr.testedById IN (SELECT ur.user.id FROM UserRegion ur WHERE ur.region.id = :regionId OR ur.region.parent.id = :regionId) OR " +
                    " ctr.testedById IN (SELECT us.user.id FROM UserStore us JOIN Store s ON s.id = us.store.id WHERE s.region.id = :regionId OR s.region.parent.id = :regionId))";
            Long total = (Long) entityManager.createQuery(totalJpql).setParameter("regionId", regionId).getSingleResult();
            if (total == 0) return 0.0;
            Long passed = (Long) entityManager.createQuery(passedJpql).setParameter("regionId", regionId).getSingleResult();
            return (double) passed / total * 100.0;
        }
    }

    public Double getComplianceScoreBefore(java.time.LocalDateTime dateTime, DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        if (regionId == null) {
            Long total = (Long) entityManager.createQuery("SELECT COUNT(ctr) FROM ControlTestResult ctr WHERE ctr.result IN ('PASSED', 'FAILED') AND ctr.testedAt < :dateTime")
                    .setParameter("dateTime", dateTime)
                    .getSingleResult();
            if (total == 0) {
                return 0.0;
            }
            Long passed = (Long) entityManager.createQuery("SELECT COUNT(ctr) FROM ControlTestResult ctr WHERE ctr.result = 'PASSED' AND ctr.testedAt < :dateTime")
                    .setParameter("dateTime", dateTime)
                    .getSingleResult();
            return (double) passed / total * 100.0;
        } else {
            String totalJpql = "SELECT COUNT(ctr) FROM ControlTestResult ctr WHERE ctr.result IN ('PASSED', 'FAILED') AND ctr.testedAt < :dateTime AND " +
                    "(ctr.testedById IN (SELECT ur.user.id FROM UserRegion ur WHERE ur.region.id = :regionId OR ur.region.parent.id = :regionId) OR " +
                    " ctr.testedById IN (SELECT us.user.id FROM UserStore us JOIN Store s ON s.id = us.store.id WHERE s.region.id = :regionId OR s.region.parent.id = :regionId))";
            String passedJpql = "SELECT COUNT(ctr) FROM ControlTestResult ctr WHERE ctr.result = 'PASSED' AND ctr.testedAt < :dateTime AND " +
                    "(ctr.testedById IN (SELECT ur.user.id FROM UserRegion ur WHERE ur.region.id = :regionId OR ur.region.parent.id = :regionId) OR " +
                    " ctr.testedById IN (SELECT us.user.id FROM UserStore us JOIN Store s ON s.id = us.store.id WHERE s.region.id = :regionId OR s.region.parent.id = :regionId))";
            Long total = (Long) entityManager.createQuery(totalJpql).setParameter("dateTime", dateTime).setParameter("regionId", regionId).getSingleResult();
            if (total == 0) return 0.0;
            Long passed = (Long) entityManager.createQuery(passedJpql).setParameter("dateTime", dateTime).setParameter("regionId", regionId).getSingleResult();
            return (double) passed / total * 100.0;
        }
    }

    /**
     * Retrieves audits completed count data from the database.
     *
     * @param scope the dashboard query scope context
     * @return the numeric result value
     */
    public Long getAuditsCompletedCount(DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        if (regionId == null) {
            return (Long) entityManager.createQuery("SELECT COUNT(ae) FROM AuditEngagement ae WHERE ae.status IN ('COMPLETED', 'CLOSED')")
                    .getSingleResult();
        } else {
            return (Long) entityManager.createQuery("SELECT COUNT(ae) FROM AuditEngagement ae " +
                    "WHERE ae.status IN ('COMPLETED', 'CLOSED') AND " +
                    "(ae.leadAuditorId IN (SELECT ur.user.id FROM UserRegion ur WHERE ur.region.id = :regionId OR ur.region.parent.id = :regionId) OR " +
                    " ae.leadAuditorId IN (SELECT us.user.id FROM UserStore us JOIN Store s ON s.id = us.store.id WHERE s.region.id = :regionId OR s.region.parent.id = :regionId))")
                    .setParameter("regionId", regionId)
                    .getSingleResult();
        }
    }

    /**
     * Retrieves corrective actions open count data from the database.
     *
     * @param scope the dashboard query scope context
     * @return the numeric result value
     */
    public Long getCorrectiveActionsOpenCount(DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        if (regionId == null) {
            return (Long) entityManager.createQuery("SELECT COUNT(cap) FROM CorrectiveActionPlan cap WHERE cap.status = 'OPEN'")
                    .getSingleResult();
        } else {
            return (Long) entityManager.createQuery("SELECT COUNT(cap) FROM CorrectiveActionPlan cap " +
                    "WHERE cap.status = 'OPEN' AND " +
                    "(cap.ownerId IN (SELECT ur.user.id FROM UserRegion ur WHERE ur.region.id = :regionId OR ur.region.parent.id = :regionId) OR " +
                    " cap.ownerId IN (SELECT us.user.id FROM UserStore us JOIN Store s ON s.id = us.store.id WHERE s.region.id = :regionId OR s.region.parent.id = :regionId))")
                    .setParameter("regionId", regionId)
                    .getSingleResult();
        }
    }

    /**
     * Retrieves overdue actions count data from the database.
     *
     * @param scope the dashboard query scope context
     * @return the numeric result value
     */
    public Long getOverdueActionsCount(DashboardScope scope) {
        Long regionId = scope != null ? scope.getRegionId() : null;
        if (regionId == null) {
            return (Long) entityManager.createQuery("SELECT COUNT(cap) FROM CorrectiveActionPlan cap WHERE cap.status = 'OPEN' AND cap.dueDate < :today")
                    .setParameter("today", LocalDate.now())
                    .getSingleResult();
        } else {
            return (Long) entityManager.createQuery("SELECT COUNT(cap) FROM CorrectiveActionPlan cap " +
                    "WHERE cap.status = 'OPEN' AND cap.dueDate < :today AND " +
                    "(cap.ownerId IN (SELECT ur.user.id FROM UserRegion ur WHERE ur.region.id = :regionId OR ur.region.parent.id = :regionId) OR " +
                    " cap.ownerId IN (SELECT us.user.id FROM UserStore us JOIN Store s ON s.id = us.store.id WHERE s.region.id = :regionId OR s.region.parent.id = :regionId))")
                    .setParameter("today", LocalDate.now())
                    .setParameter("regionId", regionId)
                    .getSingleResult();
        }
    }
}