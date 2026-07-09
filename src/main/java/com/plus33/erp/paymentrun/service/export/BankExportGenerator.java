/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.service.export
 * File              : BankExportGenerator.java
 * Purpose           : Service interface contract defining the API for Paymentrun Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankExportGeneratorController
 * Related Service   : BankExportGeneratorService, BankExportGeneratorServiceImpl
 * Related Repository: BankExportGeneratorRepository
 * Related Entity    : BankExportGenerator
 * Related DTO       : N/A
 * Related Mapper    : BankExportGeneratorMapper
 * Related DB Table  : bank_export_generators
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Paymentrun Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Paymentrun Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.paymentrun.service.export;

import com.plus33.erp.paymentrun.entity.PaymentRun;
import com.plus33.erp.paymentrun.entity.PaymentRunInvoice;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code BankExportGenerator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.service.export}</p>
 * <p><b>Layer  :</b> Component of Paymentrun Module in the PLUS33 Coffee ERP platform.</p>
 *
 * <p><b>Module Deps      :</b> Paymentrun</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface BankExportGenerator {
    String generate(PaymentRun run, List<PaymentRunInvoice> invoices);
    String getFormatName();
}
