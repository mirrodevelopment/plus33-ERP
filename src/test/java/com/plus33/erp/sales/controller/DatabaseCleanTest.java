package com.plus33.erp.sales.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(properties = "spring.flyway.enabled=false")
public class DatabaseCleanTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void cleanDatabase() {
        System.out.println("CLEANING DATABASE...");
        try {
            jdbcTemplate.execute("DELETE FROM attendance WHERE employee_id IN (SELECT id FROM employees WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM employee_leaves WHERE employee_id IN (SELECT id FROM employees WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM employee_payrolls WHERE employee_id IN (SELECT id FROM employees WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM hcm_employee_documents WHERE employee_id IN (SELECT id FROM employees WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM employee_shifts WHERE employee_id IN (SELECT id FROM employees WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM employees WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            
            jdbcTemplate.execute("DELETE FROM customer_invoice_items WHERE customer_invoice_id IN (SELECT id FROM customer_invoices WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM customer_invoices WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM payments WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM journal_entry_lines WHERE journal_entry_id IN (SELECT id FROM journal_entries WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM journal_entries WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            
            jdbcTemplate.execute("DELETE FROM sales_order_items WHERE sales_order_id IN (SELECT id FROM sales_orders WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM sales_orders WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            
            jdbcTemplate.execute("DELETE FROM budget_lines WHERE budget_id IN (SELECT id FROM budgets WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM budget_dimension_sets WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM budget_versions WHERE budget_id IN (SELECT id FROM budgets WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM budgets WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM budget_policies WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM fiscal_years WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            
            jdbcTemplate.execute("DELETE FROM stock_count_items WHERE stock_count_id IN (SELECT id FROM stock_counts WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM stock_counts WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM stock_adjustments WHERE store_id IN (SELECT id FROM stores WHERE region_id IN (SELECT id FROM regions WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))) OR warehouse_id IN (SELECT id FROM warehouses WHERE region_id IN (SELECT id FROM regions WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')))");
            jdbcTemplate.execute("DELETE FROM stock_transfer_items WHERE stock_transfer_id IN (SELECT id FROM stock_transfers WHERE source_warehouse_id IN (SELECT id FROM warehouses WHERE region_id IN (SELECT id FROM regions WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))))");
            jdbcTemplate.execute("DELETE FROM stock_transfers WHERE source_warehouse_id IN (SELECT id FROM warehouses WHERE region_id IN (SELECT id FROM regions WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')))");
            
            jdbcTemplate.execute("DELETE FROM stock_movements WHERE warehouse_id IN (SELECT id FROM warehouses WHERE region_id IN (SELECT id FROM regions WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))) OR store_id IN (SELECT id FROM stores WHERE region_id IN (SELECT id FROM regions WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')))");
            jdbcTemplate.execute("DELETE FROM inventory_stock WHERE warehouse_id IN (SELECT id FROM warehouses WHERE region_id IN (SELECT id FROM regions WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))) OR store_id IN (SELECT id FROM stores WHERE region_id IN (SELECT id FROM regions WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')))");
            
            jdbcTemplate.execute("DELETE FROM crm_timeline_events WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM crm_cases WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM crm_opportunities WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM crm_leads WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM customers WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            
            jdbcTemplate.execute("DELETE FROM supplier_invoice_items WHERE supplier_invoice_id IN (SELECT id FROM supplier_invoices WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM supplier_invoices WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM purchase_order_items WHERE purchase_order_id IN (SELECT id FROM purchase_orders WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM purchase_orders WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM purchase_request_items WHERE purchase_request_id IN (SELECT id FROM purchase_requests WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM purchase_requests WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            
            jdbcTemplate.execute("DELETE FROM products WHERE category_id IN (SELECT id FROM product_categories WHERE code LIKE 'CAT_%')");
            jdbcTemplate.execute("DELETE FROM product_categories WHERE code LIKE 'CAT_%'");
            jdbcTemplate.execute("DELETE FROM chart_of_accounts WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM suppliers WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM stores WHERE region_id IN (SELECT id FROM regions WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM warehouses WHERE region_id IN (SELECT id FROM regions WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'))");
            jdbcTemplate.execute("DELETE FROM regions WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            
            jdbcTemplate.execute("DELETE FROM shifts WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM departments WHERE company_id = (SELECT id FROM companies WHERE code = 'PLUS33_COFFEE')");
            jdbcTemplate.execute("DELETE FROM user_roles WHERE user_id IN (SELECT id FROM users WHERE email LIKE '%@plus33coffee.fr')");
            jdbcTemplate.execute("DELETE FROM users WHERE email LIKE '%@plus33coffee.fr'");
            jdbcTemplate.execute("DELETE FROM companies WHERE code = 'PLUS33_COFFEE'");
            
            // Delete the flyway schema history records for our custom seeds
            jdbcTemplate.execute("DELETE FROM flyway_schema_history WHERE version IN ('377', '378', '379', '380', '381', '382', '383', '384')");
            
            System.out.println("DATABASE CLEANED SUCCESSFULLY!");
        } catch (Exception e) {
            System.err.println("Error cleaning database: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
