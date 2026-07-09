/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.exporter
 * File              : HtmlPayrollExporter.java
 * Purpose           : Component of Workforce Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HtmlPayrollExporterController
 * Related Service   : HtmlPayrollExporterService, HtmlPayrollExporterServiceImpl
 * Related Repository: HtmlPayrollExporterRepository
 * Related Entity    : HtmlPayrollExporter
 * Related DTO       : PayrollRunResponse
 * Related Mapper    : HtmlPayrollExporterMapper
 * Related DB Table  : html_payroll_exporters
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
 * <p><b>Class  :</b> {@code HtmlPayrollExporter}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.exporter}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Workforce Module.</p>
 *
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class HtmlPayrollExporter {

    /**
     * Exports to html data as a report or downloadable file.
     *
     * @param response the response input value
     * @return the result string value
     */
    public String exportToHtml(PayrollRunResponse response) {
        return "<html><body><h1>Payroll Register: " + response.runNumber() + "</h1>" +
               "<p>Status: " + response.status() + "</p>" +
               "<p>Total Gross: " + response.totalGross() + "</p>" +
               "<p>Total Net: " + response.totalNet() + "</p></body></html>";
    }
}