package esfe.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TipoPartida {
    private Long id;
    private LocalDate fecha;
    private String glosa;
    private List<DetallePartida> detalles;

    public TipoPartida(String glosa) {
        this.fecha = LocalDate.now();
        this.glosa = glosa;
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(String codigoCuenta, String nombreCuenta, BigDecimal debe, BigDecimal haber) {
        this.detalles.add(new DetallePartida(codigoCuenta, nombreCuenta, debe, haber));
    }

    public BigDecimal getTotalDebe() {
        return detalles.stream()
                .map(DetallePartida::getDebe)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalHaber() {
        return detalles.stream()
                .map(DetallePartida::getHaber)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean estaCuadrada() {
        BigDecimal totalDebe = getTotalDebe();
        BigDecimal totalHaber = getTotalHaber();
        return totalDebe.compareTo(totalHaber) == 0 && totalDebe.compareTo(BigDecimal.ZERO) > 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public String getGlosa() { return glosa; }
    public List<DetallePartida> getDetalles() { return detalles; }


    public static class DetallePartida {
        private String codigoCuenta;
        private String nombreCuenta;
        private BigDecimal debe;
        private BigDecimal haber;

        public DetallePartida(String codigoCuenta, String nombreCuenta, BigDecimal debe, BigDecimal haber) {
            this.codigoCuenta = codigoCuenta;
            this.nombreCuenta = nombreCuenta;
            this.debe = debe != null ? debe : BigDecimal.ZERO;
            this.haber = haber != null ? haber : BigDecimal.ZERO;
        }

        public String getCodigoCuenta() { return codigoCuenta; }
        public String getNombreCuenta() { return nombreCuenta; }
        public BigDecimal getDebe() { return debe; }
        public BigDecimal getHaber() { return haber; }
    }
}
