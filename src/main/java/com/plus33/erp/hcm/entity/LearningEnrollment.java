/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.entity
 * File              : LearningEnrollment.java
 * Purpose           : JPA Entity representing a persistent database record in Hcm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LearningEnrollmentController
 * Related Service   : LearningEnrollmentService, LearningEnrollmentServiceImpl
 * Related Repository: LearningEnrollmentRepository
 * Related Entity    : LearningEnrollment
 * Related DTO       : N/A
 * Related Mapper    : LearningEnrollmentMapper
 * Related DB Table  : hcm_learning_enrollments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LearningEnrollmentRepository, LearningEnrollmentMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'hcm_learning_enrollments'. Defines persistent domain object for Hcm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.hcm.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code LearningEnrollment}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'hcm_learning_enrollments'.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_learning_enrollments}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "hcm_learning_enrollments")
public class LearningEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(nullable = false, length = 30)
    private String status = "ENROLLED";

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    // Getters and setters
    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves employee id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getEmployeeId() { return employeeId; }
    /**
     * Performs the setEmployeeId operation in this module.
     *
     * @param employeeId the employeeId input value
     */
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    /**
     * Retrieves course id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCourseId() { return courseId; }
    /**
     * Performs the setCourseId operation in this module.
     *
     * @param courseId the courseId input value
     */
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Retrieves completion date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getCompletionDate() { return completionDate; }
    /**
     * Performs the setCompletionDate operation in this module.
     *
     * @param completionDate the completionDate input value
     */
    public void setCompletionDate(LocalDate completionDate) { this.completionDate = completionDate; }
    /**
     * Retrieves expiry date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getExpiryDate() { return expiryDate; }
    /**
     * Performs the setExpiryDate operation in this module.
     *
     * @param expiryDate the expiryDate input value
     */
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}