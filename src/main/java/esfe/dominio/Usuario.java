package esfe.dominio;

public class Usuario {

    private int usuarioId;
    private int rolId;
    private String nombreUsuario;
    private byte[] passwordHash;
    private String nombreCompleto;
    private String correoElectronico;
    private boolean activo;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(int usuarioId, int rolId, String nombreUsuario,
                   byte[] passwordHash, String nombreCompleto,
                   String correoElectronico, boolean activo) {

        this.usuarioId = usuarioId;
        this.rolId = rolId;
        this.nombreUsuario = nombreUsuario;
        this.passwordHash = passwordHash;
        this.nombreCompleto = nombreCompleto;
        this.correoElectronico = correoElectronico;
        this.activo = activo;
    }

    // Getters y Setters

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}


