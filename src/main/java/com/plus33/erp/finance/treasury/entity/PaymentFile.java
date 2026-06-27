package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "payment_files")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false, unique = true)
    private PaymentBatch batch;

    @Column(name = "file_name", nullable = false, length = 150)
    private String fileName;

    @Column(name = "file_format", nullable = false, length = 30)
    private String fileFormat; // ISO20022_XML, SEPA_XML, NACHA_TXT, SWIFT_MT101

    @Column(name = "file_content", nullable = false, columnDefinition = "TEXT")
    private String fileContent;

    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }
}
