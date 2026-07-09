package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @Entity @Table(name = "holiday_calendar")
@NoArgsConstructor @AllArgsConstructor
public class HolidayCalendar {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", nullable = false, length = 10)
    private String countryCode = "FR";

    @Column(name = "region", length = 50)
    private String region;

    @Column(name = "holiday_name", nullable = false, length = 150)
    private String holidayName;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    @Column(name = "holiday_year", nullable = false)
    private Integer holidayYear;

    /** If TRUE, this holiday repeats on the same date every year */
    @Column(name = "is_recurring")
    private Boolean isRecurring = false;

    @Column(name = "is_working_day")
    private Boolean isWorkingDay = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate  protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
