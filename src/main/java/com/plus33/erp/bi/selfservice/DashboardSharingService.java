package com.plus33.erp.bi.selfservice;

import com.plus33.erp.bi.entity.BiDashboardShare;
import com.plus33.erp.bi.entity.BiDashboardSubscription;
import com.plus33.erp.bi.repository.BiDashboardShareRepository;
import com.plus33.erp.bi.repository.BiDashboardSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class DashboardSharingService {

    @Autowired BiDashboardShareRepository shareRepo;
    @Autowired BiDashboardSubscriptionRepository subscriptionRepo;

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