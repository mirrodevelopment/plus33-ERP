package com.plus33.erp.workforce.exporter;

import com.plus33.erp.workforce.dto.PayrollRunResponse;
import org.springframework.stereotype.Component;

@Component
public class CsvPayrollExporter {

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
