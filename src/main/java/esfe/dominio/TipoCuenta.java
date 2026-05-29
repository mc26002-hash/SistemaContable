package esfe.dominio;

public class TipoCuenta {

    private int tipoCuentaId;
    private String nombreTipo;
    private char naturaleza;

    public TipoCuenta() {
    }

    public TipoCuenta(int tipoCuentaId, String nombreTipo, char naturaleza) {
        this.tipoCuentaId = tipoCuentaId;
        this.nombreTipo = nombreTipo;
        this.naturaleza = naturaleza;
    }

    public int getTipoCuentaId() {
        return tipoCuentaId;
    }

    public void setTipoCuentaId(int tipoCuentaId) {
        this.tipoCuentaId = tipoCuentaId;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    public char getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(char naturaleza) {
        this.naturaleza = naturaleza;
    }

    @Override
    public String toString() {
        return "TipoCuenta{" +
                "tipoCuentaId=" + tipoCuentaId +
                ", nombreTipo='" + nombreTipo + '\'' +
                ", naturaleza=" + naturaleza +
                '}';
    }
}
