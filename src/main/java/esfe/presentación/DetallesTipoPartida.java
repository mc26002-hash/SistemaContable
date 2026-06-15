package esfe.presentación;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DetallesTipoPartida {
    private JPanel panelDetalleRoot;
    private JTable tablaDetalle;
    private JButton btnAgregarFila;
    private JButton btnEliminarFila;
    private DefaultTableModel modeloTabla;
    private Runnable alCambiarDatos;

    public DetallesTipoPartida(Runnable alCambiarDatos) {
        this.alCambiarDatos = alCambiarDatos;

        String[] columnas = {"Cuenta Contable", "Monto Débito", "Monto Crédito"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaDetalle.setModel(modeloTabla);

        JComboBox<String> cbCuentas = new JComboBox<>(new String[]{
                "Seleccione una cuenta...", "1101 - Caja General", "1102 - Bancos", "2101 - Proveedores"
        });
        tablaDetalle.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cbCuentas));

        btnAgregarFila.addActionListener(e -> {
            modeloTabla.addRow(new Object[]{"Seleccione una cuenta...", 0.0, 0.0});
            notificarCambio();
        });

        btnEliminarFila.addActionListener(e -> {
            int fila = tablaDetalle.getSelectedRow();
            if (fila != -1) {
                modeloTabla.removeRow(fila);
                notificarCambio();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila para eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
            }
        });

        tablaDetalle.addPropertyChangeListener(evt -> {
            if ("tableCellBorder".equals(evt.getPropertyName())) {
                notificarCambio();
            }
        });
    }

    private void notificarCambio() {
        if (tablaDetalle.isEditing()) {
            tablaDetalle.getCellEditor().stopCellEditing();
        }
        if (alCambiarDatos != null) alCambiarDatos.run();
    }

    public void detenerEdicionActiva() {
        if (tablaDetalle != null && tablaDetalle.isEditing()) {
            tablaDetalle.getCellEditor().stopCellEditing();
        }
    }

    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JPanel getPanel() { return panelDetalleRoot; }
}