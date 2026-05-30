package esfe.dominio;

public class TipoDocumentoFiscal {
    private int tipoDocumentoFiscalId;
    private String codigo;
    private String nombre;

    public TipoDocumentoFiscal() {

    }

    public TipoDocumentoFiscal(int tipoDocumentoFiscalId, String codigo, String nombre) {
        this.tipoDocumentoFiscalId = tipoDocumentoFiscalId;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public int getTipoDocumentoFiscalId() {
        return tipoDocumentoFiscalId;
    }

    public void setTipoDocumentoFiscalId(int tipoDocumentoFiscalId) {
        this.tipoDocumentoFiscalId = tipoDocumentoFiscalId;
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
}