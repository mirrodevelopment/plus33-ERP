package com.plus33.erp.paymentrun.service.export;

import com.plus33.erp.paymentrun.entity.PaymentRun;
import com.plus33.erp.paymentrun.entity.PaymentRunInvoice;
import com.plus33.erp.procurement.entity.Supplier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class SepaExportGenerator implements BankExportGenerator {

    @Override
    public String generate(PaymentRun run, List<PaymentRunInvoice> invoices) {
        StringBuilder sb = new StringBuilder();
        String creDtTm = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
          .append("<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.001.001.03.sepa\">\n")
          .append("  <CstmrCdtTrfInitn>\n")
          .append("    <GrpHdr>\n")
          .append("      <MsgId>SEPA-").append(run.getRunNumber()).append("</MsgId>\n")
          .append("      <CreDtTm>").append(creDtTm).append("</CreDtTm>\n")
          .append("      <NbOfTxs>").append(invoices.size()).append("</NbOfTxs>\n")
          .append("      <CtrlSum>").append(run.getTotalAmount()).append("</CtrlSum>\n")
          .append("      <InitgPty>\n")
          .append("        <Nm>").append(escapeXml(run.getCompany().getName())).append("</Nm>\n")
          .append("      </InitgPty>\n")
          .append("    </GrpHdr>\n")
          .append("    <PmtInf>\n")
          .append("      <PmtInfId>SEPA-").append(run.getRunNumber()).append("-01</PmtInfId>\n")
          .append("      <PmtMtd>TRF</PmtMtd>\n")
          .append("      <RecommdDt>").append(run.getPaymentDate()).append("</RecommdDt>\n")
          .append("      <Dbtr>\n")
          .append("        <Nm>").append(escapeXml(run.getCompany().getName())).append("</Nm>\n")
          .append("      </Dbtr>\n")
          .append("      <DbtrAcct>\n")
          .append("        <Id>\n")
          .append("          <IBAN>SEPADUMMYIBAN0001</IBAN>\n")
          .append("        </Id>\n")
          .append("      </DbtrAcct>\n")
          .append("      <DbtrAgt>\n")
          .append("        <FinInstnId>\n")
          .append("          <BIC>SEPADUMMYBIC</BIC>\n")
          .append("        </FinInstnId>\n")
          .append("      </DbtrAgt>\n")
          .append("      <ChrgBr>SLEV</ChrgBr>\n");

        for (PaymentRunInvoice item : invoices) {
            Supplier s = item.getSupplierInvoice().getSupplier();
            sb.append("      <CdtTrfTxInf>\n")
              .append("        <PmtId>\n")
              .append("          <EndToEndId>").append(item.getPaymentReference()).append("</EndToEndId>\n")
              .append("        </PmtId>\n")
              .append("        <Amt>\n")
              .append("          <InstdAmt Ccy=\"EUR\">").append(item.getPaymentAmount()).append("</InstdAmt>\n")
              .append("        </Amt>\n");

            if (s.getSwiftCode() != null && !s.getSwiftCode().isBlank()) {
                sb.append("        <CdtrAgt>\n")
                  .append("          <FinInstnId>\n")
                  .append("            <BIC>").append(escapeXml(s.getSwiftCode())).append("</BIC>\n")
                  .append("          </FinInstnId>\n")
                  .append("        </CdtrAgt>\n");
            }

            sb.append("        <Cdtr>\n")
              .append("          <Nm>").append(escapeXml(s.getName())).append("</Nm>\n")
              .append("        </Cdtr>\n")
              .append("        <CdtrAcct>\n")
              .append("          <Id>\n");

            if (s.getIban() != null && !s.getIban().isBlank()) {
                sb.append("            <IBAN>").append(escapeXml(s.getIban())).append("</IBAN>\n");
            } else {
                sb.append("            <IBAN>NOIBANSPECIFIED</IBAN>\n");
            }

            sb.append("          </Id>\n")
              .append("        </CdtrAcct>\n")
              .append("        <RmtInf>\n")
              .append("          <Ustrd>SEPA Payment ").append(escapeXml(item.getSupplierInvoice().getInvoiceNumber())).append("</Ustrd>\n")
              .append("        </RmtInf>\n")
              .append("      </CdtTrfTxInf>\n");
        }

        sb.append("    </PmtInf>\n")
          .append("  </CstmrCdtTrfInitn>\n")
          .append("</Document>\n");

        return sb.toString();
    }

    @Override
    public String getFormatName() {
        return "SEPA";
    }

    private String escapeXml(String val) {
        if (val == null) return "";
        return val.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&apos;");
    }
}
