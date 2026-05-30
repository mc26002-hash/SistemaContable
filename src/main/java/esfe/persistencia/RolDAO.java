package esfe.persistencia;

import esfe.dominio.Rol;

import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {


    private static final String SQL_SELECT = "SELECT id_rol, nombre_rol, descripcion FROM rol";
    private static final String SQL_INSERT = "INSERT INTO rol(nombre_rol, descripcion) VALUES(?, ?)";
    private static final String SQL_UPDATE = "UPDATE rol SET nombre_rol=?, descripcion=? WHERE id_rol=?";
    private static final String SQL_DELETE = "DELETE FROM rol WHERE id_rol=?";



    public List<Rol> listar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Rol rol = null;
        List<Rol> roles = new ArrayList<>();

        try {

            conn = ConnectionManager.getInstance().connect();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idRol = rs.getInt("id_rol");
                String nombreRol = rs.getString("nombre_rol");
                String descripcion = rs.getString("descripcion");

                rol = new Rol(idRol, nombreRol, descripcion);
                roles.add(rol);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {

            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        return roles;
    }


    public int insertar(Rol rol) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int registrosModificados = 0;

        try {
            conn = ConnectionManager.getInstance().connect();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, rol.getNombreRol());
            stmt.setString(2, rol.getDescripcion());

            registrosModificados = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        return registrosModificados;
    }


    public int actualizar(Rol rol) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int registrosModificados = 0;

        try {
            conn = ConnectionManager.getInstance().connect();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, rol.getNombreRol());
            stmt.setString(2, rol.getDescripcion());
            stmt.setInt(3, rol.getIdRol());

            registrosModificados = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        return registrosModificados;
    }


    public int eliminar(int idRol) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int registrosModificados = 0;

        try {
            conn = ConnectionManager.getInstance().connect();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, idRol);

            registrosModificados = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        return registrosModificados;
    }
}
