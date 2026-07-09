/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.selfservice
 * File              : DashboardSharingService.java
 * Purpose           : Business logic service layer for Bi Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DashboardSharingController
 * Related Service   : DashboardSharingService
 * Related Repository: DashboardSharingRepository
 * Related Entity    : DashboardSharing
 * Related DTO       : N/A
 * Related Mapper    : DashboardSharingMapper
 * Related DB Table  : dashboard_sharings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DashboardSharingController, DashboardSharingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Bi Module. Implements DashboardSharingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.bi.selfservice;

import com.plus33.erp.bi.entity.BiDashboardShare;
import com.plus33.erp.bi.entity.BiDashboardSubscription;
import com.plus33.erp.bi.repository.BiDashboardShareRepository;
import com.plus33.erp.bi.repository.BiDashboardSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code DashboardSharingService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.selfservice}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Bi Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DashboardSharingController
 *   --> DashboardSharingService (this)
 *   --> Validate business rules
 *   --> DashboardSharingRepository (read/write 'dashboard_sharings')
 *   --> DashboardSharingMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code dashboard_sharings}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DashboardSharingService {

    @Autowired BiDashboardShareRepository shareRepo;
    @Autowired BiDashboardSubscriptionRepository subscriptionRepo;
    /**
     * Performs the shareDashboard operation in this module.
     *
     * @param dashboardCode the dashboardCode input value
     * @param sharedBy the sharedBy input value
     * @param recipient the recipient input value
     * @param canEdit the canEdit input value
     * @return the BiDashboardShare result
     */
    @Transactional
    public BiDashboardShare shareDashboard(String dashboardCode, String sharedBy, String recipient, boolean canEdit) {
        BiDashboardShare share = shareRepo.findAll().stream()
                .filter(s -> s.getDashboardCode().equalsIgnoreCase(dashboardCode) && s.getRecipientUser().equalsIgnoreCase(recipient))
                .findFirst().orElse(null);

        if (share == null) {
            share = new BiDashboardShare();
            share.setDashboardCode(dashboardCode);
            share.setRecipientUser(recipient);
            share.setSharedAt(LocalDateTime.now());
        }
        share.setSharedBy(sharedBy);
        share.setCanEdit(canEdit);
        return shareRepo.save(share);
    }

    /**
     * Performs the subscribeDashboard operation in this module.
     *
     * @param dashboardCode the dashboardCode input value
     * @param user the user input value
     * @param cron the cron input value
     * @param format the format input value
     * @return the BiDashboardSubscription result
     */
    @Transactional
    public BiDashboardSubscription subscribeDashboard(String dashboardCode, String user, String cron, String format) {
        BiDashboardSubscription sub = new BiDashboardSubscription();
        sub.setDashboardCode(dashboardCode);
        sub.setSubscriberUser(user);
        sub.setScheduleCron(cron);
        sub.setExportFormat(format);
        sub.setStatus("ACTIVE");
        sub.setCreatedAt(LocalDateTime.now());
        return subscriptionRepo.save(sub);
    }
}