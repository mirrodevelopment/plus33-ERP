package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_vector_store")
public class PlatformVectorStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "store_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String storeCode;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "CONNECTED";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getStoreCode() { return storeCode; }
    public void setStoreCode(String storeCode) { this.storeCode = storeCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}