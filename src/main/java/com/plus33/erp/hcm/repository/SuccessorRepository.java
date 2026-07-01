package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.Successor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SuccessorRepository extends JpaRepository<Successor, Long> {
    List<Successor> findByTalentPoolId(Long talentPoolId);
}
