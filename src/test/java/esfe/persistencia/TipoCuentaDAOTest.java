package esfe.persistencia;

import esfe.dominio.TipoCuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TipoCuentaDAOTest {
    private TipoCuentaDAO tipoCuentaDAO;

    @BeforeEach
    void setUp() {
        tipoCuentaDAO = new TipoCuentaDAO();
    }

    private TipoCuenta create(TipoCuenta tipoCuenta) throws SQLException {
        TipoCuenta res = tipoCuentaDAO.create(tipoCuenta);

        assertNotNull(res, "El tipo de cuenta creado no debería ser nulo.");
        assertTrue(res.getTipoCuentaId() > 0, "El ID generado debe ser mayor que cero.");
        assertEquals(tipoCuenta.getNombreTipo(), res.getNombreTipo());
        assertEquals(tipoCuenta.getNaturaleza(), res.getNaturaleza());

        return res;
    }

    private void update(TipoCuenta tipoCuenta) throws SQLException {
        tipoCuenta.setNombreTipo(tipoCuenta.getNombreTipo() + "_u");
        tipoCuenta.setNaturaleza('H');

        boolean res = tipoCuentaDAO.update(tipoCuenta);

        assertTrue(res, "La actualización debería ser exitosa.");

        getById(tipoCuenta);
    }

    private void getById(TipoCuenta tipoCuenta) throws SQLException {
        TipoCuenta res = tipoCuentaDAO.getById(tipoCuenta.getTipoCuentaId());

        assertNotNull(res, "El tipo de cuenta obtenido por ID no debería ser nulo.");
        assertEquals(tipoCuenta.getTipoCuentaId(), res.getTipoCuentaId());
        assertEquals(tipoCuenta.getNombreTipo(), res.getNombreTipo());
        assertEquals(tipoCuenta.getNaturaleza(), res.getNaturaleza());
    }

    private void search(TipoCuenta tipoCuenta) throws SQLException {
        ArrayList<TipoCuenta> tipos = tipoCuentaDAO.search(tipoCuenta.getNombreTipo());

        boolean encontrado = false;

        for (TipoCuenta item : tipos) {
            if (item.getTipoCuentaId() == tipoCuenta.getTipoCuentaId()) {
                encontrado = true;
                break;
            }
        }

        assertTrue(encontrado, "El tipo de cuenta buscado no fue encontrado.");
    }

    private void delete(TipoCuenta tipoCuenta) throws SQLException {
        boolean res = tipoCuentaDAO.delete(tipoCuenta);

        assertTrue(res, "La eliminación debería ser exitosa.");

        TipoCuenta eliminado = tipoCuentaDAO.getById(tipoCuenta.getTipoCuentaId());

        assertNull(eliminado, "El tipo de cuenta debería haber sido eliminado.");
    }

    @Test
    void testTipoCuentaDAO() throws SQLException {
        long num = System.currentTimeMillis();

        TipoCuenta tipoCuenta = new TipoCuenta(
                0,
                "TipoCuentaTest" + num,
                'D'
        );

        TipoCuenta tipoCuentaCreado = create(tipoCuenta);

        getById(tipoCuentaCreado);

        update(tipoCuentaCreado);

        search(tipoCuentaCreado);

        delete(tipoCuentaCreado);
    }
}