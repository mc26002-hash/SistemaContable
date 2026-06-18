package esfe.persistencia;

import esfe.dominio.DocumentoFiscal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

public class DocumentoFiscalDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public DocumentoFiscalDAO() {
        conn = ConnectionManager.getInstance();
    }

    public DocumentoFiscal create(DocumentoFiscal documentoFiscal) throws SQLException {
        DocumentoFiscal res = null;

        try {
            ps = conn.connect().prepareStatement(
                    "INSERT INTO DocumentosFiscales " +
                            "(TerceroId, TipoDocumentoFiscalId, PartidaId, TipoLibro, NumeroDocumento, " +
                            "FechaDocumento, MontoExento, MontoGravado, IVA, Total, AplicaRetencion) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setInt(1, documentoFiscal.getTerceroId());
            ps.setInt(2, documentoFiscal.getTipoDocumentoFiscalId());

            if (documentoFiscal.getPartidaId() != null) {
                ps.setInt(3, documentoFiscal.getPartidaId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setString(4, documentoFiscal.getTipoLibro());
            ps.setString(5, documentoFiscal.getNumeroDocumento());
            ps.setDate(6, documentoFiscal.getFechaDocumento());
            ps.setBigDecimal(7, documentoFiscal.getMontoExento());
            ps.setBigDecimal(8, documentoFiscal.getMontoGravado());
            ps.setBigDecimal(9, documentoFiscal.getIva());
            ps.setBigDecimal(10, documentoFiscal.getTotal());
            ps.setBoolean(11, documentoFiscal.isAplicaRetencion());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Error al crear el documento fiscal: no se obtuvo ID.");
                }
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al crear el documento fiscal: " + ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (ps != null) {
                ps.close();
            }

            rs = null;
            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public boolean update(DocumentoFiscal documentoFiscal) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE DocumentosFiscales " +
                            "SET TerceroId = ?, TipoDocumentoFiscalId = ?, PartidaId = ?, " +
                            "TipoLibro = ?, NumeroDocumento = ?, FechaDocumento = ?, " +
                            "MontoExento = ?, MontoGravado = ?, IVA = ?, Total = ?, AplicaRetencion = ? " +
                            "WHERE DocumentoFiscalId = ?"
            );

            ps.setInt(1, documentoFiscal.getTerceroId());
            ps.setInt(2, documentoFiscal.getTipoDocumentoFiscalId());

            if (documentoFiscal.getPartidaId() != null) {
                ps.setInt(3, documentoFiscal.getPartidaId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setString(4, documentoFiscal.getTipoLibro());
            ps.setString(5, documentoFiscal.getNumeroDocumento());
            ps.setDate(6, documentoFiscal.getFechaDocumento());
            ps.setBigDecimal(7, documentoFiscal.getMontoExento());
            ps.setBigDecimal(8, documentoFiscal.getMontoGravado());
            ps.setBigDecimal(9, documentoFiscal.getIva());
            ps.setBigDecimal(10, documentoFiscal.getTotal());
            ps.setBoolean(11, documentoFiscal.isAplicaRetencion());
            ps.setInt(12, documentoFiscal.getDocumentoFiscalId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el documento fiscal: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public boolean delete(DocumentoFiscal documentoFiscal) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM DocumentosFiscales WHERE DocumentoFiscalId = ?"
            );

            ps.setInt(1, documentoFiscal.getDocumentoFiscalId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el documento fiscal: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public ArrayList<DocumentoFiscal> search(String texto) throws SQLException {
        ArrayList<DocumentoFiscal> records = new ArrayList<>();

        try {
            String sql =
                    "SELECT DocumentoFiscalId, TerceroId, TipoDocumentoFiscalId, PartidaId, " +
                            "TipoLibro, NumeroDocumento, FechaDocumento, MontoExento, " +
                            "MontoGravado, IVA, Total, AplicaRetencion " +
                            "FROM DocumentosFiscales";

            if (texto != null && !texto.trim().isEmpty()) {
                sql += " WHERE TipoLibro LIKE ? OR NumeroDocumento LIKE ?";
            }

            ps = conn.connect().prepareStatement(sql);

            if (texto != null && !texto.trim().isEmpty()) {
                ps.setString(1, "%" + texto + "%");
                ps.setString(2, "%" + texto + "%");
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                DocumentoFiscal documentoFiscal = new DocumentoFiscal();

                documentoFiscal.setDocumentoFiscalId(rs.getInt("DocumentoFiscalId"));
                documentoFiscal.setTerceroId(rs.getInt("TerceroId"));
                documentoFiscal.setTipoDocumentoFiscalId(rs.getInt("TipoDocumentoFiscalId"));

                int partidaId = rs.getInt("PartidaId");
                if (rs.wasNull()) {
                    documentoFiscal.setPartidaId(null);
                } else {
                    documentoFiscal.setPartidaId(partidaId);
                }

                documentoFiscal.setTipoLibro(rs.getString("TipoLibro"));
                documentoFiscal.setNumeroDocumento(rs.getString("NumeroDocumento"));
                documentoFiscal.setFechaDocumento(rs.getDate("FechaDocumento"));
                documentoFiscal.setMontoExento(rs.getBigDecimal("MontoExento"));
                documentoFiscal.setMontoGravado(rs.getBigDecimal("MontoGravado"));
                documentoFiscal.setIva(rs.getBigDecimal("IVA"));
                documentoFiscal.setTotal(rs.getBigDecimal("Total"));
                documentoFiscal.setAplicaRetencion(rs.getBoolean("AplicaRetencion"));

                records.add(documentoFiscal);
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al buscar documentos fiscales: " + ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (ps != null) {
                ps.close();
            }

            rs = null;
            ps = null;
            conn.disconnect();
        }

        return records;
    }

    public DocumentoFiscal getById(int documentoFiscalId) throws SQLException {
        DocumentoFiscal documentoFiscal = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT DocumentoFiscalId, TerceroId, TipoDocumentoFiscalId, PartidaId, " +
                            "TipoLibro, NumeroDocumento, FechaDocumento, MontoExento, " +
                            "MontoGravado, IVA, Total, AplicaRetencion " +
                            "FROM DocumentosFiscales " +
                            "WHERE DocumentoFiscalId = ?"
            );

            ps.setInt(1, documentoFiscalId);

            rs = ps.executeQuery();

            if (rs.next()) {
                documentoFiscal = new DocumentoFiscal();

                documentoFiscal.setDocumentoFiscalId(rs.getInt("DocumentoFiscalId"));
                documentoFiscal.setTerceroId(rs.getInt("TerceroId"));
                documentoFiscal.setTipoDocumentoFiscalId(rs.getInt("TipoDocumentoFiscalId"));

                int partidaId = rs.getInt("PartidaId");
                if (rs.wasNull()) {
                    documentoFiscal.setPartidaId(null);
                } else {
                    documentoFiscal.setPartidaId(partidaId);
                }

                documentoFiscal.setTipoLibro(rs.getString("TipoLibro"));
                documentoFiscal.setNumeroDocumento(rs.getString("NumeroDocumento"));
                documentoFiscal.setFechaDocumento(rs.getDate("FechaDocumento"));
                documentoFiscal.setMontoExento(rs.getBigDecimal("MontoExento"));
                documentoFiscal.setMontoGravado(rs.getBigDecimal("MontoGravado"));
                documentoFiscal.setIva(rs.getBigDecimal("IVA"));
                documentoFiscal.setTotal(rs.getBigDecimal("Total"));
                documentoFiscal.setAplicaRetencion(rs.getBoolean("AplicaRetencion"));
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el documento fiscal por id: " + ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (ps != null) {
                ps.close();
            }

            rs = null;
            ps = null;
            conn.disconnect();
        }

        return documentoFiscal;
    }
}