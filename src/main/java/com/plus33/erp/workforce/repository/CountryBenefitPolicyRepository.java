package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.CountryBenefitPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryBenefitPolicyRepository extends JpaRepository<CountryBenefitPolicy, Long> {
    Optional<CountryBenefitPolicy> findByCountryCode(String countryCode);
}
