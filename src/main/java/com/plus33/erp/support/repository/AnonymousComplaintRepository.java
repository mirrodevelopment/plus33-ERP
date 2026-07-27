package com.plus33.erp.support.repository;

import com.plus33.erp.support.entity.AnonymousComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnonymousComplaintRepository extends JpaRepository<AnonymousComplaint, Long> {
    Optional<AnonymousComplaint> findByTrackingKey(String trackingKey);
    List<AnonymousComplaint> findByStatusOrderByCreatedAtDesc(String status);
    List<AnonymousComplaint> findByStoreIdOrderByCreatedAtDesc(Long storeId);
    List<AnonymousComplaint> findAllByOrderByCreatedAtDesc();
}
