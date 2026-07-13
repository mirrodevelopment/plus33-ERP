"""
PLUS33 Coffee ERP - Empty Table Seeder
Seeds 5 realistic sample rows into every empty table and updates table_samples.txt
"""

import psycopg2
import psycopg2.extras
import random
import re
from datetime import date, datetime, timedelta
from decimal import Decimal

# ── DB connection ──────────────────────────────────────────────────────────────
DB_CFG = dict(host="localhost", port=5432, dbname="plus33_erp",
              user="postgres", password="crazy@8")

TABLE_SAMPLES_PATH = r"d:\plus33\plus33-erp\table_samples.txt"

# ── Reference IDs (pulled from live data) ─────────────────────────────────────
COMPANY_IDS   = [1]
EMPLOYEE_IDS  = list(range(1, 6))     # first 5 employees
STORE_IDS     = list(range(1, 6))
WAREHOUSE_IDS = [1, 2, 3]
REGION_IDS    = [1, 2, 3]
NATION_IDS    = [1, 2, 3]
PRODUCT_IDS   = list(range(1, 6))
SUPPLIER_IDS  = list(range(1, 4))
CUSTOMER_IDS  = [1]
USER_IDS      = list(range(1, 6))
CURRENCY_CODES = ["EUR", "USD", "GBP", "INR", "AED"]
STATUSES      = ["ACTIVE", "INACTIVE", "PENDING", "COMPLETED", "CANCELLED"]
COUNTRIES     = ["France", "Germany", "United Kingdom", "India", "UAE"]

# ── Helpers ───────────────────────────────────────────────────────────────────
def rand_date(start_days=-365, end_days=30):
    base = date.today()
    return base + timedelta(days=random.randint(start_days, end_days))

def rand_ts(start_days=-365, end_days=0):
    base = datetime.now()
    return base + timedelta(days=random.randint(start_days, end_days),
                            hours=random.randint(0, 23),
                            minutes=random.randint(0, 59))

def rand_decimal(lo=100, hi=50000, dp=2):
    return round(random.uniform(lo, hi), dp)

def rand_code(prefix="", n=6):
    return prefix + str(random.randint(10**(n-1), 10**n - 1))

