package esfe.persistencia;

import esfe.dominio.DocumentoFiscal;
import esfe.dominio.Tercero;
import esfe.dominio.TipoDocumentoFiscal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DocumentoFiscalDAOTest {
    private DocumentoFiscalDAO documentoFiscalDAO;
    private TerceroDAO terceroDAO;
    private TipoDocumentoFiscalDAO tipoDocumentoFiscalDAO;

    @BeforeEach
    void setUp() {
        documentoFiscalDAO = new DocumentoFiscalDAO();
        terceroDAO = new TerceroDAO();
        tipoDocumentoFiscalDAO = new TipoDocumentoFiscalDAO();
    }

    private DocumentoFiscal create(DocumentoFiscal documentoFiscal) throws SQLException {
        DocumentoFiscal res = documentoFiscalDAO.create(documentoFiscal);

        assertNotNull(res, "El documento fiscal creado no debería ser nulo.");
        assertEquals(documentoFiscal.getTerceroId(), res.getTerceroId());
        assertEquals(documentoFiscal.getTipoDocumentoFiscalId(), res.getTipoDocumentoFiscalId());
        assertEquals(documentoFiscal.getPartidaId(), res.getPartidaId());
        assertEquals(documentoFiscal.getTipoLibro(), res.getTipoLibro());
        assertEquals(documentoFiscal.getNumeroDocumento(), res.getNumeroDocumento());
        assertEquals(documentoFiscal.getFechaDocumento().toString(), res.getFechaDocumento().toString());
        assertEquals(documentoFiscal.getMontoExento(), res.getMontoExento());
        assertEquals(documentoFiscal.getMontoGravado(), res.getMontoGravado());
        assertEquals(documentoFiscal.getIva(), res.getIva());
        assertEquals(documentoFiscal.getTotal(), res.getTotal());
        assertEquals(documentoFiscal.isAplicaRetencion(), res.isAplicaRetencion());

        return res;
    }

    private void update(DocumentoFiscal documentoFiscal) throws SQLException {
        documentoFiscal.setTipoLibro("Venta");
        documentoFiscal.setNumeroDocumento(documentoFiscal.getNumeroDocumento() + "U");
        documentoFiscal.setMontoExento(new BigDecimal("5.00"));
        documentoFiscal.setMontoGravado(new BigDecimal("200.00"));
        documentoFiscal.setIva(new BigDecimal("26.00"));
        documentoFiscal.setTotal(new BigDecimal("231.00"));
        documentoFiscal.setAplicaRetencion(true);

        boolean res = documentoFiscalDAO.update(documentoFiscal);

        assertTrue(res, "La actualización debería ser exitosa.");

        getById(documentoFiscal);
    }

    private void getById(DocumentoFiscal documentoFiscal) throws SQLException {
        DocumentoFiscal res = documentoFiscalDAO.getById(
                documentoFiscal.getDocumentoFiscalId()
        );

        assertNotNull(res, "El documento fiscal obtenido por ID no debería ser nulo.");
        assertEquals(documentoFiscal.getDocumentoFiscalId(), res.getDocumentoFiscalId());
        assertEquals(documentoFiscal.getTerceroId(), res.getTerceroId());
        assertEquals(documentoFiscal.getTipoDocumentoFiscalId(), res.getTipoDocumentoFiscalId());
        assertEquals(documentoFiscal.getPartidaId(), res.getPartidaId());
        assertEquals(documentoFiscal.getTipoLibro(), res.getTipoLibro());
        assertEquals(documentoFiscal.getNumeroDocumento(), res.getNumeroDocumento());
        assertEquals(documentoFiscal.getFechaDocumento(), res.getFechaDocumento());
        assertEquals(documentoFiscal.getMontoExento(), res.getMontoExento());
        assertEquals(documentoFiscal.getMontoGravado(), res.getMontoGravado());
        assertEquals(documentoFiscal.getIva(), res.getIva());
        assertEquals(documentoFiscal.getTotal(), res.getTotal());
        assertEquals(documentoFiscal.isAplicaRetencion(), res.isAplicaRetencion());
    }

    private void search(DocumentoFiscal documentoFiscal) throws SQLException {
        ArrayList<DocumentoFiscal> documentos = documentoFiscalDAO.search(
                documentoFiscal.getNumeroDocumento()
        );

        boolean find = false;

        for (DocumentoFiscal item : documentos) {
            if (item.getDocumentoFiscalId() == documentoFiscal.getDocumentoFiscalId()) {
                find = true;
                break;
            }
        }

        assertTrue(find, "El documento fiscal buscado no fue encontrado.");
    }

    private void delete(DocumentoFiscal documentoFiscal) throws SQLException {
        boolean res = documentoFiscalDAO.delete(documentoFiscal);

        assertTrue(res, "La eliminación debería ser exitosa.");

        DocumentoFiscal res2 = documentoFiscalDAO.getById(
                documentoFiscal.getDocumentoFiscalId()
        );

        assertNull(res2, "El documento fiscal debería haber sido eliminado.");
    }

    @Test
    void testDocumentoFiscalDAO() throws SQLException {
        long num = System.currentTimeMillis() % 100000;

        Tercero tercero = new Tercero(
                0,
                "Cliente",
                "Tercero Documento Test " + num,
                "0614" + num,
                "123" + num,
                "doc" + num + "@test.com",
                "7000" + (num % 10000),
                true
        );

        Tercero terceroCreado = terceroDAO.create(tercero);
        assertNotNull(terceroCreado, "El tercero creado no debería ser nulo.");

        TipoDocumentoFiscal tipoDocumentoFiscal = new TipoDocumentoFiscal(
                0,
                "D" + num,
                "Tipo Documento Test " + num
        );

        TipoDocumentoFiscal tipoDocumentoFiscalCreado =
                tipoDocumentoFiscalDAO.create(tipoDocumentoFiscal);

        assertNotNull(tipoDocumentoFiscalCreado,
                "El tipo de documento fiscal creado no debería ser nulo.");

        DocumentoFiscal documentoFiscal = new DocumentoFiscal(
                0,
                terceroCreado.getTerceroId(),
                tipoDocumentoFiscalCreado.getTipoDocumentoFiscalId(),
                null,
                "Compra",
                "DOC-" + num,
                new Date(System.currentTimeMillis()),
                new BigDecimal("0.00"),
                new BigDecimal("100.00"),
                new BigDecimal("13.00"),
                new BigDecimal("113.00"),
                false
        );

        DocumentoFiscal testDocumentoFiscal = create(documentoFiscal);

        getById(testDocumentoFiscal);

        update(testDocumentoFiscal);

        search(testDocumentoFiscal);

        delete(testDocumentoFiscal);

        tipoDocumentoFiscalDAO.delete(tipoDocumentoFiscalCreado);
        terceroDAO.delete(terceroCreado);
    }
}