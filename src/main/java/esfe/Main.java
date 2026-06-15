package esfe;

import esfe.presentacion.LoginForm;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LoginForm form =
                    new LoginForm();

            form.setVisible(true);
        });
    }
}

