package esfe.persistencia;

import esfe.dominio.CentroCosto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CentroCostoDAOTest {

    private CentroCostoDAO centroCostoDAO;

    @BeforeEach
    void setUp() {
        centroCostoDAO = new CentroCostoDAO();
    }

    private CentroCosto create(CentroCosto centroCosto) throws SQLException {

        CentroCosto res = centroCostoDAO.create(centroCosto);

        assertNotNull(res, "El centro de costo creado no debería ser nulo.");
        assertTrue(res.getCentroCostoId() > 0, "El ID generado debe ser mayor que cero.");

        assertEquals(centroCosto.getCodigo(), res.getCodigo());
        assertEquals(centroCosto.getNombre(), res.getNombre());
        assertEquals(centroCosto.getResponsable(), res.getResponsable());
        assertEquals(centroCosto.getPresupuesto(), res.getPresupuesto());
        assertEquals(centroCosto.isActivo(), res.isActivo());

        return res;
    }

    private void update(CentroCosto centroCosto) throws SQLException {

        centroCosto.setCodigo(centroCosto.getCodigo() + "_U");
        centroCosto.setNombre(centroCosto.getNombre() + " Actualizado");
        centroCosto.setResponsable(centroCosto.getResponsable() + " Modificado");
        centroCosto.setPresupuesto(
                centroCosto.getPresupuesto().add(new BigDecimal("1000.00"))
        );

        boolean res = centroCostoDAO.update(centroCosto);

        assertTrue(res, "La actualización del centro de costo debería ser exitosa.");

        getById(centroCosto);
    }

    private void getById(CentroCosto centroCosto) throws SQLException {

        CentroCosto res = centroCostoDAO.getById(centroCosto.getCentroCostoId());

        assertNotNull(res, "El centro de costo obtenido por ID no debería ser nulo.");

        assertEquals(centroCosto.getCentroCostoId(), res.getCentroCostoId());
        assertEquals(centroCosto.getCodigo(), res.getCodigo());
        assertEquals(centroCosto.getNombre(), res.getNombre());
        assertEquals(centroCosto.getResponsable(), res.getResponsable());
        assertEquals(centroCosto.getPresupuesto(), res.getPresupuesto());
        assertEquals(centroCosto.isActivo(), res.isActivo());
    }

    private void search(CentroCosto centroCosto) throws SQLException {

        ArrayList<CentroCosto> centrosCosto =
                centroCostoDAO.search(centroCosto.getCodigo());

        boolean encontrado = false;

        for (CentroCosto item : centrosCosto) {

            if (item.getCentroCostoId() == centroCosto.getCentroCostoId()) {
                encontrado = true;
                break;
            }
        }

        assertTrue(encontrado,
                "El centro de costo buscado no fue encontrado.");
    }

    private void delete(CentroCosto centroCosto) throws SQLException {

        boolean res = centroCostoDAO.delete(centroCosto);

        assertTrue(res,
                "La eliminación del centro de costo debería ser exitosa.");

        CentroCosto eliminado =
                centroCostoDAO.getById(centroCosto.getCentroCostoId());

        assertNull(eliminado,
                "El centro de costo eliminado ya no debería existir.");
    }

    @Test
    void testCentroCostoDAO() throws SQLException {

        long tiempo = System.currentTimeMillis();

        CentroCosto centroCosto = new CentroCosto();

        centroCosto.setCodigo("CC" + tiempo);
        centroCosto.setNombre("Centro de Costo Prueba");
        centroCosto.setResponsable("Jose Angel Mate");
        centroCosto.setPresupuesto(new BigDecimal("5000.00"));
        centroCosto.setActivo(true);

        CentroCosto centroCostoCreado = create(centroCosto);

        getById(centroCostoCreado);

        update(centroCostoCreado);

        search(centroCostoCreado);

        delete(centroCostoCreado);
    }
}