package esfe.dominio;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TipoCuentaTest {

    @Test
    void testToString() {
        // 1. Arrange: Creamos un objeto que simule un registro real de tu BD
        TipoCuenta cuenta = new TipoCuenta();
        cuenta.setTipoCuentaId(1);
        cuenta.setNombreTipo("Activo");
        cuenta.setNaturaleza('D'); // 'D' de Deudora según tu CHECK constraint

        // 2. Act: Ejecutamos el método a probar
        String resultado = cuenta.toString();

        // 3. Assert: Validamos que el String generado tenga el formato correcto
        assertNotNull(resultado, "El método toString no debe retornar nulo");
        assertTrue(resultado.contains("TipoCuenta{"), "Debe contener el nombre de la clase");
        assertTrue(resultado.contains("tipoCuentaId=1"), "Debe reflejar el ID correcto");
        assertTrue(resultado.contains("nombreTipo='Activo'"), "Debe reflejar el nombre de tipo correcto");
        assertTrue(resultado.contains("naturaleza=D"), "Debe reflejar la naturaleza correcta");
    }
}


