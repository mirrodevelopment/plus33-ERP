package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.PickListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickListItemRepository extends JpaRepository<PickListItem, Long> {
}
