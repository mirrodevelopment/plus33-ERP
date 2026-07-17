import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ClearSwaps {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/plus33_erp";
        String user = "postgres";
        String pass = "crazy@8";
        
        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            
            // Delete all entries in employee_shift_swaps to clean up test state
            int rows = stmt.executeUpdate("DELETE FROM employee_shift_swaps");
            System.out.println("Deleted " + rows + " swap requests from database.");
            
            // Also restore original shift assignments if needed, but since we are swapping between shift 9 and 10,
            // we don't have to restore since they are valid shift IDs.
            
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