def rand_name_from_col(col):
    """Generate a plausible varchar value based on column name."""
    col_l = col.lower()
    if "email" in col_l:
        return f"user{random.randint(1,999)}@plus33coffee.com"
    if "phone" in col_l or "mobile" in col_l:
        return f"+33{random.randint(600000000,699999999)}"
    if "currency" in col_l or col_l == "currency_code":
        return random.choice(CURRENCY_CODES)
    if "status" in col_l:
        return random.choice(["ACTIVE","PENDING","COMPLETED","APPROVED","REJECTED"])
    if "country" in col_l:
        return random.choice(COUNTRIES)
    if "code" in col_l:
        return rand_code("PC-", 5)
    if "name" in col_l:
        samples = ["Arabica Blend", "Robusta Select", "Espresso Premium",
                   "House Blend", "Single Origin"]
        return random.choice(samples)
    if "description" in col_l or "notes" in col_l or "content" in col_l:
        return "Sample data entry for Plus33 Coffee ERP system."
    if "address" in col_l:
        return f"{random.randint(1,200)} Coffee Street, Paris"
    if "type" in col_l:
        return random.choice(["STANDARD","PREMIUM","BASIC","CUSTOM"])
    if "iban" in col_l:
        return f"FR76{random.randint(10000,99999)}{random.randint(10000000,99999999)}"
    if "swift" in col_l or "bic" in col_l:
        return random.choice(["BNPAFRPP","SOGEFRPP","CRLYFRPP","CEPAFRPP"])
    if "url" in col_l or "path" in col_l:
        return f"/assets/plus33/{rand_code('',6)}.pdf"
    if "version" in col_l:
        return f"v{random.randint(1,5)}.{random.randint(0,9)}"
    if "number" in col_l or "reference" in col_l or "ref" in col_l:
        return rand_code("REF-", 7)
    if "title" in col_l:
        return random.choice(["Monthly Review","Q3 Summary","Annual Audit","Weekly Report"])
    if "role" in col_l:
        return random.choice(["STORE_MANAGER","BARISTA","SUPERVISOR","ACCOUNTANT"])
    if "category" in col_l:
        return random.choice(["COFFEE","EQUIPMENT","SUPPLIES","MARKETING"])
    if "reason" in col_l or "comment" in col_l or "message" in col_l:
        return "Routine operation entry."
    if "format" in col_l:
        return "PDF"
    if "language" in col_l or "locale" in col_l:
        return random.choice(["en","fr","de","ar"])
    if "timezone" in col_l:
        return random.choice(["Europe/Paris","Asia/Kolkata","America/New_York"])
    if "ip" in col_l:
        return f"192.168.{random.randint(1,254)}.{random.randint(1,254)}"
    if "hash" in col_l or "checksum" in col_l or "token" in col_l:
        import hashlib, os
        return hashlib.sha256(os.urandom(8)).hexdigest()[:32]
    if "period" in col_l:
        return f"2026-{random.randint(1,12):02d}"
    if "frequency" in col_l:
        return random.choice(["DAILY","WEEKLY","MONTHLY","QUARTERLY"])
    if "priority" in col_l:
        return random.choice(["LOW","MEDIUM","HIGH","CRITICAL"])
    if "mode" in col_l or "method" in col_l:
        return random.choice(["AUTO","MANUAL","SCHEDULED"])
    if "unit" in col_l:
        return random.choice(["KG","LTR","PCS","BOX","BAG"])
    if "level" in col_l:
        return random.choice(["L1","L2","L3","SENIOR","JUNIOR"])
    if "branch" in col_l:
        return f"Branch-{random.randint(1,10):02d}"
    if "department" in col_l or "dept" in col_l:
        return random.choice(["Finance","Operations","HR","IT","Supply Chain"])
    if "action" in col_l:
        return random.choice(["CREATE","UPDATE","DELETE","APPROVE","REJECT"])
    if "result" in col_l or "outcome" in col_l:
        return random.choice(["PASS","FAIL","PENDING","PARTIAL"])
    if "tag" in col_l or "label" in col_l:
        return random.choice(["coffee","premium","bulk","seasonal"])
    if "source" in col_l:
        return random.choice(["MANUAL","API","IMPORT","SYSTEM"])
    if "target" in col_l:
        return random.choice(["EXPORT","SYNC","REPORT","DASHBOARD"])
    if "model" in col_l:
        return random.choice(["LinearRegression","XGBoost","ARIMA","Prophet"])
    if "driver" in col_l:
        return f"Driver-{random.randint(1,20):02d}"
    if "vehicle" in col_l:
        return f"VH-{rand_code('',4)}"
    if "zone" in col_l or "region" in col_l:
        return random.choice(["Zone-A","Zone-B","North","South","Central"])
    if "class" in col_l:
        return random.choice(["A","B","C","D"])
    if "scope" in col_l:
        return random.choice(["GLOBAL","REGIONAL","LOCAL"])
    # fallback
    return f"Sample_{col[:8]}_{random.randint(100,999)}"


