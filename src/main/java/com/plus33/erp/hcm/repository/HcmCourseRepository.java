package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HcmCourseRepository extends JpaRepository<HcmCourse, Long> {
    Optional<HcmCourse> findByCourseCode(String courseCode);
}
