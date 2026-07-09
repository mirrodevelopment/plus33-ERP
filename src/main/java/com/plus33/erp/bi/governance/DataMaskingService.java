/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.governance
 * File              : DataMaskingService.java
 * Purpose           : Business logic service layer for Bi Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DataMaskingController
 * Related Service   : DataMaskingService
 * Related Repository: DataMaskingRepository
 * Related Entity    : DataMasking
 * Related DTO       : N/A
 * Related Mapper    : DataMaskingMapper
 * Related DB Table  : data_maskings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DataMaskingController, DataMaskingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Bi Module. Implements DataMaskingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.bi.governance;

import com.plus33.erp.bi.entity.BiGovernanceClassification;
import com.plus33.erp.bi.entity.BiGovernanceMaskingRule;
import com.plus33.erp.bi.repository.BiGovernanceClassificationRepository;
import com.plus33.erp.bi.repository.BiGovernanceMaskingRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code DataMaskingService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.governance}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Bi Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DataMaskingController
 *   --> DataMaskingService (this)
 *   --> Validate business rules
 *   --> DataMaskingRepository (read/write 'data_maskings')
 *   --> DataMaskingMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code data_maskings}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DataMaskingService {

    /**
     * Performs the maskValue operation in this module.
     *
     * @param tableName the tableName input value
     * @param columnName the columnName input value
     * @param originalValue the originalValue input value
     * @return the result string value
     */
    @Autowired BiGovernanceClassificationRepository classificationRepo;
    @Autowired BiGovernanceMaskingRuleRepository maskingRuleRepo;
    public String maskValue(String tableName, String columnName, String originalValue) {
        if (originalValue == null) return null;

        List<BiGovernanceClassification> classifications = classificationRepo.findAll();
        BiGovernanceClassification matchingClassification = null;
        for (BiGovernanceClassification c : classifications) {
            if (c.getTableName().equalsIgnoreCase(tableName) && c.getColumnName().equalsIgnoreCase(columnName)) {
                matchingClassification = c;
                break;
            }
        }

        if (matchingClassification == null) {
            return originalValue;
        }

        List<BiGovernanceMaskingRule> rules = maskingRuleRepo.findAll();
        BiGovernanceMaskingRule matchingRule = null;
        for (BiGovernanceMaskingRule r : rules) {
            if (r.getIsActive() && r.getClassificationLevel().equalsIgnoreCase(matchingClassification.getClassificationLevel())) {
                matchingRule = r;
                break;
            }
        }

        if (matchingRule == null) {
            return originalValue;
        }

        return applyMasking(originalValue, matchingRule.getMaskingType());
    }

    private String applyMasking(String value, String type) {
        if ("REDACT_FULL".equalsIgnoreCase(type)) {
            return "[REDACTED]";
        } else if ("REDACT_PARTIAL".equalsIgnoreCase(type)) {
            if (value.length() <= 4) {
                return "****";
            }
            return value.substring(0, 2) + "****" + value.substring(value.length() - 2);
        } else if ("HASH_SHA256".equalsIgnoreCase(type)) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
                StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }
                return hexString.toString();
            } catch (Exception e) {
                return "[HASH_FAILED]";
            }
        } else {
            return "*****";
        }
    }
}