def value_for_column(col_name, data_type, udt_name, is_nullable, i):
    """Return a Python value suitable for the given column."""
    col_l = col_name.lower()
    dt = data_type.lower()
    udt = udt_name.lower()

    # NULLable FK columns that we can't easily resolve → NULL
    nullable_fk_patterns = [
        "journal_entry_id","leave_id","po_id","sales_order_id","invoice_id",
        "payroll_run_id","budget_id","project_id","task_id","parent_id",
        "asset_id","machine_id","work_order_id","route_id","batch_id",
        "plan_id","campaign_id","opportunity_id","case_id","lead_id",
        "contract_id","account_id","cost_center_id","gl_account_id",
        "bank_account_id","statement_id","carrier_id","dock_door_id",
        "checkin_id","forecast_id","audit_id","approval_id","revision_id",
        "template_id","workflow_id","policy_id","risk_id","control_id",
        "finding_id","framework_id","version_id","snapshot_id","schedule_id",
        "program_id","engagement_id","course_id","requisition_id",
        "position_id","goal_id","talent_id","successor_id","competency_id",
        "enrollment_id","asset_category_id","depreciation_book_id",
        "lease_id","loan_id","facility_id","pool_id","fx_deal_id",
        "hedge_id","settlement_id","transmission_id","file_id","run_id",
        "batch_run_id","job_id","etl_job_id","connector_id","saga_id",
        "saga_step_id","inbox_id","outbox_id","schema_id","workflow_instance_id",
        "lock_id","gateway_key_id","session_id","agent_id","tool_id",
        "prompt_id","memory_id","model_id","model_version_id","prediction_id",
        "causal_id","anomaly_id","aiops_id","autonomous_id","edge_node_id",
        "cloud_resource_id","device_id","config_id","backup_id","cache_id",
        "chargeback_id","cost_item_id","finops_id","compliance_id",
        "audit_log_id","conformance_id","deployment_id","dispatch_id",
        "ev_charging_id","ev_battery_id","fuel_id","esg_id","carbon_id",
        "capacity_id","resource_id","node_id","lock_key_id",
    ]
    if is_nullable == "YES" and any(col_l.endswith(p) for p in nullable_fk_patterns):
        return None

    # ── boolean ───────────────────────────────────────────────────────────────
    if "bool" in dt or "bool" in udt:
        return random.choice([True, False])

    # ── timestamps ────────────────────────────────────────────────────────────
    if "timestamp" in dt:
        if "updated" in col_l or "modified" in col_l:
            return rand_ts(-30, 0)
        if "created" in col_l or "inserted" in col_l:
            return rand_ts(-180, -30)
        if "expire" in col_l or "expiry" in col_l or "expiration" in col_l:
            return rand_ts(30, 365)
        if "start" in col_l:
            return rand_ts(-90, -1)
        if "end" in col_l or "completed" in col_l or "closed" in col_l:
            return rand_ts(-60, 0)
        return rand_ts(-180, 0)

    # ── date ──────────────────────────────────────────────────────────────────
    if dt == "date" or "date" in udt:
        if "expire" in col_l or "expiry" in col_l:
            return rand_date(30, 365)
        if "start" in col_l or "from" in col_l:
            return rand_date(-180, -1)
        if "end" in col_l or "to" in col_l:
            return rand_date(0, 180)
        return rand_date(-180, 0)

    # ── time ──────────────────────────────────────────────────────────────────
    if dt == "time without time zone" or "time" in udt:
        hour = random.randint(6, 22)
        minute = random.choice([0, 15, 30, 45])
        return f"{hour:02d}:{minute:02d}:00"

    # ── numeric / decimal ─────────────────────────────────────────────────────
    if "numeric" in dt or "decimal" in dt:
        if any(k in col_l for k in ["rate","percent","margin","ratio","score","factor"]):
            return round(random.uniform(0.5, 99.9), 2)
        if any(k in col_l for k in ["qty","quantity","count","units","pieces"]):
            return round(random.uniform(1, 500), 2)
        if any(k in col_l for k in ["weight","volume","capacity"]):
            return round(random.uniform(0.5, 1000), 3)
        if any(k in col_l for k in ["balance","amount","cost","price","revenue","profit","expense","budget","salary","pay","wage","gross","net","total"]):
            return rand_decimal(500, 50000)
        if any(k in col_l for k in ["lat","latitude"]):
            return round(random.uniform(43.0, 51.0), 6)
        if any(k in col_l for k in ["lon","longitude","lng"]):
            return round(random.uniform(-5.0, 15.0), 6)
        return round(random.uniform(1, 10000), 2)

    # ── integers ──────────────────────────────────────────────────────────────
    if "int" in dt or "serial" in dt or dt in ("bigint","integer","smallint"):
        # FK references
        if col_l in ("id",) or col_l.endswith("_id"):
            # handle specific FK columns
            if "company_id" == col_l:
                return COMPANY_IDS[0]
            if "employee_id" == col_l:
                return random.choice(EMPLOYEE_IDS)
            if col_l in ("store_id","outlet_id"):
                return random.choice(STORE_IDS)
            if "warehouse_id" == col_l:
                return random.choice(WAREHOUSE_IDS)
            if "region_id" == col_l:
                return random.choice(REGION_IDS)
            if "nation_id" == col_l:
                return random.choice(NATION_IDS)
            if "product_id" == col_l:
                return random.choice(PRODUCT_IDS)
            if "supplier_id" == col_l:
                return random.choice(SUPPLIER_IDS)
            if "customer_id" == col_l:
                return CUSTOMER_IDS[0]
            if col_l in ("user_id","created_by","updated_by","approved_by",
                         "modified_by","submitted_by","processed_by",
                         "written_off_by","received_by","requested_by"):
                return random.choice(USER_IDS)
            if "unit_id" == col_l:
                return random.randint(1, 5)
            # generic serial PK → skip (auto)
            if col_l == "id":
                return None   # handled separately
            # unknown FK → small integer guess
            if is_nullable == "YES":
                return None
            return random.randint(1, 5)
        # non-FK integers
        if any(k in col_l for k in ["minutes","hours","days","seconds","ms","duration"]):
            return random.randint(1, 480)
        if any(k in col_l for k in ["count","qty","quantity","units","pieces","pages","line_number","sequence","order","rank","position","score","priority_num","limit"]):
            return random.randint(1, 100)
        if any(k in col_l for k in ["year"]):
            return random.randint(2020, 2026)
        if any(k in col_l for k in ["month"]):
            return random.randint(1, 12)
        if any(k in col_l for k in ["day"]):
            return random.randint(1, 28)
        if any(k in col_l for k in ["port"]):
            return random.randint(1024, 65535)
        if any(k in col_l for k in ["max","min","threshold","capacity","target","budget"]):
            return random.randint(100, 10000)
        if any(k in col_l for k in ["sort","index","iteration","attempt","version_num","revision_num"]):
            return random.randint(1, 10)
        return random.randint(1, 500)

    # ── text / varchar / char ─────────────────────────────────────────────────
    if dt in ("character varying","varchar","text","char","character",
              "name","citext") or "char" in udt:
        return rand_name_from_col(col_name)

    # ── uuid ──────────────────────────────────────────────────────────────────
    if "uuid" in dt or "uuid" in udt:
        import uuid
        return str(uuid.uuid4())

    # ── json / jsonb ──────────────────────────────────────────────────────────
    if "json" in dt or "json" in udt:
        import json
        return json.dumps({"source": "seed", "index": i, "tag": "plus33"})

    # ── array ─────────────────────────────────────────────────────────────────
    if dt == "ARRAY" or "[]" in udt:
        return "{}"

    # ── inet / cidr ───────────────────────────────────────────────────────────
    if "inet" in dt or "cidr" in dt:
        return f"192.168.{random.randint(1,254)}.{random.randint(1,254)}"

    # ── interval ─────────────────────────────────────────────────────────────
    if "interval" in dt:
        return f"{random.randint(1, 30)} days"

    # fallback
    if is_nullable == "YES":
        return None
    return f"val_{col_name[:8]}_{i}"


