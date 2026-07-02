package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getDeviceCode() { return deviceCode; }
    public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public String getOpcUaNamespace() { return opcUaNamespace; }
    public void setOpcUaNamespace(String opcUaNamespace) { this.opcUaNamespace = opcUaNamespace; }
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public String getPlcAddress() { return plcAddress; }
    public void setPlcAddress(String plcAddress) { this.plcAddress = plcAddress; }
    public Integer getModbusUnitId() { return modbusUnitId; }
    public void setModbusUnitId(Integer modbusUnitId) { this.modbusUnitId = modbusUnitId; }
}