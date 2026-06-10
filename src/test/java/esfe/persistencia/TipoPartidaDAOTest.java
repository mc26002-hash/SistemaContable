package esfe.persistencia;

import esfe.dominio.TipoPartida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TipoPartidaDAOTest {
    private TipoPartidaDAO tipoPartidaDAO;

    @BeforeEach
    void setUp() {
        tipoPartidaDAO = new TipoPartidaDAO();
    }

    private TipoPartida create(TipoPartida tipoPartida) throws SQLException {
        TipoPartida res = tipoPartidaDAO.create(tipoPartida);

        assertNotNull(res, "El tipo de partida creado no debería ser nulo.");
        assertTrue(res.getTipoPartidaId() > 0, "El ID generado debe ser mayor que cero.");
        assertEquals(tipoPartida.getNombreTipo(), res.getNombreTipo());

        return res;
    }

    private void update(TipoPartida tipoPartida) throws SQLException {
        tipoPartida.setNombreTipo(tipoPartida.getNombreTipo() + "_u");

        boolean res = tipoPartidaDAO.update(tipoPartida);

        assertTrue(res, "La actualización debería ser exitosa.");

        getById(tipoPartida);
    }

    private void getById(TipoPartida tipoPartida) throws SQLException {
        TipoPartida res = tipoPartidaDAO.getById(tipoPartida.getTipoPartidaId());

        assertNotNull(res, "El tipo de partida obtenido por ID no debería ser nulo.");
        assertEquals(tipoPartida.getTipoPartidaId(), res.getTipoPartidaId());
        assertEquals(tipoPartida.getNombreTipo(), res.getNombreTipo());
    }

    private void search(TipoPartida tipoPartida) throws SQLException {
        ArrayList<TipoPartida> tipos = tipoPartidaDAO.search(tipoPartida.getNombreTipo());

        boolean encontrado = false;

        for (TipoPartida item : tipos) {
            if (item.getTipoPartidaId() == tipoPartida.getTipoPartidaId()) {
                encontrado = true;
                break;
            }
        }

        assertTrue(encontrado, "El tipo de partida buscado no fue encontrado.");
    }

    private void delete(TipoPartida tipoPartida) throws SQLException {
        boolean res = tipoPartidaDAO.delete(tipoPartida);

        assertTrue(res, "La eliminación debería ser exitosa.");

        TipoPartida res2 = tipoPartidaDAO.getById(tipoPartida.getTipoPartidaId());

        assertNull(res2, "El tipo de partida debería haber sido eliminado.");
    }

    @Test
    void testTipoPartidaDAO() throws SQLException {
        String nombreTipo = "Tipo Partida Test " + System.currentTimeMillis();

        TipoPartida tipoPartida = new TipoPartida(
                0,
                nombreTipo
        );

        TipoPartida testTipoPartida = create(tipoPartida);

        getById(testTipoPartida);

        update(testTipoPartida);

        search(testTipoPartida);

        delete(testTipoPartida);
    }
}