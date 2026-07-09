/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.service.export
 * File              : NeftRtgsExportGenerator.java
 * Purpose           : Component of Paymentrun Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: NeftRtgsExportGeneratorController
 * Related Service   : NeftRtgsExportGeneratorService, NeftRtgsExportGeneratorServiceImpl
 * Related Repository: NeftRtgsExportGeneratorRepository
 * Related Entity    : NeftRtgsExportGenerator
 * Related DTO       : N/A
 * Related Mapper    : NeftRtgsExportGeneratorMapper
 * Related DB Table  : neft_rtgs_export_generators
 * Related REST APIs : N/A
 * Depends On        : Procurement Module
 * Used By           : Paymentrun Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Paymentrun Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.paymentrun.service.export;

import com.plus33.erp.paymentrun.entity.PaymentRun;
import com.plus33.erp.paymentrun.entity.PaymentRunInvoice;
import com.plus33.erp.procurement.entity.Supplier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code NeftRtgsExportGenerator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.service.export}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Paymentrun Module.</p>
 *
 * <p><b>Module Deps      :</b> Paymentrun, Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class NeftRtgsExportGenerator implements BankExportGenerator {

    /**
     * Generates the paymentrun based on input parameters and business rules.
     *
     * @param run the run input value
     * @param invoices the invoices input value
     * @return the result string value
     */
    /**
     * Generates the paymentrun based on input parameters and business rules.
     *
     * @param run the run input value
     * @param invoices the invoices input value
     * @return the result string value
     */
    @Override
    public String generate(PaymentRun run, List<PaymentRunInvoice> invoices) {
        StringBuilder sb = new StringBuilder();
        sb.append("TXN_TYPE,BENEFICIARY_ACCOUNT,BENEFICIARY_NAME,IFSC_CODE,AMOUNT,SENDER_ACCOUNT,REMARKS\n");

        for (PaymentRunInvoice item : invoices) {
            Supplier s = item.getSupplierInvoice().getSupplier();
            // Determine NEFT or RTGS based on amount (RTGS is typically for amounts >= 200,000 INR)
            String type = item.getPaymentAmount().compareTo(java.math.BigDecimal.valueOf(200000.00)) >= 0 ? "RTGS" : "NEFT";
            
            sb.append(type).append(",")
              .append(s.getBankAccountNumber() != null ? s.getBankAccountNumber() : "").append(",")
              .append(escapeCsv(s.getName())).append(",")
              .append(s.getSwiftCode() != null ? s.getSwiftCode() : "").append(",") // Swift Code represents IFSC here
              .append(item.getPaymentAmount()).append(",")
              .append(run.getBankAccountCode()).append(",")
              .append(escapeCsv("Ref " + item.getPaymentReference())).append("\n");
        }

        return sb.toString();
    }

    /**
     * Retrieves format name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves format name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public String getFormatName() {
        return "NEFT_RTGS";
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }
}