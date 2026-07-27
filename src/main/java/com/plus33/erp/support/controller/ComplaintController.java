package com.plus33.erp.support.controller;

import com.plus33.erp.support.entity.AnonymousComplaint;
import com.plus33.erp.support.service.SupportService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final SupportService supportService;

    /** Submit an anonymous workplace complaint / grievance */
    @PostMapping("/anonymous")
    public ResponseEntity<Map<String, Object>> submitAnonymousComplaint(@RequestBody AnonymousComplaint complaint) {
        AnonymousComplaint created = supportService.submitAnonymousComplaint(complaint);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", created);
        response.put("trackingKey", created.getTrackingKey());
        response.put("message", "Anonymous complaint submitted safely. Keep your Tracking Key: " + created.getTrackingKey());
        return ResponseEntity.ok(response);
    }

    /** Submitter tracking lookup using secret Tracking Key */
    @GetMapping("/track/{trackingKey}")
    public ResponseEntity<Map<String, Object>> trackAnonymousComplaint(@PathVariable String trackingKey) {
        Map<String, Object> details = supportService.trackAnonymousComplaint(trackingKey);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", details);
        return ResponseEntity.ok(response);
    }

    /** Restricted Executive Overview for Ultimate Admin & National Compliance Admin */
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getComplaintsOverview(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) String targetRole) {
        List<AnonymousComplaint> complaints = supportService.getAllAnonymousComplaints(status, storeId, targetRole);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", complaints);
        return ResponseEntity.ok(response);
    }

    /** Executive Admin endpoint to publish official investigation response & update status */
    @PutMapping("/{id}/response")
    public ResponseEntity<Map<String, Object>> publishResponse(
            @PathVariable Long id,
            @RequestBody ComplaintResponseRequest req) {
        AnonymousComplaint updated = supportService.publishComplaintResponse(id, req.getStatus(), req.getComplianceResponse());
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", updated);
        response.put("message", "Investigation response published to submitter.");
        return ResponseEntity.ok(response);
    }

    /** Complainant endpoint to escalate pending complaint to next higher admin level */
    @PutMapping("/escalate/{trackingKey}")
    public ResponseEntity<Map<String, Object>> escalateComplaint(@PathVariable String trackingKey) {
        AnonymousComplaint escalated = supportService.escalateComplaint(trackingKey);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", escalated);
        response.put("message", "Complaint escalated successfully to " + escalated.getTargetRole() + ".");
        return ResponseEntity.ok(response);
    }

    @Data
    public static class ComplaintResponseRequest {
        private String status;
        private String complianceResponse;
    }
}
