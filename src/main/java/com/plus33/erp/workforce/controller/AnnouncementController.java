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
    private final com.plus33.erp.workforce.repository.UserRegionRepository userRegionRepository;
    private final com.plus33.erp.workforce.repository.UserStoreRepository userStoreRepository;

    public AnnouncementController(
            AnnouncementRepository announcementRepository,
            AnnouncementReadRepository announcementReadRepository,
            AnnouncementReactionRepository announcementReactionRepository,
            UserRepository userRepository,
            com.plus33.erp.workforce.repository.UserRegionRepository userRegionRepository,
            com.plus33.erp.workforce.repository.UserStoreRepository userStoreRepository) {
        this.announcementRepository = announcementRepository;
        this.announcementReadRepository = announcementReadRepository;
        this.announcementReactionRepository = announcementReactionRepository;
        this.userRepository = userRepository;
        this.userRegionRepository = userRegionRepository;
        this.userStoreRepository = userStoreRepository;
    }

    @GetMapping
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<AnnouncementResponse>>> getAnnouncements(Principal principal) {
        String email = principal != null ? principal.getName() : "anonymous";
        List<Announcement> announcements = announcementRepository.findAll();
        
        // Target filtering based on user scope
        if (principal != null) {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User u = userOpt.get();
                boolean isSuperAdmin = u.getRoles().stream()
                    .anyMatch(r -> "ULTIMATE_ADMIN".equalsIgnoreCase(r.getCode()) || "NATIONAL_ADMIN".equalsIgnoreCase(r.getCode()));
                
                if (!isSuperAdmin) {
                    Set<Long> userRegionIds = new HashSet<>();
                    for (com.plus33.erp.workforce.entity.UserRegion ur : userRegionRepository.findByIdUserId(u.getId())) {
                        if (ur.getRegion() != null) {
                            userRegionIds.add(ur.getRegion().getId());
                            if (ur.getRegion().getParent() != null) {
                                userRegionIds.add(ur.getRegion().getParent().getId());
                            }
                        }
                    }
                    
                    Set<Long> userStoreIds = new HashSet<>();
                    List<com.plus33.erp.workforce.entity.UserStore> userStores = userStoreRepository.findByIdUserId(u.getId());
                    for (com.plus33.erp.workforce.entity.UserStore us : userStores) {
                        if (us.getStore() != null) {
                            userStoreIds.add(us.getStore().getId());
                            if (us.getStore().getRegion() != null) {
                                userRegionIds.add(us.getStore().getRegion().getId());
                                if (us.getStore().getRegion().getParent() != null) {
                                    userRegionIds.add(us.getStore().getRegion().getParent().getId());
                                }
                            }
                        }
                    }
                    
                    announcements = announcements.stream().filter(ann -> {
                        if (ann.getTargetRegionId() == null && ann.getTargetStoreId() == null) {
                            return true;
                        }
                        if (ann.getTargetRegionId() != null && userRegionIds.contains(ann.getTargetRegionId())) {
                            return true;
                        }
                        if (ann.getTargetStoreId() != null && userStoreIds.contains(ann.getTargetStoreId())) {
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());
                }
            }
        }

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
 
            // Resolve publisher role dynamically
            String publisherRole = "Management";
            String pub = ann.getPublisher();
            if (pub != null && !pub.trim().isEmpty() && !pub.equalsIgnoreCase("Management") && !pub.equalsIgnoreCase("System")) {
                List<User> users = userRepository.findByFullName(pub.trim());
                if (users != null && !users.isEmpty()) {
                    User user = users.get(0);
                    if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                        com.plus33.erp.security.entity.Role r = user.getRoles().iterator().next();
                        String rCode = r.getCode();
                        if ("ULTIMATE_ADMIN".equalsIgnoreCase(rCode)) {
                            publisherRole = "System Admin";
                        } else if ("NATIONAL_ADMIN".equalsIgnoreCase(rCode)) {
                            publisherRole = "National Admin";
                        } else if ("REGIONAL_ADMIN".equalsIgnoreCase(rCode)) {
                            publisherRole = "Regional Admin";
                        } else if ("STORE_ADMIN".equalsIgnoreCase(rCode)) {
                            publisherRole = "Store Admin";
                        } else if ("SHIFT_SUPERVISOR".equalsIgnoreCase(rCode)) {
                            publisherRole = "Shift Supervisor";
                        } else if ("SENIOR_EMPLOYEE".equalsIgnoreCase(rCode) || "STORE_EMPLOYEE".equalsIgnoreCase(rCode)) {
                            publisherRole = "Store Employee";
                        } else {
                            publisherRole = r.getName();
                        }
                    }
                }
            }

            return new AnnouncementResponse(
                ann.getId(),
                ann.getTitle(),
                ann.getContent(),
                ann.getPriority(),
                ann.getPublisher(),
                publisherRole,
                dateStr,
                ann.getCreatedAt().toString(),
                read,
                reactionCounts,
                ann.getImageUrl(),
                ann.getTargetRegionId(),
                ann.getTargetStoreId()
            );
        }).collect(Collectors.toList());
 
        return ResponseEntity.ok(ApiResponse.success("Announcements retrieved successfully", responses));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Announcement>> createAnnouncement(
            @Valid @RequestBody AnnouncementRequest request, Principal principal) {
        String email = principal != null ? principal.getName() : "System";
        
        // Retrieve user info for publisher name and determine target region/store
        String publisherName = "Management";
        Long targetRegionId = null;
        Long targetStoreId = null;
        if (principal != null) {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User u = userOpt.get();
                publisherName = u.getFirstName() + " " + u.getLastName();
                
                boolean isRegionalAdmin = u.getRoles().stream()
                    .anyMatch(r -> "REGIONAL_ADMIN".equalsIgnoreCase(r.getCode()));
                boolean isNationalAdmin = u.getRoles().stream()
                    .anyMatch(r -> "NATIONAL_ADMIN".equalsIgnoreCase(r.getCode()));
                boolean isStoreRole = u.getRoles().stream()
                    .anyMatch(r -> "STORE_ADMIN".equalsIgnoreCase(r.getCode()) || "STORE".equalsIgnoreCase(r.getCode()) || "SHIFT_SUPERVISOR".equalsIgnoreCase(r.getCode()));
                
                if (isRegionalAdmin || isNationalAdmin) {
                    List<com.plus33.erp.workforce.entity.UserRegion> urList = userRegionRepository.findByIdUserId(u.getId());
                    if (urList != null && !urList.isEmpty() && urList.get(0).getRegion() != null) {
                        targetRegionId = urList.get(0).getRegion().getId();
                    }
                } else if (isStoreRole) {
                    List<com.plus33.erp.workforce.entity.UserStore> usList = userStoreRepository.findByIdUserId(u.getId());
                    if (usList != null && !usList.isEmpty() && usList.get(0).getStore() != null) {
                        targetStoreId = usList.get(0).getStore().getId();
                    }
                }
            }
        }

        Announcement ann = new Announcement();
        ann.setTitle(request.title());
        ann.setContent(request.content());
        ann.setPriority(request.priority());
        ann.setPublisher(publisherName);
        ann.setCreatedAt(LocalDateTime.now());
        ann.setImageUrl(request.imageUrl());
        ann.setTargetRegionId(targetRegionId);
        ann.setTargetStoreId(targetStoreId);

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
        Optional<Announcement> annOpt = announcementRepository.findById(id);
        if (annOpt.isPresent()) {
            Announcement ann = annOpt.get();
            String imageUrl = ann.getImageUrl();
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                try {
                    String cleanPath = imageUrl;
                    if (cleanPath.startsWith("/")) {
                        cleanPath = cleanPath.substring(1);
                    }
                    java.nio.file.Path filePath = java.nio.file.Paths.get("frontend", cleanPath);
                    if (java.nio.file.Files.exists(filePath)) {
                        java.nio.file.Files.delete(filePath);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to delete attachment file: " + e.getMessage());
                }
            }
            announcementRepository.delete(ann);
        }
        return ResponseEntity.ok(ApiResponse.success("Announcement deleted successfully", null));
    }
}
