package com.plus33.erp.workforce.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.workforce.entity.Announcement;
import com.plus33.erp.workforce.entity.AnnouncementRead;
import com.plus33.erp.workforce.entity.AnnouncementReaction;
import com.plus33.erp.workforce.dto.AnnouncementRequest;
import com.plus33.erp.workforce.dto.AnnouncementResponse;
import com.plus33.erp.workforce.repository.AnnouncementRepository;
import com.plus33.erp.workforce.repository.AnnouncementReadRepository;
import com.plus33.erp.workforce.repository.AnnouncementReactionRepository;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.security.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/announcements")
public class AnnouncementController {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementReadRepository announcementReadRepository;
    private final AnnouncementReactionRepository announcementReactionRepository;
    private final UserRepository userRepository;

    public AnnouncementController(
            AnnouncementRepository announcementRepository,
            AnnouncementReadRepository announcementReadRepository,
            AnnouncementReactionRepository announcementReactionRepository,
            UserRepository userRepository) {
        this.announcementRepository = announcementRepository;
        this.announcementReadRepository = announcementReadRepository;
        this.announcementReactionRepository = announcementReactionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AnnouncementResponse>>> getAnnouncements(Principal principal) {
        String email = principal != null ? principal.getName() : "anonymous";
        List<Announcement> announcements = announcementRepository.findAll();
        
        // Sort descending by creation date
        announcements.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));

        List<AnnouncementResponse> responses = announcements.stream().map(ann -> {
            boolean read = false;
            if (principal != null) {
                read = announcementReadRepository.findByAnnouncementIdAndUsername(ann.getId(), email).isPresent();
            }

            List<AnnouncementReaction> reactions = announcementReactionRepository.findByAnnouncementId(ann.getId());
            Map<String, Integer> reactionCounts = new HashMap<>();
            reactionCounts.put("thumbsUp", 0);
            reactionCounts.put("heart", 0);
            reactionCounts.put("lightbulb", 0);
            reactionCounts.put("coffee", 0);

            for (AnnouncementReaction reaction : reactions) {
                String type = reaction.getReactionType();
                reactionCounts.put(type, reactionCounts.getOrDefault(type, 0) + 1);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
            String dateStr = ann.getCreatedAt().format(formatter);

            return new AnnouncementResponse(
                ann.getId(),
                ann.getTitle(),
                ann.getContent(),
                ann.getPriority(),
                ann.getPublisher(),
                dateStr,
                ann.getCreatedAt().toString(),
                read,
                reactionCounts
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Announcements retrieved successfully", responses));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Announcement>> createAnnouncement(
            @Valid @RequestBody AnnouncementRequest request, Principal principal) {
        String email = principal != null ? principal.getName() : "System";
        
        // Retrieve user info for publisher name
        String publisherName = "Management";
        if (principal != null) {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User u = userOpt.get();
                publisherName = u.getFirstName() + " " + u.getLastName();
            }
        }

        Announcement ann = new Announcement();
        ann.setTitle(request.title());
        ann.setContent(request.content());
        ann.setPriority(request.priority());
        ann.setPublisher(publisherName);
        ann.setCreatedAt(LocalDateTime.now());

        Announcement saved = announcementRepository.save(ann);
        return ResponseEntity.ok(ApiResponse.success("Announcement broadcasted successfully", saved));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }
        String email = principal.getName();

        Optional<AnnouncementRead> existing = announcementReadRepository.findByAnnouncementIdAndUsername(id, email);
        if (existing.isEmpty()) {
            AnnouncementRead read = new AnnouncementRead();
            read.setAnnouncementId(id);
            read.setUsername(email);
            read.setReadAt(LocalDateTime.now());
            announcementReadRepository.save(read);
        }

        return ResponseEntity.ok(ApiResponse.success("Announcement marked as read", null));
    }

    @PostMapping("/{id}/react")
    public ResponseEntity<ApiResponse<Void>> react(
            @PathVariable Long id, @RequestParam String type, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }
        String email = principal.getName();

        // Validate type
        if (!Arrays.asList("thumbsUp", "heart", "lightbulb", "coffee").contains(type)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid reaction type"));
        }

        // Toggle reaction: if exists, delete it; else, create it
        List<AnnouncementReaction> existing = announcementReactionRepository.findByAnnouncementIdAndUsername(id, email);
        Optional<AnnouncementReaction> specificType = existing.stream()
                .filter(r -> r.getReactionType().equals(type))
                .findFirst();

        if (specificType.isPresent()) {
            announcementReactionRepository.delete(specificType.get());
        } else {
            AnnouncementReaction reaction = new AnnouncementReaction();
            reaction.setAnnouncementId(id);
            reaction.setUsername(email);
            reaction.setReactionType(type);
            reaction.setCreatedAt(LocalDateTime.now());
            announcementReactionRepository.save(reaction);
        }

        return ResponseEntity.ok(ApiResponse.success("Reaction updated successfully", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAnnouncement(@PathVariable Long id) {
        announcementRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Announcement deleted successfully", null));
    }
}
