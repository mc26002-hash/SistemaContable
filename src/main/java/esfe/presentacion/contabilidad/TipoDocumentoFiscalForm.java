package esfe.presentacion.contabilidad;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import esfe.dominio.TipoDocumentoFiscal;
import esfe.persistencia.TipoDocumentoFiscalDAO;
import esfe.presentacion.MainForm;
import esfe.utils.WindowConfig;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TipoDocumentoFiscalForm extends JDialog {

    private JPanel mainPanel;
    private JPanel panelSuperior;
    private JPanel panelInsertar;
    private JPanel panelBotones;
    private JPanel panelDatos;
    private JTable tablaTiposDocumento;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnBuscar;

    private TipoDocumentoFiscalDAO tipoDocumentoFiscalDAO;
    private TipoDocumentoFiscal registroSeleccionado;
    private boolean modoEdicion = false;

    public TipoDocumentoFiscalForm(MainForm mainForm) {
        tipoDocumentoFiscalDAO = new TipoDocumentoFiscalDAO();

        setContentPane(mainPanel);
        setModal(true);
        setTitle("Tipos de Documento Fiscal");
        WindowConfig.configurarVentana(this);
        setLocationRelativeTo(mainForm);

        cargarTabla();

        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> cargarParaEditar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> buscar());
    }

    private void guardar() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();

            if (codigo.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Código y nombre son obligatorios.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (modoEdicion && registroSeleccionado != null) {
                registroSeleccionado.setCodigo(codigo);
                registroSeleccionado.setNombre(nombre);

                boolean res = tipoDocumentoFiscalDAO.update(registroSeleccionado);

                if (res) {
                    JOptionPane.showMessageDialog(this,
                            "Registro actualizado correctamente.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                TipoDocumentoFiscal nuevo = new TipoDocumentoFiscal();
                nuevo.setCodigo(codigo);
                nuevo.setNombre(nombre);

                TipoDocumentoFiscal creado = tipoDocumentoFiscalDAO.create(nuevo);

                if (creado != null && creado.getTipoDocumentoFiscalId() > 0) {
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
        TipoDocumentoFiscal seleccionado = obtenerSeleccionado();

        if (seleccionado == null) {
            return;
        }

        registroSeleccionado = seleccionado;
        modoEdicion = true;

        txtCodigo.setText(seleccionado.getCodigo());
        txtNombre.setText(seleccionado.getNombre());

        btnGuardar.setText("Actualizar");
    }

    private void eliminar() {
        try {
            TipoDocumentoFiscal seleccionado = obtenerSeleccionado();

            if (seleccionado == null) {
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea eliminar este tipo de documento fiscal?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            boolean res = tipoDocumentoFiscalDAO.delete(seleccionado);

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
            String textoBusqueda = (txtCodigo.getText() + " " + txtNombre.getText()).trim();

            ArrayList<TipoDocumentoFiscal> lista = tipoDocumentoFiscalDAO.search(textoBusqueda);

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
            ArrayList<TipoDocumentoFiscal> lista = tipoDocumentoFiscalDAO.search("");
            crearTabla(lista);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearTabla(ArrayList<TipoDocumentoFiscal> lista) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Id");
        model.addColumn("Código");
        model.addColumn("Nombre");

        tablaTiposDocumento.setModel(model);

        for (TipoDocumentoFiscal item : lista) {
            model.addRow(new Object[]{
                    item.getTipoDocumentoFiscalId(),
                    item.getCodigo(),
                    item.getNombre()
            });
        }

        ocultarColumna(0);
    }

    private TipoDocumentoFiscal obtenerSeleccionado() {
        try {
            int fila = tablaTiposDocumento.getSelectedRow();

            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un registro de la tabla.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }

            int id = (int) tablaTiposDocumento.getValueAt(fila, 0);

            TipoDocumentoFiscal seleccionado = tipoDocumentoFiscalDAO.getById(id);

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
        txtCodigo.setText("");
        txtNombre.setText("");
        registroSeleccionado = null;
        modoEdicion = false;
        btnGuardar.setText("Guardar");
        tablaTiposDocumento.clearSelection();
    }

    private void ocultarColumna(int columna) {
        tablaTiposDocumento.getColumnModel().getColumn(columna).setMaxWidth(0);
        tablaTiposDocumento.getColumnModel().getColumn(columna).setMinWidth(0);
        tablaTiposDocumento.getColumnModel().getColumn(columna).setPreferredWidth(0);
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
        mainPanel.add(panelSuperior, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        panelInsertar = new JPanel();
        panelInsertar.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelSuperior.add(panelInsertar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelInsertar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Insertar", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Código");
        panelInsertar.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Nombre");
        panelInsertar.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtCodigo = new JTextField();
        panelInsertar.add(txtCodigo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtNombre = new JTextField();
        panelInsertar.add(txtNombre, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
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
        mainPanel.add(panelDatos, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panelDatos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Datos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelDatos.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tablaTiposDocumento = new JTable();
        tablaTiposDocumento.setToolTipText("");
        scrollPane1.setViewportView(tablaTiposDocumento);
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