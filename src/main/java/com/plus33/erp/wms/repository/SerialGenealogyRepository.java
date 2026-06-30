package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.SerialGenealogy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SerialGenealogyRepository extends JpaRepository<SerialGenealogy, Long> {
    List<SerialGenealogy> findByCompanyIdAndChildSerialNumber(Long companyId, String childSerialNumber);
}
