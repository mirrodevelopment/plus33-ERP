package com.plus33.erp.analytics.service;

import com.plus33.erp.analytics.dto.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProcurementAnalyticsServiceImpl implements ProcurementAnalyticsService {

    private final JdbcTemplate jdbcTemplate;

    public ProcurementAnalyticsServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ProcurementSummaryResponse getSummary(Long companyId) {
        try {
            String sql = "SELECT company_id, total_purchase_requests, total_purchase_orders, total_goods_receipts, total_supplier_invoices, total_payments, total_spend, avg_pr_cycle_time_days FROM mv_procurement_summary WHERE company_id = ?";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new ProcurementSummaryResponse(
                    rs.getLong("company_id"),
                    rs.getLong("total_purchase_requests"),
                    rs.getLong("total_purchase_orders"),
                    rs.getLong("total_goods_receipts"),
                    rs.getLong("total_supplier_invoices"),
                    rs.getLong("total_payments"),
                    rs.getBigDecimal("total_spend"),
                    rs.getBigDecimal("avg_pr_cycle_time_days")
            ), companyId);
        } catch (Exception e) {
            return new ProcurementSummaryResponse(companyId, 0L, 0L, 0L, 0L, 0L, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO);
        }
    }

    @Override
    public List<SupplierPerformanceResponse> getSuppliers(Long companyId) {
        String sql = "SELECT company_id, supplier_id, supplier_name, total_orders, total_spend, on_time_delivery_rate, avg_lead_time_days FROM mv_supplier_performance WHERE company_id = ? ORDER BY total_spend DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new SupplierPerformanceResponse(
                rs.getLong("company_id"),
                rs.getLong("supplier_id"),
                rs.getString("supplier_name"),
                rs.getLong("total_orders"),
                rs.getBigDecimal("total_spend"),
                rs.getBigDecimal("on_time_delivery_rate"),
                rs.getBigDecimal("avg_lead_time_days")
        ), companyId);
    }

    @Override
    public List<PayablesAgingResponse> getPayablesAging(Long companyId) {
        String sql = "SELECT company_id, supplier_id, supplier_name, total_outstanding, aging_current, aging_1_30, aging_31_60, aging_61_90, aging_90_plus FROM mv_payables_aging WHERE company_id = ? ORDER BY total_outstanding DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new PayablesAgingResponse(
                rs.getLong("company_id"),
                rs.getLong("supplier_id"),
                rs.getString("supplier_name"),
                rs.getBigDecimal("total_outstanding"),
                rs.getBigDecimal("aging_current"),
                rs.getBigDecimal("aging_1_30"),
                rs.getBigDecimal("aging_31_60"),
                rs.getBigDecimal("aging_61_90"),
                rs.getBigDecimal("aging_90_plus")
        ), companyId);
    }

    @Override
    public List<PoFulfilmentResponse> getPurchaseOrders(Long companyId) {
        String sql = "SELECT company_id, purchase_order_id, order_number, supplier_name, status, total_amount, expected_delivery_date, total_items_ordered, total_quantity_ordered, total_quantity_received, fulfillment_rate FROM mv_po_fulfilment WHERE company_id = ? ORDER BY expected_delivery_date DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new PoFulfilmentResponse(
                rs.getLong("company_id"),
                rs.getLong("purchase_order_id"),
                rs.getString("order_number"),
                rs.getString("supplier_name"),
                rs.getString("status"),
                rs.getBigDecimal("total_amount"),
                rs.getDate("expected_delivery_date") != null ? rs.getDate("expected_delivery_date").toLocalDate() : null,
                rs.getLong("total_items_ordered"),
                rs.getBigDecimal("total_quantity_ordered"),
                rs.getBigDecimal("total_quantity_received"),
                rs.getBigDecimal("fulfillment_rate")
        ), companyId);
    }

    @Override
    public List<InvoiceMatchingResponse> getInvoiceMatching(Long companyId) {
        String sql = "SELECT company_id, supplier_invoice_id, invoice_number, purchase_order_id, order_number, supplier_name, invoice_total_amount, po_total_amount, has_quantity_mismatch, has_price_mismatch FROM mv_invoice_matching WHERE company_id = ? ORDER BY invoice_number";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new InvoiceMatchingResponse(
                rs.getLong("company_id"),
                rs.getLong("supplier_invoice_id"),
                rs.getString("invoice_number"),
                rs.getLong("purchase_order_id"),
                rs.getString("order_number"),
                rs.getString("supplier_name"),
                rs.getBigDecimal("invoice_total_amount"),
                rs.getBigDecimal("po_total_amount"),
                rs.getBoolean("has_quantity_mismatch"),
                rs.getBoolean("has_price_mismatch")
        ), companyId);
    }
}
