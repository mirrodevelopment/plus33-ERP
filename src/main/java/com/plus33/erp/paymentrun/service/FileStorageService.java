/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.service
 * File              : FileStorageService.java
 * Purpose           : Business logic service layer for Paymentrun Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FileStorageController
 * Related Service   : FileStorageService
 * Related Repository: FileStorageRepository
 * Related Entity    : FileStorage
 * Related DTO       : N/A
 * Related Mapper    : FileStorageMapper
 * Related DB Table  : file_storages
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FileStorageController, FileStorageServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Paymentrun Module. Implements FileStorageService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code FileStorageService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Paymentrun Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * FileStorageController
 *   --> FileStorageService (this)
 *   --> Validate business rules
 *   --> FileStorageRepository (read/write 'file_storages')
 *   --> FileStorageMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code file_storages}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class FileStorageService {

    private final Path storageDirectory = Paths.get("storage", "exports", "bank");

    /**
     * Persists the export file entity to the database.
     *
     * @param fileName the fileName input value
     * @param content the content input value
     * @return the StoredFileMetadata result
     */
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