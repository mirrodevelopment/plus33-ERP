package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_transit_route")
public class PlatformTransitRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(name = "origin_node_id", nullable = false)
    @NotNull
    private Long originNodeId;

    @Column(name = "dest_node_id", nullable = false)
    @NotNull
    private Long destNodeId;

    @Column(name = "route_path_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String routePathJson;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "IN_TRANSIT";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public Long getOriginNodeId() { return originNodeId; }
    public void setOriginNodeId(Long originNodeId) { this.originNodeId = originNodeId; }
    public Long getDestNodeId() { return destNodeId; }
    public void setDestNodeId(Long destNodeId) { this.destNodeId = destNodeId; }
    public String getRoutePathJson() { return routePathJson; }
    public void setRoutePathJson(String routePathJson) { this.routePathJson = routePathJson; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}