package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    @Query("SELECT h FROM Holiday h JOIN h.holidayCalendar hc JOIN hc.policyGroup pg " +
           "WHERE pg.code = :policyGroupCode AND hc.year = :year")
    List<Holiday> findByPolicyGroupCodeAndYear(String policyGroupCode, Integer year);

    @Query("SELECT h FROM Holiday h JOIN h.holidayCalendar hc JOIN hc.policyGroup pg " +
           "WHERE pg.code = :policyGroupCode AND h.holidayDate BETWEEN :start AND :end")
    List<Holiday> findByPolicyGroupCodeAndHolidayDateBetween(String policyGroupCode, LocalDate start, LocalDate end);

    @Query("SELECT COUNT(h) > 0 FROM Holiday h JOIN h.holidayCalendar hc JOIN hc.policyGroup pg " +
           "WHERE pg.code = :policyGroupCode AND h.holidayDate = :date")
    boolean existsByPolicyGroupCodeAndHolidayDate(String policyGroupCode, LocalDate date);

    // Legacy country-code mapped methods for backward compatibility
    @Query("SELECT h FROM Holiday h JOIN h.holidayCalendar hc JOIN hc.policyGroup pg " +
           "WHERE ((pg.code = 'INDIA' AND :countryCode IN ('IN', 'IND')) OR " +
           "       (pg.code = 'UAE' AND :countryCode IN ('AE', 'UAE')) OR " +
           "       (pg.code = 'EU' AND :countryCode NOT IN ('IN', 'IND', 'AE', 'UAE'))) " +
           "AND h.holidayDate BETWEEN :start AND :end")
    List<Holiday> findByCountryCodeAndHolidayDateBetween(String countryCode, LocalDate start, LocalDate end);

    @Query("SELECT COUNT(h) > 0 FROM Holiday h JOIN h.holidayCalendar hc JOIN hc.policyGroup pg " +
           "WHERE ((pg.code = 'INDIA' AND :countryCode IN ('IN', 'IND')) OR " +
           "       (pg.code = 'UAE' AND :countryCode IN ('AE', 'UAE')) OR " +
           "       (pg.code = 'EU' AND :countryCode NOT IN ('IN', 'IND', 'AE', 'UAE'))) " +
           "AND h.holidayDate = :date")
    boolean existsByCountryCodeAndHolidayDate(String countryCode, LocalDate date);

    @Query("SELECT h FROM Holiday h JOIN h.holidayCalendar hc JOIN hc.policyGroup pg " +
           "WHERE ((pg.code = 'INDIA' AND :countryCode IN ('IN', 'IND')) OR " +
           "       (pg.code = 'UAE' AND :countryCode IN ('AE', 'UAE')) OR " +
           "       (pg.code = 'EU' AND :countryCode NOT IN ('IN', 'IND', 'AE', 'UAE'))) " +
           "AND hc.year = :year")
    List<Holiday> findByCountryCodeAndHolidayYear(String countryCode, Integer year);
}
