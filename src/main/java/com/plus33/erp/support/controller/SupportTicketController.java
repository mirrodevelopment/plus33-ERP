package com.plus33.erp.support.controller;

import com.plus33.erp.support.entity.KnowledgeBaseFaq;
import com.plus33.erp.support.entity.SupportTicket;
import com.plus33.erp.support.service.SupportService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/support")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportService supportService;

    /** Create a new support ticket */
    @PostMapping("/tickets")
    public ResponseEntity<Map<String, Object>> createTicket(@RequestBody SupportTicket ticket) {
        SupportTicket created = supportService.createTicket(ticket);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", created);
        response.put("message", "Support ticket " + created.getTicketCode() + " created successfully.");
        return ResponseEntity.ok(response);
    }

    /** Get current user's submitted support tickets */
    @GetMapping("/tickets/my")
    public ResponseEntity<Map<String, Object>> getMyTickets(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long reporterId) {
        Long id = userId != null ? userId : reporterId;
        List<SupportTicket> tickets = supportService.getMyTickets(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", tickets);
        return ResponseEntity.ok(response);
    }

    /** Admin list of support tickets */
    @GetMapping("/tickets/admin")
    public ResponseEntity<Map<String, Object>> getAllTickets(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) String targetRole) {
        List<SupportTicket> tickets = supportService.getAllTickets(status, storeId, targetRole);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", tickets);
        return ResponseEntity.ok(response);
    }

    /** Update support ticket status and post admin response */
    @PutMapping("/tickets/{id}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest req) {
        SupportTicket updated = supportService.updateTicketStatus(id, req.getStatus(), req.getAdminResponse());
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    /** Get Knowledge Base FAQs */
    @GetMapping("/faq")
    public ResponseEntity<Map<String, Object>> getFaqs() {
        List<KnowledgeBaseFaq> faqs = supportService.getFaqs();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", faqs);
        return ResponseEntity.ok(response);
    }

    /** Complainant endpoint to escalate pending support ticket to next higher admin level */
    @PutMapping("/tickets/escalate/{ticketCode}")
    public ResponseEntity<Map<String, Object>> escalateTicket(@PathVariable String ticketCode) {
        SupportTicket escalated = supportService.escalateTicket(ticketCode);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", escalated);
        response.put("message", "Ticket escalated successfully to " + escalated.getTargetRole() + ".");
        return ResponseEntity.ok(response);
    }

    @Data
    public static class StatusUpdateRequest {
        private String status;
        private String adminResponse;
    }
}
