package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.compliance.attestation.TpmAttestationValidator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ZeroTrustAttestationTest {

    @Autowired TpmAttestationValidator attestationValidator;

    @Autowired PlatformDeviceAttestationRepository attestationRepo;

    @Test
    void testZeroTrustAttestationScenarios() {
        // Zero-trust TPM quote attestations verification over 40 iterations
        for (int i = 1; i <= 40; i++) {
            attestationValidator.verifyQuote(1L, "NONCE-VALUE-SHA512-" + i, "VERIFIED", BigDecimal.valueOf(95.50));
        }

        List<PlatformDeviceAttestation> attestations = attestationRepo.findAll();
        assertTrue(attestations.size() >= 40);
        assertEquals("VERIFIED", attestations.get(0).getAttestationResult());
        assertEquals("Infineon", attestations.get(0).getTpmManufacturer());
        assertTrue(attestations.get(0).getSecureBootStatus());
    }
}
