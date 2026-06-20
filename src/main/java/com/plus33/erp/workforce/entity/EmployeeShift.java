package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "employee_shifts")
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeShift {

    @EmbeddedId
    private EmployeeShiftId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("shiftId")
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class EmployeeShiftId implements Serializable {

        @Column(name = "employee_id")
        private Long employeeId;

        @Column(name = "shift_id")
        private Long shiftId;

        @Column(name = "effective_from")
        private LocalDate effectiveFrom;
    }
}
