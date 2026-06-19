package esfe.presentacion.contabilidad;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import esfe.dominio.Tercero;
import esfe.persistencia.TerceroDAO;
import esfe.presentacion.MainForm;
import esfe.utils.WindowConfig;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TercerosForm extends JDialog {
    private JPanel mainPanel;
    private JPanel panelSuperior;
    private JPanel panelInsertar;
    private JPanel panelBotones;
    private JPanel panelDatos;
    private JTable table1;
    private JTextField txtNombre;
    private JTextField txtNIT;
    private JTextField txtNRC;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnBuscar;
    private JComboBox comboBoxTipo;
    private JCheckBox checkBoxActivo;

    private TerceroDAO terceroDAO;
    private Tercero registroSeleccionado;
    private boolean modoEdicion = false;

    public TercerosForm(MainForm mainForm) {
        terceroDAO = new TerceroDAO();

        setContentPane(mainPanel);
        setModal(true);
        setTitle("Terceros");
        WindowConfig.configurarVentana(this);
        setLocationRelativeTo(mainForm);

        cargarComboTipo();
        checkBoxActivo.setSelected(true);
        cargarTabla();

        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> cargarParaEditar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> buscar());
    }

    private void cargarComboTipo() {
        comboBoxTipo.removeAllItems();
        comboBoxTipo.addItem("Todos");
        comboBoxTipo.addItem("Cliente");
        comboBoxTipo.addItem("Proveedor");
    }

    private void guardar() {
        try {
            String tipoTercero = comboBoxTipo.getSelectedItem().toString();
            String nombre = txtNombre.getText().trim();
            String nit = txtNIT.getText().trim();
            String nrc = txtNRC.getText().trim();
            String correo = txtCorreo.getText().trim();
            String telefono = txtTelefono.getText().trim();
            boolean activo = checkBoxActivo.isSelected();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El nombre es obligatorio.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (modoEdicion && registroSeleccionado != null) {
                registroSeleccionado.setTipoTercero(tipoTercero);
                registroSeleccionado.setNombre(nombre);
                registroSeleccionado.setNit(nit);
                registroSeleccionado.setNrc(nrc);
                registroSeleccionado.setCorreo(correo);
                registroSeleccionado.setTelefono(telefono);
                registroSeleccionado.setActivo(activo);

                boolean res = terceroDAO.update(registroSeleccionado);

                if (res) {
                    JOptionPane.showMessageDialog(this,
                            "Registro actualizado correctamente.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                Tercero nuevo = new Tercero();
                nuevo.setTipoTercero(tipoTercero);
                nuevo.setNombre(nombre);
                nuevo.setNit(nit);
                nuevo.setNrc(nrc);
                nuevo.setCorreo(correo);
                nuevo.setTelefono(telefono);
                nuevo.setActivo(activo);

                Tercero creado = terceroDAO.create(nuevo);

                if (creado != null && creado.getTerceroId() > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Registro guardado correctamente.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }

            limpiarFormulario();
            cargarTabla();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarParaEditar() {
        Tercero seleccionado = obtenerSeleccionado();

        if (seleccionado == null) {
            return;
        }

        registroSeleccionado = seleccionado;
        modoEdicion = true;

        comboBoxTipo.setSelectedItem(seleccionado.getTipoTercero());
        txtNombre.setText(seleccionado.getNombre());
        txtNIT.setText(seleccionado.getNit());
        txtNRC.setText(seleccionado.getNrc());
        txtCorreo.setText(seleccionado.getCorreo());
        txtTelefono.setText(seleccionado.getTelefono());
        checkBoxActivo.setSelected(seleccionado.isActivo());

        btnGuardar.setText("Actualizar");
    }

    private void eliminar() {
        try {
            Tercero seleccionado = obtenerSeleccionado();

            if (seleccionado == null) {
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea eliminar este tercero?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            boolean res = terceroDAO.delete(seleccionado);

            if (res) {
                JOptionPane.showMessageDialog(this,
                        "Registro eliminado correctamente.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            limpiarFormulario();
            cargarTabla();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscar() {
        try {
            String textoBusqueda = "";

            if (!comboBoxTipo.getSelectedItem().toString().equals("Todos")) {
                textoBusqueda = comboBoxTipo.getSelectedItem().toString();
            } else if (!txtNombre.getText().trim().isEmpty()) {
                textoBusqueda = txtNombre.getText().trim();
            } else if (!txtNIT.getText().trim().isEmpty()) {
                textoBusqueda = txtNIT.getText().trim();
            } else if (!txtNRC.getText().trim().isEmpty()) {
                textoBusqueda = txtNRC.getText().trim();
            } else if (!txtCorreo.getText().trim().isEmpty()) {
                textoBusqueda = txtCorreo.getText().trim();
            } else if (!txtTelefono.getText().trim().isEmpty()) {
                textoBusqueda = txtTelefono.getText().trim();
            }

            ArrayList<Tercero> lista = terceroDAO.search(textoBusqueda);
            crearTabla(lista);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        try {
            ArrayList<Tercero> lista = terceroDAO.search("");
            crearTabla(lista);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearTabla(ArrayList<Tercero> lista) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Id");
        model.addColumn("Tipo");
        model.addColumn("Nombre");
        model.addColumn("NIT");
        model.addColumn("NRC");
        model.addColumn("Correo");
        model.addColumn("Teléfono");
        model.addColumn("Activo");

        table1.setModel(model);

        for (Tercero item : lista) {
            model.addRow(new Object[]{
                    item.getTerceroId(),
                    item.getTipoTercero(),
                    item.getNombre(),
                    item.getNit(),
                    item.getNrc(),
                    item.getCorreo(),
                    item.getTelefono(),
                    item.isActivo() ? "Sí" : "No"
            });
        }

        ocultarColumna(0);
    }

    private Tercero obtenerSeleccionado() {
        try {
            int fila = table1.getSelectedRow();

            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un registro de la tabla.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }

            int id = (int) table1.getValueAt(fila, 0);

            Tercero seleccionado = terceroDAO.getById(id);

            if (seleccionado == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el registro seleccionado.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
            }

            return seleccionado;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void limpiarFormulario() {
        comboBoxTipo.setSelectedIndex(0);
        txtNombre.setText("");
        txtNIT.setText("");
        txtNRC.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        checkBoxActivo.setSelected(true);

        registroSeleccionado = null;
        modoEdicion = false;
        btnGuardar.setText("Guardar");
        table1.clearSelection();
    }

    private void ocultarColumna(int columna) {
        table1.getColumnModel().getColumn(columna).setMaxWidth(0);
        table1.getColumnModel().getColumn(columna).setMinWidth(0);
        table1.getColumnModel().getColumn(columna).setPreferredWidth(0);
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
        mainPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelSuperior = new JPanel();
        panelSuperior.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panelSuperior, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelInsertar = new JPanel();
        panelInsertar.setLayout(new GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelSuperior.add(panelInsertar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelInsertar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Insertar", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Nombre");
        panelInsertar.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("NIT");
        panelInsertar.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("NRC");
        panelInsertar.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Correo");
        panelInsertar.add(label4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Teléfono");
        panelInsertar.add(label5, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNombre = new JTextField();
        panelInsertar.add(txtNombre, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtNIT = new JTextField();
        panelInsertar.add(txtNIT, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtNRC = new JTextField();
        panelInsertar.add(txtNRC, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtCorreo = new JTextField();
        panelInsertar.add(txtCorreo, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtTelefono = new JTextField();
        panelInsertar.add(txtTelefono, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Tipo");
        panelInsertar.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxTipo = new JComboBox();
        panelInsertar.add(comboBoxTipo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxActivo = new JCheckBox();
        checkBoxActivo.setText("CheckBox");
        panelInsertar.add(checkBoxActivo, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Activo");
        panelInsertar.add(label7, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelSuperior.add(panelBotones, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelBotones.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Botones", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        btnGuardar = new JButton();
        btnGuardar.setText("Guardar");
        panelBotones.add(btnGuardar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnEditar = new JButton();
        btnEditar.setText("Editar");
        panelBotones.add(btnEditar, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnEliminar = new JButton();
        btnEliminar.setText("Eliminar");
        panelBotones.add(btnEliminar, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnBuscar = new JButton();
        btnBuscar.setText("Buscar");
        panelBotones.add(btnBuscar, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelDatos = new JPanel();
        panelDatos.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panelDatos, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelDatos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Datos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelDatos.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        scrollPane1.setViewportView(table1);
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}