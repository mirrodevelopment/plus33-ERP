package com.plus33.erp.hcm.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hcm_rosters")
public class Roster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;

    @Column(name = "shift_pattern_id", nullable = false)
    private Long shiftPatternId;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public LocalDate getShiftDate() { return shiftDate; }
    public void setShiftDate(LocalDate shiftDate) { this.shiftDate = shiftDate; }
    public Long getShiftPatternId() { return shiftPatternId; }
    public void setShiftPatternId(Long shiftPatternId) { this.shiftPatternId = shiftPatternId; }
}
