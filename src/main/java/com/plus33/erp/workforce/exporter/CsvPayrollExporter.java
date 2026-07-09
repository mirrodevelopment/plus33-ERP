/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.exporter
 * File              : CsvPayrollExporter.java
 * Purpose           : Component of Workforce Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CsvPayrollExporterController
 * Related Service   : CsvPayrollExporterService, CsvPayrollExporterServiceImpl
 * Related Repository: CsvPayrollExporterRepository
 * Related Entity    : CsvPayrollExporter
 * Related DTO       : PayrollRunResponse
 * Related Mapper    : CsvPayrollExporterMapper
 * Related DB Table  : csv_payroll_exporters
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.workforce.exporter;

import com.plus33.erp.workforce.dto.PayrollRunResponse;
import org.springframework.stereotype.Component;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code CsvPayrollExporter}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.exporter}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Workforce Module.</p>
 *
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class CsvPayrollExporter {

    /**
     * Exports to csv data as a report or downloadable file.
     *
     * @param response the response input value
     * @return the result string value
     */
    public String exportToCsv(PayrollRunResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append("RunNumber,Status,TotalGross,TotalNet,TotalTaxes\n");
        sb.append(response.runNumber()).append(",")
          .append(response.status()).append(",")
          .append(response.totalGross()).append(",")
          .append(response.totalNet()).append(",")
          .append(response.totalTaxes()).append("\n");
        return sb.toString();
    }
}