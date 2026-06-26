package com.plus33.erp.paymentrun.service.export;

import com.plus33.erp.paymentrun.entity.PaymentRun;
import com.plus33.erp.paymentrun.entity.PaymentRunInvoice;

import java.util.List;

public interface BankExportGenerator {
    String generate(PaymentRun run, List<PaymentRunInvoice> invoices);
    String getFormatName();
}
