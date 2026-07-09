package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "attendance_breaks")
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceBreak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", nullable = false)
    private Attendance attendance;

    @Column(name = "break_start", nullable = false)
    private LocalDateTime breakStart;

    @Column(name = "break_end")
    private LocalDateTime breakEnd;

    @Column(name = "duration_minutes")
    private Integer durationMinutes = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
