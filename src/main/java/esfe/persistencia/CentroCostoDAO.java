package esfe.persistencia;

import esfe.dominio.CentroCosto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CentroCostoDAO {

    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public CentroCostoDAO() {
        conn = ConnectionManager.getInstance();
    }

    public CentroCosto create(CentroCosto centroCosto) throws SQLException {
        CentroCosto res = null;
        int idGenerado = 0;

        try {
            ps = conn.connect().prepareStatement(
                    "INSERT INTO CentrosCosto " +
                            "(Codigo, Nombre, Responsable, Presupuesto, Activo) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, centroCosto.getCodigo());
            ps.setString(2, centroCosto.getNombre());
            ps.setString(3, centroCosto.getResponsable());
            ps.setBigDecimal(4, centroCosto.getPresupuesto());
            ps.setBoolean(5, centroCosto.isActivo());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                } else {
                    throw new SQLException("Error al crear el centro de costo: no se obtuvo ID.");
                }
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al crear el centro de costo: " + ex.getMessage(), ex);
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

        if (idGenerado > 0) {
            res = getById(idGenerado);
        }

        return res;
    }

    public boolean update(CentroCosto centroCosto) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE CentrosCosto " +
                            "SET Codigo = ?, Nombre = ?, Responsable = ?, " +
                            "Presupuesto = ?, Activo = ? " +
                            "WHERE CentroCostoId = ?"
            );

            ps.setString(1, centroCosto.getCodigo());
            ps.setString(2, centroCosto.getNombre());
            ps.setString(3, centroCosto.getResponsable());
            ps.setBigDecimal(4, centroCosto.getPresupuesto());
            ps.setBoolean(5, centroCosto.isActivo());
            ps.setInt(6, centroCosto.getCentroCostoId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el centro de costo: " + ex.getMessage(), ex);
        } finally {

            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public boolean delete(CentroCosto centroCosto) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM CentrosCosto WHERE CentroCostoId = ?"
            );

            ps.setInt(1, centroCosto.getCentroCostoId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el centro de costo: " + ex.getMessage(), ex);
        } finally {

            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public ArrayList<CentroCosto> search(String texto) throws SQLException {

        ArrayList<CentroCosto> records = new ArrayList<>();

        try {

            ps = conn.connect().prepareStatement(
                    "SELECT CentroCostoId, Codigo, Nombre, Responsable, Presupuesto, Activo " +
                            "FROM CentrosCosto " +
                            "WHERE Codigo LIKE ? " +
                            "OR Nombre LIKE ? " +
                            "OR Responsable LIKE ?"
            );

            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");
            ps.setString(3, "%" + texto + "%");

            rs = ps.executeQuery();

            while (rs.next()) {

                CentroCosto centroCosto = new CentroCosto();

                centroCosto.setCentroCostoId(rs.getInt("CentroCostoId"));
                centroCosto.setCodigo(rs.getString("Codigo"));
                centroCosto.setNombre(rs.getString("Nombre"));
                centroCosto.setResponsable(rs.getString("Responsable"));
                centroCosto.setPresupuesto(rs.getBigDecimal("Presupuesto"));;
                centroCosto.setActivo(rs.getBoolean("Activo"));

                records.add(centroCosto);
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al buscar centros de costo: " + ex.getMessage(), ex);
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

    public CentroCosto getById(int centroCostoId) throws SQLException {

        CentroCosto centroCosto = null;

        try {

            ps = conn.connect().prepareStatement(
                    "SELECT CentroCostoId, Codigo, Nombre, Responsable, Presupuesto, Activo " +
                            "FROM CentrosCosto " +
                            "WHERE CentroCostoId = ?"
            );

            ps.setInt(1, centroCostoId);

            rs = ps.executeQuery();

            if (rs.next()) {

                centroCosto = new CentroCosto();

                centroCosto.setCentroCostoId(rs.getInt("CentroCostoId"));
                centroCosto.setCodigo(rs.getString("Codigo"));
                centroCosto.setNombre(rs.getString("Nombre"));
                centroCosto.setResponsable(rs.getString("Responsable"));
                centroCosto.setPresupuesto(rs.getBigDecimal("Presupuesto"));
                centroCosto.setActivo(rs.getBoolean("Activo"));
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el centro de costo por id: " + ex.getMessage(), ex);
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

        return centroCosto;
    }
}
