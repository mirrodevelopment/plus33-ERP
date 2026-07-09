/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformIotGateway.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformIotGatewayController
 * Related Service   : PlatformIotGatewayService, PlatformIotGatewayServiceImpl
 * Related Repository: PlatformIotGatewayRepository
 * Related Entity    : PlatformIotGateway
 * Related DTO       : N/A
 * Related Mapper    : PlatformIotGatewayMapper
 * Related DB Table  : platform_iot_gateway
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformIotGatewayRepository, PlatformIotGatewayMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_iot_gateway'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformIotGateway}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_iot_gateway'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_iot_gateway}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_iot_gateway")
public class PlatformIotGateway {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "gateway_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String gatewayCode;

    @Column(name = "gateway_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String gatewayStatus = "OFFLINE";

    @Column(name = "firmware_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String firmwareVersion;

    @Column(name = "certificate_thumbprint", nullable = false)
    @NotNull
    @Size(max = 150)
    private String certificateThumbprint;

    @Column(name = "edge_cluster", nullable = false)
    @NotNull
    @Size(max = 100)
    private String edgeCluster;

    @Column(name = "mqtt_client_id", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String mqttClientId;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Integer version) { this.version = version; }
    /**
     * Retrieves gateway code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getGatewayCode() { return gatewayCode; }
    /**
     * Performs the setGatewayCode operation in this module.
     *
     * @param gatewayCode the gatewayCode input value
     */
    public void setGatewayCode(String gatewayCode) { this.gatewayCode = gatewayCode; }
    /**
     * Retrieves gateway status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getGatewayStatus() { return gatewayStatus; }
    /**
     * Performs the setGatewayStatus operation in this module.
     *
     * @param gatewayStatus the gatewayStatus input value
     */
    public void setGatewayStatus(String gatewayStatus) { this.gatewayStatus = gatewayStatus; }
    /**
     * Retrieves firmware version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFirmwareVersion() { return firmwareVersion; }
    /**
     * Performs the setFirmwareVersion operation in this module.
     *
     * @param firmwareVersion the firmwareVersion input value
     */
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
    /**
     * Retrieves certificate thumbprint data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCertificateThumbprint() { return certificateThumbprint; }
    /**
     * Performs the setCertificateThumbprint operation in this module.
     *
     * @param certificateThumbprint the certificateThumbprint input value
     */
    public void setCertificateThumbprint(String certificateThumbprint) { this.certificateThumbprint = certificateThumbprint; }
    /**
     * Retrieves edge cluster data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEdgeCluster() { return edgeCluster; }
    /**
     * Performs the setEdgeCluster operation in this module.
     *
     * @param edgeCluster the edgeCluster input value
     */
    public void setEdgeCluster(String edgeCluster) { this.edgeCluster = edgeCluster; }
    /**
     * Retrieves mqtt client id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMqttClientId() { return mqttClientId; }
    /**
     * Performs the setMqttClientId operation in this module.
     *
     * @param mqttClientId the mqttClientId input value
     */
    public void setMqttClientId(String mqttClientId) { this.mqttClientId = mqttClientId; }
    /**
     * Retrieves last seen data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLastSeen() { return lastSeen; }
    /**
     * Performs the setLastSeen operation in this module.
     *
     * @param lastSeen the lastSeen input value
     */
    public void setLastSeen(LocalDateTime lastSeen) { this.lastSeen = lastSeen; }
}