package esfe.persistencia;

import esfe.dominio.TipoPartida;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TipoPartidaDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public TipoPartidaDAO() {
        conn = ConnectionManager.getInstance();
    }

    public TipoPartida create(TipoPartida tipoPartida) throws SQLException {
        TipoPartida res = null;

        try {
            ps = conn.connect().prepareStatement(
                    "INSERT INTO TiposPartida (NombreTipo) " +
                            "VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, tipoPartida.getNombreTipo());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Error al crear el tipo de partida: no se obtuvo ID.");
                }
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al crear el tipo de partida: " + ex.getMessage(), ex);
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

    public boolean update(TipoPartida tipoPartida) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE TiposPartida " +
                            "SET NombreTipo = ? " +
                            "WHERE TipoPartidaId = ?"
            );

            ps.setString(1, tipoPartida.getNombreTipo());
            ps.setInt(2, tipoPartida.getTipoPartidaId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el tipo de partida: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public boolean delete(TipoPartida tipoPartida) throws SQLException {
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM TiposPartida WHERE TipoPartidaId = ?"
            );

            ps.setInt(1, tipoPartida.getTipoPartidaId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el tipo de partida: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                ps.close();
            }

            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public ArrayList<TipoPartida> search(String nombreTipo) throws SQLException {
        ArrayList<TipoPartida> records = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT TipoPartidaId, NombreTipo " +
                            "FROM TiposPartida " +
                            "WHERE NombreTipo LIKE ?"
            );

            ps.setString(1, "%" + nombreTipo + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                TipoPartida tipoPartida = new TipoPartida();

                tipoPartida.setTipoPartidaId(rs.getInt("TipoPartidaId"));
                tipoPartida.setNombreTipo(rs.getString("NombreTipo"));

                records.add(tipoPartida);
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al buscar tipos de partida: " + ex.getMessage(), ex);
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

    public TipoPartida getById(int tipoPartidaId) throws SQLException {
        TipoPartida tipoPartida = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT TipoPartidaId, NombreTipo " +
                            "FROM TiposPartida " +
                            "WHERE TipoPartidaId = ?"
            );

            ps.setInt(1, tipoPartidaId);

            rs = ps.executeQuery();

            if (rs.next()) {
                tipoPartida = new TipoPartida();

                tipoPartida.setTipoPartidaId(rs.getInt("TipoPartidaId"));
                tipoPartida.setNombreTipo(rs.getString("NombreTipo"));
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el tipo de partida por id: " + ex.getMessage(), ex);
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

        return tipoPartida;
    }
}