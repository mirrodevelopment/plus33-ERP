package com.plus33.erp.platform.secrets;

import com.plus33.erp.platform.entity.PlatformSecretDefinition;
import com.plus33.erp.platform.repository.PlatformSecretDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class PlatformSecretsProvider {
    @Autowired PlatformSecretDefinitionRepository secretRepo;

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

    public String getSecretKey(String alias) {
        return secretRepo.findAll().stream()
                .filter(s -> s.getAliasPath().equals(alias))
                .map(PlatformSecretDefinition::getSecretKey)
                .findFirst()
                .orElse(null);
    }
}