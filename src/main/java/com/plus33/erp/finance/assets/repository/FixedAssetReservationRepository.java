package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetReservation;
import com.plus33.erp.finance.assets.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetReservationRepository extends JpaRepository<FixedAssetReservation, Long> {
    List<FixedAssetReservation> findAllByFixedAssetId(Long fixedAssetId);
    List<FixedAssetReservation> findAllByFixedAssetIdOrderByReservationDateDesc(Long fixedAssetId);
    List<FixedAssetReservation> findAllByStatus(ReservationStatus status);
}
