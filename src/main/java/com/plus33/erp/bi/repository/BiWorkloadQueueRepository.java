package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiWorkloadQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiWorkloadQueueRepository extends JpaRepository<BiWorkloadQueue, Long> {
}