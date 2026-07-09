/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : DimDate.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DimDateController
 * Related Service   : DimDateService, DimDateServiceImpl
 * Related Repository: DimDateRepository
 * Related Entity    : DimDate
 * Related DTO       : N/A
 * Related Mapper    : DimDateMapper
 * Related DB Table  : dim_date
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DimDateRepository, DimDateMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'dim_date'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code DimDate}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'dim_date'.</p>
 *
 * <p><b>Database Table   :</b> {@code dim_date}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "dim_date")
public class DimDate {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "date_key", nullable = false, unique = true) private Integer dateKey;
    @Column(name = "full_date", nullable = false, unique = true) private java.time.LocalDate fullDate;
    @Column(name = "day_of_week") private Integer dayOfWeek;
    @Column(name = "day_name", length = 20) private String dayName;
    @Column(name = "day_of_month") private Integer dayOfMonth;
    @Column(name = "month_number") private Integer monthNumber;
    @Column(name = "month_name", length = 20) private String monthName;
    @Column(name = "quarter_number") private Integer quarterNumber;
    @Column(name = "quarter_name", length = 10) private String quarterName;
    @Column(name = "year_number") private Integer yearNumber;
    @Column(name = "fiscal_year") private Integer fiscalYear;
    @Column(name = "fiscal_quarter") private Integer fiscalQuarter;
    @Column(name = "fiscal_month") private Integer fiscalMonth;
    @Column(name = "is_weekend", nullable = false) private Boolean isWeekend = false;
    @Column(name = "is_holiday", nullable = false) private Boolean isHoliday = false;
    @Column(name = "is_working_day", nullable = false) private Boolean isWorkingDay = true;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves date key data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getDateKey() { return dateKey; } public void setDateKey(Integer v) { this.dateKey = v; }
    /**
     * Retrieves full date data from the database.
     *
     * @param v the v input value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public java.time.LocalDate getFullDate() { return fullDate; } public void setFullDate(java.time.LocalDate v) { this.fullDate = v; }
    /**
     * Retrieves day of week data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getDayOfWeek() { return dayOfWeek; } public void setDayOfWeek(Integer v) { this.dayOfWeek = v; }
    /**
     * Retrieves month number data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getMonthNumber() { return monthNumber; } public void setMonthNumber(Integer v) { this.monthNumber = v; }
    /**
     * Retrieves month name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMonthName() { return monthName; } public void setMonthName(String v) { this.monthName = v; }
    /**
     * Retrieves quarter number data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getQuarterNumber() { return quarterNumber; } public void setQuarterNumber(Integer v) { this.quarterNumber = v; }
    /**
     * Retrieves year number data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getYearNumber() { return yearNumber; } public void setYearNumber(Integer v) { this.yearNumber = v; }
    /**
     * Retrieves fiscal year data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getFiscalYear() { return fiscalYear; } public void setFiscalYear(Integer v) { this.fiscalYear = v; }
    /**
     * Retrieves is weekend data from the database.
     *
     * @param v the v input value
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIsWeekend() { return isWeekend; } public void setIsWeekend(Boolean v) { this.isWeekend = v; }
    /**
     * Retrieves is holiday data from the database.
     *
     * @param v the v input value
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIsHoliday() { return isHoliday; } public void setIsHoliday(Boolean v) { this.isHoliday = v; }
    /**
     * Retrieves is working day data from the database.
     *
     * @param v the v input value
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIsWorkingDay() { return isWorkingDay; } public void setIsWorkingDay(Boolean v) { this.isWorkingDay = v; }
}