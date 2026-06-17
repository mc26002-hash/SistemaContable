package esfe.dominio;

import java.math.BigDecimal;
import java.sql.Date;

public class DocumentoFiscal {

    private int documentoFiscalId;
    private int terceroId;
    private int tipoDocumentoFiscalId;
    private Integer partidaId; // Puede ser null

    private String tipoLibro;
    private String numeroDocumento;
    private Date fechaDocumento;

    private BigDecimal montoExento;
    private BigDecimal montoGravado;
    private BigDecimal iva;
    private BigDecimal total;

    private boolean aplicaRetencion;

    public DocumentoFiscal() {
    }

    public DocumentoFiscal(int documentoFiscalId,
                           int terceroId,
                           int tipoDocumentoFiscalId,
                           Integer partidaId,
                           String tipoLibro,
                           String numeroDocumento,
                           Date fechaDocumento,
                           BigDecimal montoExento,
                           BigDecimal montoGravado,
                           BigDecimal iva,
                           BigDecimal total,
                           boolean aplicaRetencion) {

        this.documentoFiscalId = documentoFiscalId;
        this.terceroId = terceroId;
        this.tipoDocumentoFiscalId = tipoDocumentoFiscalId;
        this.partidaId = partidaId;
        this.tipoLibro = tipoLibro;
        this.numeroDocumento = numeroDocumento;
        this.fechaDocumento = fechaDocumento;
        this.montoExento = montoExento;
        this.montoGravado = montoGravado;
        this.iva = iva;
        this.total = total;
        this.aplicaRetencion = aplicaRetencion;
    }

    public int getDocumentoFiscalId() {
        return documentoFiscalId;
    }

    public void setDocumentoFiscalId(int documentoFiscalId) {
        this.documentoFiscalId = documentoFiscalId;
    }

    public int getTerceroId() {
        return terceroId;
    }

    public void setTerceroId(int terceroId) {
        this.terceroId = terceroId;
    }

    public int getTipoDocumentoFiscalId() {
        return tipoDocumentoFiscalId;
    }

    public void setTipoDocumentoFiscalId(int tipoDocumentoFiscalId) {
        this.tipoDocumentoFiscalId = tipoDocumentoFiscalId;
    }

    public Integer getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(Integer partidaId) {
        this.partidaId = partidaId;
    }

    public String getTipoLibro() {
        return tipoLibro;
    }

    public void setTipoLibro(String tipoLibro) {
        this.tipoLibro = tipoLibro;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Date getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(Date fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public BigDecimal getMontoExento() {
        return montoExento;
    }

    public void setMontoExento(BigDecimal montoExento) {
        this.montoExento = montoExento;
    }

    public BigDecimal getMontoGravado() {
        return montoGravado;
    }

    public void setMontoGravado(BigDecimal montoGravado) {
        this.montoGravado = montoGravado;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public boolean isAplicaRetencion() {
        return aplicaRetencion;
    }

    public void setAplicaRetencion(boolean aplicaRetencion) {
        this.aplicaRetencion = aplicaRetencion;
    }
}