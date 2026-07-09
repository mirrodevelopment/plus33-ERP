/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformLogisticsDelayPrediction.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformLogisticsDelayPredictionController
 * Related Service   : PlatformLogisticsDelayPredictionService, PlatformLogisticsDelayPredictionServiceImpl
 * Related Repository: PlatformLogisticsDelayPredictionRepository
 * Related Entity    : PlatformLogisticsDelayPrediction
 * Related DTO       : N/A
 * Related Mapper    : PlatformLogisticsDelayPredictionMapper
 * Related DB Table  : platform_logistics_delay_prediction
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformLogisticsDelayPredictionRepository, PlatformLogisticsDelayPredictionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_logistics_delay_prediction'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformLogisticsDelayPrediction}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_logistics_delay_prediction'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_logistics_delay_prediction}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_logistics_delay_prediction")
public class PlatformLogisticsDelayPrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transit_route_id", nullable = false)
    @NotNull
    private Long transitRouteId;

    @Column(name = "prediction_model", nullable = false)
    @NotNull
    @Size(max = 100)
    private String predictionModel;

    @Column(name = "prediction_confidence", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal predictionConfidence;

    @Column(name = "predicted_arrival", nullable = false)
    @NotNull
    private LocalDateTime predictedArrival;

    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;

    @Column(name = "generated_at", nullable = false)
    @NotNull
    private LocalDateTime generatedAt = LocalDateTime.now();

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
     * Retrieves transit route id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTransitRouteId() { return transitRouteId; }
    /**
     * Performs the setTransitRouteId operation in this module.
     *
     * @param transitRouteId the transitRouteId input value
     */
    public void setTransitRouteId(Long transitRouteId) { this.transitRouteId = transitRouteId; }
    /**
     * Retrieves prediction model data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPredictionModel() { return predictionModel; }
    /**
     * Performs the setPredictionModel operation in this module.
     *
     * @param predictionModel the predictionModel input value
     */
    public void setPredictionModel(String predictionModel) { this.predictionModel = predictionModel; }
    /**
     * Retrieves prediction confidence data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPredictionConfidence() { return predictionConfidence; }
    /**
     * Performs the setPredictionConfidence operation in this module.
     *
     * @param predictionConfidence the predictionConfidence input value
     */
    public void setPredictionConfidence(BigDecimal predictionConfidence) { this.predictionConfidence = predictionConfidence; }
    /**
     * Retrieves predicted arrival data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getPredictedArrival() { return predictedArrival; }
    /**
     * Performs the setPredictedArrival operation in this module.
     *
     * @param predictedArrival the predictedArrival input value
     */
    public void setPredictedArrival(LocalDateTime predictedArrival) { this.predictedArrival = predictedArrival; }
    /**
     * Retrieves actual arrival data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getActualArrival() { return actualArrival; }
    /**
     * Performs the setActualArrival operation in this module.
     *
     * @param actualArrival the actualArrival input value
     */
    public void setActualArrival(LocalDateTime actualArrival) { this.actualArrival = actualArrival; }
    /**
     * Retrieves generated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    /**
     * Performs the setGeneratedAt operation in this module.
     *
     * @param generatedAt the generatedAt input value
     */
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}