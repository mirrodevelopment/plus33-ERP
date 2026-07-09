/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformScadaDevice.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformScadaDeviceController
 * Related Service   : PlatformScadaDeviceService, PlatformScadaDeviceServiceImpl
 * Related Repository: PlatformScadaDeviceRepository
 * Related Entity    : PlatformScadaDevice
 * Related DTO       : N/A
 * Related Mapper    : PlatformScadaDeviceMapper
 * Related DB Table  : platform_scada_device
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformScadaDeviceRepository, PlatformScadaDeviceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_scada_device'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformScadaDevice}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_scada_device'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_scada_device}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_scada_device")
public class PlatformScadaDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "device_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String deviceCode;

    @Column(name = "device_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String deviceType;

    @Column(name = "opc_ua_namespace")
    @Size(max = 150)
    private String opcUaNamespace;

    @Column(name = "node_id")
    @Size(max = 150)
    private String nodeId;

    @Column(name = "plc_address")
    @Size(max = 100)
    private String plcAddress;

    @Column(name = "modbus_unit_id")
    private Integer modbusUnitId;

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
     * Retrieves device code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDeviceCode() { return deviceCode; }
    /**
     * Performs the setDeviceCode operation in this module.
     *
     * @param deviceCode the deviceCode input value
     */
    public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode; }
    /**
     * Retrieves device type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDeviceType() { return deviceType; }
    /**
     * Performs the setDeviceType operation in this module.
     *
     * @param deviceType the deviceType input value
     */
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    /**
     * Retrieves opc ua namespace data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOpcUaNamespace() { return opcUaNamespace; }
    /**
     * Performs the setOpcUaNamespace operation in this module.
     *
     * @param opcUaNamespace the opcUaNamespace input value
     */
    public void setOpcUaNamespace(String opcUaNamespace) { this.opcUaNamespace = opcUaNamespace; }
    /**
     * Retrieves node id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNodeId() { return nodeId; }
    /**
     * Performs the setNodeId operation in this module.
     *
     * @param nodeId the nodeId input value
     */
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    /**
     * Retrieves plc address data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPlcAddress() { return plcAddress; }
    /**
     * Performs the setPlcAddress operation in this module.
     *
     * @param plcAddress the plcAddress input value
     */
    public void setPlcAddress(String plcAddress) { this.plcAddress = plcAddress; }
    /**
     * Retrieves modbus unit id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getModbusUnitId() { return modbusUnitId; }
    /**
     * Performs the setModbusUnitId operation in this module.
     *
     * @param modbusUnitId the modbusUnitId input value
     */
    public void setModbusUnitId(Integer modbusUnitId) { this.modbusUnitId = modbusUnitId; }
}