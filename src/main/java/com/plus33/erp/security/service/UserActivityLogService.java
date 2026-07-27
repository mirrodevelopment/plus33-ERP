/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : UserActivityLogService.java
 * Path              : src/main/java/com/plus33/erp/security/service/UserActivityLogService.java
 * Purpose           : Service interface for user activity log management, user data pulling,
 *                     and role-based scoped activity log retrieval.
 * Version           : 1.0.0
 ******************************************************************************/
package com.plus33.erp.security.service;

import com.plus33.erp.security.entity.UserActivityLog;

import java.util.List;
import java.util.Map;

public interface UserActivityLogService {

    /**
     * Retrieves the activity logs for the currently authenticated user.
     */
    List<UserActivityLog> getMyActivityLogs(String email, String startDate, String endDate);

    /**
     * Retrieves the list of target users accessible by the requesting admin for log pulling.
     */
    List<Map<String, String>> getTargetUsersForAdmin(String adminEmail);

    /**
     * Pulls activity logs for a target user within the admin's scope and date range.
     */
    List<UserActivityLog> searchActivityLogs(String adminEmail, String targetEmail, String startDate, String endDate);
}
