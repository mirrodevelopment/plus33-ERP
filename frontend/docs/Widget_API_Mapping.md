# Widget API Integration Registry

Details data ingestion schedules, refreshes, and cache settings for reusable widgets.

| Widget | Service | Backend Endpoint | Refresh Rate | Cache TTL |
| :--- | :--- | :--- | :--- | :--- |
| InventoryAnalyticsController Widget | AnalyticsService | $(@{verb=GET; path=/api/v1/analytics/inventory/dashboard; permission=None; dto=; signature=public ResponseEntity<ApiResponse<InventoryDashboardResponse>> getDashboard(}.path) | 60 seconds | 5 minutes |
| InventoryAnalyticsController Widget | AnalyticsService | $(@{verb=GET; path=/api/v1/analytics/inventory/kpis; permission=INVENTORY_ANALYTICS_VIEW; dto=; signature=public ResponseEntity<ApiResponse<InventoryKpisResponse>> getKpis(}.path) | 60 seconds | 5 minutes |
| InventoryAnalyticsController Widget | AnalyticsService | $(@{verb=GET; path=/api/v1/analytics/inventory/health; permission=INVENTORY_ANALYTICS_VIEW; dto=; signature=public ResponseEntity<ApiResponse<List<AnalyticsHealthResponse>>> getHealth() {}.path) | 60 seconds | 5 minutes |
| ProcurementAnalyticsController Widget | AnalyticsService | $(@{verb=GET; path=/api/v1/analytics/procurement/summary; permission=None; dto=; signature=public ResponseEntity<ApiResponse<ProcurementSummaryResponse>> getSummary(}.path) | 60 seconds | 5 minutes |
| APController Widget | OrganizationService | $(@{verb=GET; path=/api/v1/ap/dashboard; permission=None; dto=; signature=public ResponseEntity<ApiResponse<APDashboardResponse>> getAPDashboard(}.path) | 60 seconds | 5 minutes |
| ARController Widget | OrganizationService | $(@{verb=GET; path=/api/v1/ar/summary; permission=AR_VIEW; dto=; signature=public ResponseEntity<ApiResponse<ARSummaryResponse>> getARSummary(}.path) | 60 seconds | 5 minutes |
| BiDashboardController Widget | AnalyticsService | $(@{verb=GET; path=/api/bi/kpis; permission=None; dto=; signature=public ResponseEntity<List<BiKpiDefinition>> getAllActiveKpis() {}.path) | 60 seconds | 5 minutes |
| BiDashboardController Widget | AnalyticsService | $(@{verb=GET; path=/api/bi/kpis/{kpiCode}; permission=None; dto=; signature=public ResponseEntity<BiKpiDefinition> getKpi(@PathVariable String kpiCode) {}.path) | 60 seconds | 5 minutes |
| BiDashboardController Widget | AnalyticsService | $(@{verb=GET; path=/api/bi/system/health; permission=None; dto=; signature=public ResponseEntity<BiOperationalMetrics.SystemHealthSummary> getSystemHealth() {}.path) | 60 seconds | 5 minutes |
| BiDashboardController Widget | AnalyticsService | $(@{verb=GET; path=/api/bi/system/stats; permission=None; dto=; signature=public ResponseEntity<Map<String, Object>> getSystemStats() {}.path) | 60 seconds | 5 minutes |
| EdgeController Widget | OrganizationService | $(@{verb=POST; path=/api/edge/health/metrics; permission=None; dto=; signature=public ResponseEntity<Void> recordMetrics(}.path) | 60 seconds | 5 minutes |
| FixedAssetController Widget | FinanceService | $(@{verb=GET; path=/api/v1/fixed-assets/dashboard; permission=FIXED_ASSET_AUDIT; dto=; signature=public ResponseEntity<ApiResponse<FixedAssetDashboardResponse>> getDashboard(}.path) | 60 seconds | 5 minutes |
| PaymentRunController Widget | FinanceService | $(@{verb=GET; path=/api/v1/payment-runs/dashboard; permission=PAYMENT_RUN_VIEW; dto=; signature=public ResponseEntity<ApiResponse<PaymentRunDashboardResponse>> getPaymentRunDashboard(}.path) | 60 seconds | 5 minutes |
| PlatformOpsController Widget | PlatformService | $(@{verb=GET; path=/api/platform/dashboard; permission=None; dto=; signature=public ResponseEntity<Map<String, Object>> getDashboard() {}.path) | 60 seconds | 5 minutes |
| PayrollController Widget | WorkforceService | $(@{verb=GET; path=/api/v2/payroll/dashboard/{companyId}; permission=PAYROLL_VIEW; dto=; signature=public ResponseEntity<PayrollDashboardSummaryResponse> getDashboardSummary(@PathVariable Long companyId) {}.path) | 60 seconds | 5 minutes |
