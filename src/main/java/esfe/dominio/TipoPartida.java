package esfe.dominio;

public class TipoPartida {
    private int tipoPartidaId;
    private String nombreTipo;

    public TipoPartida() {

    }

    public TipoPartida(int tipoPartidaId, String nombreTipo) {
        this.tipoPartidaId = tipoPartidaId;
        this.nombreTipo = nombreTipo;
    }

    public int getTipoPartidaId() {
        return tipoPartidaId;
    }

    public void setTipoPartidaId(int tipoPartidaId) {
        this.tipoPartidaId = tipoPartidaId;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }
}