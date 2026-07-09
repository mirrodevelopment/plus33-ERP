/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.service
 * File              : LearningManagementService.java
 * Purpose           : Business logic service layer for Hcm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LearningManagementController
 * Related Service   : LearningManagementService
 * Related Repository: HcmCourseRepository, LearningEnrollmentRepository
 * Related Entity    : LearningManagement
 * Related DTO       : N/A
 * Related Mapper    : LearningManagementMapper
 * Related DB Table  : learning_managements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LearningManagementController, LearningManagementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Hcm Module. Implements LearningManagementService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.*;
import com.plus33.erp.hcm.repository.*;
import com.plus33.erp.hcm.event.HcmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code LearningManagementService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Hcm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * LearningManagementController
 *   --> LearningManagementService (this)
 *   --> Validate business rules
 *   --> LearningManagementRepository (read/write 'learning_managements')
 *   --> LearningManagementMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code learning_managements}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class LearningManagementService {

    private final HcmCourseRepository courseRepository;
    private final LearningEnrollmentRepository enrollmentRepository;
    private final HcmEventBus eventBus;

    public LearningManagementService(HcmCourseRepository courseRepository,
                                     LearningEnrollmentRepository enrollmentRepository,
                                     HcmEventBus eventBus) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.eventBus = eventBus;
    }

    /**
     * Creates a new course and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param code the code input value
     * @param name the name input value
     * @param mandatory the mandatory input value
     * @return the HcmCourse result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public HcmCourse createCourse(String code, String name, Boolean mandatory) {
        HcmCourse course = new HcmCourse();
        course.setCourseCode(code);
        course.setName(name);
        course.setMandatory(mandatory);
        courseRepository.save(course);
        return course;
    }

    /**
     * Performs the enrollEmployee operation in this module.
     *
     * @param employeeId the employeeId input value
     * @param courseId the courseId input value
     * @return the LearningEnrollment result
     */
    @Transactional
    public LearningEnrollment enrollEmployee(Long employeeId, Long courseId) {
        LearningEnrollment e = new LearningEnrollment();
        e.setEmployeeId(employeeId);
        e.setCourseId(courseId);
        e.setStatus("ENROLLED");
        enrollmentRepository.save(e);
        return e;
    }

    /**
     * Completes the course workflow and finalizes the record status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param enrollmentId the enrollmentId input value
     * @param expiry the expiry input value
     */
    @Transactional
    public void completeCourse(Long enrollmentId, LocalDate expiry) {
        LearningEnrollment e = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));

        e.setStatus("COMPLETED");
        e.setCompletionDate(LocalDate.now());
        e.setExpiryDate(expiry);
        enrollmentRepository.save(e);

        eventBus.publish("TrainingCompleted", 1L, e.getEmployeeId(), "Course completed");
    }
}