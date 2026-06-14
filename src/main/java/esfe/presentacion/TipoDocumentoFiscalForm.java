package esfe.presentacion;

import esfe.dominio.TipoDocumentoFiscal;
import esfe.persistencia.TipoDocumentoFiscalDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        pack();
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
}