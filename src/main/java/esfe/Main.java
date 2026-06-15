
package esfe;

import esfe.presentación.MainForm;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MainForm mainForm = new MainForm();
            mainForm.setTitle("Sistema Contable - Registro de Partidas");
            mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainForm.setSize(700, 500);
            mainForm.setLocationRelativeTo(null); // Centra la pantalla
            mainForm.setVisible(true);
        });
    }
}