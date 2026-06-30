package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.MrpPeggingLink;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MrpPeggingLinkRepository extends JpaRepository<MrpPeggingLink, Long> {
    List<MrpPeggingLink> findByMrpRunId(Long mrpRunId);
    List<MrpPeggingLink> findByCompanyId(Long companyId);
    List<MrpPeggingLink> findByDemandTypeAndDemandId(String demandType, Long demandId);
    List<MrpPeggingLink> findBySupplyTypeAndSupplyId(String supplyType, Long supplyId);
}
