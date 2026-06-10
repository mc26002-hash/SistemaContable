package esfe.persistencia;

import esfe.dominio.Rol;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RolDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public RolDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Rol create(Rol rol) throws SQLException {
        Rol res = null;

        try {
            ps = conn.connect().prepareStatement(
                    "INSERT INTO Roles (NombreRol, Descripcion, Activo) " +
                            "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, rol.getNombreRol());
            ps.setString(2, rol.getDescripcion());
            ps.setBoolean(3, rol.getActivo());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Error al crear el rol: no se obtuvo ID.");
                }
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al crear el rol: " + ex.getMessage(), ex);
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

    public boolean update(Rol rol) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE Roles " +
                            "SET NombreRol = ?, Descripcion = ?, Activo = ? " +
                            "WHERE RolId = ?"
            );

            ps.setString(1, rol.getNombreRol());
            ps.setString(2, rol.getDescripcion());
            ps.setBoolean(3, rol.getActivo());
            ps.setInt(4, rol.getRolId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el rol: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public boolean delete(Rol rol) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM Roles WHERE RolId = ?"
            );

            ps.setInt(1, rol.getRolId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el rol: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public ArrayList<Rol> search(String nombreRol) throws SQLException {
        ArrayList<Rol> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT RolId, NombreRol, Descripcion, Activo " +
                            "FROM Roles " +
                            "WHERE NombreRol LIKE ?"
            );

            ps.setString(1, "%" + nombreRol + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                Rol rol = new Rol();

                rol.setRolId(rs.getInt("RolId"));
                rol.setNombreRol(rs.getString("NombreRol"));
                rol.setDescripcion(rs.getString("Descripcion"));
                rol.setActivo(rs.getBoolean("Activo"));

                records.add(rol);
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al buscar roles: " + ex.getMessage(), ex);
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

    public Rol getById(int rolId) throws SQLException {
        Rol rol = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT RolId, NombreRol, Descripcion, Activo " +
                            "FROM Roles " +
                            "WHERE RolId = ?"
            );

            ps.setInt(1, rolId);

            rs = ps.executeQuery();

            if (rs.next()) {
                rol = new Rol();

                rol.setRolId(rs.getInt("RolId"));
                rol.setNombreRol(rs.getString("NombreRol"));
                rol.setDescripcion(rs.getString("Descripcion"));
                rol.setActivo(rs.getBoolean("Activo"));
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el rol por id: " + ex.getMessage(), ex);
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

        return rol;
    }
}