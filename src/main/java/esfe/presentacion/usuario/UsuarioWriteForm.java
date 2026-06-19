package esfe.presentacion.usuario;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import esfe.dominio.Rol;
import esfe.dominio.Usuario;
import esfe.persistencia.UsuarioDAO;
import esfe.utils.CUD;
import esfe.utils.WindowConfig;
import esfe.dominio.Rol;
import esfe.persistencia.RolDAO;

import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class UsuarioWriteForm extends JDialog {
    private JPanel mainPanel;

    private JTextField txtNombreUsuario;
    private JTextField txtNombreCompleto;
    private JTextField txtCorreoElectronico;

    private JPasswordField txtPassword;

    private JComboBox<Rol> cbRol;
    private JComboBox<String> cbEstado;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private JLabel lbPassword;

    private UsuarioDAO usuarioDAO;
    private RolDAO rolDAO;
    private Usuario usuario;
    private CUD cud;

    public UsuarioWriteForm(CUD cud, Usuario usuario) {

        this.cud = cud;
        this.usuario = usuario;

        usuarioDAO = new UsuarioDAO();
        rolDAO = new RolDAO();

        setContentPane(mainPanel);
        setModal(true);

        init();

        WindowConfig.configurarVentana(this);

        btnCancelar.addActionListener(e -> dispose());

        btnGuardar.addActionListener(e -> guardar());
    }

    private void init() {

        cargarRoles();
        cargarEstados();

        switch (cud) {

            case CREATE:

                setTitle("Nuevo Usuario");
                btnGuardar.setText("Guardar");
                break;

            case UPDATE:

                setTitle("Editar Usuario");
                btnGuardar.setText("Guardar");
                break;

            case DELETE:

                setTitle("Eliminar Usuario");
                btnGuardar.setText("Eliminar");
                break;
        }

        cargarDatos();
    }

    private void cargarRoles() {
        try {
            cbRol.removeAllItems();

            for (Rol rol : rolDAO.search("")) {
                if (rol.getActivo()) {
                    cbRol.addItem(rol);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar roles: " + ex.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarEstados() {

        cbEstado.removeAllItems();

        cbEstado.addItem("Activo");
        cbEstado.addItem("Inactivo");
    }

    private void cargarDatos() {

        if (usuario == null) {
            return;
        }

        txtNombreUsuario.setText(
                usuario.getNombreUsuario()
        );

        txtNombreCompleto.setText(
                usuario.getNombreCompleto()
        );

        txtCorreoElectronico.setText(
                usuario.getCorreoElectronico()
        );

        for (int i = 0; i < cbRol.getItemCount(); i++) {
            Rol rol = cbRol.getItemAt(i);

            if (rol.getRolId() == usuario.getRolId()) {
                cbRol.setSelectedIndex(i);
                break;
            }
        }

        cbEstado.setSelectedItem(
                usuario.isActivo()
                        ? "Activo"
                        : "Inactivo"
        );

        if (cud != CUD.CREATE) {

            txtPassword.setVisible(false);
            lbPassword.setVisible(false);
        }

        if (cud == CUD.DELETE) {

            txtNombreUsuario.setEditable(false);
            txtNombreCompleto.setEditable(false);
            txtCorreoElectronico.setEditable(false);

            cbRol.setEnabled(false);
            cbEstado.setEnabled(false);
        }
    }

    private boolean validar() {

        if (txtNombreUsuario.getText().trim().isEmpty()) {
            return false;
        }

        if (txtNombreCompleto.getText().trim().isEmpty()) {
            return false;
        }

        if (txtCorreoElectronico.getText().trim().isEmpty()) {
            return false;
        }

        if (cud == CUD.CREATE &&
                txtPassword.getPassword().length == 0) {
            return false;
        }

        return true;
    }

    private void obtenerValoresFormulario() {

        System.out.println("Rol seleccionado: " + usuario.getRolId());

        usuario.setNombreUsuario(
                txtNombreUsuario.getText().trim()
        );

        usuario.setNombreCompleto(
                txtNombreCompleto.getText().trim()
        );

        usuario.setCorreoElectronico(
                txtCorreoElectronico.getText().trim()
        );

        Rol rolSeleccionado = (Rol) cbRol.getSelectedItem();

        if (rolSeleccionado != null) {
            usuario.setRolId(rolSeleccionado.getRolId());
        }

        usuario.setActivo(
                cbEstado.getSelectedItem()
                        .equals("Activo")
        );

        if (cud == CUD.CREATE) {

            usuario.setPasswordHash(
                    new String(
                            txtPassword.getPassword()
                    ).getBytes(StandardCharsets.UTF_8)
            );
        }
    }

    private void guardar() {

        try {

            if (!validar()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Complete todos los campos.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            obtenerValoresFormulario();

            if (cud == CUD.CREATE) {

                Usuario nuevoUsuario =
                        usuarioDAO.create(usuario);

                if (nuevoUsuario != null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Usuario creado correctamente.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose();
                }

            } else if (cud == CUD.UPDATE) {

                boolean actualizado =
                        usuarioDAO.update(usuario);

                if (actualizado) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Usuario actualizado correctamente.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "No fue posible actualizar el usuario.",
                            "Validación",
                            JOptionPane.WARNING_MESSAGE
                    );
                }

            } else if (cud == CUD.DELETE) {

                boolean eliminado =
                        usuarioDAO.delete(usuario);

                if (eliminado) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Usuario eliminado correctamente.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "No fue posible eliminar el usuario.",
                            "Validación",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
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
        mainPanel.setLayout(new GridLayoutManager(11, 5, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Usuario");
        mainPanel.add(label1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txtNombreUsuario = new JTextField();
        mainPanel.add(txtNombreUsuario, new GridConstraints(3, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Nombre");
        mainPanel.add(label2, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNombreCompleto = new JTextField();
        mainPanel.add(txtNombreCompleto, new GridConstraints(4, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lbPassword = new JLabel();
        lbPassword.setText("Contraseña");
        mainPanel.add(lbPassword, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtPassword = new JPasswordField();
        mainPanel.add(txtPassword, new GridConstraints(6, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Rol");
        mainPanel.add(label3, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbRol = new JComboBox();
        mainPanel.add(cbRol, new GridConstraints(7, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnGuardar = new JButton();
        btnGuardar.setText("Guardar");
        mainPanel.add(btnGuardar, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCancelar = new JButton();
        btnCancelar.setText("Cancelar");
        mainPanel.add(btnCancelar, new GridConstraints(9, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtCorreoElectronico = new JTextField();
        mainPanel.add(txtCorreoElectronico, new GridConstraints(5, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Correo Electronico");
        mainPanel.add(label4, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbEstado = new JComboBox();
        mainPanel.add(cbEstado, new GridConstraints(8, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Estado");
        mainPanel.add(label5, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(6, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        mainPanel.add(spacer4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$("Fira Code", Font.BOLD, 16, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Gestión de Usuarios");
        mainPanel.add(label6, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        mainPanel.add(spacer5, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        mainPanel.add(spacer6, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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
