package com.plus33.erp.store.task.repository;

import com.plus33.erp.store.task.entity.StoreTaskReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreTaskReportRepository extends JpaRepository<StoreTaskReport, Long> {

    List<StoreTaskReport> findByTaskIdOrderByCreatedAtDesc(Long taskId);
}
