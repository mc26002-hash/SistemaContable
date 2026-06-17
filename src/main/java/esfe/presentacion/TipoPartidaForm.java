
package esfe.presentacion;

import esfe.dominio.TipoPartida;
import esfe.persistencia.TipoPartidaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class TipoPartidaForm extends JDialog {

    private JPanel panelprincipal;
    private JTable tablaTiposPartida;
    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnBuscar;
    private JTextField txtNombre;
    private JPanel panelAgregar;
    private JPanel panelBotones;
    private JPanel panelDatos;

    private TipoPartidaDAO tipoPartidaDAO;
    private TipoPartida registroSeleccionado;
    private boolean modoEdicion = false;

    public TipoPartidaForm(TipoPartidaForm tipoPartidaForm) {
        tipoPartidaDAO = new TipoPartidaDAO();

        setContentPane(panelprincipal);
        setModal(true);
        setTitle("Tipos de Partida");
        pack();
        setLocationRelativeTo(tipoPartidaForm);

        cargarTabla();

        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> cargarParaEditar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> buscar());
    }

    private void guardar() {
        try {
            String nombre = txtNombre.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El nombre es obligatorio.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (modoEdicion && registroSeleccionado != null) {
                registroSeleccionado.setNombreTipo(nombre);

                boolean res = tipoPartidaDAO.update(registroSeleccionado);

                if (res) {
                    JOptionPane.showMessageDialog(this,
                            "Registro actualizado correctamente.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                TipoPartida nuevo = new TipoPartida();
                nuevo.setNombreTipo(nombre);

                TipoPartida creado = tipoPartidaDAO.create(nuevo);

                if (creado != null && creado.getTipoPartidaId() > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Registro guardado correctamente.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }

            limpiarFormulario();
            cargarTabla();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void cargarParaEditar() {
        TipoPartida seleccionado = obtenerSeleccionado();

        if (seleccionado == null) {
            return;
        }

        registroSeleccionado = seleccionado;
        modoEdicion = true;

        txtNombre.setText(seleccionado.getNombreTipo());
        btnGuardar.setText("Actualizar");
    }

    private void eliminar() {
        try {
            TipoPartida seleccionado = obtenerSeleccionado();

            if (seleccionado == null) {
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea eliminar este tipo de partida?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            boolean res = tipoPartidaDAO.delete(seleccionado);

            if (res) {
                JOptionPane.showMessageDialog(this,
                        "Registro eliminado correctamente.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            limpiarFormulario();
            cargarTabla();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void buscar() {
        try {
            String textoBusqueda = txtNombre.getText().trim();
            ArrayList<TipoPartida> lista = tipoPartidaDAO.search(textoBusqueda);
            crearTabla(lista);

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void cargarTabla() {
        try {
            ArrayList<TipoPartida> lista = tipoPartidaDAO.search("");
            crearTabla(lista);

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void crearTabla(ArrayList<TipoPartida> lista) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Id");
        model.addColumn("Nombre");

        tablaTiposPartida.setModel(model);

        for (TipoPartida item : lista) {
            model.addRow(new Object[]{
                    item.getTipoPartidaId(),
                    item.getNombreTipo()
            });
        }

        ocultarColumna(0);
    }

    private TipoPartida obtenerSeleccionado() {
        try {
            int fila = tablaTiposPartida.getSelectedRow();

            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un registro de la tabla.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }

            int id = (int) tablaTiposPartida.getValueAt(fila, 0);
            TipoPartida seleccionado = tipoPartidaDAO.getById(id);

            if (seleccionado == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el registro seleccionado.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
            }

            return seleccionado;

        } catch (Exception ex) {
            showError(ex.getMessage());
            return null;
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        registroSeleccionado = null;
        modoEdicion = false;
        btnGuardar.setText("Guardar");
        tablaTiposPartida.clearSelection();
    }

    private void ocultarColumna(int columna) {
        tablaTiposPartida.getColumnModel().getColumn(columna).setMaxWidth(0);
        tablaTiposPartida.getColumnModel().getColumn(columna).setMinWidth(0);
        tablaTiposPartida.getColumnModel().getColumn(columna).setPreferredWidth(0);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}