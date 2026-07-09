package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.CountryWorkPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryWorkPolicyRepository extends JpaRepository<CountryWorkPolicy, Long> {

    Optional<CountryWorkPolicy> findByCountryCode(String countryCode);
}
