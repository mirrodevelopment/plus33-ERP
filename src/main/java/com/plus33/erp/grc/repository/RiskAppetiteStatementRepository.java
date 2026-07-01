package com.plus33.erp.grc.repository;

import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RiskAppetiteStatementRepository extends JpaRepository<RiskAppetiteStatement, Long> {
    Optional<RiskAppetiteStatement> findByCompanyIdAndRiskCategoryAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
        Long companyId, String riskCategory, LocalDate date1, LocalDate date2);
}
