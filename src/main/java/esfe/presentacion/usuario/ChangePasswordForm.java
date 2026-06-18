package esfe.presentacion.usuario;

import esfe.dominio.Usuario;
import esfe.persistencia.UsuarioDAO;
import esfe.utils.PasswordHasher;

import javax.swing.*;

public class ChangePasswordForm extends JDialog {

    private JPanel mainPanel;
    private JButton btnActualizar;
    private JTextField txtEmail;
    private JPasswordField txtContraseña;
    private JButton btnCancelar;

    private Usuario usuario;
    private UsuarioDAO usuarioDAO;

    public ChangePasswordForm(Usuario usuario) {
        this.usuario = usuario;
        usuarioDAO = new UsuarioDAO();

        setContentPane(mainPanel);
        setModal(true);
        setTitle("Cambiar Contraseña");
        setSize(450, 300);
        setLocationRelativeTo(null);

        txtEmail.setEditable(false);
        txtEmail.setText(usuario.getCorreoElectronico());

        btnCancelar.addActionListener(e -> dispose());
        btnActualizar.addActionListener(e -> changePassword());
    }

    private void changePassword() {
        try {
            String password = new String(txtContraseña.getPassword()).trim();

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "La contraseña es obligatoria.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            usuario.setPasswordHash(
                    PasswordHasher.hashPasswordBytes(password)
            );

            boolean res = usuarioDAO.updatePassword(usuario);

            if (res) {
                JOptionPane.showMessageDialog(this,
                        "Contraseña actualizada correctamente.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No fue posible actualizar la contraseña.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}