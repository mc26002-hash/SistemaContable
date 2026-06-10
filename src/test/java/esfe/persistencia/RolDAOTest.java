package esfe.persistencia;

import esfe.dominio.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RolDAOTest {
    private RolDAO rolDAO;

    @BeforeEach
    void setUp() {
        rolDAO = new RolDAO();
    }

    private Rol create(Rol rol) throws SQLException {
        Rol res = rolDAO.create(rol);

        assertNotNull(res, "El rol creado no debería ser nulo.");
        assertTrue(res.getRolId() > 0, "El ID del rol creado debe ser mayor que cero.");
        assertEquals(rol.getNombreRol(), res.getNombreRol());
        assertEquals(rol.getDescripcion(), res.getDescripcion());
        assertEquals(rol.getActivo(), res.getActivo());

        return res;
    }

    private void update(Rol rol) throws SQLException {
        rol.setNombreRol(rol.getNombreRol() + "_u");
        rol.setDescripcion("Descripción actualizada");
        rol.setActivo(false);

        boolean res = rolDAO.update(rol);

        assertTrue(res, "La actualización del rol debería ser exitosa.");

        Rol rolActualizado = rolDAO.getById(rol.getRolId());

        assertNotNull(rolActualizado, "El rol actualizado no debería ser nulo.");
        assertEquals(rol.getRolId(), rolActualizado.getRolId());
        assertEquals(rol.getNombreRol(), rolActualizado.getNombreRol());
        assertEquals(rol.getDescripcion(), rolActualizado.getDescripcion());
        assertEquals(rol.getActivo(), rolActualizado.getActivo());
    }

    private void getById(Rol rol) throws SQLException {
        Rol res = rolDAO.getById(rol.getRolId());

        assertNotNull(res, "El rol obtenido por ID no debería ser nulo.");
        assertEquals(rol.getRolId(), res.getRolId());
        assertEquals(rol.getNombreRol(), res.getNombreRol());
        assertEquals(rol.getDescripcion(), res.getDescripcion());
        assertEquals(rol.getActivo(), res.getActivo());
    }

    private void search(Rol rol) throws SQLException {
        ArrayList<Rol> roles = rolDAO.search(rol.getNombreRol());
        boolean encontrado = false;

        for (Rol item : roles) {
            if (item.getRolId() == rol.getRolId()) {
                encontrado = true;
                break;
            }
        }

        assertTrue(encontrado, "El rol creado debería aparecer en la búsqueda.");
    }

    private void delete(Rol rol) throws SQLException {
        boolean res = rolDAO.delete(rol);

        assertTrue(res, "La eliminación del rol debería ser exitosa.");

        Rol rolEliminado = rolDAO.getById(rol.getRolId());

        assertNull(rolEliminado, "El rol eliminado ya no debería encontrarse por ID.");
    }

    @Test
    void testRolDAO() throws SQLException {
        String nombreRol = "Rol_Test_" + System.currentTimeMillis();

        Rol rol = new Rol(
                0,
                nombreRol,
                "Rol de prueba",
                true
        );

        Rol rolCreado = create(rol);

        getById(rolCreado);

        update(rolCreado);

        search(rolCreado);

        delete(rolCreado);
    }
}