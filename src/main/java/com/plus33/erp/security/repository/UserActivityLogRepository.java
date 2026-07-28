/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : UserActivityLogRepository.java
 * Path              : src/main/java/com/plus33/erp/security/repository/UserActivityLogRepository.java
 * Purpose           : Spring Data JPA repository for the 'user_activity_logs' table.
 * Version           : 1.0.0
 ******************************************************************************/
package com.plus33.erp.security.repository;

import com.plus33.erp.security.entity.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {
    
    List<UserActivityLog> findAllByOrderByLoginTimeDesc();

    List<UserActivityLog> findByUsernameOrderByLoginTimeDesc(String username);

    List<UserActivityLog> findByUsernameInOrderByLoginTimeDesc(List<String> usernames);

    List<UserActivityLog> findByLoginTimeBetweenOrderByLoginTimeDesc(java.time.LocalDateTime start, java.time.LocalDateTime end);

    List<UserActivityLog> findByUsernameInAndLoginTimeBetweenOrderByLoginTimeDesc(List<String> usernames, java.time.LocalDateTime start, java.time.LocalDateTime end);

    List<UserActivityLog> findByUsernameAndLoginTimeBetweenOrderByLoginTimeDesc(String username, java.time.LocalDateTime start, java.time.LocalDateTime end);

    Optional<UserActivityLog> findFirstByUsernameAndStatusAndLogoutTimeIsNullOrderByLoginTimeDesc(
            String username, 
            String status
    );

    @org.springframework.data.jpa.repository.Query("SELECT l FROM UserActivityLog l WHERE " +
        "(LOWER(l.username) = LOWER(:target) OR LOWER(l.username) LIKE LOWER(CONCAT(:prefix, '%')) " +
        "OR (:userId IS NOT NULL AND l.userId = :userId)) " +
        "AND (:start IS NULL OR l.loginTime >= :start) " +
        "AND (:end IS NULL OR l.loginTime <= :end) " +
        "ORDER BY l.loginTime DESC")
    List<UserActivityLog> findFlexibleLogs(
        @org.springframework.data.repository.query.Param("target") String target,
        @org.springframework.data.repository.query.Param("prefix") String prefix,
        @org.springframework.data.repository.query.Param("userId") Long userId,
        @org.springframework.data.repository.query.Param("start") java.time.LocalDateTime start,
        @org.springframework.data.repository.query.Param("end") java.time.LocalDateTime end
    );
}
