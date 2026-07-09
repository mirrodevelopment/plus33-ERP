/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.secrets
 * File              : PlatformSecretsProvider.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformSecretsProviderController
 * Related Service   : PlatformSecretsProvider
 * Related Repository: PlatformSecretsProviderRepository
 * Related Entity    : PlatformSecretsProvider
 * Related DTO       : N/A
 * Related Mapper    : PlatformSecretsProviderMapper
 * Related DB Table  : platform_secrets_providers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformSecretsProviderController, PlatformSecretsProviderImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements PlatformSecretsProviderService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.secrets;

import com.plus33.erp.platform.entity.PlatformSecretDefinition;
import com.plus33.erp.platform.repository.PlatformSecretDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformSecretsProvider}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.secrets}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PlatformSecretsProviderController
 *   --> PlatformSecretsProvider (this)
 *   --> Validate business rules
 *   --> PlatformSecretsProviderRepository (read/write 'platform_secrets_providers')
 *   --> PlatformSecretsProviderMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code platform_secrets_providers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PlatformSecretsProvider {
    @Autowired PlatformSecretDefinitionRepository secretRepo;
    /**
     * Creates a new secret and persists it to the database.
     *
     * @param alias the alias input value
     * @param key the key input value
     * @param rotationPolicy the rotationPolicy input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void registerSecret(String alias, String key, String rotationPolicy) {
        PlatformSecretDefinition def = new PlatformSecretDefinition();
        def.setAliasPath(alias);
        def.setSecretKey(key);
        def.setRotationPolicy(rotationPolicy);
        def.setLastRotation(LocalDateTime.now());
        def.setNextRotation(LocalDateTime.now().plusDays(30));
        def.setProviderVersion("1");
        secretRepo.save(def);
    }

    /**
     * Performs the rotateSecret operation in this module.
     *
     * @param alias the alias input value
     * @param newKey the newKey input value
     */
    @Transactional
    public void rotateSecret(String alias, String newKey) {
        PlatformSecretDefinition def = secretRepo.findAll().stream()
                .filter(s -> s.getAliasPath().equals(alias))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Secret alias not found"));

        def.setSecretKey(newKey);
        def.setLastRotation(LocalDateTime.now());
        def.setNextRotation(LocalDateTime.now().plusDays(30));
        def.setProviderVersion(String.valueOf(Integer.parseInt(def.getProviderVersion()) + 1));
        secretRepo.save(def);
    }

    /**
     * Retrieves secret key data from the database.
     *
     * @param alias the alias input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSecretKey(String alias) {
        return secretRepo.findAll().stream()
                .filter(s -> s.getAliasPath().equals(alias))
                .map(PlatformSecretDefinition::getSecretKey)
                .findFirst()
                .orElse(null);
    }
}