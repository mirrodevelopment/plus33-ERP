package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ControlLibraryRepository extends JpaRepository<ControlLibrary, Long> {
    Optional<ControlLibrary> findByControlCode(String code);
    List<ControlLibrary> findByCompanyIdAndStatus(Long companyId, String status);
}
