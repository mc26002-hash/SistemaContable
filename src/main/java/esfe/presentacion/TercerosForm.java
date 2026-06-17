package esfe.presentacion;

import esfe.dominio.Tercero;
import esfe.persistencia.TerceroDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        pack();
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
}