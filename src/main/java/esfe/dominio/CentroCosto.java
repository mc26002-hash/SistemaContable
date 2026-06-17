package esfe.dominio;

import java.math.BigDecimal;

public class CentroCosto {

    private int centroCostoId;
    private String codigo;
    private String nombre;
    private String responsable;
    private BigDecimal presupuesto;
    private boolean activo;

    // Constructor vacío
    public CentroCosto() {
    }

    // Constructor con parámetros
    public CentroCosto(int centroCostoId, String codigo, String nombre,
                       String responsable, BigDecimal presupuesto,
                       boolean activo) {

        this.centroCostoId = centroCostoId;
        this.codigo = codigo;
        this.nombre = nombre;
        this.responsable = responsable;
        this.presupuesto = presupuesto;
        this.activo = activo;
    }

    // Getters y Setters

    public int getCentroCostoId() {
        return centroCostoId;
    }

    public void setCentroCostoId(int centroCostoId) {
        this.centroCostoId = centroCostoId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public BigDecimal getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}