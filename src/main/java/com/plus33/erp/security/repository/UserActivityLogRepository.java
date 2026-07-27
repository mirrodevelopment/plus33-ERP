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

    Optional<UserActivityLog> findFirstByUsernameAndStatusAndLogoutTimeIsNullOrderByLoginTimeDesc(
            String username, 
            String status
    );
}
