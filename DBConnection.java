import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/attendance_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Karthibsql@270";
    private static Connection con = null;

    public static Connection getConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                System.out.println("Driver not found: " + e.getMessage());
            }
        }
        return con;
    }
}