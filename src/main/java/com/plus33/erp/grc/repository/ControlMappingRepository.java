package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ControlMappingRepository extends JpaRepository<ControlMapping, Long> {
    List<ControlMapping> findByControlLibraryId(Long controlLibraryId);
    List<ControlMapping> findByFrameworkId(Long frameworkId);
    boolean existsByControlLibraryIdAndFrameworkId(Long controlLibraryId, Long frameworkId);
}
