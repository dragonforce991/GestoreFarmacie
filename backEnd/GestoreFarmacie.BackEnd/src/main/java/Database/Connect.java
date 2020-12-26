package Database;
import java.sql.*;

public class Connect {

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestfarma?user=root&password=root&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CET");
        return conn;
    }
} 