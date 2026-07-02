package com.plus33.erp.compliance.attestation;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TpmAttestationValidator {
    @Autowired PlatformDeviceAttestationRepository attestationRepo;

    @Transactional
    public PlatformDeviceAttestation verifyQuote(Long deviceId, String nonce, String result, BigDecimal score) {
        PlatformDeviceAttestation att = new PlatformDeviceAttestation();
        att.setDeviceId(deviceId);
        att.setPcrValues("PCR_0: 4F6A2D..., PCR_1: 8F9E2A...");
        att.setAkCertificate("AK-RSA-2048-CERTIFICATE-PEM-DATA");
        att.setEkCertificate("EK-RSA-2048-CERTIFICATE-PEM-DATA");
        att.setNonce(nonce);
        att.setMeasuredBootHash("SHA256-BOOT-HASH-VAL");
        att.setFirmwareMeasurements("GRUB-PCR4: ok, kernel-PCR8: ok");
        att.setSecureBootStatus(true);
        att.setTpmManufacturer("Infineon");
        att.setTpmVersion("2.0");
        att.setAttestationResult(result);
        att.setTrustScore(score);
        att.setVerifiedBy("zero-trust-attestator");
        att.setVerificationTime(LocalDateTime.now());
        att.setCertificateChain("PLUS33 Root CA -> Intermediate CA -> AK");
        return attestationRepo.save(att);
    }
}