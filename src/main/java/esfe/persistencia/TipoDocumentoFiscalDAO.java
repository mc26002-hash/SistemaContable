package esfe.persistencia;

import esfe.dominio.TipoDocumentoFiscal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TipoDocumentoFiscalDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public TipoDocumentoFiscalDAO() {
        conn = ConnectionManager.getInstance();
    }

    public TipoDocumentoFiscal create(TipoDocumentoFiscal tipoDocumentoFiscal) throws SQLException {
        TipoDocumentoFiscal res = null;

        try {
            ps = conn.connect().prepareStatement(
                    "INSERT INTO TiposDocumentoFiscal (Codigo, Nombre) " +
                            "VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, tipoDocumentoFiscal.getCodigo());
            ps.setString(2, tipoDocumentoFiscal.getNombre());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Error al crear el tipo de documento fiscal: no se obtuvo ID.");
                }
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al crear el tipo de documento fiscal: " + ex.getMessage(), ex);
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

    public boolean update(TipoDocumentoFiscal tipoDocumentoFiscal) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE TiposDocumentoFiscal " +
                            "SET Codigo = ?, Nombre = ? " +
                            "WHERE TipoDocumentoFiscalId = ?"
            );

            ps.setString(1, tipoDocumentoFiscal.getCodigo());
            ps.setString(2, tipoDocumentoFiscal.getNombre());
            ps.setInt(3, tipoDocumentoFiscal.getTipoDocumentoFiscalId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el tipo de documento fiscal: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public boolean delete(TipoDocumentoFiscal tipoDocumentoFiscal) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM TiposDocumentoFiscal WHERE TipoDocumentoFiscalId = ?"
            );

            ps.setInt(1, tipoDocumentoFiscal.getTipoDocumentoFiscalId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el tipo de documento fiscal: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public ArrayList<TipoDocumentoFiscal> search(String texto) throws SQLException {
        ArrayList<TipoDocumentoFiscal> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT TipoDocumentoFiscalId, Codigo, Nombre " +
                            "FROM TiposDocumentoFiscal " +
                            "WHERE Nombre LIKE ? OR Codigo LIKE ?"
            );

            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                TipoDocumentoFiscal tipoDocumentoFiscal = new TipoDocumentoFiscal();

                tipoDocumentoFiscal.setTipoDocumentoFiscalId(rs.getInt("TipoDocumentoFiscalId"));
                tipoDocumentoFiscal.setCodigo(rs.getString("Codigo"));
                tipoDocumentoFiscal.setNombre(rs.getString("Nombre"));

                records.add(tipoDocumentoFiscal);
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al buscar tipos de documento fiscal: " + ex.getMessage(), ex);
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

    public TipoDocumentoFiscal getById(int tipoDocumentoFiscalId) throws SQLException {
        TipoDocumentoFiscal tipoDocumentoFiscal = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT TipoDocumentoFiscalId, Codigo, Nombre " +
                            "FROM TiposDocumentoFiscal " +
                            "WHERE TipoDocumentoFiscalId = ?"
            );

            ps.setInt(1, tipoDocumentoFiscalId);

            rs = ps.executeQuery();

            if (rs.next()) {
                tipoDocumentoFiscal = new TipoDocumentoFiscal();

                tipoDocumentoFiscal.setTipoDocumentoFiscalId(rs.getInt("TipoDocumentoFiscalId"));
                tipoDocumentoFiscal.setCodigo(rs.getString("Codigo"));
                tipoDocumentoFiscal.setNombre(rs.getString("Nombre"));
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el tipo de documento fiscal por id: " + ex.getMessage(), ex);
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

        return tipoDocumentoFiscal;
    }
}