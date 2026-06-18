
package esfe;


import esfe.presentacion.MainForm;
import esfe.presentacion.usuario.LoginForm;
import esfe.dominio.Usuario;
import esfe.presentacion.MainForm;
import esfe.presentacion.usuario.LoginForm;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            LoginForm loginForm = new LoginForm();
//            loginForm.setVisible(true);
//
//            Usuario usuario = loginForm.getUsuarioAutenticado();
//
//            if (usuario != null) {
//                MainForm mainForm = new MainForm(usuario);
//                mainForm.setVisible(true);
//            } else {
//                System.exit(0);
//            }
//        });

        SwingUtilities.invokeLater(() -> {
            MainForm mainForm = new MainForm();
            mainForm.setVisible(true);
        });

    }
}