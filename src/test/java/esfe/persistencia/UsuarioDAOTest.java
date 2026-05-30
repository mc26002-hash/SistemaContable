package esfe.persistencia;

import esfe.dominio.Usuario;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOTest {

    private UsuarioDAO usuarioDAO;
    private Usuario usuarioPrueba;

    @BeforeEach
    void setUp() throws SQLException {

        usuarioDAO = new UsuarioDAO();

        usuarioPrueba = new Usuario();

        usuarioPrueba.setRolId(1);

        // IMPORTANTE:
        // usar datos diferentes para evitar duplicados en Somee
        long tiempo = System.currentTimeMillis();

        usuarioPrueba.setNombreUsuario("usuario" + tiempo);

        usuarioPrueba.setPasswordHash(
                "Admin123*".getBytes(StandardCharsets.UTF_8)
        );

        usuarioPrueba.setNombreCompleto("Usuario de Prueba");

        usuarioPrueba.setCorreoElectronico(
                "correo" + tiempo + "@gmail.com"
        );

        usuarioPrueba.setActivo(true);
    }

    @AfterEach
    void tearDown() {

        usuarioDAO = null;
        usuarioPrueba = null;
    }

    @Test
    @DisplayName("Insertar usuario correctamente")
    void insertarUsuario() {

        boolean resultado = usuarioDAO.insertar(usuarioPrueba);

        assertTrue(resultado,
                "El usuario debe insertarse correctamente");
    }

    @Test
    @DisplayName("Obtener lista de usuarios")
    void obtenerTodos() {

        List<Usuario> usuarios = usuarioDAO.obtenerTodos();

        assertNotNull(usuarios,
                "La lista no debe ser nula");

        assertFalse(usuarios.isEmpty(),
                "La lista no debe estar vacía");
    }

    @Test
    @DisplayName("Buscar usuario por ID")
    void obtenerPorId() {

        // Primero insertamos usuario
        boolean insertado = usuarioDAO.insertar(usuarioPrueba);

        assertTrue(insertado);

        // Obtener lista
        List<Usuario> usuarios = usuarioDAO.obtenerTodos();

        // Tomar último usuario insertado
        Usuario ultimoUsuario = usuarios.get(usuarios.size() - 1);

        // Buscar por ID
        Usuario encontrado =
                usuarioDAO.obtenerPorId(
                        ultimoUsuario.getUsuarioId()
                );

        assertNotNull(encontrado,
                "El usuario encontrado no debe ser nulo");

        assertEquals(
                ultimoUsuario.getUsuarioId(),
                encontrado.getUsuarioId()
        );
    }
}
