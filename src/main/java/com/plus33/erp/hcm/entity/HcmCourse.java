/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.entity
 * File              : HcmCourse.java
 * Purpose           : JPA Entity representing a persistent database record in Hcm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HcmCourseController
 * Related Service   : HcmCourseService, HcmCourseServiceImpl
 * Related Repository: HcmCourseRepository
 * Related Entity    : HcmCourse
 * Related DTO       : N/A
 * Related Mapper    : HcmCourseMapper
 * Related DB Table  : hcm_courses
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : HcmCourseRepository, HcmCourseMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'hcm_courses'. Defines persistent domain object for Hcm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.hcm.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code HcmCourse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'hcm_courses'.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_courses}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "hcm_courses")
public class HcmCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_code", nullable = false, unique = true, length = 50)
    private String courseCode;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private Boolean mandatory = false;

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
     * Retrieves course code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCourseCode() { return courseCode; }
    /**
     * Performs the setCourseCode operation in this module.
     *
     * @param courseCode the courseCode input value
     */
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    /**
     * Retrieves name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getName() { return name; }
    /**
     * Performs the setName operation in this module.
     *
     * @param name the name input value
     */
    public void setName(String name) { this.name = name; }
    /**
     * Retrieves mandatory data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getMandatory() { return mandatory; }
    /**
     * Performs the setMandatory operation in this module.
     *
     * @param mandatory the mandatory input value
     */
    public void setMandatory(Boolean mandatory) { this.mandatory = mandatory; }
}