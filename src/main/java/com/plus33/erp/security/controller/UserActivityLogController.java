/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : UserActivityLogController.java
 * Path              : src/main/java/com/plus33/erp/security/controller/UserActivityLogController.java
 * Purpose           : REST Controller providing user activity log endpoints, target user lists
 *                     for admin pulling, and date-range filtered search results.
 * Version           : 1.0.0
 ******************************************************************************/
package com.plus33.erp.security.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.security.entity.UserActivityLog;
import com.plus33.erp.security.service.UserActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/activity-logs")
@RequiredArgsConstructor
public class UserActivityLogController {

    private final UserActivityLogService userActivityLogService;

    /**
     * Retrieves the currently logged-in user's own activity logs.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<UserActivityLog>>> getMyActivityLogs(
            Principal principal,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }
        List<UserActivityLog> logs = userActivityLogService.getMyActivityLogs(principal.getName(), startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("User activity logs retrieved", logs));
    }

    /**
     * Retrieves target user dropdown options for admin log pulling.
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getTargetUsers(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }
        List<Map<String, String>> users = userActivityLogService.getTargetUsersForAdmin(principal.getName());
        return ResponseEntity.ok(ApiResponse.success("Target users retrieved", users));
    }

    /**
     * Pulls activity logs for a target user selected by admin.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserActivityLog>>> searchLogs(
            Principal principal,
            @RequestParam(required = false) String targetEmail,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }
        List<UserActivityLog> logs = userActivityLogService.searchActivityLogs(principal.getName(), targetEmail, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Activity logs pulled", logs));
    }

    /**
     * Aggregates a comprehensive consolidated data package for a target user.
     */
    @GetMapping("/user-package")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserPackage(
            Principal principal,
            @RequestParam(required = false) String targetEmail) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }
        Map<String, Object> pkg = userActivityLogService.getUserConsolidatedDataPackage(principal.getName(), targetEmail);
        return ResponseEntity.ok(ApiResponse.success("User data package retrieved", pkg));
    }
}
