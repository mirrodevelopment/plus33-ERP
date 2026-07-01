package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.*;
import com.plus33.erp.hcm.repository.*;
import com.plus33.erp.hcm.event.HcmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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

    @Transactional
    public HcmCourse createCourse(String code, String name, Boolean mandatory) {
        HcmCourse course = new HcmCourse();
        course.setCourseCode(code);
        course.setName(name);
        course.setMandatory(mandatory);
        courseRepository.save(course);
        return course;
    }

    @Transactional
    public LearningEnrollment enrollEmployee(Long employeeId, Long courseId) {
        LearningEnrollment e = new LearningEnrollment();
        e.setEmployeeId(employeeId);
        e.setCourseId(courseId);
        e.setStatus("ENROLLED");
        enrollmentRepository.save(e);
        return e;
    }

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
