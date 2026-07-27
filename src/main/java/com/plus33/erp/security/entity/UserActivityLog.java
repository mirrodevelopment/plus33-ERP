/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : UserActivityLog.java
 * Path              : src/main/java/com/plus33/erp/security/entity/UserActivityLog.java
 * Purpose           : JPA entity mapping the 'user_activity_logs' table — stores login attempts,
 *                     IP address, location, browser client user-agents, success status,
 *                     explicit logout timestamps, and periodic activity heartbeats.
 * Version           : 1.0.0
 ******************************************************************************/
package com.plus33.erp.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity_logs")
@Getter
@Setter
public class UserActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "login_time", nullable = false, updatable = false)
    private LocalDateTime loginTime = LocalDateTime.now();

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "location")
    private String location;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;

    @Column(name = "last_active_time")
    private LocalDateTime lastActiveTime;
}
