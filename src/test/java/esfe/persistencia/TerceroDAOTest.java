package esfe.persistencia;

import esfe.dominio.Tercero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TerceroDAOTest {
    private TerceroDAO terceroDAO;

    @BeforeEach
    void setUp() {
        terceroDAO = new TerceroDAO();
    }

    private Tercero create(Tercero tercero) throws SQLException {
        Tercero res = terceroDAO.create(tercero);

        assertNotNull(res, "El tercero creado no debería ser nulo.");
        assertEquals(tercero.getTipoTercero(), res.getTipoTercero());
        assertEquals(tercero.getNombre(), res.getNombre());
        assertEquals(tercero.getNit(), res.getNit());
        assertEquals(tercero.getNrc(), res.getNrc());
        assertEquals(tercero.getCorreo(), res.getCorreo());
        assertEquals(tercero.getTelefono(), res.getTelefono());
        assertEquals(tercero.isActivo(), res.isActivo());

        return res;
    }

    private void update(Tercero tercero) throws SQLException {
        tercero.setNombre(tercero.getNombre() + "_u");
        tercero.setCorreo("update_" + tercero.getCorreo());
        tercero.setTelefono("77778888");
        tercero.setActivo(false);

        boolean res = terceroDAO.update(tercero);

        assertTrue(res, "La actualización debería ser exitosa.");

        getById(tercero);
    }

    private void getById(Tercero tercero) throws SQLException {
        Tercero res = terceroDAO.getById(tercero.getTerceroId());

        assertNotNull(res, "El tercero obtenido por ID no debería ser nulo.");
        assertEquals(tercero.getTerceroId(), res.getTerceroId());
        assertEquals(tercero.getTipoTercero(), res.getTipoTercero());
        assertEquals(tercero.getNombre(), res.getNombre());
        assertEquals(tercero.getNit(), res.getNit());
        assertEquals(tercero.getNrc(), res.getNrc());
        assertEquals(tercero.getCorreo(), res.getCorreo());
        assertEquals(tercero.getTelefono(), res.getTelefono());
        assertEquals(tercero.isActivo(), res.isActivo());
    }

    private void search(Tercero tercero) throws SQLException {
        ArrayList<Tercero> terceros = terceroDAO.search(tercero.getNombre());

        boolean find = false;

        for (Tercero item : terceros) {
            if (item.getTerceroId() == tercero.getTerceroId()) {
                find = true;
                break;
            }
        }

        assertTrue(find, "El tercero buscado no fue encontrado.");
    }

    private void delete(Tercero tercero) throws SQLException {
        boolean res = terceroDAO.delete(tercero);

        assertTrue(res, "La eliminación debería ser exitosa.");

        Tercero res2 = terceroDAO.getById(tercero.getTerceroId());

        assertNull(res2, "El tercero debería haber sido eliminado.");
    }

    @Test
    void testTerceroDAO() throws SQLException {
        long num = System.currentTimeMillis() % 100000;

        Tercero tercero = new Tercero(
                0,
                "Cliente",
                "Tercero Test " + num,
                "0614" + num,
                "123" + num,
                "tercero" + num + "@test.com",
                "7000" + (num % 10000),
                true
        );

        Tercero testTercero = create(tercero);

        getById(testTercero);

        update(testTercero);

        search(testTercero);

        delete(testTercero);
    }
}