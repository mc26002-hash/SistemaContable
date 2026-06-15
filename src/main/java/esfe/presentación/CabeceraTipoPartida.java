package esfe.presentación;

import javax.swing.*;

public class CabeceraTipoPartida {
    private JPanel panelCabeceraRoot;
    private JTextField txtFecha;
    private JTextField txtGlosa;
    private JButton btnLimpiar;

    public CabeceraTipoPartida() {

        btnLimpiar.addActionListener(e -> {
            txtFecha.setText("");
            txtGlosa.setText("");
        });
    }

    public String getFecha() { return txtFecha.getText(); }
    public String getGlosa() { return txtGlosa.getText(); }
    public JPanel getPanel() { return panelCabeceraRoot; }
}
