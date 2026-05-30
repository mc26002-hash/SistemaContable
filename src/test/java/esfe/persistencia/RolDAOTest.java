package esfe.persistencia;

import esfe.dominio.Rol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolDAOTest {

    @Test
    void insertarDebeUnoCuandoinsertaCorrectamente() {
        RolDAO dao = new RolDAO();

        Rol rol = new Rol();
        rol.setNombreRol("Administrador_" + System.currentTimeMillis());
        rol.setDescripcion("Rol administrador");

        int resultado = dao.insertar(rol);

        assertEquals(1, resultado);
    }
}