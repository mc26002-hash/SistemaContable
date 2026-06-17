package esfe.presentacion.usuario;

import esfe.dominio.Usuario;
import esfe.persistencia.UsuarioDAO;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import esfe.utils.PasswordHasher;

public class LoginForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtUsuario;
    private JButton btnCancelar;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JPanel panelIzquierdo;
    private JPanel panelSuperior;
    private JPanel panelInferior;

    private UsuarioDAO usuarioDAO;
    private Usuario usuarioAutenticado;

    public LoginForm() {
        usuarioDAO = new UsuarioDAO();

        setContentPane(mainPanel);
        setModal(true);
        setTitle("Iniciar Sesión");

        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        btnCancelar.addActionListener(e -> System.exit(0));
        btnIngresar.addActionListener(e -> login());

        getRootPane().setDefaultButton(btnIngresar);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void login() {
        try {
            String usuarioTexto = txtUsuario.getText().trim();
            String passwordTexto = new String(txtPassword.getPassword()).trim();

            if (usuarioTexto.isEmpty() || passwordTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Ingrese usuario/correo y contraseña.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Usuario usuario = new Usuario();

            usuario.setCorreoElectronico(usuarioTexto);
            usuario.setPasswordHash(
                    PasswordHasher.hashPasswordBytes(passwordTexto)
            );

            Usuario resultado = usuarioDAO.authenticate(usuario);

            if (resultado != null
                    && resultado.getUsuarioId() > 0
                    && resultado.isActivo()) {

                usuarioAutenticado = resultado;

                JOptionPane.showMessageDialog(this,
                        "Bienvenido " + resultado.getNombreCompleto(),
                        "Login",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();

            } else {
                JOptionPane.showMessageDialog(this,
                        "Usuario/correo o contraseña incorrectos.",
                        "Login",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
}