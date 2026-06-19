package esfe.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String STR_CONNECTION =
            "jdbc:sqlserver://ContabilidadESFE.mssql.somee.com:1433;" +
                    "databaseName=ContabilidadESFE;" +
                    "encrypt=true;" +
                    "trustServerCertificate=true;";

    private static final String DB_USER = "ContabilidadGab_SQLLogin_1";
    private static final String DB_PASSWORD = "a7l6kuot7x";

    private Connection connection;
    private static ConnectionManager instance;

    private ConnectionManager() {
        this.connection = null;

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