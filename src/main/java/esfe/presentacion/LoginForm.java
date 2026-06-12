package esfe.presentacion;

import esfe.dominio.Usuario;
import esfe.persistencia.UsuarioDAO;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.charset.StandardCharsets;

public class LoginForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSalir;

    private UsuarioDAO usuarioDAO;

    public LoginForm() {
        usuarioDAO = new UsuarioDAO();

        setContentPane(mainPanel);
        setModal(true);
        setTitle("Iniciar Sesión");
        pack();
        setLocationRelativeTo(null);

        btnSalir.addActionListener(e -> System.exit(0));
        btnLogin.addActionListener(e -> login());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void login() {
        try {
            Usuario usuario = new Usuario();

            usuario.setCorreoElectronico(txtEmail.getText().trim());
            usuario.setPasswordHash(
                    new String(txtPassword.getPassword())
                            .getBytes(StandardCharsets.UTF_8)
            );

            Usuario usuarioAutenticado = usuarioDAO.authenticate(usuario);

            if (usuarioAutenticado != null
                    && usuarioAutenticado.getUsuarioId() > 0
                    && usuarioAutenticado.isActivo()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Bienvenido " + usuarioAutenticado.getNombreCompleto(),
                        "Login",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Correo o contraseña incorrectos.",
                        "Login",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }
    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
    }
}

