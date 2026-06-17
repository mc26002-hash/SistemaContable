package esfe.dominio;

public class Tercero {
    private int terceroId;
    private String tipoTercero;
    private String nombre;
    private String nit;
    private String nrc;
    private String correo;
    private String telefono;
    private boolean activo;

    public Tercero() {

    }

    public Tercero(int terceroId, String tipoTercero, String nombre, String nit, String nrc, String correo, String telefono, boolean activo) {
        this.terceroId = terceroId;
        this.tipoTercero = tipoTercero;
        this.nombre = nombre;
        this.nit = nit;
        this.nrc = nrc;
        this.correo = correo;
        this.telefono = telefono;
        this.activo = activo;
    }

    public int getTerceroId() {
        return terceroId;
    }

    public void setTerceroId(int terceroId) {
        this.terceroId = terceroId;
    }

    public String getTipoTercero() {
        return tipoTercero;
    }

    public void setTipoTercero(String tipoTercero) {
        this.tipoTercero = tipoTercero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}