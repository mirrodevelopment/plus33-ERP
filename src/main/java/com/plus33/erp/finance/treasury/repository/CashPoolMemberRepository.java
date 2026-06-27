package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.CashPoolMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashPoolMemberRepository extends JpaRepository<CashPoolMember, Long> {
    List<CashPoolMember> findByPoolId(Long poolId);
    Optional<CashPoolMember> findByBankAccountId(Long bankAccountId);
}
