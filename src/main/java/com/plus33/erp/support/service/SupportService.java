package com.plus33.erp.support.service;

import com.plus33.erp.support.entity.AnonymousComplaint;
import com.plus33.erp.support.entity.KnowledgeBaseFaq;
import com.plus33.erp.support.entity.SupportTicket;

import java.util.List;
import java.util.Map;

public interface SupportService {
    // Support Ticket Methods
    SupportTicket createTicket(SupportTicket ticket);
    List<SupportTicket> getMyTickets(Long reporterId);
    List<SupportTicket> getAllTickets(String status, Long storeId, String targetRole);
    SupportTicket updateTicketStatus(Long ticketId, String status, String adminResponse);
    SupportTicket escalateTicket(String ticketCode);

    // Anonymous Complaint Methods
    AnonymousComplaint submitAnonymousComplaint(AnonymousComplaint complaint);
    Map<String, Object> trackAnonymousComplaint(String trackingKey);
    List<AnonymousComplaint> getAllAnonymousComplaints(String status, Long storeId, String targetRole);
    AnonymousComplaint publishComplaintResponse(Long complaintId, String status, String complianceResponse);
    AnonymousComplaint escalateComplaint(String trackingKey);

    // FAQ Methods
    List<KnowledgeBaseFaq> getFaqs();
}
