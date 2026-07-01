package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "platform_logistics_node")
public class PlatformLogisticsNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "node_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String nodeCode;

    @Column(name = "node_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String nodeType;

    @Column(nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal longitude;

    @Size(max = 50)
    private String geohash;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String region;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String timezone;

    @Column(nullable = false)
    @NotNull
    private Integer capacity = 0;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

    @Column(name = "metadata_json", columnDefinition = "TEXT")
    private String metadataJson;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getNodeCode() { return nodeCode; }
    public void setNodeCode(String nodeCode) { this.nodeCode = nodeCode; }
    public String getNodeType() { return nodeType; }
    public void setNodeType(String nodeType) { this.nodeType = nodeType; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public String getGeohash() { return geohash; }
    public void setGeohash(String geohash) { this.geohash = geohash; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMetadataJson() { return metadataJson; }
    public void setMetadataJson(String metadataJson) { this.metadataJson = metadataJson; }
}