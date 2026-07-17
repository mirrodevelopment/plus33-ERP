import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueryUsers {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/plus33_erp";
        String user = "postgres";
        String pass = "crazy@8";
        
        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT id, employee_id, shift_date, current_shift_id, preferred_shift_id, status FROM employee_shift_swaps"
            );
            
            System.out.println("ID | EmployeeID | ShiftDate | CurrentShiftID | PreferredShiftID | Status");
            System.out.println("-------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.println(rs.getLong("id") + " | " + rs.getLong("employee_id") + " | " + rs.getDate("shift_date") + " | " + rs.getLong("current_shift_id") + " | " + rs.getLong("preferred_shift_id") + " | " + rs.getString("status"));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
