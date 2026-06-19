package esfe.presentacion.contabilidad;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import esfe.dominio.DocumentoFiscal;
import esfe.dominio.Tercero;
import esfe.dominio.TipoDocumentoFiscal;
import esfe.persistencia.DocumentoFiscalDAO;
import esfe.persistencia.TerceroDAO;
import esfe.persistencia.TipoDocumentoFiscalDAO;
import esfe.presentacion.MainForm;
import esfe.utils.WindowConfig;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

public class DocumentoFiscalForm extends JDialog {
    private JPanel mainPanel;
    private JTable tableDatos;
    private JPanel panelSuperior;
    private JPanel panelInferior;
    private JTextField txtNumeroDocumento;
    private JTextField txtFechaDocumento;
    private JCheckBox checkBoxRetencion;
    private JTextField txtTotal;
    private JTextField txtIVA;
    private JTextField txtMontoGravado;
    private JTextField txtMontoExento;
    private JComboBox comboBoxTercero;
    private JComboBox comboBoxTipoDocumento;
    private JComboBox comboBoxTipoLibro;
    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnBuscar;
    private JTextField txtPartidaId;

    private DocumentoFiscalDAO documentoFiscalDAO;
    private TerceroDAO terceroDAO;
    private TipoDocumentoFiscalDAO tipoDocumentoFiscalDAO;

    private DocumentoFiscal registroSeleccionado;
    private boolean modoEdicion = false;

