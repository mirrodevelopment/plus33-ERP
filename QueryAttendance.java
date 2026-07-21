import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueryAttendance {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/plus33_erp";
        String user = "postgres";
        String pass = "crazy@8";
        
        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            
            System.out.println("=== USERS & ROLES ===");
            ResultSet rs = stmt.executeQuery(
                "SELECT u.id, u.email, r.name FROM users u " +
                "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
                "LEFT JOIN roles r ON ur.role_id = r.id " +
                "LIMIT 15"
            );
            while (rs.next()) {
                System.out.println(rs.getLong("id") + " | " + rs.getString("email") + " | " + rs.getString("name"));
            }
            rs.close();
            
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
