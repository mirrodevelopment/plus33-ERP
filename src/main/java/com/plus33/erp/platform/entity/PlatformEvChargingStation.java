package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "platform_ev_charging_station")
public class PlatformEvChargingStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "station_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String stationCode;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(name = "location_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String locationName;

    @Column(nullable = false, precision = 10, scale = 6)
    @NotNull
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 6)
    @NotNull
    private BigDecimal longitude;

    @Column(name = "charger_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String chargerType; // AC, DC

    @Column(name = "connector_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String connectorType; // CCS2, CHAdeMO, Type2, NACS

    @Column(name = "max_power_kw", nullable = false, precision = 7, scale = 2)
    @NotNull
    private BigDecimal maxPowerKw;

    @Column(name = "simultaneous_connectors", nullable = false)
    @NotNull
    private Integer simultaneousConnectors;

    @Column(name = "availability_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String availabilityStatus; // AVAILABLE, OCCUPIED, OFFLINE

    @Column(name = "tariff_plan_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String tariffPlanCode;

    @Column(name = "renewable_supported", nullable = false)
    @NotNull
    private Boolean renewableSupported = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStationCode() { return stationCode; }
    public void setStationCode(String stationCode) { this.stationCode = stationCode; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public String getChargerType() { return chargerType; }
    public void setChargerType(String chargerType) { this.chargerType = chargerType; }
    public String getConnectorType() { return connectorType; }
    public void setConnectorType(String connectorType) { this.connectorType = connectorType; }
    public BigDecimal getMaxPowerKw() { return maxPowerKw; }
    public void setMaxPowerKw(BigDecimal maxPowerKw) { this.maxPowerKw = maxPowerKw; }
    public Integer getSimultaneousConnectors() { return simultaneousConnectors; }
    public void setSimultaneousConnectors(Integer simultaneousConnectors) { this.simultaneousConnectors = simultaneousConnectors; }
    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    public String getTariffPlanCode() { return tariffPlanCode; }
    public void setTariffPlanCode(String tariffPlanCode) { this.tariffPlanCode = tariffPlanCode; }
    public Boolean getRenewableSupported() { return renewableSupported; }
    public void setRenewableSupported(Boolean renewableSupported) { this.renewableSupported = renewableSupported; }
}