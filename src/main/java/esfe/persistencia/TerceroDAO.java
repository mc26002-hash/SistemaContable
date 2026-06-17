package esfe.persistencia;

import esfe.dominio.Tercero;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TerceroDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public TerceroDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Tercero create(Tercero tercero) throws SQLException {
        Tercero res = null;

        try {
            ps = conn.connect().prepareStatement(
                    "INSERT INTO Terceros " +
                            "(TipoTercero, Nombre, NIT, NRC, Correo, Telefono, Activo) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, tercero.getTipoTercero());
            ps.setString(2, tercero.getNombre());
            ps.setString(3, tercero.getNit());
            ps.setString(4, tercero.getNrc());
            ps.setString(5, tercero.getCorreo());
            ps.setString(6, tercero.getTelefono());
            ps.setBoolean(7, tercero.isActivo());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Error al crear el tercero: no se obtuvo ID.");
                }
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al crear el tercero: " + ex.getMessage(), ex);
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

    public boolean update(Tercero tercero) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE Terceros " +
                            "SET TipoTercero = ?, Nombre = ?, NIT = ?, NRC = ?, " +
                            "Correo = ?, Telefono = ?, Activo = ? " +
                            "WHERE TerceroId = ?"
            );

            ps.setString(1, tercero.getTipoTercero());
            ps.setString(2, tercero.getNombre());
            ps.setString(3, tercero.getNit());
            ps.setString(4, tercero.getNrc());
            ps.setString(5, tercero.getCorreo());
            ps.setString(6, tercero.getTelefono());
            ps.setBoolean(7, tercero.isActivo());
            ps.setInt(8, tercero.getTerceroId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el tercero: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public boolean delete(Tercero tercero) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM Terceros WHERE TerceroId = ?"
            );

            ps.setInt(1, tercero.getTerceroId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el tercero: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public ArrayList<Tercero> search(String texto) throws SQLException {
        ArrayList<Tercero> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT TerceroId, TipoTercero, Nombre, NIT, NRC, Correo, Telefono, Activo " +
                            "FROM Terceros " +
                            "WHERE TipoTercero LIKE ? " +
                            "OR Nombre LIKE ? " +
                            "OR NIT LIKE ? " +
                            "OR NRC LIKE ? " +
                            "OR Correo LIKE ? " +
                            "OR Telefono LIKE ?"
            );

            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");
            ps.setString(3, "%" + texto + "%");
            ps.setString(4, "%" + texto + "%");
            ps.setString(5, "%" + texto + "%");
            ps.setString(6, "%" + texto + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                Tercero tercero = new Tercero();

                tercero.setTerceroId(rs.getInt("TerceroId"));
                tercero.setTipoTercero(rs.getString("TipoTercero"));
                tercero.setNombre(rs.getString("Nombre"));
                tercero.setNit(rs.getString("NIT"));
                tercero.setNrc(rs.getString("NRC"));
                tercero.setCorreo(rs.getString("Correo"));
                tercero.setTelefono(rs.getString("Telefono"));
                tercero.setActivo(rs.getBoolean("Activo"));

                records.add(tercero);
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al buscar terceros: " + ex.getMessage(), ex);
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

    public Tercero getById(int terceroId) throws SQLException {
        Tercero tercero = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT TerceroId, TipoTercero, Nombre, NIT, NRC, Correo, Telefono, Activo " +
                            "FROM Terceros " +
                            "WHERE TerceroId = ?"
            );

            ps.setInt(1, terceroId);

            rs = ps.executeQuery();

            if (rs.next()) {
                tercero = new Tercero();

                tercero.setTerceroId(rs.getInt("TerceroId"));
                tercero.setTipoTercero(rs.getString("TipoTercero"));
                tercero.setNombre(rs.getString("Nombre"));
                tercero.setNit(rs.getString("NIT"));
                tercero.setNrc(rs.getString("NRC"));
                tercero.setCorreo(rs.getString("Correo"));
                tercero.setTelefono(rs.getString("Telefono"));
                tercero.setActivo(rs.getBoolean("Activo"));
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el tercero por id: " + ex.getMessage(), ex);
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

        return tercero;
    }
}