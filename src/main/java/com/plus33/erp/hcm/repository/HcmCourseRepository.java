/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : HcmCourseRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
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
 * Used By           : HcmCourseService, HcmCourseServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'hcm_courses' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code HcmCourseRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'hcm_courses' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_courses}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface HcmCourseRepository extends JpaRepository<HcmCourse, Long> {
    Optional<HcmCourse> findByCourseCode(String courseCode);
}
