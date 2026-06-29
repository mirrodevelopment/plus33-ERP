package com.plus33.erp.workforce.exporter;

import com.plus33.erp.workforce.dto.PayrollRunResponse;
import org.springframework.stereotype.Component;

@Component
public class HtmlPayrollExporter {

    public String exportToHtml(PayrollRunResponse response) {
        return "<html><body><h1>Payroll Register: " + response.runNumber() + "</h1>" +
               "<p>Status: " + response.status() + "</p>" +
               "<p>Total Gross: " + response.totalGross() + "</p>" +
               "<p>Total Net: " + response.totalNet() + "</p></body></html>";
    }
}
