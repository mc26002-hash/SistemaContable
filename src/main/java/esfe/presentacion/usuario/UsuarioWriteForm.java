package esfe.presentacion.usuario;

import esfe.dominio.Rol;
import esfe.dominio.Usuario;
import esfe.persistencia.UsuarioDAO;
import esfe.utils.CUD;
import esfe.utils.WindowConfig;
import esfe.dominio.Rol;
import esfe.persistencia.RolDAO;
import java.sql.SQLException;

import javax.swing.*;
import java.nio.charset.StandardCharsets;

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

}
