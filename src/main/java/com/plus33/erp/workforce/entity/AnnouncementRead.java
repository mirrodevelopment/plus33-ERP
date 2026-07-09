package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
    name = "announcement_reads",
    uniqueConstraints = @UniqueConstraint(name = "uk_announcement_reads_user", columnNames = {"announcement_id", "username"})
)
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementRead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "announcement_id", nullable = false)
    private Long announcementId;

    @Column(nullable = false)
    private String username;

    @Column(name = "read_at", nullable = false)
    private LocalDateTime readAt = LocalDateTime.now();
}
