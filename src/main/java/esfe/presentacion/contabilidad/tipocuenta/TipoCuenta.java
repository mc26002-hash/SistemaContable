package esfe.presentacion.contabilidad.tipocuenta;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import esfe.utils.WindowConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        WindowConfig.configurarVentana(this);
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setForeground(new Color(-13487306));
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayoutManager(5, 6, new Insets(0, 0, 0, 0), -1, -1));
        panelPrincipal.setForeground(new Color(-13291982));
        mainPanel.add(panelPrincipal, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setForeground(new Color(-6316129));
        panelPrincipal.add(scrollPane1, new GridConstraints(4, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        scrollPane1.setViewportView(table1);
        editarButton = new JButton();
        editarButton.setForeground(new Color(-16777216));
        editarButton.setText("Editar");
        panelPrincipal.add(editarButton, new GridConstraints(2, 2, 2, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(90, 30), 0, false));
        deshabilitarButton = new JButton();
        deshabilitarButton.setForeground(new Color(-16777216));
        deshabilitarButton.setText("Eliminar");
        panelPrincipal.add(deshabilitarButton, new GridConstraints(2, 4, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(120, 30), 0, false));
        nuevaCuentaButton = new JButton();
        nuevaCuentaButton.setForeground(new Color(-16777216));
        nuevaCuentaButton.setText("Nueva Cuenta");
        panelPrincipal.add(nuevaCuentaButton, new GridConstraints(2, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(120, 30), 0, false));
        lblTitulo2 = new JLabel();
        lblTitulo2.setText("Catalogo de Cuentas Contables");
        panelPrincipal.add(lblTitulo2, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(200, 200), 0, false));
        buscarButton = new JButton();
        buscarButton.setText("Buscar");
        panelPrincipal.add(buscarButton, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mostrarTodos = new JButton();
        mostrarTodos.setText("mostrarTodos");
        panelPrincipal.add(mostrarTodos, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panelPrincipal.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}