/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : BomExplosionDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomExplosionDtoController
 * Related Service   : BomExplosionDtoService, BomExplosionDtoServiceImpl
 * Related Repository: BomExplosionDtoRepository
 * Related Entity    : BomExplosionDto
 * Related DTO       : BomExplosionDto, BomExplosionLineDto
 * Related Mapper    : BomExplosionDtoMapper
 * Related DB Table  : bom_explosion_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BomExplosionDtoController, BomExplosionDtoService, BomExplosionDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
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

    /**
     * Retrieves bom header id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getBomHeaderId() { return bomHeaderId; }
    /**
     * Performs the setBomHeaderId operation in this module.
     *
     * @param bomHeaderId the bomHeaderId input value
     */
    public void setBomHeaderId(Long bomHeaderId) { this.bomHeaderId = bomHeaderId; }
    /**
     * Retrieves bom number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBomNumber() { return bomNumber; }
    /**
     * Performs the setBomNumber operation in this module.
     *
     * @param bomNumber the bomNumber input value
     */
    public void setBomNumber(String bomNumber) { this.bomNumber = bomNumber; }
    /**
     * Retrieves product id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getProductId() { return productId; }
    /**
     * Performs the setProductId operation in this module.
     *
     * @param productId the productId input value
     */
    public void setProductId(Long productId) { this.productId = productId; }
    /**
     * Retrieves product code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProductCode() { return productCode; }
    /**
     * Performs the setProductCode operation in this module.
     *
     * @param productCode the productCode input value
     */
    public void setProductCode(String productCode) { this.productCode = productCode; }
    /**
     * Retrieves product name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProductName() { return productName; }
    /**
     * Performs the setProductName operation in this module.
     *
     * @param productName the productName input value
     */
    public void setProductName(String productName) { this.productName = productName; }
    /**
     * Retrieves quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getQuantity() { return quantity; }
    /**
     * Performs the setQuantity operation in this module.
     *
     * @param quantity the quantity input value
     */
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    /**
     * Retrieves components data from the database.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<BomExplosionLineDto> getComponents() { return components; }
    /**
     * Performs the setComponents operation in this module.
     *
     * @param components the components input value
     */
    public void setComponents(List<BomExplosionLineDto> components) { this.components = components; }
}
