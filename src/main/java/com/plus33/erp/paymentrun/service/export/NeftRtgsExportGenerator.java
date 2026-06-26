package com.plus33.erp.paymentrun.service.export;

import com.plus33.erp.paymentrun.entity.PaymentRun;
import com.plus33.erp.paymentrun.entity.PaymentRunInvoice;
import com.plus33.erp.procurement.entity.Supplier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NeftRtgsExportGenerator implements BankExportGenerator {

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
