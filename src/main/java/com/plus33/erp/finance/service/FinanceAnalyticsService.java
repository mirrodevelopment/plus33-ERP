/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.service
 * File              : FinanceAnalyticsService.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FinanceAnalyticsController
 * Related Service   : FinanceAnalyticsService
 * Related Repository: FinanceAnalyticsRepository
 * Related Entity    : FinanceAnalytics
 * Related DTO       : N/A
 * Related Mapper    : FinanceAnalyticsMapper
 * Related DB Table  : finance_analyticss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FinanceAnalyticsController, FinanceAnalyticsServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements FinanceAnalyticsService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FinanceAnalyticsService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * FinanceAnalyticsController
 *   --> FinanceAnalyticsService (this)
 *   --> Validate business rules
 *   --> FinanceAnalyticsRepository (read/write 'finance_analyticss')
 *   --> FinanceAnalyticsMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code finance_analyticss}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class FinanceAnalyticsService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Retrieves total expenses data from the database.
     *
     * @param from the from input value
     * @param to the to input value
     * @param regionId the regionId input value
     * @param storeId the storeId input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalExpenses(LocalDate from, LocalDate to, Long regionId, Long storeId) {
        StringBuilder sb = new StringBuilder(
            "SELECT COALESCE(SUM(jel.debitAmount), 0) FROM JournalEntryLine jel " +
            "JOIN jel.journalEntry je");
        if (storeId != null || regionId != null) {
            sb.append(" JOIN UserStore us ON je.createdBy.id = us.user.id");
            if (regionId != null) {
                sb.append(" JOIN Store s ON us.store.id = s.id");
            }
        }
        sb.append(" WHERE je.entryDate BETWEEN :from AND :to");
        if (storeId != null) {
            sb.append(" AND us.store.id = :storeId");
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
     * Retrieves monthly finance trend data from the database.
     *
     * @param from the from input value
     * @param to the to input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getMonthlyFinanceTrend(LocalDate from, LocalDate to) {
        // Group revenue (customer invoices) and expenses (supplier invoices) by month
        String jpql = "SELECT SUBSTRING(CAST(ci.invoiceDate AS string), 1, 7), COALESCE(SUM(ci.totalAmount), 0) " +
                "FROM CustomerInvoice ci " +
                "WHERE ci.invoiceDate BETWEEN :from AND :to " +
                "GROUP BY SUBSTRING(CAST(ci.invoiceDate AS string), 1, 7) " +
                "ORDER BY SUBSTRING(CAST(ci.invoiceDate AS string), 1, 7) ASC";

        List<Object[]> revenueList = entityManager.createQuery(jpql)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();

        String expJpql = "SELECT SUBSTRING(CAST(si.invoiceDate AS string), 1, 7), COALESCE(SUM(si.totalAmount), 0) " +
                "FROM SupplierInvoice si " +
                "WHERE si.invoiceDate BETWEEN :from AND :to " +
                "GROUP BY SUBSTRING(CAST(si.invoiceDate AS string), 1, 7) " +
                "ORDER BY SUBSTRING(CAST(si.invoiceDate AS string), 1, 7) ASC";

        List<Object[]> expenseList = entityManager.createQuery(expJpql)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();

        Map<String, BigDecimal[]> monthMap = new TreeMap<>();
        for (Object[] row : revenueList) {
            String month = (String) row[0];
            BigDecimal rev = (BigDecimal) row[1];
            monthMap.put(month, new BigDecimal[]{rev, BigDecimal.ZERO});
        }
        for (Object[] row : expenseList) {
            String month = (String) row[0];
            BigDecimal exp = (BigDecimal) row[1];
            BigDecimal[] vals = monthMap.get(month);
            if (vals == null) {
                monthMap.put(month, new BigDecimal[]{BigDecimal.ZERO, exp});
            } else {
                vals[1] = exp;
            }
        }

        List<Object[]> result = new ArrayList<>();
        for (Map.Entry<String, BigDecimal[]> entry : monthMap.entrySet()) {
            result.add(new Object[]{entry.getKey(), entry.getValue()[0], entry.getValue()[1]});
        }
        return result;
    }

    /**
     * Retrieves daily finance trend data from the database.
     *
     * @param endDate inclusive end date for date-range filtering
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getDailyFinanceTrend(LocalDate endDate) {
        LocalDate startDate = endDate.minusDays(6);
        
        String jpql = "SELECT CAST(ci.invoiceDate AS string), COALESCE(SUM(ci.totalAmount), 0) " +
                "FROM CustomerInvoice ci " +
                "WHERE ci.invoiceDate BETWEEN :from AND :to " +
                "GROUP BY CAST(ci.invoiceDate AS string) " +
                "ORDER BY CAST(ci.invoiceDate AS string) ASC";

        List<Object[]> revenueList = entityManager.createQuery(jpql)
                .setParameter("from", startDate)
                .setParameter("to", endDate)
                .getResultList();

        String expJpql = "SELECT CAST(si.invoiceDate AS string), COALESCE(SUM(si.totalAmount), 0) " +
                "FROM SupplierInvoice si " +
                "WHERE si.invoiceDate BETWEEN :from AND :to " +
                "GROUP BY CAST(si.invoiceDate AS string) " +
                "ORDER BY CAST(si.invoiceDate AS string) ASC";

        List<Object[]> expenseList = entityManager.createQuery(expJpql)
                .setParameter("from", startDate)
                .setParameter("to", endDate)
                .getResultList();

        Map<String, BigDecimal[]> dayMap = new TreeMap<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate d = endDate.minusDays(i);
            dayMap.put(d.toString(), new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
        }

        for (Object[] row : revenueList) {
            String day = (String) row[0];
            BigDecimal rev = (BigDecimal) row[1];
            if (dayMap.containsKey(day)) {
                dayMap.get(day)[0] = rev;
            }
        }
        for (Object[] row : expenseList) {
            String day = (String) row[0];
            BigDecimal exp = (BigDecimal) row[1];
            if (dayMap.containsKey(day)) {
                dayMap.get(day)[1] = exp;
            }
        }

        List<Object[]> result = new ArrayList<>();
        for (Map.Entry<String, BigDecimal[]> entry : dayMap.entrySet()) {
            result.add(new Object[]{entry.getKey(), entry.getValue()[0], entry.getValue()[1]});
        }
        return result;
    }
}