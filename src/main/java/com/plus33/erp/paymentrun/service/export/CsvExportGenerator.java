/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.service.export
 * File              : CsvExportGenerator.java
 * Purpose           : Component of Paymentrun Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CsvExportGeneratorController
 * Related Service   : CsvExportGeneratorService, CsvExportGeneratorServiceImpl
 * Related Repository: CsvExportGeneratorRepository
 * Related Entity    : CsvExportGenerator
 * Related DTO       : N/A
 * Related Mapper    : CsvExportGeneratorMapper
 * Related DB Table  : csv_export_generators
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
 * <p><b>Class  :</b> {@code CsvExportGenerator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.service.export}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Paymentrun Module.</p>
 *
 * <p><b>Module Deps      :</b> Paymentrun, Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class CsvExportGenerator implements BankExportGenerator {

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
        // CSV Header
        sb.append("Supplier Code,Supplier Name,Bank Name,Account Number,Swift Code,IBAN,Amount,Currency,Payment Reference\n");

        for (PaymentRunInvoice item : invoices) {
            Supplier s = item.getSupplierInvoice().getSupplier();
            sb.append(escapeCsv(s.getCode())).append(",")
              .append(escapeCsv(s.getName())).append(",")
              .append(escapeCsv(s.getBankName())).append(",")
              .append(escapeCsv(s.getBankAccountNumber())).append(",")
              .append(escapeCsv(s.getSwiftCode())).append(",")
              .append(escapeCsv(s.getIban())).append(",")
              .append(item.getPaymentAmount()).append(",")
              .append(item.getSupplierInvoice().getCurrencyCode()).append(",")
              .append(escapeCsv(item.getPaymentReference())).append("\n");
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
        return "CSV";
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }
}