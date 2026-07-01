package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiDashboardSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiDashboardSubscriptionRepository extends JpaRepository<BiDashboardSubscription, Long> {
}