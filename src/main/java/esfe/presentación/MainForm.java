package esfe.presentación;

import esfe.persistencia.TipoPartidaDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainForm extends JFrame {
    private JPanel mainPanel;

    private TipoPartidaDAO tipoPartidaDAO;

    private CabeceraTipoPartida cabeceraForm;
    private DetallesTipoPartida detalleForm;
    private TValidacionesTipoPartida accionesForm;

    public MainForm() {
        this.tipoPartidaDAO = new TipoPartidaDAO();

        setTitle("Registro de Partida Contable");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        if (mainPanel == null) {
            mainPanel = new JPanel(new BorderLayout(5, 5));
        }
        setContentPane(mainPanel);

        cabeceraForm = new CabeceraTipoPartida();

        detalleForm = new DetallesTipoPartida(this::actualizarTotalesPantalla);
        accionesForm = new TValidacionesTipoPartida(this::guardar, this::dispose);

        mainPanel.add(cabeceraForm.getPanel(), BorderLayout.NORTH);
        mainPanel.add(detalleForm.getPanel(), BorderLayout.CENTER);
        mainPanel.add(accionesForm.getPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void actualizarTotalesPantalla() {
        DefaultTableModel modelo = detalleForm.getModeloTabla();
        double totalDebito = 0;
        double totalCredito = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            try {
                Object deb = modelo.getValueAt(i, 1);
                Object cred = modelo.getValueAt(i, 2);
                if (deb != null) totalDebito += Double.parseDouble(deb.toString());
                if (cred != null) totalCredito += Double.parseDouble(cred.toString());
            } catch (Exception e) {}
        }
        accionesForm.actualizarTotales(totalDebito, totalCredito);
    }

    private void guardar() {
        if (detalleForm != null) {
            detalleForm.detenerEdicionActiva();
        }

        DefaultTableModel modelo = detalleForm.getModeloTabla();
        double totalDebito = 0;
        double totalCredito = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            try {
                Object deb = modelo.getValueAt(i, 1);
                Object cred = modelo.getValueAt(i, 2);
                if (deb != null) totalDebito += Double.parseDouble(deb.toString());
                if (cred != null) totalCredito += Double.parseDouble(cred.toString());
            } catch (Exception e) {}
        }

        try {

            if (Math.abs(totalDebito - totalCredito) > 0.001) {

                accionesForm.mostrarError("VALIDACIÓN MATEMÁTICA ERROR: El total de débitos debe ser igual al de créditos.");
            } else if (totalDebito == 0 && totalCredito == 0) {
                accionesForm.mostrarError("Error: No se pueden registrar transacciones con valor cero.");
            } else {
                accionesForm.mostrarError("");

                JOptionPane.showMessageDialog(this,
                        "¡La partida ha sido cuadrada matemáticamente y registrada con éxito!",
                        "Registro Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);

                this.dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error en el sistema: " + ex.getMessage(),
                    "System Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}