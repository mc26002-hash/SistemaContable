
package esfe;



import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import esfe.presentacion.MainForm;
import esfe.presentacion.usuario.LoginForm;
import esfe.dominio.Usuario;
import esfe.presentacion.MainForm;
import esfe.presentacion.usuario.LoginForm;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try {
            FlatMacLightLaf.setup();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);

            Usuario usuario = loginForm.getUsuarioAutenticado();

            if (usuario != null) {
                MainForm mainForm = new MainForm(usuario);
                mainForm.setVisible(true);
            } else {
                System.exit(0);
            }
        });

//        SwingUtilities.invokeLater(() -> {
//            MainForm mainForm = new MainForm();
//            mainForm.setVisible(true);
//        });

    }
}