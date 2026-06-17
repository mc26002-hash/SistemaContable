
package esfe;


import esfe.presentacion.MainForm;
import esfe.presentacion.usuario.LoginForm;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            MainForm mainForm = new MainForm();
//            mainForm.setVisible(true);
//        });

        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);

            if (loginForm.getUsuarioAutenticado() != null) {
                MainForm mainForm = new MainForm();
                mainForm.setVisible(true);
            }
        });


    }
}