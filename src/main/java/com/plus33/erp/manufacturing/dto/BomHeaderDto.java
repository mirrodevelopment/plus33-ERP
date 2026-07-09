/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : BomHeaderDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomHeaderDtoController
 * Related Service   : BomHeaderDtoService, BomHeaderDtoServiceImpl
 * Related Repository: BomHeaderDtoRepository
 * Related Entity    : BomHeaderDto
 * Related DTO       : BomHeaderDto, BomLineDto
 * Related Mapper    : BomHeaderDtoMapper
 * Related DB Table  : bom_header_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BomHeaderDtoController, BomHeaderDtoService, BomHeaderDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code BomHeaderDto}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class BomHeaderDto {
    private Long id;
    private Long companyId;
    private String bomNumber;
    private Long productId;
    private String productCode;
    private String productName;
    private BigDecimal baseQuantity;
    private String unitCode;
    private String status;
    private Integer version;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Long routingHeaderId;
    private String notes;
    private List<BomLineDto> lines;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BomHeaderDto() {}

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
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
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
     * Retrieves bom code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBomCode() { return bomNumber; }
    /**
     * Performs the setBomCode operation in this module.
     *
     * @param bomCode the bomCode input value
     */
    public void setBomCode(String bomCode) { this.bomNumber = bomCode; }
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
     * Retrieves base quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getBaseQuantity() { return baseQuantity; }
    /**
     * Performs the setBaseQuantity operation in this module.
     *
     * @param baseQuantity the baseQuantity input value
     */
    public void setBaseQuantity(BigDecimal baseQuantity) { this.baseQuantity = baseQuantity; }
    /**
     * Retrieves unit code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getUnitCode() { return unitCode; }
    /**
     * Performs the setUnitCode operation in this module.
     *
     * @param unitCode the unitCode input value
     */
    public void setUnitCode(String unitCode) { this.unitCode = unitCode; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
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
     * Retrieves effective from data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    /**
     * Performs the setEffectiveFrom operation in this module.
     *
     * @param effectiveFrom the effectiveFrom input value
     */
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    /**
     * Retrieves effective to data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveTo() { return effectiveTo; }
    /**
     * Performs the setEffectiveTo operation in this module.
     *
     * @param effectiveTo the effectiveTo input value
     */
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    /**
     * Retrieves routing header id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRoutingHeaderId() { return routingHeaderId; }
    /**
     * Performs the setRoutingHeaderId operation in this module.
     *
     * @param routingHeaderId the routingHeaderId input value
     */
    public void setRoutingHeaderId(Long routingHeaderId) { this.routingHeaderId = routingHeaderId; }
    /**
     * Retrieves notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNotes() { return notes; }
    /**
     * Performs the setNotes operation in this module.
     *
     * @param notes the notes input value
     */
    public void setNotes(String notes) { this.notes = notes; }
    /**
     * Retrieves lines data from the database.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<BomLineDto> getLines() { return lines; }
    /**
     * Performs the setLines operation in this module.
     *
     * @param lines the lines input value
     */
    public void setLines(List<BomLineDto> lines) { this.lines = lines; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
