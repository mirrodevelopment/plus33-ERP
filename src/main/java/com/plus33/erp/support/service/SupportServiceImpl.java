package com.plus33.erp.support.service;

import com.plus33.erp.support.entity.AnonymousComplaint;
import com.plus33.erp.support.entity.KnowledgeBaseFaq;
import com.plus33.erp.support.entity.SupportTicket;
import com.plus33.erp.support.repository.AnonymousComplaintRepository;
import com.plus33.erp.support.repository.KnowledgeBaseFaqRepository;
import com.plus33.erp.support.repository.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {

    private final SupportTicketRepository ticketRepository;
    private final AnonymousComplaintRepository complaintRepository;
    private final KnowledgeBaseFaqRepository faqRepository;

    @Override
    @Transactional
    public SupportTicket createTicket(SupportTicket ticket) {
        String code = "TK-2026-" + String.format("%04d", (int)(Math.random() * 9000) + 1000);
        ticket.setTicketCode(code);
        if (ticket.getStatus() == null || ticket.getStatus().isEmpty()) ticket.setStatus("OPEN");
        if (ticket.getPriority() == null || ticket.getPriority().isEmpty()) ticket.setPriority("MEDIUM");
        if (ticket.getCategory() == null || ticket.getCategory().isEmpty()) ticket.setCategory("TECHNICAL_SUPPORT");
        if (ticket.getTargetRole() == null || ticket.getTargetRole().isEmpty()) ticket.setTargetRole("STORE_ADMIN");
        return ticketRepository.save(ticket);
    }

    @Override
    public List<SupportTicket> getMyTickets(Long reporterId) {
        if (reporterId == null) return Collections.emptyList();
        return ticketRepository.findByReporterIdOrderByCreatedAtDesc(reporterId);
    }

    @Override
    public List<SupportTicket> getAllTickets(String status, Long storeId, String targetRole) {
        List<SupportTicket> list = ticketRepository.findAllByOrderByCreatedAtDesc();
        if (status != null && !status.isEmpty()) {
            list = list.stream().filter(t -> status.equalsIgnoreCase(t.getStatus())).toList();
        }
        if (storeId != null) {
            list = list.stream().filter(t -> storeId.equals(t.getStoreId())).toList();
        }
        if (targetRole != null && !targetRole.isEmpty()) {
            list = list.stream().filter(t -> targetRole.equalsIgnoreCase(t.getTargetRole()) || Boolean.TRUE.equals(t.getIsEscalated())).toList();
        }
        return list;
    }

    @Override
    @Transactional
    public SupportTicket updateTicketStatus(Long ticketId, String status, String adminResponse) {
        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Support ticket not found: " + ticketId));

        if (status != null && !status.isEmpty()) {
            ticket.setStatus(status);
            if ("RESOLVED".equalsIgnoreCase(status) || "CLOSED".equalsIgnoreCase(status)) {
                ticket.setResolvedAt(LocalDateTime.now());
            }
        }
        if (adminResponse != null) {
            ticket.setAdminResponse(adminResponse);
        }
        return ticketRepository.save(ticket);
    }

    // ---------------------------------------------------------------------------
    // ANONYMOUS COMPLAINTS (Strict Privacy & Deferred Response Disclosure)
    // ---------------------------------------------------------------------------

    @Override
    @Transactional
    public AnonymousComplaint submitAnonymousComplaint(AnonymousComplaint complaint) {
        String key = "TK-ANO-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        complaint.setTrackingKey(key);
        if (complaint.getStatus() == null || complaint.getStatus().isEmpty()) complaint.setStatus("OPEN");
        if (complaint.getSeverity() == null || complaint.getSeverity().isEmpty()) complaint.setSeverity("HIGH");
        if (complaint.getCategory() == null || complaint.getCategory().isEmpty()) complaint.setCategory("WORKPLACE_COMPLAINT");
        if (complaint.getTargetRole() == null || complaint.getTargetRole().isEmpty()) complaint.setTargetRole("ULTIMATE_ADMIN");
        complaint.setComplianceResponse(null);
        complaint.setResponsePublishedAt(null);
        return complaintRepository.save(complaint);
    }

    @Override
    public Map<String, Object> trackAnonymousComplaint(String trackingKey) {
        if (trackingKey == null || trackingKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Tracking key is required");
        }
        String key = trackingKey.trim().toUpperCase();

        Optional<AnonymousComplaint> complaintOpt = complaintRepository.findByTrackingKey(key);
        if (complaintOpt.isPresent()) {
            AnonymousComplaint complaint = complaintOpt.get();
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("trackingKey", complaint.getTrackingKey());
            result.put("category", complaint.getCategory());
            result.put("subcategory", complaint.getSubcategory());
            result.put("severity", complaint.getSeverity());
            result.put("subject", complaint.getSubject());
            result.put("description", complaint.getDescription());
            result.put("status", complaint.getStatus());
            result.put("targetRole", complaint.getTargetRole());
            result.put("customCategory", complaint.getCustomCategory());
            result.put("escalationLevel", complaint.getEscalationLevel());
            result.put("isEscalated", complaint.getIsEscalated());
            result.put("createdAt", complaint.getCreatedAt());
            result.put("updatedAt", complaint.getUpdatedAt());

            if (complaint.getResponsePublishedAt() != null) {
                result.put("complianceResponse", complaint.getComplianceResponse());
                result.put("responsePublishedAt", complaint.getResponsePublishedAt());
                result.put("responseAvailable", true);
            } else {
                result.put("complianceResponse", null);
                result.put("responsePublishedAt", null);
                result.put("responseAvailable", false);
                result.put("responseMessage", "Investigation in progress by Executive Compliance. Admin resolution will appear here once published.");
            }
            return result;
        }

        Optional<SupportTicket> ticketOpt = ticketRepository.findByTicketCode(key);
        if (ticketOpt.isPresent()) {
            SupportTicket ticket = ticketOpt.get();
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("trackingKey", ticket.getTicketCode());
            result.put("category", ticket.getCategory());
            result.put("subcategory", ticket.getSubcategory());
            result.put("severity", ticket.getPriority());
            result.put("subject", ticket.getSubject());
            result.put("description", ticket.getDescription());
            result.put("status", ticket.getStatus());
            result.put("targetRole", ticket.getTargetRole());
            result.put("customCategory", ticket.getCustomCategory());
            result.put("escalationLevel", ticket.getEscalationLevel());
            result.put("isEscalated", ticket.getIsEscalated());
            result.put("createdAt", ticket.getCreatedAt());
            result.put("updatedAt", ticket.getUpdatedAt());

            if (ticket.getAdminResponse() != null && !ticket.getAdminResponse().trim().isEmpty()) {
                result.put("complianceResponse", ticket.getAdminResponse());
                result.put("responsePublishedAt", ticket.getResolvedAt() != null ? ticket.getResolvedAt() : ticket.getUpdatedAt());
                result.put("responseAvailable", true);
            } else {
                result.put("complianceResponse", null);
                result.put("responsePublishedAt", null);
                result.put("responseAvailable", false);
                result.put("responseMessage", "Investigation in progress by Admin. Resolution will appear here once published.");
            }
            return result;
        }

        throw new NoSuchElementException("No complaint or ticket found for tracking key: " + trackingKey);
    }

    @Override
    public List<AnonymousComplaint> getAllAnonymousComplaints(String status, Long storeId, String targetRole) {
        List<AnonymousComplaint> list = new ArrayList<>(complaintRepository.findAllByOrderByCreatedAtDesc());
        if (status != null && !status.isEmpty()) {
            list = list.stream().filter(c -> status.equalsIgnoreCase(c.getStatus())).toList();
        }
        if (storeId != null) {
            list = list.stream().filter(c -> storeId.equals(c.getStoreId())).toList();
        }
        if (targetRole != null && !targetRole.isEmpty()) {
            list = list.stream().filter(c -> targetRole.equalsIgnoreCase(c.getTargetRole()) || Boolean.TRUE.equals(c.getIsEscalated())).toList();
        }

        List<SupportTicket> tickets = getAllTickets(status, storeId, targetRole);
        List<AnonymousComplaint> mappedTickets = tickets.stream().map(t -> AnonymousComplaint.builder()
                .id(t.getId())
                .trackingKey(t.getTicketCode())
                .category(t.getCategory())
                .subcategory(t.getSubcategory())
                .severity(t.getPriority() != null ? t.getPriority() : "MEDIUM")
                .status(t.getStatus())
                .subject(t.getSubject())
                .description(t.getDescription())
                .storeId(t.getStoreId())
                .complianceResponse(t.getAdminResponse())
                .responsePublishedAt(t.getResolvedAt())
                .targetRole(t.getTargetRole() != null ? t.getTargetRole() : "STORE_ADMIN")
                .customCategory(t.getCustomCategory())
                .escalationLevel(t.getEscalationLevel() != null ? t.getEscalationLevel() : 0)
                .isEscalated(t.getIsEscalated() != null ? t.getIsEscalated() : false)
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .reporterId(t.getReporterId())
                .reporterName(t.getReporterName())
                .build()
        ).toList();

        List<AnonymousComplaint> combined = new ArrayList<>();
        combined.addAll(list);
        combined.addAll(mappedTickets);
        combined.sort((a, b) -> {
            if (a.getCreatedAt() == null || b.getCreatedAt() == null) return 0;
            return b.getCreatedAt().compareTo(a.getCreatedAt());
        });
        return combined;
    }

    @Override
    @Transactional
    public AnonymousComplaint publishComplaintResponse(Long complaintId, String status, String complianceResponse) {
        Optional<AnonymousComplaint> complaintOpt = complaintRepository.findById(complaintId);
        if (complaintOpt.isPresent()) {
            AnonymousComplaint complaint = complaintOpt.get();
            boolean isClosing = "RESOLVED".equalsIgnoreCase(status) || "CLOSED".equalsIgnoreCase(status);
            if (isClosing && (complianceResponse == null || complianceResponse.trim().isEmpty())) {
                throw new IllegalArgumentException("An official investigation closing report is mandatory to mark complaint as " + status);
            }

            if (status != null && !status.isEmpty()) {
                complaint.setStatus(status.toUpperCase());
            }
            if (complianceResponse != null && !complianceResponse.trim().isEmpty()) {
                complaint.setComplianceResponse(complianceResponse.trim());
                complaint.setResponsePublishedAt(LocalDateTime.now());
            }
            return complaintRepository.save(complaint);
        }

        Optional<SupportTicket> ticketOpt = ticketRepository.findById(complaintId);
        if (ticketOpt.isPresent()) {
            SupportTicket ticket = ticketOpt.get();
            boolean isClosing = "RESOLVED".equalsIgnoreCase(status) || "CLOSED".equalsIgnoreCase(status);
            if (isClosing && (complianceResponse == null || complianceResponse.trim().isEmpty())) {
                throw new IllegalArgumentException("An official investigation closing report is mandatory to mark ticket as " + status);
            }

            if (status != null && !status.isEmpty()) {
                ticket.setStatus(status.toUpperCase());
                if (isClosing) {
                    ticket.setResolvedAt(LocalDateTime.now());
                }
            }
            if (complianceResponse != null && !complianceResponse.trim().isEmpty()) {
                ticket.setAdminResponse(complianceResponse.trim());
            }
            ticketRepository.save(ticket);
            return AnonymousComplaint.builder()
                    .id(ticket.getId())
                    .trackingKey(ticket.getTicketCode())
                    .category(ticket.getCategory())
                    .status(ticket.getStatus())
                    .complianceResponse(ticket.getAdminResponse())
                    .responsePublishedAt(ticket.getResolvedAt())
                    .targetRole(ticket.getTargetRole())
                    .reporterId(ticket.getReporterId())
                    .reporterName(ticket.getReporterName())
                    .build();
        }

        throw new IllegalArgumentException("Complaint or Ticket not found: " + complaintId);
    }

    @Override
    @Transactional
    public AnonymousComplaint escalateComplaint(String trackingKey) {
        if (trackingKey == null || trackingKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Tracking key is required for escalation");
        }
        AnonymousComplaint complaint = complaintRepository.findByTrackingKey(trackingKey.trim().toUpperCase())
                .orElseThrow(() -> new NoSuchElementException("No complaint found for tracking key: " + trackingKey));

        String currentRole = complaint.getTargetRole() != null ? complaint.getTargetRole().toUpperCase() : "STORE_ADMIN";
        String nextRole = switch (currentRole) {
            case "STORE_ADMIN" -> "REGIONAL_ADMIN";
            case "REGIONAL_ADMIN" -> "NATIONAL_ADMIN";
            default -> "ULTIMATE_ADMIN";
        };

        complaint.setTargetRole(nextRole);
        complaint.setIsEscalated(true);
        int currentLevel = complaint.getEscalationLevel() != null ? complaint.getEscalationLevel() : 0;
        complaint.setEscalationLevel(currentLevel + 1);

        String note = "\n\n[ESCALATED BY COMPLAINANT -> Bumped to " + nextRole + " at " + LocalDateTime.now() + "]";
        complaint.setDescription(complaint.getDescription() + note);

        return complaintRepository.save(complaint);
    }

    @Override
    @Transactional
    public SupportTicket escalateTicket(String ticketCode) {
        if (ticketCode == null || ticketCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket code is required for escalation");
        }
        SupportTicket ticket = ticketRepository.findByTicketCode(ticketCode.trim().toUpperCase())
                .orElseThrow(() -> new NoSuchElementException("No support ticket found for code: " + ticketCode));

        String currentRole = ticket.getTargetRole() != null ? ticket.getTargetRole().toUpperCase() : "STORE_ADMIN";
        String nextRole = switch (currentRole) {
            case "STORE_ADMIN" -> "REGIONAL_ADMIN";
            case "REGIONAL_ADMIN" -> "NATIONAL_ADMIN";
            default -> "ULTIMATE_ADMIN";
        };

        ticket.setTargetRole(nextRole);
        ticket.setIsEscalated(true);
        int currentLevel = ticket.getEscalationLevel() != null ? ticket.getEscalationLevel() : 0;
        ticket.setEscalationLevel(currentLevel + 1);

        String note = "\n\n[ESCALATED BY COMPLAINANT -> Bumped to " + nextRole + " at " + LocalDateTime.now() + "]";
        ticket.setDescription(ticket.getDescription() + note);

        return ticketRepository.save(ticket);
    }

    // ---------------------------------------------------------------------------
    // KNOWLEDGE BASE
    // ---------------------------------------------------------------------------

    @Override
    public List<KnowledgeBaseFaq> getFaqs() {
        return faqRepository.findAllByOrderBySortOrderAsc();
    }
}
