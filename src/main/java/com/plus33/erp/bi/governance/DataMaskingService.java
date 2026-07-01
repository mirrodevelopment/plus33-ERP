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

@Service
public class DataMaskingService {

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