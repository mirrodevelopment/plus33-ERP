package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectResource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProjectResourceRepository extends JpaRepository<ProjectResource, Long> {
    Optional<ProjectResource> findByEmail(String email);
}
