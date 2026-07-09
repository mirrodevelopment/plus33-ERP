/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.controller
 * File              : BiSelfServiceController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiSelfServiceController
 * Related Service   : BiSelfServiceControllerService, BiSelfServiceControllerServiceImpl
 * Related Repository: BiSelfServiceControllerRepository
 * Related Entity    : BiSelfServiceController
 * Related DTO       : N/A
 * Related Mapper    : BiSelfServiceControllerMapper
 * Related DB Table  : bi_self_service_controllers
 * Related REST APIs : POST /api/bi/self-service/workspace, POST /api/bi/self-service/query, POST /api/bi/self-service/share, POST /api/bi/self-service/subscribe
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Bi Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/bi/self-service/workspace, POST /api/bi/self-service/query, POST /api/bi/self-service/share, POST /api/bi/self-service/subscribe
 ******************************************************************************/
package com.plus33.erp.bi.controller;

import com.plus33.erp.bi.entity.BiDashboardShare;
import com.plus33.erp.bi.entity.BiDashboardSubscription;
import com.plus33.erp.bi.entity.BiSelfServiceWorkspace;
import com.plus33.erp.bi.selfservice.DashboardSharingService;
import com.plus33.erp.bi.selfservice.SelfServiceAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiSelfServiceController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to BiSelfServiceService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> BiSelfServiceController.endpoint()
 *   --> BiSelfServiceService.method()
 *   --> BiSelfServiceRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/bi/self-service/workspace, POST /api/bi/self-service/query, POST /api/bi/self-service/share, POST /api/bi/self-service/subscribe</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/bi/self-service")
public class BiSelfServiceController {

    @Autowired SelfServiceAnalyticsService selfService;
    @Autowired DashboardSharingService sharingService;
    /**
     * Persists the workspace entity to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return the BiSelfServiceWorkspace result
     */
    @PostMapping("/workspace")
    public BiSelfServiceWorkspace saveWorkspace(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String user,
            @RequestParam Long companyId,
            @RequestParam String configJson) {
        return selfService.saveWorkspace(code, name, user, companyId, configJson);
    }

    /**
     * Returns a filtered paginated list of bi records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     */
    @PostMapping("/query")
    public SelfServiceAnalyticsService.QueryResult query(
            @RequestParam String user,
            @RequestParam Long companyId,
            @RequestParam String sql) {
        return selfService.executeQuery(user, companyId, sql);
    }

    /**
     * Performs the shareDashboard operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return the BiDashboardShare result
     */
    @PostMapping("/share")
    public BiDashboardShare shareDashboard(
            @RequestParam String dashboardCode,
            @RequestParam String sharedBy,
            @RequestParam String recipient,
            @RequestParam boolean canEdit) {
        return sharingService.shareDashboard(dashboardCode, sharedBy, recipient, canEdit);
    }

    /**
     * Performs the subscribe operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return the BiDashboardSubscription result
     */
    @PostMapping("/subscribe")
    public BiDashboardSubscription subscribe(
            @RequestParam String dashboardCode,
            @RequestParam String user,
            @RequestParam String cron,
            @RequestParam String format) {
        return sharingService.subscribeDashboard(dashboardCode, user, cron, format);
    }
}