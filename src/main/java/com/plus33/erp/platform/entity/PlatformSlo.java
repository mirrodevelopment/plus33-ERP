package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "platform_slo")
public class PlatformSlo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(nullable = false, unique = true)
    @NotNull
    @Size(max = 150)
    private String name;

    @Column(name = "target_percentage", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal targetPercentage;

    @Column(name = "service_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String serviceName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getTargetPercentage() { return targetPercentage; }
    public void setTargetPercentage(BigDecimal targetPercentage) { this.targetPercentage = targetPercentage; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
}