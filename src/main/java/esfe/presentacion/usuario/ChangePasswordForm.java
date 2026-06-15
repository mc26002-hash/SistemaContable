package esfe.presentacion.usuario;

import esfe.dominio.Usuario;
import esfe.persistencia.UsuarioDAO;

import javax.swing.*;
import java.nio.charset.StandardCharsets;

public class ChangePasswordForm extends JDialog {

    private JPanel mainPanel;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnCambiarPassword;
    private JButton btnCancelar;

    private Usuario usuario;
    private UsuarioDAO usuarioDAO;

    public ChangePasswordForm(Usuario usuario) {

        this.usuario = usuario;

        usuarioDAO = new UsuarioDAO();

        setContentPane(mainPanel);
        setModal(true);
        setTitle("Cambiar Contraseña");

        pack();
        setLocationRelativeTo(null);

        txtUsuario.setEditable(false);

        txtUsuario.setText(
                usuario.getNombreUsuario()
        );

        btnCancelar.addActionListener(e -> dispose());

        btnCambiarPassword.addActionListener(
                e -> changePassword()
        );
    }

    private void changePassword() {

        try {

            String password =
                    new String(
                            txtPassword.getPassword()
                    ).trim();

            if (password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "La contraseña es obligatoria.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            usuario.setPasswordHash(
                    password.getBytes(
                            StandardCharsets.UTF_8
                    )
            );

            boolean res =
                    usuarioDAO.updatePassword(usuario);

            if (res) {

                JOptionPane.showMessageDialog(
                        this,
                        "Contraseña actualizada correctamente.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No fue posible actualizar la contraseña.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
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
}