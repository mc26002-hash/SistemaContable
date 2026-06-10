package esfe.persistencia;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionManagerTest {
    private ConnectionManager connectionManager;

    @BeforeEach
    void setUp() {
        connectionManager = ConnectionManager.getInstance();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connectionManager != null) {
            connectionManager.disconnect();
            connectionManager = null;
        }
    }

    @Test
    void connect() throws SQLException {
        Connection conn = connectionManager.connect();

        assertNotNull(conn, "La conexión no debe ser nula");
        assertFalse(conn.isClosed(), "La conexión debe estar abierta");
    }
}