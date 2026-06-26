package com.plus33.erp.paymentrun.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class FileStorageService {

    private final Path storageDirectory = Paths.get("storage", "exports", "bank");

    public StoredFileMetadata storeExportFile(String fileName, String content) {
        try {
            // Ensure directories exist
            Files.createDirectories(storageDirectory);

            Path filePath = storageDirectory.resolve(fileName);
            Files.writeString(filePath, content, StandardCharsets.UTF_8);

            String checksum = calculateSha256(content);

            return new StoredFileMetadata(
                fileName,
                filePath.toAbsolutePath().toString(),
                checksum,
                LocalDateTime.now()
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to store export file: " + fileName, e);
        }
    }

    private String calculateSha256(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public record StoredFileMetadata(
        String fileName,
        String storagePath,
        String checksum,
        LocalDateTime generatedAt
    ) {}
}
