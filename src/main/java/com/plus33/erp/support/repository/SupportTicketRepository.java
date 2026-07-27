package com.plus33.erp.support.repository;

import com.plus33.erp.support.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    Optional<SupportTicket> findByTicketCode(String ticketCode);
    List<SupportTicket> findByReporterIdOrderByCreatedAtDesc(Long reporterId);
    List<SupportTicket> findByStoreIdOrderByCreatedAtDesc(Long storeId);
    List<SupportTicket> findByStatusOrderByCreatedAtDesc(String status);
    List<SupportTicket> findAllByOrderByCreatedAtDesc();
}