def get_empty_tables(conn):
    with conn.cursor() as cur:
        cur.execute("""
            SELECT t.table_name
            FROM information_schema.tables t
            JOIN pg_stat_user_tables s ON s.relname = t.table_name
            WHERE t.table_schema = 'public'
              AND s.n_live_tup = 0
            ORDER BY t.table_name
        """)
        return [r[0] for r in cur.fetchall()]


def get_columns(conn, table):
    with conn.cursor() as cur:
        cur.execute("""
            SELECT column_name, data_type, udt_name, is_nullable,
                   column_default
            FROM information_schema.columns
            WHERE table_schema = 'public' AND table_name = %s
            ORDER BY ordinal_position
        """, (table,))
        return cur.fetchall()


def build_insert(table, columns, row_values):
    col_names = [c[0] for c in columns]
    placeholders = ["%s"] * len(col_names)
    sql = f'INSERT INTO "{table}" ({", ".join(f"{c}" for c in col_names)}) VALUES ({", ".join(placeholders)}) ON CONFLICT DO NOTHING'
    return sql, row_values


def seed_table(conn, table, n=5):
    cols = get_columns(conn, table)
    if not cols:
        return [], []

    # Filter out auto-generated columns (serial PKs, columns with nextval defaults)
    insertable = []
    for col in cols:
        col_name, data_type, udt_name, is_nullable, col_default = col
        # skip auto-increment serial PKs
        if col_default and "nextval" in str(col_default):
            continue
        # skip generated columns
        if col_default and "generated" in str(col_default).lower():
            continue
        insertable.append(col)

    if not insertable:
        return [], []

    inserted_rows = []
    for i in range(1, n + 1):
        row = []
        for col in insertable:
            col_name, data_type, udt_name, is_nullable, col_default = col
            val = value_for_column(col_name, data_type, udt_name, is_nullable, i)
            row.append(val)

        try:
            with conn.cursor() as cur:
                sql, row_vals = build_insert(table, insertable, row)
                cur.execute(sql, row_vals)
                conn.commit()
                inserted_rows.append(row)
        except Exception as e:
            conn.rollback()
            print(f"  [SKIP] {table} row {i}: {e}")

    return insertable, inserted_rows


