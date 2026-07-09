package com.plus33.erp.workforce.entity;

import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "attendance_audit_trail")
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceAuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    @Column(name = "action_type", nullable = false, length = 50)
    private String actionType;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(length = 255)
    private String device;

    @Column(name = "previous_value", columnDefinition = "TEXT")
    private String previousValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
