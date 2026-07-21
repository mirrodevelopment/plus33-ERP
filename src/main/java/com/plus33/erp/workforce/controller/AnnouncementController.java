package com.plus33.erp.workforce.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.workforce.entity.BroadcastAnnouncement;
import com.plus33.erp.workforce.entity.AnnouncementRead;
import com.plus33.erp.workforce.entity.AnnouncementReaction;
import com.plus33.erp.workforce.dto.AnnouncementRequest;
import com.plus33.erp.workforce.dto.AnnouncementResponse;
import com.plus33.erp.workforce.repository.BroadcastAnnouncementRepository;
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

    private final BroadcastAnnouncementRepository broadcastAnnouncementRepository;
    private final AnnouncementReadRepository announcementReadRepository;
    private final AnnouncementReactionRepository announcementReactionRepository;
    private final UserRepository userRepository;
    private final com.plus33.erp.workforce.repository.UserRegionRepository userRegionRepository;
    private final com.plus33.erp.workforce.repository.UserStoreRepository userStoreRepository;

    public AnnouncementController(
            BroadcastAnnouncementRepository broadcastAnnouncementRepository,
            AnnouncementReadRepository announcementReadRepository,
            AnnouncementReactionRepository announcementReactionRepository,
            UserRepository userRepository,
            com.plus33.erp.workforce.repository.UserRegionRepository userRegionRepository,
            com.plus33.erp.workforce.repository.UserStoreRepository userStoreRepository) {
        this.broadcastAnnouncementRepository = broadcastAnnouncementRepository;
        this.announcementReadRepository = announcementReadRepository;
        this.announcementReactionRepository = announcementReactionRepository;
        this.userRepository = userRepository;
        this.userRegionRepository = userRegionRepository;
        this.userStoreRepository = userStoreRepository;
    }

    @GetMapping
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<AnnouncementResponse>>> getAnnouncements(
            @RequestParam(required = false, defaultValue = "false") boolean includeDeleted,
            Principal principal) {
        String email = principal != null ? principal.getName() : "anonymous";
        LocalDateTime now = LocalDateTime.now();

        boolean isUltimateAdmin = false;
        if (principal != null) {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                isUltimateAdmin = userOpt.get().getRoles().stream()
                        .anyMatch(r -> "ULTIMATE_ADMIN".equalsIgnoreCase(r.getCode()));
            }
        }

        List<BroadcastAnnouncement> announcements;
        if (includeDeleted && isUltimateAdmin) {
            announcements = broadcastAnnouncementRepository.findAllByOrderByCreatedAtDesc();
        } else {
            announcements = broadcastAnnouncementRepository.findByIsDeletedFalseAndExpiresAtAfterOrderByCreatedAtDesc(now);
        }

        // Scope target filtering
        if (principal != null && !isUltimateAdmin) {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User u = userOpt.get();
                boolean isNationalAdmin = u.getRoles().stream()
                        .anyMatch(r -> "NATIONAL_ADMIN".equalsIgnoreCase(r.getCode()));

                if (!isNationalAdmin) {
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

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

            String dateStr = ann.getCreatedAt().format(formatter);
            String expStr = ann.getExpiresAt() != null ? ann.getExpiresAt().format(formatter) : null;
            String delStr = ann.getDeletedAt() != null ? ann.getDeletedAt().format(formatter) : null;

            return new AnnouncementResponse(
                ann.getId(),
                ann.getTitle(),
                ann.getContent(),
                ann.getPriority(),
                ann.getPublisher(),
                ann.getPublisherRole(),
                ann.getPublisherColor(),
                ann.getMediaType(),
                dateStr,
                ann.getCreatedAt().toString(),
                expStr,
                delStr,
                Boolean.TRUE.equals(ann.getIsDeleted()),
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
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<ApiResponse<BroadcastAnnouncement>> createAnnouncement(
            @Valid @RequestBody AnnouncementRequest request, Principal principal) {
        String email = principal != null ? principal.getName() : "System";

        String publisherName = "Management";
        String publisherRole = "STORE_ADMIN";
        Long targetRegionId = null;
        Long targetStoreId = null;

        if (principal != null) {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User u = userOpt.get();
                publisherName = u.getFirstName() + " " + u.getLastName();

                if (u.getRoles() != null && !u.getRoles().isEmpty()) {
                    String roleCode = u.getRoles().iterator().next().getCode();
                    publisherRole = roleCode != null ? roleCode.toUpperCase() : "STORE_ADMIN";
                }

                boolean isRegionalAdmin = u.getRoles().stream()
                        .anyMatch(r -> "REGIONAL_ADMIN".equalsIgnoreCase(r.getCode()));
                boolean isNationalAdmin = u.getRoles().stream()
                        .anyMatch(r -> "NATIONAL_ADMIN".equalsIgnoreCase(r.getCode()));
                boolean isStoreRole = u.getRoles().stream()
                        .anyMatch(r -> "STORE_ADMIN".equalsIgnoreCase(r.getCode()) || "STORE".equalsIgnoreCase(r.getCode()) || "SHIFT_SUPERVISOR".equalsIgnoreCase(r.getCode()));

                if (isRegionalAdmin || isNationalAdmin) {
                    List<com.plus33.erp.workforce.entity.UserRegion> urList = userRegionRepository.findByIdUserId(u.getId());
                    if (urList != null && !urList.isEmpty() && urList.get(0).getId() != null) {
                        targetRegionId = urList.get(0).getId().getRegionId();
                    }
                } else if (isStoreRole) {
                    List<com.plus33.erp.workforce.entity.UserStore> usList = userStoreRepository.findByIdUserId(u.getId());
                    if (usList != null && !usList.isEmpty() && usList.get(0).getId() != null) {
                        targetStoreId = usList.get(0).getId().getStoreId();
                    }
                }
            }
        }

        BroadcastAnnouncement ann = new BroadcastAnnouncement();
        ann.setTitle(request.title());
        ann.setContent(request.content());
        ann.setPriority(request.priority());
        ann.setPublisher(publisherName);
        ann.setPublisherRole(publisherRole);
        ann.setImageUrl(request.imageUrl());
        ann.setTargetRegionId(targetRegionId);
        ann.setTargetStoreId(targetStoreId);

        BroadcastAnnouncement saved = broadcastAnnouncementRepository.save(ann);
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

        if (!Arrays.asList("thumbsUp", "heart", "lightbulb", "coffee").contains(type)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid reaction type"));
        }

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

    private int getRoleRank(String roleCode) {
        if (roleCode == null) return 0;
        String normalized = roleCode.toUpperCase().replace("-", "_").replace(" ", "_");
        if (normalized.contains("ULTIMATE")) return 5;
        if (normalized.contains("NATIONAL")) return 4;
        if (normalized.contains("REGIONAL")) return 3;
        if (normalized.contains("STORE_ADMIN") || normalized.equals("STORE")) return 2;
        if (normalized.contains("SUPERVISOR")) return 1;
        return 0;
    }

    /**
     * Soft-delete endpoint: Sets is_deleted = true and deleted_at = NOW().
     * Enforces role rank hierarchy: Lower-level roles CANNOT delete announcements published by superior roles.
     */
    @DeleteMapping("/{id}")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<ApiResponse<Void>> deleteAnnouncement(@PathVariable Long id, Principal principal) {
        Optional<BroadcastAnnouncement> annOpt = broadcastAnnouncementRepository.findById(id);
        if (annOpt.isEmpty()) {
            return ResponseEntity.status(404).body(ApiResponse.error("Announcement not found"));
        }

        BroadcastAnnouncement ann = annOpt.get();

        if (principal != null) {
            String email = principal.getName();
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User u = userOpt.get();
                String currentUserRoleCode = (u.getRoles() != null && !u.getRoles().isEmpty())
                        ? u.getRoles().iterator().next().getCode()
                        : "STORE_ADMIN";

                int currentUserRank = getRoleRank(currentUserRoleCode);
                int publisherRank = getRoleRank(ann.getPublisherRole());

                if (currentUserRank < publisherRank) {
                    return ResponseEntity.status(403).body(
                        ApiResponse.error("Access Denied: You do not have permission to delete an announcement published by a superior role (" + ann.getPublisherRole() + ")")
                    );
                }
            }
        }

        ann.setIsDeleted(true);
        ann.setDeletedAt(LocalDateTime.now());
        broadcastAnnouncementRepository.save(ann);

        return ResponseEntity.ok(ApiResponse.success("Announcement archived and marked as deleted", null));
    }
}
