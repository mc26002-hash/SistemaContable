package esfe;

import esfe.presentacion.TipoCuenta; // Importamos tu catálogo
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Aseguramos que la interfaz gráfica corra de forma segura en su propio hilo
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 1. Creamos el marco principal de la aplicación (la base invisible de atrás)
                JFrame marcoPrincipal = new JFrame();
                marcoPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // 2. Instanciamos tu pantalla de TipoCuenta pasándole el marco principal
                TipoCuenta pantallaCatalogo = new TipoCuenta(marcoPrincipal);

                // 3. Hacemos visible tu catálogo en la pantalla de la computadora
                pantallaCatalogo.setVisible(true);
            }
        });
    }
}