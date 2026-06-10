package esfe.persistencia;

import esfe.dominio.TipoCuenta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TipoCuentaDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public TipoCuentaDAO() {
        conn = ConnectionManager.getInstance();
    }

    public TipoCuenta create(TipoCuenta tipoCuenta) throws SQLException {
        TipoCuenta res = null;

        try {
            ps = conn.connect().prepareStatement(
                    "INSERT INTO TiposCuenta (NombreTipo, Naturaleza) " +
                            "VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, tipoCuenta.getNombreTipo());
            ps.setString(2, String.valueOf(tipoCuenta.getNaturaleza()));

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Error al crear el tipo de cuenta: no se obtuvo ID.");
                }
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al crear el tipo de cuenta: " + ex.getMessage(), ex);
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

    public boolean update(TipoCuenta tipoCuenta) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE TiposCuenta " +
                            "SET NombreTipo = ?, Naturaleza = ? " +
                            "WHERE TipoCuentaId = ?"
            );

            ps.setString(1, tipoCuenta.getNombreTipo());
            ps.setString(2, String.valueOf(tipoCuenta.getNaturaleza()));
            ps.setInt(3, tipoCuenta.getTipoCuentaId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el tipo de cuenta: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public boolean delete(TipoCuenta tipoCuenta) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM TiposCuenta WHERE TipoCuentaId = ?"
            );

            ps.setInt(1, tipoCuenta.getTipoCuentaId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el tipo de cuenta: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public ArrayList<TipoCuenta> search(String nombreTipo) throws SQLException {
        ArrayList<TipoCuenta> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT TipoCuentaId, NombreTipo, Naturaleza " +
                            "FROM TiposCuenta " +
                            "WHERE NombreTipo LIKE ?"
            );

            ps.setString(1, "%" + nombreTipo + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                TipoCuenta tipoCuenta = new TipoCuenta();

                tipoCuenta.setTipoCuentaId(rs.getInt("TipoCuentaId"));
                tipoCuenta.setNombreTipo(rs.getString("NombreTipo"));
                tipoCuenta.setNaturaleza(rs.getString("Naturaleza").charAt(0));

                records.add(tipoCuenta);
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al buscar tipos de cuenta: " + ex.getMessage(), ex);
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

    public TipoCuenta getById(int tipoCuentaId) throws SQLException {
        TipoCuenta tipoCuenta = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT TipoCuentaId, NombreTipo, Naturaleza " +
                            "FROM TiposCuenta " +
                            "WHERE TipoCuentaId = ?"
            );

            ps.setInt(1, tipoCuentaId);

            rs = ps.executeQuery();

            if (rs.next()) {
                tipoCuenta = new TipoCuenta();

                tipoCuenta.setTipoCuentaId(rs.getInt("TipoCuentaId"));
                tipoCuenta.setNombreTipo(rs.getString("NombreTipo"));
                tipoCuenta.setNaturaleza(rs.getString("Naturaleza").charAt(0));
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el tipo de cuenta por id: " + ex.getMessage(), ex);
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

        return tipoCuenta;
    }
}
