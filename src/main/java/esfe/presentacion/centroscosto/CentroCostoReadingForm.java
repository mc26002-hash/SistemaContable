package esfe.presentacion.centroscosto;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import esfe.dominio.CentroCosto;
import esfe.persistencia.CentroCostoDAO;
import esfe.utils.CUD;
import esfe.utils.WindowConfig;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;

public class CentroCostoReadingForm extends JDialog {

    private JPanel mainPanel;

    private JTextField txtBuscar;

    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnEliminar;

    private JTable tableCentrosCosto;

    private CentroCostoDAO centroCostoDAO;

    public CentroCostoReadingForm() {

        centroCostoDAO = new CentroCostoDAO();

        setContentPane(mainPanel);
        setModal(true);

        setTitle("Centros de Costo");

        WindowConfig.configurarVentana(this);
        setLocationRelativeTo(null);

        btnNuevo.addActionListener(
                e -> nuevo()
        );

        btnEditar.addActionListener(
                e -> editar()
        );

        btnEliminar.addActionListener(
                e -> eliminar()
        );

        txtBuscar.addActionListener(
                e -> cargarDatos()
        );

        cargarDatos();
    }

    private void cargarDatos() {

        try {

            ArrayList<CentroCosto> lista =
                    centroCostoDAO.search(
                            txtBuscar.getText().trim()
                    );

            DefaultTableModel model =
                    new DefaultTableModel();

            model.addColumn("ID");
            model.addColumn("Código");
            model.addColumn("Nombre");
            model.addColumn("Responsable");
            model.addColumn("Presupuesto");
            model.addColumn("Estado");

            for (CentroCosto centroCosto : lista) {

                model.addRow(new Object[]{

                        centroCosto.getCentroCostoId(),
                        centroCosto.getCodigo(),
                        centroCosto.getNombre(),
                        centroCosto.getResponsable(),
                        centroCosto.getPresupuesto(),

                        centroCosto.isActivo()
                                ? "Activo"
                                : "Inactivo"
                });
            }

            tableCentrosCosto.setModel(model);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private CentroCosto obtenerSeleccionado() {

        try {

            int fila =
                    tableCentrosCosto.getSelectedRow();

            if (fila < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un registro.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            int id = Integer.parseInt(
                    tableCentrosCosto
                            .getValueAt(fila, 0)
                            .toString()
            );

            return centroCostoDAO.getById(id);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );

            return null;
        }
    }

    private void nuevo() {

        CentroCosto centroCosto =
                new CentroCosto();

        CentroCostoWriteForm form =
                new CentroCostoWriteForm(
                        CUD.CREATE,
                        centroCosto
                );

        form.setVisible(true);

        cargarDatos();
    }

    private void editar() {

        CentroCosto centroCosto =
                obtenerSeleccionado();

        if (centroCosto == null) {
            return;
        }

        CentroCostoWriteForm form =
                new CentroCostoWriteForm(
                        CUD.UPDATE,
                        centroCosto
                );

        form.setVisible(true);

        cargarDatos();
    }

    private void eliminar() {

        CentroCosto centroCosto =
                obtenerSeleccionado();

        if (centroCosto == null) {
            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar el registro?",
                        "Confirmación",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        CentroCostoWriteForm form =
                new CentroCostoWriteForm(
                        CUD.DELETE,
                        centroCosto
                );

        form.setVisible(true);

        cargarDatos();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(7, 4, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Buscar");
        mainPanel.add(label1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtBuscar = new JTextField();
        mainPanel.add(txtBuscar, new GridConstraints(3, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnNuevo = new JButton();
        btnNuevo.setText("Nuevo");
        mainPanel.add(btnNuevo, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnEditar = new JButton();
        btnEditar.setText("Editar");
        mainPanel.add(btnEditar, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnEliminar = new JButton();
        btnEliminar.setText("Eliminar");
        mainPanel.add(btnEliminar, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tableCentrosCosto = new JTable();
        mainPanel.add(tableCentrosCosto, new GridConstraints(5, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Fira Code", Font.BOLD, 16, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Centros de Costo");
        mainPanel.add(label2, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        mainPanel.add(spacer4, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}