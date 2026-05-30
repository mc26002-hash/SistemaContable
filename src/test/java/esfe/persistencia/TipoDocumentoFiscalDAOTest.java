package esfe.persistencia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import esfe.dominio.TipoDocumentoFiscal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TipoDocumentoFiscalDAOTest {
    private TipoDocumentoFiscalDAO tipoDocumentoFiscalDAO;

    @BeforeEach
    void setUp() {
        tipoDocumentoFiscalDAO = new TipoDocumentoFiscalDAO();
    }

    private TipoDocumentoFiscal create(TipoDocumentoFiscal tipoDocumentoFiscal) throws SQLException {
        TipoDocumentoFiscal res = tipoDocumentoFiscalDAO.create(tipoDocumentoFiscal);

        assertNotNull(res, "El tipo de documento fiscal creado no debería ser nulo.");
        assertEquals(tipoDocumentoFiscal.getCodigo(), res.getCodigo(), "El código debe ser igual al original.");
        assertEquals(tipoDocumentoFiscal.getNombre(), res.getNombre(), "El nombre debe ser igual al original.");

        return res;
    }

    private void update(TipoDocumentoFiscal tipoDocumentoFiscal) throws SQLException {
        tipoDocumentoFiscal.setCodigo(tipoDocumentoFiscal.getCodigo() + "U");
        tipoDocumentoFiscal.setNombre(tipoDocumentoFiscal.getNombre() + "_u");

        boolean res = tipoDocumentoFiscalDAO.update(tipoDocumentoFiscal);

        assertTrue(res, "La actualización debería ser exitosa.");

        getById(tipoDocumentoFiscal);
    }

    private void getById(TipoDocumentoFiscal tipoDocumentoFiscal) throws SQLException {
        TipoDocumentoFiscal res = tipoDocumentoFiscalDAO.getById(
                tipoDocumentoFiscal.getTipoDocumentoFiscalId()
        );

        assertNotNull(res, "El tipo de documento fiscal obtenido por ID no debería ser nulo.");
        assertEquals(tipoDocumentoFiscal.getTipoDocumentoFiscalId(), res.getTipoDocumentoFiscalId());
        assertEquals(tipoDocumentoFiscal.getCodigo(), res.getCodigo());
        assertEquals(tipoDocumentoFiscal.getNombre(), res.getNombre());
    }

    private void search(TipoDocumentoFiscal tipoDocumentoFiscal) throws SQLException {
        ArrayList<TipoDocumentoFiscal> tipos = tipoDocumentoFiscalDAO.search(
                tipoDocumentoFiscal.getNombre()
        );

        boolean find = false;

        for (TipoDocumentoFiscal item : tipos) {
            if (item.getNombre().contains(tipoDocumentoFiscal.getNombre())
                    || item.getCodigo().contains(tipoDocumentoFiscal.getCodigo())) {
                find = true;
                break;
            }
        }

        assertTrue(find, "El tipo de documento fiscal buscado no fue encontrado.");
    }

    private void delete(TipoDocumentoFiscal tipoDocumentoFiscal) throws SQLException {
        boolean res = tipoDocumentoFiscalDAO.delete(tipoDocumentoFiscal);

        assertTrue(res, "La eliminación debería ser exitosa.");

        TipoDocumentoFiscal res2 = tipoDocumentoFiscalDAO.getById(
                tipoDocumentoFiscal.getTipoDocumentoFiscalId()
        );

        assertNull(res2, "El tipo de documento fiscal debería haber sido eliminado.");
    }

    @Test
    void testTipoDocumentoFiscalDAO() throws SQLException {
        Random random = new Random();
        int num = random.nextInt(1000) + 1;

        TipoDocumentoFiscal tipoDocumentoFiscal = new TipoDocumentoFiscal(
                0,
                "T" + num,
                "Documento Fiscal Test " + num
        );

        TipoDocumentoFiscal testTipoDocumentoFiscal = create(tipoDocumentoFiscal);

        update(testTipoDocumentoFiscal);

        search(testTipoDocumentoFiscal);

        delete(testTipoDocumentoFiscal);
    }
}