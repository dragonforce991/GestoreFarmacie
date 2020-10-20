package Database;
import java.sql.*;

public class Connect {

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestorefarmacie","root","");
        return conn;
    }
} 