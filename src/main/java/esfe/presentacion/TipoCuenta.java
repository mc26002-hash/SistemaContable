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
import java.util.ArrayList;


public class TipoCuenta extends JDialog {

    private JPanel mainPanel;
    private JPanel panelPrincipal;
    private JTable table1;
    private JButton nuevaCuentaButton;
    private JButton editarButton;
    private JButton deshabilitarButton;
    private JLabel lblTitulo2;
    private JButton buscarButton;
    private JButton mostrarTodos;

    private ArrayList<Integer> listaIdsInternos = new ArrayList<>();

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

            int id = listaIdsInternos.get(filaSeleccionada);
            String nombreActual = (String) table1.getValueAt(filaSeleccionada, 0);

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

            int id = listaIdsInternos.get(filaSeleccionada);
            String nombre = (String) table1.getValueAt(filaSeleccionada, 0);

            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea eliminar el tipo de cuenta '" + nombre + "'?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                eliminarRegistroEnSomee(id);
                llenarTabla();
            }
        });

        // 4. EVENTO: Buscar por Nombre (¡MODIFICADO!)
        buscarButton.addActionListener(e -> {
            String textoBusqueda = JOptionPane.showInputDialog(this, "Ingrese el nombre (o la letra inicial) a buscar:");

            if (textoBusqueda == null) return; // Si cancela, no hace nada

            if (textoBusqueda.trim().isEmpty()) {
                llenarTabla(); // Si lo deja vacío, muestra todos
            } else {
                buscarPorNombreEnSomee(textoBusqueda.trim()); // Llama al nuevo buscador por texto
            }
        });

        // 5. EVENTO: Mostrar Todos
        mostrarTodos.addActionListener(e -> {
            llenarTabla();
        });
    }

    // Carga completa de la tabla
    private void llenarTabla() {
        String[] columnas = {"Nombre de Tipo", "Naturaleza"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);

        listaIdsInternos.clear();
        String sql = "SELECT TipoCuentaId, NombreTipo, Naturaleza FROM TiposCuenta";

        try (Connection con = DriverManager.getConnection(url, usuario, password);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                listaIdsInternos.add(rs.getInt("TipoCuentaId"));

                Object[] fila = new Object[2];
                fila[0] = rs.getString("NombreTipo");
                String nat = rs.getString("Naturaleza");
                fila[1] = (nat != null && nat.equalsIgnoreCase("H")) ? "Haber (Pasivo/Patr./Ing.)" : "Debe (Activo/Gasto)";
                modeloTabla.addRow(fila);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar desde Somee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        table1.setModel(modeloTabla);
    }

    // ─── LÓGICA DE BÚSQUEDA POR TEXTO (LIKE ?) ───
    private void buscarPorNombreEnSomee(String textoBuscado) {
        String[] columnas = {"Nombre de Tipo", "Naturaleza"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);

        listaIdsInternos.clear();

        // Usamos LIKE con el comodín al final para buscar lo que EMPIECE con ese texto
        String sql = "SELECT TipoCuentaId, NombreTipo, Naturaleza FROM TiposCuenta WHERE NombreTipo LIKE ?";

        try (Connection con = DriverManager.getConnection(url, usuario, password);
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Configuramos el parámetro agregándole el '%' al final. Ejemplo: "Act%"
            ps.setString(1, textoBuscado + "%");
            ResultSet rs = ps.executeQuery();

            boolean seEncontro = false;
            while (rs.next()) {
                seEncontro = true;
                listaIdsInternos.add(rs.getInt("TipoCuentaId")); // Guardamos su ID oculto para que Editar/Borrar sigan funcionando

                Object[] fila = new Object[2];
                fila[0] = rs.getString("NombreTipo");
                String nat = rs.getString("Naturaleza");
                fila[1] = (nat != null && nat.equalsIgnoreCase("H")) ? "Haber (Pasivo/Patr./Ing.)" : "Debe (Activo/Gasto)";
                modeloTabla.addRow(fila);
            }
            rs.close();

            if (!seEncontro) {
                JOptionPane.showMessageDialog(this, "No se encontró ningún tipo de cuenta que empiece con: " + textoBuscado, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
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