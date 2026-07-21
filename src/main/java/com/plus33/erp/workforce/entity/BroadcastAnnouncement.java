package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "broadcast_announcements")
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastAnnouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 50)
    private String priority = "Standard Notice";

    @Column(nullable = false)
    private String publisher;

    @Column(name = "publisher_role", nullable = false, length = 50)
    private String publisherRole = "STORE_ADMIN";

    @Column(name = "publisher_color", nullable = false, length = 30)
    private String publisherColor = "#c9a46a";

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(name = "media_type", nullable = false, length = 30)
    private String mediaType = "IMAGE";

    @Column(name = "target_region_id")
    private Long targetRegionId;

    @Column(name = "target_store_id")
    private Long targetStoreId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (expiresAt == null) {
            expiresAt = createdAt.plusDays(15);
        }
        if (isDeleted == null) {
            isDeleted = false;
        }

        // Auto-assign mediaType based on imageUrl extension
        if (imageUrl != null && !imageUrl.isBlank()) {
            String lower = imageUrl.toLowerCase();
            if (lower.endsWith(".mp4") || lower.endsWith(".webm") || lower.endsWith(".ogg") || lower.endsWith(".mov")) {
                mediaType = "VIDEO";
            } else if (lower.endsWith(".mp3") || lower.endsWith(".wav") || lower.endsWith(".aac") || lower.endsWith(".m4a")) {
                mediaType = "AUDIO";
            } else if (lower.endsWith(".pdf")) {
                mediaType = "PDF";
            } else if (lower.endsWith(".doc") || lower.endsWith(".docx") || lower.endsWith(".xls") || lower.endsWith(".xlsx") || lower.endsWith(".zip")) {
                mediaType = "DOCUMENT";
            } else {
                mediaType = "IMAGE";
            }
        }

        // Auto-assign role color if default
        if (publisherRole != null) {
            String roleUpper = publisherRole.toUpperCase();
            if (roleUpper.contains("ULTIMATE")) {
                publisherColor = "#d946ef"; // Royal Gold / Violet
            } else if (roleUpper.contains("NATIONAL")) {
                publisherColor = "#3b82f6"; // Sapphire Blue
            } else if (roleUpper.contains("REGIONAL")) {
                publisherColor = "#06b6d4"; // Emerald Cyan
            } else if (roleUpper.contains("SUPERVISOR")) {
                publisherColor = "#f97316"; // Sunset Coral
            } else {
                publisherColor = "#c9a46a"; // Warm Coffee Amber
            }
        }
    }
}
