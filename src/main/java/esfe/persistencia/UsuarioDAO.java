package esfe.persistencia;

import esfe.dominio.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private final ConnectionManager connectionManager;

    // Constructor
    public UsuarioDAO() throws SQLException {
        connectionManager = ConnectionManager.getInstance();
    }

    // INSERTAR USUARIO
    public boolean insertar(Usuario usuario) {

        String sql = """
                INSERT INTO Usuarios
                (RolId, NombreUsuario, PasswordHash, NombreCompleto, CorreoElectronico, Activo)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = connectionManager.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, usuario.getRolId());
            stmt.setString(2, usuario.getNombreUsuario());
            stmt.setBytes(3, usuario.getPasswordHash());
            stmt.setString(4, usuario.getNombreCompleto());
            stmt.setString(5, usuario.getCorreoElectronico());
            stmt.setBoolean(6, usuario.isActivo());

            int filas = stmt.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    // LISTAR USUARIOS
    public List<Usuario> obtenerTodos() {

        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT * FROM Usuarios";

        try (
                Connection conn = connectionManager.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {

            while (rs.next()) {

                Usuario usuario = new Usuario();

                usuario.setUsuarioId(rs.getInt("UsuarioId"));
                usuario.setRolId(rs.getInt("RolId"));
                usuario.setNombreUsuario(rs.getString("NombreUsuario"));
                usuario.setPasswordHash(rs.getBytes("PasswordHash"));
                usuario.setNombreCompleto(rs.getString("NombreCompleto"));
                usuario.setCorreoElectronico(rs.getString("CorreoElectronico"));
                usuario.setActivo(rs.getBoolean("Activo"));

                lista.add(usuario);
            }

        } catch (SQLException e) {

            System.out.println("Error al obtener usuarios: " + e.getMessage());
        }

        return lista;
    }

    // BUSCAR USUARIO POR ID
    public Usuario obtenerPorId(int id) {

        String sql = "SELECT * FROM Usuarios WHERE UsuarioId = ?";

        try (
                Connection conn = connectionManager.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Usuario usuario = new Usuario();

                    usuario.setUsuarioId(rs.getInt("UsuarioId"));
                    usuario.setRolId(rs.getInt("RolId"));
                    usuario.setNombreUsuario(rs.getString("NombreUsuario"));
                    usuario.setPasswordHash(rs.getBytes("PasswordHash"));
                    usuario.setNombreCompleto(rs.getString("NombreCompleto"));
                    usuario.setCorreoElectronico(rs.getString("CorreoElectronico"));
                    usuario.setActivo(rs.getBoolean("Activo"));

                    return usuario;
                }
            }

        } catch (SQLException e) {

            System.out.println("Error al buscar usuario: " + e.getMessage());
        }

        return null;
    }
}