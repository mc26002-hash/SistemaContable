package esfe.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import esfe.dominio.TipoDocumentoFiscal;

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
                    "INSERT INTO TiposDocumentoFiscal (Codigo, Nombre) VALUES (?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, tipoDocumentoFiscal.getCodigo());
            ps.setString(2, tipoDocumentoFiscal.getNombre());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();

                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    tipoDocumentoFiscal.setTipoDocumentoFiscalId(idGenerado);
                    res = tipoDocumentoFiscal;
                } else {
                    throw new SQLException("Creating TipoDocumentoFiscal failed, no ID obtained.");
                }
            }

            ps.close();

        } catch (SQLException ex) {
            throw new SQLException("Error al crear el tipo de documento fiscal: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public boolean update(TipoDocumentoFiscal tipoDocumentoFiscal) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE TiposDocumentoFiscal SET Codigo = ?, Nombre = ? WHERE TipoDocumentoFiscalId = ?"
            );

            ps.setString(1, tipoDocumentoFiscal.getCodigo());
            ps.setString(2, tipoDocumentoFiscal.getNombre());
            ps.setInt(3, tipoDocumentoFiscal.getTipoDocumentoFiscalId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

            ps.close();

        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el tipo de documento fiscal: " + ex.getMessage(), ex);
        } finally {
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

            ps.close();

        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el tipo de documento fiscal: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public ArrayList<TipoDocumentoFiscal> search(String nombre) throws SQLException {
        ArrayList<TipoDocumentoFiscal> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT TipoDocumentoFiscalId, Codigo, Nombre " +
                            "FROM TiposDocumentoFiscal " +
                            "WHERE Nombre LIKE ? OR Codigo LIKE ?"
            );

            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                TipoDocumentoFiscal tipoDocumentoFiscal = new TipoDocumentoFiscal();

                tipoDocumentoFiscal.setTipoDocumentoFiscalId(rs.getInt(1));
                tipoDocumentoFiscal.setCodigo(rs.getString(2));
                tipoDocumentoFiscal.setNombre(rs.getString(3));

                records.add(tipoDocumentoFiscal);
            }

            ps.close();
            rs.close();

        } catch (SQLException ex) {
            throw new SQLException("Error al buscar tipos de documento fiscal: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }

        return records;
    }

    public TipoDocumentoFiscal getById(int id) throws SQLException {
        TipoDocumentoFiscal tipoDocumentoFiscal = new TipoDocumentoFiscal();

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT TipoDocumentoFiscalId, Codigo, Nombre " +
                            "FROM TiposDocumentoFiscal " +
                            "WHERE TipoDocumentoFiscalId = ?"
            );

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                tipoDocumentoFiscal.setTipoDocumentoFiscalId(rs.getInt(1));
                tipoDocumentoFiscal.setCodigo(rs.getString(2));
                tipoDocumentoFiscal.setNombre(rs.getString(3));
            } else {
                tipoDocumentoFiscal = null;
            }

            ps.close();
            rs.close();

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el tipo de documento fiscal por id: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }

        return tipoDocumentoFiscal;
    }
}