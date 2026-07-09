/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.alert
 * File              : AlertEngine.java
 * Purpose           : Business logic service layer for Bi Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AlertEngineController
 * Related Service   : AlertEngine
 * Related Repository: BiAlertRuleRepository
 * Related Entity    : AlertEngine
 * Related DTO       : N/A
 * Related Mapper    : AlertEngineMapper
 * Related DB Table  : alert_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AlertEngineController, AlertEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Bi Module. Implements AlertEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.bi.alert;

import com.plus33.erp.bi.entity.BiAlertRule;
import com.plus33.erp.bi.repository.BiAlertRuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AlertEngine: Evaluates alert rules against actual KPI values.
 * Fires bi_alert_trigger records for threshold breaches.
 */
@Service
public class AlertEngine {

    private static final Logger log = LoggerFactory.getLogger(AlertEngine.class);
    private final BiAlertRuleRepository ruleRepo;
    private final JdbcTemplate jdbc;

    public AlertEngine(BiAlertRuleRepository ruleRepo, JdbcTemplate jdbc) {
        this.ruleRepo = ruleRepo;
        this.jdbc = jdbc;
    }

    /**
     * Performs the evaluateRules operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param kpiCode the kpiCode input value
     * @param actualValue the actualValue input value
     * @return List of matching records the numeric result value
     */
    @Transactional
    public List<Long> evaluateRules(Long companyId, String kpiCode, BigDecimal actualValue) {
        List<BiAlertRule> rules = ruleRepo.findByIsActiveTrue();
        List<Long> firedIds = new java.util.ArrayList<>();
        for (BiAlertRule rule : rules) {
            boolean fired = evaluate(rule, actualValue);
            if (fired) {
                Long triggerId = jdbc.queryForObject("""
                    INSERT INTO bi_alert_trigger(rule_id, company_id, actual_value, threshold_value, severity, status, message, triggered_at)
                    VALUES (?,?,?,?,?,'OPEN',? || ': actual=' || ? || ' threshold=' || ?,CURRENT_TIMESTAMP)
                    RETURNING id
                    """, Long.class, rule.getId(), companyId, actualValue, rule.getThresholdValue(),
                    rule.getSeverity(), rule.getRuleName(), actualValue.toPlainString(), rule.getThresholdValue().toPlainString());
                firedIds.add(triggerId);
                log.warn("[ALERT] Fired ruleId={} name={} actual={} threshold={}", rule.getId(), rule.getRuleName(), actualValue, rule.getThresholdValue());
            }
        }
        return firedIds;
    }

    private boolean evaluate(BiAlertRule rule, BigDecimal actualValue) {
        if (actualValue == null) return false;
        return switch (rule.getConditionType()) {
            case "BELOW_THRESHOLD" -> actualValue.compareTo(rule.getThresholdValue()) < 0;
            case "ABOVE_THRESHOLD" -> actualValue.compareTo(rule.getThresholdValue()) > 0;
            case "EQUALS"          -> actualValue.compareTo(rule.getThresholdValue()) == 0;
            default -> false;
        };
    }
}