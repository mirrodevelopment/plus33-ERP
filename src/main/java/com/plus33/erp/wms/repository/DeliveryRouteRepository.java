package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, Long> {
    Optional<DeliveryRoute> findByRouteNumber(String routeNumber);
    List<DeliveryRoute> findByCompanyIdAndStatus(Long companyId, String status);
}
