/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.repository
 * File              : FixedAssetReservationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetReservationController
 * Related Service   : FixedAssetReservationService, FixedAssetReservationServiceImpl
 * Related Repository: FixedAssetReservationRepository
 * Related Entity    : FixedAssetReservation
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetReservationMapper
 * Related DB Table  : fixed_asset_reservations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetReservationService, FixedAssetReservationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'fixed_asset_reservations' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetReservation;
import com.plus33.erp.finance.assets.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetReservationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'fixed_asset_reservations' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_reservations}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface FixedAssetReservationRepository extends JpaRepository<FixedAssetReservation, Long> {
    List<FixedAssetReservation> findAllByFixedAssetId(Long fixedAssetId);
    List<FixedAssetReservation> findAllByFixedAssetIdOrderByReservationDateDesc(Long fixedAssetId);
    List<FixedAssetReservation> findAllByStatus(ReservationStatus status);
}