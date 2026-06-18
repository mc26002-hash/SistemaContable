package esfe.presentacion.contabilidad;

import esfe.dominio.DocumentoFiscal;
import esfe.dominio.Tercero;
import esfe.dominio.TipoDocumentoFiscal;
import esfe.persistencia.DocumentoFiscalDAO;
import esfe.persistencia.TerceroDAO;
import esfe.persistencia.TipoDocumentoFiscalDAO;
import esfe.presentacion.MainForm;
import esfe.utils.WindowConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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