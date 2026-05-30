package esfe.persistencia;

import esfe.dominio.TipoPartida;
import java.util.ArrayList;
import java.util.List;

public class TipoPartidaDAO {

    private final List<TipoPartida> database = new ArrayList<>();
    private long idSequence = 1;

    public void guardar(TipoPartida partida) {
        if (!partida.estaCuadrada()) {
            throw new IllegalArgumentException("No se puede guardar: La partida no está cuadrada (Debe != Haber).");
        }

        partida.setId(idSequence++);
        database.add(partida);
        System.out.println("Partida guardada con éxito en la BD. ID asignado: " + partida.getId());
    }

    public List<TipoPartida> obtenerTodas() {
        return new ArrayList<>(database);
    }
}