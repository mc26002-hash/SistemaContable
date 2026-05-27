package esfe.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String STR_CONNECTION = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    private Connection connection;
    private static ConnectionManager instance;

    private ConnectionManager() {
        this.connection = null;

        if (STR_CONNECTION == null || DB_USER == null || DB_PASSWORD == null) {
            throw new RuntimeException("Faltan variables de entorno: DB_URL, DB_USER o DB_PASSWORD");
        }

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar el driver JDBC de SQL Server", e);
        }
    }

    public synchronized Connection connect() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            try {
                this.connection = DriverManager.getConnection(
                        STR_CONNECTION,
                        DB_USER,
                        DB_PASSWORD
                );
            } catch (SQLException exception) {
                throw new SQLException("Error al conectar a la base de datos: " + exception.getMessage(), exception);
            }
        }
        return this.connection;
    }

    public void disconnect() throws SQLException {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException exception) {
                throw new SQLException("Error al cerrar la conexión: " + exception.getMessage(), exception);
            } finally {
                this.connection = null;
            }
        }
    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }
}