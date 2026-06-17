package esfe.presentacion.usuario;

import esfe.dominio.Usuario;
import esfe.persistencia.UsuarioDAO;
import esfe.presentacion.usuario.UsuarioWriteForm;
import esfe.utils.CUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class UsuarioReadingForm extends JDialog {

    private JPanel mainPanel;
    private JTextField txtBuscar;
    private JButton btnNuevoUsuario;
    private JButton btnEditar;
    private JButton btnCambiarPassword;
    private JButton btnPermisos;
    private JTable tableUsuarios;
    private JTextArea txtPermisosRol;
    private JButton btnEliminar;

    private UsuarioDAO usuarioDAO;

    public UsuarioReadingForm() {

        usuarioDAO = new UsuarioDAO();

        setContentPane(mainPanel);
        setModal(true);
        setTitle("Gestión de Usuarios");
        pack();
        setLocationRelativeTo(null);

        txtPermisosRol.setEditable(false);

        txtPermisosRol.setText(
                "Administrador:\n" +
                        "Acceso completo al sistema.\n\n" +

                        "Contador:\n" +
                        "Crear asientos, generar reportes, cerrar períodos.\n\n" +

                        "Asistente:\n" +
                        "Consultar reportes, crear asientos (requiere aprobación)"
        );

        txtBuscar.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {

                if (!txtBuscar.getText().trim().isEmpty()) {

                    search(txtBuscar.getText());

                } else {

                    DefaultTableModel emptyModel =
                            new DefaultTableModel();

                    tableUsuarios.setModel(emptyModel);
                }
            }
        });

        btnNuevoUsuario.addActionListener(e -> {

            UsuarioWriteForm form =
                    new UsuarioWriteForm(
                            CUD.CREATE,
                            new Usuario()
                    );

            form.setVisible(true);
        });

        btnEditar.addActionListener(e -> {

            Usuario usuario = getUsuarioFromTableRow();

            if (usuario != null) {

                UsuarioWriteForm form =
                        new UsuarioWriteForm(
                                CUD.UPDATE,
                                usuario
                        );

                form.setVisible(true);
            }
        });

        btnEliminar.addActionListener(e -> {

            Usuario usuario = getUsuarioFromTableRow();

            if (usuario != null) {

                UsuarioWriteForm form =
                        new UsuarioWriteForm(
                                CUD.DELETE,
                                usuario
                        );

                form.setVisible(true);
            }
        });

        btnCambiarPassword.addActionListener(e -> {

            Usuario usuario =
                    getUsuarioFromTableRow();

            if (usuario != null) {

                ChangePasswordForm form =
                        new ChangePasswordForm(usuario);

                form.setVisible(true);

                search(txtBuscar.getText());
            }
        });

        btnPermisos.addActionListener(e -> {

            Usuario usuario = getUsuarioFromTableRow();

            if (usuario != null) {

                String permisos = "";

                switch (usuario.getRolId()) {

                    case 1:
                        permisos =
                                "Administrador\n" +
                                        "Acceso completo al sistema.";
                        break;

                    case 2:
                        permisos =
                                "Contador\n" +
                                        "Crear asientos, generar reportes, cerrar períodos";
                        break;

                    case 3:
                        permisos =
                                "Asistente\n" +
                                        "Consultar reportes, crear asientos (requiere aprobación)";
                        break;

                    default:
                        permisos =
                                "No hay permisos definidos para este rol.";
                }

                JOptionPane.showMessageDialog(
                        this,
                        permisos,
                        "Permisos del Rol",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
    }

    private void search(String query) {

        try {

            ArrayList<Usuario> usuarios =
                    usuarioDAO.search(query);

            createTable(usuarios);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void createTable(ArrayList<Usuario> usuarios) {

        DefaultTableModel model =
                new DefaultTableModel() {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        model.addColumn("Id");
        model.addColumn("Usuario");
        model.addColumn("Nombre Completo");
        model.addColumn("Rol");
        model.addColumn("Estado");

        tableUsuarios.setModel(model);

        Object[] row = null;

        for (int i = 0; i < usuarios.size(); i++) {

            Usuario usuario = usuarios.get(i);

            model.addRow(row);

            model.setValueAt(
                    usuario.getUsuarioId(),
                    i,
                    0
            );

            model.setValueAt(
                    usuario.getNombreUsuario(),
                    i,
                    1
            );

            model.setValueAt(
                    usuario.getNombreCompleto(),
                    i,
                    2
            );

            model.setValueAt(
                    usuario.getRolId(),
                    i,
                    3
            );

            model.setValueAt(
                    usuario.isActivo()
                            ? "Activo"
                            : "Inactivo",
                    i,
                    4
            );
        }

        hideCol(0);
    }

    private void hideCol(int columna) {

        tableUsuarios
                .getColumnModel()
                .getColumn(columna)
                .setMaxWidth(0);

        tableUsuarios
                .getColumnModel()
                .getColumn(columna)
                .setMinWidth(0);

        tableUsuarios
                .getTableHeader()
                .getColumnModel()
                .getColumn(columna)
                .setMaxWidth(0);

        tableUsuarios
                .getTableHeader()
                .getColumnModel()
                .getColumn(columna)
                .setMinWidth(0);
    }

    private Usuario getUsuarioFromTableRow() {

        try {

            int filaSeleccionada =
                    tableUsuarios.getSelectedRow();

            int id;

            if (filaSeleccionada != -1) {

                id = (int) tableUsuarios.getValueAt(
                        filaSeleccionada,
                        0
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione una fila.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            Usuario usuario =
                    usuarioDAO.getById(id);

            if (usuario == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró el usuario.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            return usuario;

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );

            return null;
        }
    }
}