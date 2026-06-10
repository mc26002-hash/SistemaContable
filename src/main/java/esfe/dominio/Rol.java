package esfe.dominio;

public class Rol {
    private int rolId;
    private String nombreRol;
    private String descripcion;
    private boolean activo;

    public Rol() {

    }

    public Rol(int rolId, String nombreRol, String descripcion, boolean activo) {
        this.rolId = rolId;
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getStrEstatus() {
        return activo ? "ACTIVO" : "INACTIVO";
    }
}