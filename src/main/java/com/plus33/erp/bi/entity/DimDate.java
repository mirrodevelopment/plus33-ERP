package com.plus33.erp.bi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dim_date")
public class DimDate {
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
    public Integer getDateKey() { return dateKey; } public void setDateKey(Integer v) { this.dateKey = v; }
    public java.time.LocalDate getFullDate() { return fullDate; } public void setFullDate(java.time.LocalDate v) { this.fullDate = v; }
    public Integer getDayOfWeek() { return dayOfWeek; } public void setDayOfWeek(Integer v) { this.dayOfWeek = v; }
    public Integer getMonthNumber() { return monthNumber; } public void setMonthNumber(Integer v) { this.monthNumber = v; }
    public String getMonthName() { return monthName; } public void setMonthName(String v) { this.monthName = v; }
    public Integer getQuarterNumber() { return quarterNumber; } public void setQuarterNumber(Integer v) { this.quarterNumber = v; }
    public Integer getYearNumber() { return yearNumber; } public void setYearNumber(Integer v) { this.yearNumber = v; }
    public Integer getFiscalYear() { return fiscalYear; } public void setFiscalYear(Integer v) { this.fiscalYear = v; }
    public Boolean getIsWeekend() { return isWeekend; } public void setIsWeekend(Boolean v) { this.isWeekend = v; }
    public Boolean getIsHoliday() { return isHoliday; } public void setIsHoliday(Boolean v) { this.isHoliday = v; }
    public Boolean getIsWorkingDay() { return isWorkingDay; } public void setIsWorkingDay(Boolean v) { this.isWorkingDay = v; }
}
