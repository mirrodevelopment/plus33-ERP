package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformSecretDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlatformSecretDefinitionRepository extends JpaRepository<PlatformSecretDefinition, Long> {
    Optional<PlatformSecretDefinition> findByAliasPath(String aliasPath);
}