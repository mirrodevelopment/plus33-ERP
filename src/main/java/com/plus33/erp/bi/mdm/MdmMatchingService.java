/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.mdm
 * File              : MdmMatchingService.java
 * Purpose           : Business logic service layer for Bi Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MdmMatchingController
 * Related Service   : MdmMatchingService
 * Related Repository: MdmMatchingRepository
 * Related Entity    : MdmMatching
 * Related DTO       : N/A
 * Related Mapper    : MdmMatchingMapper
 * Related DB Table  : mdm_matchings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MdmMatchingController, MdmMatchingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Bi Module. Implements MdmMatchingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.bi.mdm;

import org.springframework.stereotype.Service;

@Service
public class MdmMatchingService {

    /**
     * Calculates similarity totals including subtotal, tax, discounts, and net amount.
     *
     * @param s1 the s1 input value
     * @param s2 the s2 input value
     * @return the double result
     */
    public double calculateSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        s1 = s1.trim().toLowerCase();
        s2 = s2.trim().toLowerCase();
        if (s1.equals(s2)) return 100.0;
        
        int len1 = s1.length();
        int len2 = s2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];
        
        for (int i = 0; i <= len1; i++) dp[i][0] = i;
        for (int j = 0; j <= len2; j++) dp[0][j] = j;
        
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                    Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + cost
                );
            }
        }
        
        int distance = dp[len1][len2];
        int maxLen = Math.max(len1, len2);
        if (maxLen == 0) return 100.0;
        return (1.0 - ((double) distance / maxLen)) * 100.0;
    }
}