def format_value(v):
    if v is None:
        return "NULL"
    if isinstance(v, bool):
        return "true" if v else "false"
    if isinstance(v, (datetime,)):
        return v.strftime("%Y-%m-%d %H:%M:%S")
    if isinstance(v, date):
        return v.strftime("%Y-%m-%d")
    return str(v)[:30]


def build_table_block(table, cols, rows):
    """Format a table block matching the existing table_samples.txt style."""
    if not cols:
        return None

    headers = [f"{c[0]} [{c[2]}]" for c in cols]
    # Build column widths
    col_widths = [max(len(h), 4) for h in headers]
    for row in rows:
        for j, val in enumerate(row):
            col_widths[j] = max(col_widths[j], len(format_value(val)))

    def sep():
        return "+" + "+".join("-" * (w + 2) for w in col_widths) + "+"

    def header_row():
        cells = [f" {headers[j]:<{col_widths[j]}} " for j in range(len(headers))]
        return "|" + "|".join(cells) + "|"

    def data_row(row):
        cells = [f" {format_value(row[j]):<{col_widths[j]}} " for j in range(len(row))]
        return "|" + "|".join(cells) + "|"

    lines = [f"\nTable: {table}", sep(), header_row(), sep()]
    if rows:
        for row in rows:
            lines.append(data_row(row))
    else:
        total_width = sum(col_widths) + 3 * len(col_widths) - 1
        lines.append(f"| {'No active records found.':<{total_width}} |")
    lines.append(sep())
    lines.append("")
    return "\n".join(lines)


def update_table_samples(path, updates: dict):
    """Replace 'No active records found.' blocks with actual data rows."""
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()

    for table_name, (cols, rows) in updates.items():
        if not rows:
            continue
        new_block = build_table_block(table_name, cols, rows)
        if not new_block:
            continue

        # Find the existing block for this table and replace the "No active records found." line
        # Pattern: look for the table header, then the "No active records" line between separators
        pattern = (
            r"(Table: " + re.escape(table_name) + r"\n"
            r"\+[-+]+\+\n"
            r"\|[^\n]+\|\n"
            r"\+[-+]+\+\n)"
            r"(\| No active records found\.[^\n]*\|\n)"
            r"(\+[-+]+\+)"
        )

        def make_replacement(m):
            header_block = m.group(1)
            sep_line = m.group(3)
            data_lines = ""
            for row in rows:
                # parse col widths from the header separator
                sep = m.group(1).split("\n")[1]  # +---+---+
                widths = [len(s) - 2 for s in sep.split("+")[1:-1]]
                cells = []
                for j, val in enumerate(row):
                    w = widths[j] if j < len(widths) else 20
                    cells.append(f" {format_value(val):<{w}} ")
                data_lines += "|" + "|".join(cells) + "|\n"
            return header_block + data_lines + sep_line

        content = re.sub(pattern, make_replacement, content)

    with open(path, "w", encoding="utf-8") as f:
        f.write(content)

    print(f"Updated {path}")


def main():
    print("Connecting to database...")
    conn = psycopg2.connect(**DB_CFG)

    print("Fetching empty tables...")
    empty_tables = get_empty_tables(conn)
    print(f"Found {len(empty_tables)} empty tables to seed.")

    updates = {}
    total_inserted = 0

    for idx, table in enumerate(empty_tables, 1):
        print(f"[{idx}/{len(empty_tables)}] Seeding {table}...")
        try:
            cols, rows = seed_table(conn, table, n=5)
            if rows:
                updates[table] = (cols, rows)
                total_inserted += len(rows)
                print(f"  -> Inserted {len(rows)} rows")
            else:
                print(f"  -> Skipped (no insertable cols or FK constraints)")
        except Exception as e:
            conn.rollback()
            print(f"  [ERROR] {table}: {e}")

    conn.close()
    print(f"\nTotal rows inserted: {total_inserted}")
    print(f"Tables seeded: {len(updates)}")

    print("\nUpdating table_samples.txt...")
    update_table_samples(TABLE_SAMPLES_PATH, updates)
    print("Done!")


if __name__ == "__main__":
    main()
