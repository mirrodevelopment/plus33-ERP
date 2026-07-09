/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformMeshCertificateRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformMeshCertificateController
 * Related Service   : PlatformMeshCertificateService, PlatformMeshCertificateServiceImpl
 * Related Repository: PlatformMeshCertificateRepository
 * Related Entity    : PlatformMeshCertificate
 * Related DTO       : N/A
 * Related Mapper    : PlatformMeshCertificateMapper
 * Related DB Table  : platform_mesh_certificates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformMeshCertificateService, PlatformMeshCertificateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_mesh_certificates' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformMeshCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformMeshCertificateRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_mesh_certificates' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_mesh_certificates}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformMeshCertificateRepository extends JpaRepository<PlatformMeshCertificate, Long> {
}