    public DocumentoFiscalForm(MainForm mainForm) {
        documentoFiscalDAO = new DocumentoFiscalDAO();
        terceroDAO = new TerceroDAO();
        tipoDocumentoFiscalDAO = new TipoDocumentoFiscalDAO();

        setContentPane(mainPanel);
        setModal(true);
        setTitle("Documentos Fiscales");
        WindowConfig.configurarVentana(this);
        setLocationRelativeTo(mainForm);

        cargarCombos();
        cargarTabla();

        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> cargarParaEditar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> buscar());
    }

    private void cargarCombos() {
        try {
            comboBoxTercero.removeAllItems();
            ArrayList<Tercero> terceros = terceroDAO.search("");

            for (Tercero t : terceros) {
                comboBoxTercero.addItem(new ComboItem(t.getTerceroId(), t.getNombre()));
            }

            comboBoxTipoDocumento.removeAllItems();
            ArrayList<TipoDocumentoFiscal> tipos = tipoDocumentoFiscalDAO.search("");

            for (TipoDocumentoFiscal t : tipos) {
                comboBoxTipoDocumento.addItem(new ComboItem(t.getTipoDocumentoFiscalId(), t.getNombre()));
            }

            comboBoxTipoLibro.removeAllItems();
            comboBoxTipoLibro.addItem("");
            comboBoxTipoLibro.addItem("Compra");
            comboBoxTipoLibro.addItem("Venta");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error al cargar combos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        try {
            if (comboBoxTercero.getSelectedItem() == null || comboBoxTipoDocumento.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar tercero y tipo de documento.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String numeroDocumento = txtNumeroDocumento.getText().trim();
            String fechaTexto = txtFechaDocumento.getText().trim();

            if (numeroDocumento.isEmpty() || fechaTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Número y fecha del documento son obligatorios.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            ComboItem tercero = (ComboItem) comboBoxTercero.getSelectedItem();
            ComboItem tipoDocumento = (ComboItem) comboBoxTipoDocumento.getSelectedItem();

            DocumentoFiscal documento = modoEdicion && registroSeleccionado != null
                    ? registroSeleccionado
                    : new DocumentoFiscal();

            documento.setTerceroId(tercero.getId());
            documento.setTipoDocumentoFiscalId(tipoDocumento.getId());
            documento.setTipoLibro(comboBoxTipoLibro.getSelectedItem().toString());
            documento.setNumeroDocumento(numeroDocumento);
            documento.setFechaDocumento(Date.valueOf(fechaTexto));

            documento.setMontoExento(new BigDecimal(txtMontoExento.getText().trim()));
            documento.setMontoGravado(new BigDecimal(txtMontoGravado.getText().trim()));
            documento.setIva(new BigDecimal(txtIVA.getText().trim()));
            documento.setTotal(new BigDecimal(txtTotal.getText().trim()));
            documento.setAplicaRetencion(checkBoxRetencion.isSelected());

            if (txtPartidaId.getText().trim().isEmpty()) {
                documento.setPartidaId(null);
            } else {
                documento.setPartidaId(Integer.parseInt(txtPartidaId.getText().trim()));
            }

            if (modoEdicion) {
                boolean res = documentoFiscalDAO.update(documento);

                if (res) {
                    JOptionPane.showMessageDialog(this, "Registro actualizado correctamente.");
                }
            } else {
                DocumentoFiscal creado = documentoFiscalDAO.create(documento);

                if (creado != null && creado.getDocumentoFiscalId() > 0) {
                    JOptionPane.showMessageDialog(this, "Registro guardado correctamente.");
                }
            }

            limpiarFormulario();
            cargarTabla();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Revise los datos. Fecha debe ser yyyy-mm-dd y montos numéricos.\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarParaEditar() {
        DocumentoFiscal seleccionado = obtenerSeleccionado();

        if (seleccionado == null) {
            return;
        }

        registroSeleccionado = seleccionado;
        modoEdicion = true;

        seleccionarComboPorId(comboBoxTercero, seleccionado.getTerceroId());
        seleccionarComboPorId(comboBoxTipoDocumento, seleccionado.getTipoDocumentoFiscalId());
        comboBoxTipoLibro.setSelectedItem(seleccionado.getTipoLibro());

        txtNumeroDocumento.setText(seleccionado.getNumeroDocumento());
        txtFechaDocumento.setText(seleccionado.getFechaDocumento().toString());
        txtMontoExento.setText(seleccionado.getMontoExento().toString());
        txtMontoGravado.setText(seleccionado.getMontoGravado().toString());
        txtIVA.setText(seleccionado.getIva().toString());
        txtTotal.setText(seleccionado.getTotal().toString());
        checkBoxRetencion.setSelected(seleccionado.isAplicaRetencion());

        txtPartidaId.setText(seleccionado.getPartidaId() == null ? "" : seleccionado.getPartidaId().toString());

        btnGuardar.setText("Actualizar");
    }

    private void eliminar() {
        try {
            DocumentoFiscal seleccionado = obtenerSeleccionado();

            if (seleccionado == null) {
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea eliminar este documento fiscal?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            boolean res = documentoFiscalDAO.delete(seleccionado);

            if (res) {
                JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.");
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
            String textoBusqueda = (
                    txtNumeroDocumento.getText() + " " +
                            comboBoxTipoLibro.getSelectedItem().toString()
            ).trim();

            ArrayList<DocumentoFiscal> lista = documentoFiscalDAO.search(textoBusqueda);
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
            ArrayList<DocumentoFiscal> lista = documentoFiscalDAO.search("");
            crearTabla(lista);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearTabla(ArrayList<DocumentoFiscal> lista) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Id");
        model.addColumn("TerceroId");
        model.addColumn("TipoDocId");
        model.addColumn("Tipo Libro");
        model.addColumn("Número");
        model.addColumn("Fecha");
        model.addColumn("Exento");
        model.addColumn("Gravado");
        model.addColumn("IVA");
        model.addColumn("Total");
        model.addColumn("Retención");

        tableDatos.setModel(model);

        for (DocumentoFiscal item : lista) {
            model.addRow(new Object[]{
                    item.getDocumentoFiscalId(),
                    item.getTerceroId(),
                    item.getTipoDocumentoFiscalId(),
                    item.getTipoLibro(),
                    item.getNumeroDocumento(),
                    item.getFechaDocumento(),
                    item.getMontoExento(),
                    item.getMontoGravado(),
                    item.getIva(),
                    item.getTotal(),
                    item.isAplicaRetencion() ? "Sí" : "No"
            });
        }

        ocultarColumna(0);
    }

    private DocumentoFiscal obtenerSeleccionado() {
        try {
            int fila = tableDatos.getSelectedRow();

            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un registro de la tabla.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }

            int id = (int) tableDatos.getValueAt(fila, 0);
            return documentoFiscalDAO.getById(id);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void limpiarFormulario() {
        if (comboBoxTercero.getItemCount() > 0) {
            comboBoxTercero.setSelectedIndex(0);
        }

        if (comboBoxTipoDocumento.getItemCount() > 0) {
            comboBoxTipoDocumento.setSelectedIndex(0);
        }

        comboBoxTipoLibro.setSelectedIndex(0);
        txtNumeroDocumento.setText("");
        txtFechaDocumento.setText("");
        txtMontoExento.setText("0.00");
        txtMontoGravado.setText("0.00");
        txtIVA.setText("0.00");
        txtTotal.setText("0.00");
        txtPartidaId.setText("");
        checkBoxRetencion.setSelected(false);

        registroSeleccionado = null;
        modoEdicion = false;
        btnGuardar.setText("Guardar");
        tableDatos.clearSelection();
    }

    private void ocultarColumna(int columna) {
        tableDatos.getColumnModel().getColumn(columna).setMaxWidth(0);
        tableDatos.getColumnModel().getColumn(columna).setMinWidth(0);
        tableDatos.getColumnModel().getColumn(columna).setPreferredWidth(0);
    }

    private void seleccionarComboPorId(JComboBox comboBox, int id) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            ComboItem item = (ComboItem) comboBox.getItemAt(i);

            if (item.getId() == id) {
                comboBox.setSelectedIndex(i);
                return;
            }
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
        mainPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelSuperior = new JPanel();
        panelSuperior.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panelSuperior, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelSuperior.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Documento Fiscal", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Tercero");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Tipo Documento");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Tipo Libro");
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Número Documento");
        panel1.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Fecha Documento");
        panel1.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNumeroDocumento = new JTextField();
        panel1.add(txtNumeroDocumento, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtFechaDocumento = new JTextField();
        panel1.add(txtFechaDocumento, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        comboBoxTercero = new JComboBox();
        panel1.add(comboBoxTercero, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxTipoDocumento = new JComboBox();
        panel1.add(comboBoxTipoDocumento, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxTipoLibro = new JComboBox();
        panel1.add(comboBoxTipoLibro, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelSuperior.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Montos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label6 = new JLabel();
        label6.setText("Monto Exento");
        panel2.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Monto Gravado");
        panel2.add(label7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("IVA");
        panel2.add(label8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Total");
        panel2.add(label9, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Aplica Retención");
        panel2.add(label10, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxRetencion = new JCheckBox();
        checkBoxRetencion.setText("CheckBox");
        panel2.add(checkBoxRetencion, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtTotal = new JTextField();
        panel2.add(txtTotal, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtIVA = new JTextField();
        panel2.add(txtIVA, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtMontoGravado = new JTextField();
        panel2.add(txtMontoGravado, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtMontoExento = new JTextField();
        panel2.add(txtMontoExento, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelSuperior.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Partida Contable", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label11 = new JLabel();
        label11.setText("Partida ID");
        panel3.add(label11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtPartidaId = new JTextField();
        panel3.add(txtPartidaId, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelSuperior.add(panel4, new GridConstraints(0, 1, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Acciones", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        btnGuardar = new JButton();
        btnGuardar.setText("Guardar");
        panel4.add(btnGuardar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnEditar = new JButton();
        btnEditar.setText("Editar");
        panel4.add(btnEditar, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnEliminar = new JButton();
        btnEliminar.setText("Eliminar");
        panel4.add(btnEliminar, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnBuscar = new JButton();
        btnBuscar.setText("Buscar");
        panel4.add(btnBuscar, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelInferior = new JPanel();
        panelInferior.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panelInferior, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelInferior.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Datos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelInferior.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableDatos = new JTable();
        scrollPane1.setViewportView(tableDatos);
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    private static class ComboItem {
        private int id;
        private String texto;

        public ComboItem(int id, String texto) {
            this.id = id;
            this.texto = texto;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return texto;
        }
    }
}