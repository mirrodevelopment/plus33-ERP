package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "payment_transmission_logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransmissionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private PaymentFile file;

    @Column(name = "transmission_method", nullable = false, length = 30)
    private String transmissionMethod; // API, SFTP

    @Column(nullable = false, length = 30)
    private String status; // SUCCESS, FAILED

    @Column(name = "request_payload", columnDefinition = "TEXT")
    private String requestPayload;

    @Column(name = "response_payload", columnDefinition = "TEXT")
    private String responsePayload;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "transmitted_at", nullable = false, updatable = false)
    private LocalDateTime transmittedAt;

    @PrePersist
    protected void onCreate() {
        transmittedAt = LocalDateTime.now();
    }
}
