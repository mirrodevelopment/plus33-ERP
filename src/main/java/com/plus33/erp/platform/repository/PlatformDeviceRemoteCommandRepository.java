package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformDeviceRemoteCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformDeviceRemoteCommandRepository extends JpaRepository<PlatformDeviceRemoteCommand, Long> {
}