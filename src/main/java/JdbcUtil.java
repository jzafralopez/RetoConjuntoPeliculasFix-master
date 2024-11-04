import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {
    private static final Connection connection;

    static {
        String url = "jdbc:mysql://localhost:3306/coleccion_peliculas";
        String user = "root";
        String password = "NIPUTAIDEA32";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}