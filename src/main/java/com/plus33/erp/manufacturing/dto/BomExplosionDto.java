package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.util.List;

public class BomExplosionDto {
    private Long bomHeaderId;
    private String bomNumber;
    private Long productId;
    private String productCode;
    private String productName;
    private BigDecimal quantity;
    private List<BomExplosionLineDto> components;

    public static class BomExplosionLineDto {
        private Long productId;
        private String productCode;
        private String productName;
        private int level;
        private BigDecimal requiredQuantity;
        private String unitCode;
        private String componentType;
        private boolean phantom;

        public BomExplosionLineDto() {}

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getProductCode() { return productCode; }
        public void setProductCode(String productCode) { this.productCode = productCode; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
        public BigDecimal getRequiredQuantity() { return requiredQuantity; }
        public void setRequiredQuantity(BigDecimal requiredQuantity) { this.requiredQuantity = requiredQuantity; }
        public String getUnitCode() { return unitCode; }
        public void setUnitCode(String unitCode) { this.unitCode = unitCode; }
        public String getComponentType() { return componentType; }
        public void setComponentType(String componentType) { this.componentType = componentType; }
        public boolean isPhantom() { return phantom; }
        public void setPhantom(boolean phantom) { this.phantom = phantom; }
    }

    public BomExplosionDto() {}

    public Long getBomHeaderId() { return bomHeaderId; }
    public void setBomHeaderId(Long bomHeaderId) { this.bomHeaderId = bomHeaderId; }
    public String getBomNumber() { return bomNumber; }
    public void setBomNumber(String bomNumber) { this.bomNumber = bomNumber; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public List<BomExplosionLineDto> getComponents() { return components; }
    public void setComponents(List<BomExplosionLineDto> components) { this.components = components; }
}
