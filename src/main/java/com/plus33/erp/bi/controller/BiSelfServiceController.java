package com.plus33.erp.bi.controller;

import com.plus33.erp.bi.entity.BiDashboardShare;
import com.plus33.erp.bi.entity.BiDashboardSubscription;
import com.plus33.erp.bi.entity.BiSelfServiceWorkspace;
import com.plus33.erp.bi.selfservice.DashboardSharingService;
import com.plus33.erp.bi.selfservice.SelfServiceAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bi/self-service")
public class BiSelfServiceController {

    @Autowired SelfServiceAnalyticsService selfService;
    @Autowired DashboardSharingService sharingService;

    @PostMapping("/workspace")
    public BiSelfServiceWorkspace saveWorkspace(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String user,
            @RequestParam Long companyId,
            @RequestParam String configJson) {
        return selfService.saveWorkspace(code, name, user, companyId, configJson);
    }

    @PostMapping("/query")
    public SelfServiceAnalyticsService.QueryResult query(
            @RequestParam String user,
            @RequestParam Long companyId,
            @RequestParam String sql) {
        return selfService.executeQuery(user, companyId, sql);
    }

    @PostMapping("/share")
    public BiDashboardShare shareDashboard(
            @RequestParam String dashboardCode,
            @RequestParam String sharedBy,
            @RequestParam String recipient,
            @RequestParam boolean canEdit) {
        return sharingService.shareDashboard(dashboardCode, sharedBy, recipient, canEdit);
    }

    @PostMapping("/subscribe")
    public BiDashboardSubscription subscribe(
            @RequestParam String dashboardCode,
            @RequestParam String user,
            @RequestParam String cron,
            @RequestParam String format) {
        return sharingService.subscribeDashboard(dashboardCode, user, cron, format);
    }
}