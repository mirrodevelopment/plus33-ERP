/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : UserStoreRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: UserStoreController
 * Related Service   : UserStoreService, UserStoreServiceImpl
 * Related Repository: UserStoreRepository
 * Related Entity    : UserStore
 * Related DTO       : N/A
 * Related Mapper    : UserStoreMapper
 * Related DB Table  : user_stores
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : UserStoreService, UserStoreServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'user_stores' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code UserStoreRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'user_stores' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code user_stores}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface UserStoreRepository extends JpaRepository<UserStore, UserStore.UserStoreId> {
    boolean existsByIdStoreId(Long storeId);
    List<UserStore> findByIdUserId(Long userId);
    void deleteByIdUserId(Long userId);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(us) FROM UserStore us WHERE us.id.storeId = :storeId")
    long countByStoreId(@org.springframework.data.repository.query.Param("storeId") Long storeId);
}
