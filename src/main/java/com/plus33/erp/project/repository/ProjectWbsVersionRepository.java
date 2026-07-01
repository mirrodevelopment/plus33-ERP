package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectWbsVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectWbsVersionRepository extends JpaRepository<ProjectWbsVersion, Long> {
    List<ProjectWbsVersion> findByWbsId(Long wbsId);
}
