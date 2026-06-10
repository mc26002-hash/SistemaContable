package esfe.persistencia;

import esfe.dominio.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOTest {
    private UsuarioDAO usuarioDAO;

    @BeforeEach
    void setUp() {
        usuarioDAO = new UsuarioDAO();
    }

    private Usuario create(Usuario usuario) throws SQLException {
        Usuario res = usuarioDAO.create(usuario);

        assertNotNull(res, "El usuario creado no debería ser nulo.");
        assertTrue(res.getUsuarioId() > 0, "El ID generado debe ser mayor que cero.");
        assertEquals(usuario.getRolId(), res.getRolId());
        assertEquals(usuario.getNombreUsuario(), res.getNombreUsuario());
        assertEquals(usuario.getNombreCompleto(), res.getNombreCompleto());
        assertEquals(usuario.getCorreoElectronico(), res.getCorreoElectronico());
        assertEquals(usuario.isActivo(), res.isActivo());
        assertArrayEquals(usuario.getPasswordHash(), res.getPasswordHash());

        return res;
    }

    private void update(Usuario usuario) throws SQLException {
        usuario.setNombreUsuario(usuario.getNombreUsuario() + "_u");
        usuario.setNombreCompleto(usuario.getNombreCompleto() + " Actualizado");
        usuario.setCorreoElectronico("u" + usuario.getCorreoElectronico());
        usuario.setActivo(true);

        boolean res = usuarioDAO.update(usuario);

        assertTrue(res, "La actualización del usuario debería ser exitosa.");

        getById(usuario);
    }

    private void getById(Usuario usuario) throws SQLException {
        Usuario res = usuarioDAO.getById(usuario.getUsuarioId());

        assertNotNull(res, "El usuario obtenido por ID no debería ser nulo.");
        assertEquals(usuario.getUsuarioId(), res.getUsuarioId());
        assertEquals(usuario.getRolId(), res.getRolId());
        assertEquals(usuario.getNombreUsuario(), res.getNombreUsuario());
        assertEquals(usuario.getNombreCompleto(), res.getNombreCompleto());
        assertEquals(usuario.getCorreoElectronico(), res.getCorreoElectronico());
        assertEquals(usuario.isActivo(), res.isActivo());
    }

    private void search(Usuario usuario) throws SQLException {
        ArrayList<Usuario> usuarios = usuarioDAO.search(usuario.getNombreUsuario());
        boolean encontrado = false;

        for (Usuario item : usuarios) {
            if (item.getUsuarioId() == usuario.getUsuarioId()) {
                encontrado = true;
                break;
            }
        }

        assertTrue(encontrado, "El usuario buscado no fue encontrado.");
    }

    private void authenticate(Usuario usuario) throws SQLException {
        Usuario res = usuarioDAO.authenticate(usuario);

        assertNotNull(res, "La autenticación debería retornar un usuario.");
        assertEquals(usuario.getCorreoElectronico(), res.getCorreoElectronico());
        assertTrue(res.isActivo(), "El usuario autenticado debe estar activo.");
    }

    private void authenticationFails(Usuario usuario) throws SQLException {
        Usuario res = usuarioDAO.authenticate(usuario);

        assertNull(res, "La autenticación debería fallar con contraseña incorrecta.");
    }

    private void updatePassword(Usuario usuario) throws SQLException {
        usuario.setPasswordHash("NuevaClave123*".getBytes(StandardCharsets.UTF_8));

        boolean res = usuarioDAO.updatePassword(usuario);

        assertTrue(res, "La actualización de contraseña debería ser exitosa.");

        authenticate(usuario);
    }

    private void delete(Usuario usuario) throws SQLException {
        boolean res = usuarioDAO.delete(usuario);

        assertTrue(res, "La eliminación del usuario debería ser exitosa.");

        Usuario eliminado = usuarioDAO.getById(usuario.getUsuarioId());

        assertNull(eliminado, "El usuario eliminado ya no debería existir.");
    }

    @Test
    void testUsuarioDAO() throws SQLException {
        long tiempo = System.currentTimeMillis();

        Usuario usuario = new Usuario();

        usuario.setRolId(1);
        usuario.setNombreUsuario("usuario" + tiempo);
        usuario.setPasswordHash("Admin123*".getBytes(StandardCharsets.UTF_8));
        usuario.setNombreCompleto("Usuario de Prueba");
        usuario.setCorreoElectronico("correo" + tiempo + "@gmail.com");
        usuario.setActivo(true);

        Usuario usuarioCreado = create(usuario);

        getById(usuarioCreado);

        update(usuarioCreado);

        search(usuarioCreado);

        authenticate(usuarioCreado);

        usuarioCreado.setPasswordHash("ClaveIncorrecta".getBytes(StandardCharsets.UTF_8));
        authenticationFails(usuarioCreado);

        updatePassword(usuarioCreado);

        delete(usuarioCreado);
    }
}