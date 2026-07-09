/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Iot Module
 * Package           : com.plus33.erp.iot.scada
 * File              : ScadaDeviceManager.java
 * Purpose           : Business logic service layer for Iot Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ScadaDeviceManagerController
 * Related Service   : ScadaDeviceManager
 * Related Repository: ScadaDeviceManagerRepository
 * Related Entity    : ScadaDeviceManager
 * Related DTO       : N/A
 * Related Mapper    : ScadaDeviceManagerMapper
 * Related DB Table  : scada_device_managers
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : ScadaDeviceManagerController, ScadaDeviceManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Iot Module. Implements ScadaDeviceManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.iot.scada;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Iot Module</b>
 *
 * <p><b>Class  :</b> {@code ScadaDeviceManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.iot.scada}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Iot Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ScadaDeviceManagerController
 *   --> ScadaDeviceManager (this)
 *   --> Validate business rules
 *   --> ScadaDeviceManagerRepository (read/write 'scada_device_managers')
 *   --> ScadaDeviceManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code scada_device_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ScadaDeviceManager {
    @Autowired PlatformScadaDeviceRepository deviceRepo;
    @Autowired PlatformScadaSignalRegisterRepository registerRepo;
    /**
     * Creates a new device and persists it to the database.
     *
     * @param code the code input value
     * @param type the type input value
     * @param ns the ns input value
     * @param nodeId the nodeId input value
     * @param plc the plc input value
     * @param unitId the unitId input value
     * @return the PlatformScadaDevice result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformScadaDevice registerDevice(String code, String type, String ns, String nodeId, String plc, int unitId) {
        PlatformScadaDevice d = new PlatformScadaDevice();
        d.setDeviceCode(code);
        d.setDeviceType(type);
        d.setOpcUaNamespace(ns);
        d.setNodeId(nodeId);
        d.setPlcAddress(plc);
        d.setModbusUnitId(unitId);
        return deviceRepo.save(d);
    }

    /**
     * Creates a new signal and persists it to the database.
     *
     * @param deviceId the deviceId input value
     * @param code the code input value
     * @param type the type input value
     * @return the PlatformScadaSignalRegister result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformScadaSignalRegister registerSignal(Long deviceId, String code, String type) {
        PlatformScadaSignalRegister r = new PlatformScadaSignalRegister();
        r.setDeviceId(deviceId);
        r.setRegisterCode(code);
        r.setRegisterType(type);
        return registerRepo.save(r);
    }
}