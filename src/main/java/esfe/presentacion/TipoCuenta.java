package esfe.presentacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class TipoCuenta extends JDialog {

    private JPanel mainPanel;
    private JPanel panelPrincipal;
    private JTable table1;
    private JButton nuevaCuentaButton;
    private JButton editarButton;
    private JButton deshabilitarButton;
    private JLabel lblTitulo2;
    private JButton buscarButton;

    // ─── CORREGIDO: Ahora se llama exactamente 'mostrarTodos' como en tu .form ───
    private JButton mostrarTodos;

    // Credenciales de tu servidor en Somee
    private final String url = "jdbc:sqlserver://ContabilidadESFE.mssql.somee.com:1433;databaseName=ContabilidadESFE;encrypt=true;trustServerCertificate=true;";
    private final String usuario = "ContabilidadGab_SQLLogin_1";
    private final String password = "a7l6kuot7x";

    public TipoCuenta(JFrame padre) {
        super(padre, true);
        setTitle("Catálogo de Cuentas Contables");
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(padre);

        // Carga inicial de datos al abrir
        llenarTabla();

        // 1. EVENTO: Nueva Cuenta
        nuevaCuentaButton.addActionListener(e -> {
            CrearCuentaForm pantallaCrear = new CrearCuentaForm(TipoCuenta.this);
            pantallaCrear.setVisible(true);
            llenarTabla();
        });

        // 2. EVENTO: Editar Cuenta
        editarButton.addActionListener(e -> {
            int filaSeleccionada = table1.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un registro de la tabla para editar.", "Ninguna selección", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = (int) table1.getValueAt(filaSeleccionada, 0);
            String nombreActual = (String) table1.getValueAt(filaSeleccionada, 1);

            String nuevoNombre = JOptionPane.showInputDialog(this, "Modificar nombre de la cuenta:", nombreActual);
            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                actualizarNombreEnSomee(id, nuevoNombre.trim());
                llenarTabla();
            }
        });

        // 3. EVENTO: Deshabilitar/Eliminar Cuenta
        deshabilitarButton.addActionListener(e -> {
            int filaSeleccionada = table1.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un registro para eliminar.", "Ninguna selección", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = (int) table1.getValueAt(filaSeleccionada, 0);
            String nombre = (String) table1.getValueAt(filaSeleccionada, 1);

            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea eliminar el tipo de cuenta '" + nombre + "'?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                eliminarRegistroEnSomee(id);
                llenarTabla();
            }
        });

        // 4. EVENTO: Buscar por ID
        buscarButton.addActionListener(e -> {
            String idBusqueda = JOptionPane.showInputDialog(this, "Ingrese el ID exacto que desea buscar:");

            if (idBusqueda == null) return;

            if (idBusqueda.trim().isEmpty()) {
                llenarTabla();
            } else {
                try {
                    int idInt = Integer.parseInt(idBusqueda.trim());
                    buscarPorIdEnSomee(idInt);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "Por favor, ingrese un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 5. EVENTO: ─── CORREGIDO CON TU NUEVO NOMBRE 'mostrarTodos' ───
        mostrarTodos.addActionListener(e -> {
            llenarTabla();
        });
    }

    // Carga completa de la tabla
    private void llenarTabla() {
        String[] columnas = {"Código (Id)", "Nombre de Tipo", "Naturaleza"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        String sql = "SELECT TipoCuentaId, NombreTipo, Naturaleza FROM TiposCuenta";

        try (Connection con = DriverManager.getConnection(url, usuario, password);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getInt("TipoCuentaId");
                fila[1] = rs.getString("NombreTipo");
                String nat = rs.getString("Naturaleza");
                fila[2] = (nat != null && nat.equalsIgnoreCase("H")) ? "Haber (Pasivo/Patr./Ing.)" : "Debe (Activo/Gasto)";
                modeloTabla.addRow(fila);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar desde Somee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        table1.setModel(modeloTabla);
    }

    // Filtrar búsqueda por ID
    private void buscarPorIdEnSomee(int idBuscado) {
        String[] columnas = {"Código (Id)", "Nombre de Tipo", "Naturaleza"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        String sql = "SELECT TipoCuentaId, NombreTipo, Naturaleza FROM TiposCuenta WHERE TipoCuentaId = ?";

        try (Connection con = DriverManager.getConnection(url, usuario, password);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idBuscado);
            ResultSet rs = ps.executeQuery();

            boolean seEncontro = false;
            while (rs.next()) {
                seEncontro = true;
                Object[] fila = new Object[3];
                fila[0] = rs.getInt("TipoCuentaId");
                fila[1] = rs.getString("NombreTipo");
                String nat = rs.getString("Naturaleza");
                fila[2] = (nat != null && nat.equalsIgnoreCase("H")) ? "Haber (Pasivo/Patr./Ing.)" : "Debe (Activo/Gasto)";
                modeloTabla.addRow(fila);
            }
            rs.close();

            if (!seEncontro) {
                JOptionPane.showMessageDialog(this, "No se encontró ningún tipo de cuenta con el ID: " + idBuscado, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                llenarTabla();
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al ejecutar la búsqueda: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        table1.setModel(modeloTabla);
    }

    private void actualizarNombreEnSomee(int id, String nuevoNombre) {
        String sql = "UPDATE TiposCuenta SET NombreTipo = ? WHERE TipoCuentaId = ?";
        try (Connection con = DriverManager.getConnection(url, usuario, password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRegistroEnSomee(int id) {
        String sql = "DELETE FROM TiposCuenta WHERE TipoCuentaId = ?";
        try (Connection con = DriverManager.getConnection(url, usuario, password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}