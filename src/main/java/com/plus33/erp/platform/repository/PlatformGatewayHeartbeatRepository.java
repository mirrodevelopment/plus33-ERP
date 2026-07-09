/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformGatewayHeartbeatRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformGatewayHeartbeatController
 * Related Service   : PlatformGatewayHeartbeatService, PlatformGatewayHeartbeatServiceImpl
 * Related Repository: PlatformGatewayHeartbeatRepository
 * Related Entity    : PlatformGatewayHeartbeat
 * Related DTO       : N/A
 * Related Mapper    : PlatformGatewayHeartbeatMapper
 * Related DB Table  : platform_gateway_heartbeats
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformGatewayHeartbeatService, PlatformGatewayHeartbeatServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_gateway_heartbeats' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformGatewayHeartbeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformGatewayHeartbeatRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_gateway_heartbeats' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_gateway_heartbeats}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformGatewayHeartbeatRepository extends JpaRepository<PlatformGatewayHeartbeat, Long> {
}