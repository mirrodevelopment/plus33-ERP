package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiDashboardShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiDashboardShareRepository extends JpaRepository<BiDashboardShare, Long> {
}