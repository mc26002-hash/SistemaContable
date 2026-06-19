package esfe.presentacion.usuario;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import esfe.dominio.Usuario;
import esfe.persistencia.UsuarioDAO;
import esfe.presentacion.usuario.UsuarioWriteForm;
import esfe.utils.CUD;
import esfe.utils.WindowConfig;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Locale;

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


        System.out.println("mainPanel = " + mainPanel);

        setContentPane(mainPanel);
        setModal(true);
        setTitle("Gestión de Usuarios");
        WindowConfig.configurarVentana(this);

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

                search(txtBuscar.getText().trim());
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
        search("");
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
                    obtenerNombreRol(usuario.getRolId()),
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

    private String obtenerNombreRol(int rolId) {
        switch (rolId) {
            case 1:
                return "Administrador";
            case 2:
                return "Contador";
            case 3:
                return "Auxiliar Contable";
            default:
                return "Rol ID: " + rolId;
        }
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
        mainPanel.setLayout(new GridLayoutManager(7, 8, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Nombre");
        mainPanel.add(label1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txtBuscar = new JTextField();
        mainPanel.add(txtBuscar, new GridConstraints(2, 2, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new GridConstraints(3, 2, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableUsuarios = new JTable();
        scrollPane1.setViewportView(tableUsuarios);
        btnEditar = new JButton();
        btnEditar.setText("Editar");
        mainPanel.add(btnEditar, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCambiarPassword = new JButton();
        btnCambiarPassword.setText("Cambiar Contraseña");
        mainPanel.add(btnCambiarPassword, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnPermisos = new JButton();
        btnPermisos.setText("Permisos");
        mainPanel.add(btnPermisos, new GridConstraints(5, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnEliminar = new JButton();
        btnEliminar.setText("Eliminar");
        mainPanel.add(btnEliminar, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtPermisosRol = new JTextArea();
        mainPanel.add(txtPermisosRol, new GridConstraints(4, 2, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        btnNuevoUsuario = new JButton();
        btnNuevoUsuario.setText("Ir a crear");
        mainPanel.add(btnNuevoUsuario, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(3, 6, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        mainPanel.add(spacer4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        mainPanel.add(spacer5, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Fira Code", Font.BOLD, 16, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Gestión de Usuarios");
        mainPanel.add(label2, new GridConstraints(1, 2, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        mainPanel.add(spacer6, new GridConstraints(6, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}