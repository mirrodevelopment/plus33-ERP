package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.PpmProject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PpmProjectRepository extends JpaRepository<PpmProject, Long> {
    Optional<PpmProject> findByProjectNumber(String projectNumber);
}
