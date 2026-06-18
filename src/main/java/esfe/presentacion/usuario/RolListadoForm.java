package esfe.presentacion.usuario;


import esfe.dominio.Rol;
import esfe.persistencia.RolDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class RolListadoForm extends JDialog  {
    private JPanel panelListado;
    private JTable table1;
    private JButton btnAgregar;
    private JButton btnEliminar;
    private JButton btnModificar;

    private RolDAO controladorDAO;
    private ArrayList<Rol> listaRoles;
    private DefaultTableModel modeloTabla;

    public RolListadoForm() {
        setTitle("Sistema Contable - Registro General de Roles");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        panelListado = new JPanel(new BorderLayout(10, 10));
        panelListado.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("REGISTRO GENERAL DE ROLES", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelListado.add(titulo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID Rol", "Nombre del Rol", "Descripción", "Estado"}, 0
        );
        table1 = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(table1);
        panelListado.add(scrollPane, BorderLayout.CENTER);

        btnAgregar = new JButton("Nuevo Rol");
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 12));

        btnModificar = new JButton("Modificar Rol");
        btnModificar.setFont(new Font("Arial", Font.BOLD, 12));
        btnModificar.setOpaque(true);
        btnModificar.setBorderPainted(false);


        btnEliminar = new JButton("Eliminar Rol");
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelListado.add(panelBotones, BorderLayout.SOUTH);

        setContentPane(panelListado);
        controladorDAO = new RolDAO();
        cargarDatosTabla();

        btnAgregar.addActionListener(e -> {
            JTextField txtNombre = new JTextField(20);
            JTextField txtDescripcion = new JTextField(20);
            JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});

            JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
            formulario.add(new JLabel("Nombre del Rol:"));
            formulario.add(txtNombre);
            formulario.add(new JLabel("Descripción:"));
            formulario.add(txtDescripcion);
            formulario.add(new JLabel("Estado:"));
            formulario.add(cmbEstado);

            int resultado = JOptionPane.showConfirmDialog(
                    this,
                    formulario,
                    "Crear Nuevo Rol",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (resultado == JOptionPane.OK_OPTION) {
                if (txtNombre.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "El nombre no puede estar vacío.",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    Rol nuevo = new Rol();
                    nuevo.setNombreRol(txtNombre.getText().trim());
                    nuevo.setDescripcion(txtDescripcion.getText().trim());
                    nuevo.setActivo(cmbEstado.getSelectedIndex() == 0);
                    controladorDAO.create(nuevo);
                    JOptionPane.showMessageDialog(this,
                            "¡Rol guardado con éxito!",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarDatosTabla();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al guardar: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnModificar.addActionListener(e -> {
            int fila = table1.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, seleccione un rol de la tabla para modificar.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Rol seleccionado = listaRoles.get(fila);

            JTextField txtNombre = new JTextField(seleccionado.getNombreRol(), 20);
            JTextField txtDescripcion = new JTextField(seleccionado.getDescripcion(), 20);
            JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
            cmbEstado.setSelectedIndex(seleccionado.getActivo() ? 0 : 1);

            JTextField txtId = new JTextField(String.valueOf(seleccionado.getRolId()));
            txtId.setEditable(false);
            txtId.setBackground(Color.LIGHT_GRAY);

            JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));
            formulario.add(new JLabel("ID Rol (no editable):"));
            formulario.add(txtId);
            formulario.add(new JLabel("Nombre del Rol:"));
            formulario.add(txtNombre);
            formulario.add(new JLabel("Descripción:"));
            formulario.add(txtDescripcion);
            formulario.add(new JLabel("Estado:"));
            formulario.add(cmbEstado);

            int resultado = JOptionPane.showConfirmDialog(
                    this,
                    formulario,
                    "Modificar Rol ID: " + seleccionado.getRolId(),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (resultado == JOptionPane.OK_OPTION) {
                if (txtNombre.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "El nombre no puede estar vacío.",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    seleccionado.setNombreRol(txtNombre.getText().trim());
                    seleccionado.setDescripcion(txtDescripcion.getText().trim());
                    seleccionado.setActivo(cmbEstado.getSelectedIndex() == 0);
                    controladorDAO.update(seleccionado);
                    JOptionPane.showMessageDialog(this,
                            "¡Rol actualizado con éxito!",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarDatosTabla();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al actualizar: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        btnEliminar.addActionListener(e -> {
            int fila = table1.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, seleccione un rol de la tabla para eliminar.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Rol seleccionado = listaRoles.get(fila);
            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "⚠️ ADVERTENCIA DE ELIMINACIÓN\n\n" +
                            "¿Está seguro de eliminar el Rol ID: " + seleccionado.getRolId() +
                            " (" + seleccionado.getNombreRol() + ")?\n" +
                            "Esta acción borrará el registro de forma permanente.",
                    "Confirmar Baja",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                try {
                    controladorDAO.delete(seleccionado);
                    JOptionPane.showMessageDialog(this, "Rol eliminado correctamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarDatosTabla();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setSize(680, 450);
        setLocationRelativeTo(null);
    }

    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0);
        try {
            listaRoles = controladorDAO.search("");
            for (Rol r : listaRoles) {
                modeloTabla.addRow(new Object[]{
                        r.getRolId(),
                        r.getNombreRol(),
                        r.getDescripcion(),
                        r.getStrEstatus()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar la tabla: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}