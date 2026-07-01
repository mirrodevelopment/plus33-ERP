package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ComplianceEvidenceRepository extends JpaRepository<ComplianceEvidence, Long> {
    Optional<ComplianceEvidence> findByContentHash(String hash);
    boolean existsByContentHash(String hash);
    long countByReferenceTypeAndReferenceId(String type, Long referenceId);
}
