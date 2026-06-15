package esfe.presentación;

import javax.swing.*;

public class TValidacionesTipoPartida {
    private JPanel panelAccionesRoot;
    private JTextField txtTotalDebito;
    private JTextField txtTotalCredito;
    private JLabel lblErrorAlerta;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public TValidacionesTipoPartida(Runnable accionGuardar, Runnable accionCancelar) {
        btnGuardar.addActionListener(e -> accionGuardar.run());
        btnCancelar.addActionListener(e -> accionCancelar.run());
    }

    public void actualizarTotales(double debito, double credito) {
        txtTotalDebito.setText("Total Débito: $" + String.format("%.2f", debito));
        txtTotalCredito.setText("Total Crédito: $" + String.format("%.2f", credito));
    }

    public void mostrarError(String mensaje) {
        lblErrorAlerta.setText(mensaje);
    }

    public JPanel getPanel() { return panelAccionesRoot; }
}