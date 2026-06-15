package esfe;

import esfe.presentacion.centroscosto.CentroCostoReadingForm;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            CentroCostoReadingForm form =
                    new CentroCostoReadingForm();

            form.setVisible(true);
        });
    }
}
