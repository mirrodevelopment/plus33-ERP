package com.plus33.erp.paymentrun.service.export;

import com.plus33.erp.paymentrun.entity.PaymentRun;
import com.plus33.erp.paymentrun.entity.PaymentRunInvoice;
import com.plus33.erp.procurement.entity.Supplier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvExportGenerator implements BankExportGenerator {

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
