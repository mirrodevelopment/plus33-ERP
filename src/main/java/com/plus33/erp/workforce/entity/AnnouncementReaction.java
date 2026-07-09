package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
    name = "announcement_reactions",
    uniqueConstraints = @UniqueConstraint(name = "uk_announcement_reactions_user", columnNames = {"announcement_id", "username", "reaction_type"})
)
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "announcement_id", nullable = false)
    private Long announcementId;

    @Column(nullable = false)
    private String username;

    @Column(name = "reaction_type", nullable = false, length = 50)
    private String reactionType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
