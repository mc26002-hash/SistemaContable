package esfe.persistencia;

import esfe.dominio.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UsuarioDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public UsuarioDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Usuario create(Usuario usuario) throws SQLException {
        Usuario res = null;
        int idGenerado = 0;

        try {
            ps = conn.connect().prepareStatement(
                    "INSERT INTO Usuarios " +
                            "(RolId, NombreUsuario, PasswordHash, NombreCompleto, CorreoElectronico, Activo) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setInt(1, usuario.getRolId());
            ps.setString(2, usuario.getNombreUsuario());
            ps.setBytes(3, usuario.getPasswordHash());
            ps.setString(4, usuario.getNombreCompleto());
            ps.setString(5, usuario.getCorreoElectronico());
            ps.setBoolean(6, usuario.isActivo());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                } else {
                    throw new SQLException("Error al crear el usuario: no se obtuvo ID.");
                }
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al crear el usuario: " + ex.getMessage(), ex);
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

    public boolean update(Usuario usuario) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE Usuarios " +
                            "SET RolId = ?, NombreUsuario = ?, NombreCompleto = ?, " +
                            "CorreoElectronico = ?, Activo = ? " +
                            "WHERE UsuarioId = ?"
            );

            ps.setInt(1, usuario.getRolId());
            ps.setString(2, usuario.getNombreUsuario());
            ps.setString(3, usuario.getNombreCompleto());
            ps.setString(4, usuario.getCorreoElectronico());
            ps.setBoolean(5, usuario.isActivo());
            ps.setInt(6, usuario.getUsuarioId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el usuario: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public boolean delete(Usuario usuario) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM Usuarios WHERE UsuarioId = ?"
            );

            ps.setInt(1, usuario.getUsuarioId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el usuario: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public ArrayList<Usuario> search(String texto) throws SQLException {
        ArrayList<Usuario> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT UsuarioId, RolId, NombreUsuario, PasswordHash, " +
                            "NombreCompleto, CorreoElectronico, Activo " +
                            "FROM Usuarios " +
                            "WHERE NombreUsuario LIKE ? " +
                            "OR NombreCompleto LIKE ? " +
                            "OR CorreoElectronico LIKE ?"
            );

            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");
            ps.setString(3, "%" + texto + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario();

                usuario.setUsuarioId(rs.getInt("UsuarioId"));
                usuario.setRolId(rs.getInt("RolId"));
                usuario.setNombreUsuario(rs.getString("NombreUsuario"));
                usuario.setPasswordHash(rs.getBytes("PasswordHash"));
                usuario.setNombreCompleto(rs.getString("NombreCompleto"));
                usuario.setCorreoElectronico(rs.getString("CorreoElectronico"));
                usuario.setActivo(rs.getBoolean("Activo"));

                records.add(usuario);
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al buscar usuarios: " + ex.getMessage(), ex);
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

    public Usuario getById(int usuarioId) throws SQLException {
        Usuario usuario = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT UsuarioId, RolId, NombreUsuario, PasswordHash, " +
                            "NombreCompleto, CorreoElectronico, Activo " +
                            "FROM Usuarios " +
                            "WHERE UsuarioId = ?"
            );

            ps.setInt(1, usuarioId);

            rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();

                usuario.setUsuarioId(rs.getInt("UsuarioId"));
                usuario.setRolId(rs.getInt("RolId"));
                usuario.setNombreUsuario(rs.getString("NombreUsuario"));
                usuario.setPasswordHash(rs.getBytes("PasswordHash"));
                usuario.setNombreCompleto(rs.getString("NombreCompleto"));
                usuario.setCorreoElectronico(rs.getString("CorreoElectronico"));
                usuario.setActivo(rs.getBoolean("Activo"));
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el usuario por id: " + ex.getMessage(), ex);
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

        return usuario;
    }

    public Usuario authenticate(Usuario usuario) throws SQLException {
        Usuario usuarioAutenticado = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT UsuarioId, RolId, NombreUsuario, PasswordHash, " +
                            "NombreCompleto, CorreoElectronico, Activo " +
                            "FROM Usuarios " +
                            "WHERE CorreoElectronico = ? " +
                            "AND PasswordHash = ? " +
                            "AND Activo = 1"
            );

            ps.setString(1, usuario.getCorreoElectronico());
            ps.setBytes(2, usuario.getPasswordHash());

            rs = ps.executeQuery();

            if (rs.next()) {
                usuarioAutenticado = new Usuario();

                usuarioAutenticado.setUsuarioId(rs.getInt("UsuarioId"));
                usuarioAutenticado.setRolId(rs.getInt("RolId"));
                usuarioAutenticado.setNombreUsuario(rs.getString("NombreUsuario"));
                usuarioAutenticado.setPasswordHash(rs.getBytes("PasswordHash"));
                usuarioAutenticado.setNombreCompleto(rs.getString("NombreCompleto"));
                usuarioAutenticado.setCorreoElectronico(rs.getString("CorreoElectronico"));
                usuarioAutenticado.setActivo(rs.getBoolean("Activo"));
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al autenticar el usuario: " + ex.getMessage(), ex);
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

        return usuarioAutenticado;
    }

    public boolean updatePassword(Usuario usuario) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE Usuarios " +
                            "SET PasswordHash = ? " +
                            "WHERE UsuarioId = ?"
            );

            ps.setBytes(1, usuario.getPasswordHash());
            ps.setInt(2, usuario.getUsuarioId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el password del usuario: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }
}
