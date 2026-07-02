package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getGatewayCode() { return gatewayCode; }
    public void setGatewayCode(String gatewayCode) { this.gatewayCode = gatewayCode; }
    public String getGatewayStatus() { return gatewayStatus; }
    public void setGatewayStatus(String gatewayStatus) { this.gatewayStatus = gatewayStatus; }
    public String getFirmwareVersion() { return firmwareVersion; }
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
    public String getCertificateThumbprint() { return certificateThumbprint; }
    public void setCertificateThumbprint(String certificateThumbprint) { this.certificateThumbprint = certificateThumbprint; }
    public String getEdgeCluster() { return edgeCluster; }
    public void setEdgeCluster(String edgeCluster) { this.edgeCluster = edgeCluster; }
    public String getMqttClientId() { return mqttClientId; }
    public void setMqttClientId(String mqttClientId) { this.mqttClientId = mqttClientId; }
    public LocalDateTime getLastSeen() { return lastSeen; }
    public void setLastSeen(LocalDateTime lastSeen) { this.lastSeen = lastSeen; }
}