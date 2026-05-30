package esfe.persistencia;

import esfe.dominio.TipoPartida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TipoPartidaDAOTest {

    private TipoPartidaDAO repository;

    @BeforeEach
    void setUp() {
        repository = new TipoPartidaDAO();
    }

    @Test
    void testCalculoTotalesEnTiempoRealYCuadrado() {

        TipoPartida partida = new TipoPartida("Venta de mercadería");

        partida.agregarDetalle("101", "Caja", new BigDecimal("1000.00"), BigDecimal.ZERO);

        assertEquals(new BigDecimal("1000.00"), partida.getTotalDebe());
        assertEquals(BigDecimal.ZERO, partida.getTotalHaber());
        assertFalse(partida.estaCuadrada(), "No debería estar cuadrada con solo una línea");

        partida.agregarDetalle("701", "Ventas", BigDecimal.ZERO, new BigDecimal("1000.00"));

        assertEquals(new BigDecimal("1000.00"), partida.getTotalDebe());
        assertEquals(new BigDecimal("1000.00"), partida.getTotalHaber());
        assertTrue(partida.estaCuadrada(), "La partida debería estar perfectamente cuadrada");
    }

    @Test
    void testGuardarPartidaDescuadradaLanzaExcepcion() {
        TipoPartida partidaDescuadrada = new TipoPartida("Partida errónea");
        partidaDescuadrada.agregarDetalle("101", "Caja", new BigDecimal("500.00"), BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class, () -> {
            repository.guardar(partidaDescuadrada);
        });
    }

    @Test
    void testGuardarPartidaExitosa() {
        TipoPartida partidaValida = new TipoPartida("Pago de servicios");
        partidaValida.agregarDetalle("631", "Luz", new BigDecimal("150.00"), BigDecimal.ZERO);
        partidaValida.agregarDetalle("101", "Caja", BigDecimal.ZERO, new BigDecimal("150.00"));

        assertDoesNotThrow(() -> repository.guardar(partidaValida));
        assertNotNull(partidaValida.getId(), "El ID debería haber sido asignado por el repositorio");
    }
}