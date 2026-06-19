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
import javax.swing.text.StyleContext;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Locale;

public class CentroCostoWriteForm extends JDialog {

    private JPanel mainPanel;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtResponsable;
    private JTextField txtPresupuesto;

    private JComboBox<String> cbEstado;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private CentroCostoDAO centroCostoDAO;
    private CentroCosto centroCosto;
    private CUD cud;

    public CentroCostoWriteForm(
            CUD cud,
            CentroCosto centroCosto
    ) {

        this.cud = cud;
        this.centroCosto = centroCosto;

        centroCostoDAO = new CentroCostoDAO();

        setContentPane(mainPanel);
        setModal(true);

        init();

        WindowConfig.configurarVentana(this);
        setLocationRelativeTo(null);

        btnCancelar.addActionListener(
                e -> dispose()
        );

        btnGuardar.addActionListener(
                e -> guardar()
        );
    }

    private void init() {

        cargarEstados();

        switch (cud) {

            case CREATE:

                setTitle("Nuevo Centro de Costo");
                btnGuardar.setText("Guardar");
                break;

            case UPDATE:

                setTitle("Editar Centro de Costo");
                btnGuardar.setText("Guardar");
                break;

            case DELETE:

                setTitle("Eliminar Centro de Costo");
                btnGuardar.setText("Eliminar");
                break;
        }

        cargarDatos();
    }

    private void cargarEstados() {

        cbEstado.removeAllItems();

        cbEstado.addItem("Activo");
        cbEstado.addItem("Inactivo");
    }

    private void cargarDatos() {

        if (centroCosto == null) {
            return;
        }

        txtCodigo.setText(
                centroCosto.getCodigo()
        );

        txtNombre.setText(
                centroCosto.getNombre()
        );

        txtResponsable.setText(
                centroCosto.getResponsable()
        );

        if (centroCosto.getPresupuesto() != null) {

            txtPresupuesto.setText(
                    centroCosto.getPresupuesto().toString()
            );
        }

        cbEstado.setSelectedItem(
                centroCosto.isActivo()
                        ? "Activo"
                        : "Inactivo"
        );

        if (cud == CUD.DELETE) {

            txtCodigo.setEditable(false);
            txtNombre.setEditable(false);
            txtResponsable.setEditable(false);
            txtPresupuesto.setEditable(false);

            cbEstado.setEnabled(false);
        }
    }

    private boolean validar() {

        if (txtCodigo.getText()
                .trim()
                .isEmpty()) {

            return false;
        }

        if (txtNombre.getText()
                .trim()
                .isEmpty()) {

            return false;
        }

        if (txtPresupuesto.getText()
                .trim()
                .isEmpty()) {

            return false;
        }

        try {

            new BigDecimal(
                    txtPresupuesto.getText()
                            .trim()
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "El presupuesto debe ser numérico.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

    private void obtenerValoresFormulario() {

        centroCosto.setCodigo(
                txtCodigo.getText()
                        .trim()
        );

        centroCosto.setNombre(
                txtNombre.getText()
                        .trim()
        );

        centroCosto.setResponsable(
                txtResponsable.getText()
                        .trim()
        );

        centroCosto.setPresupuesto(
                new BigDecimal(
                        txtPresupuesto.getText()
                                .trim()
                )
        );

        centroCosto.setActivo(
                cbEstado.getSelectedItem()
                        .equals("Activo")
        );
    }

    private void guardar() {

        try {

            if (!validar()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Complete todos los campos obligatorios.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            obtenerValoresFormulario();

            if (cud == CUD.CREATE) {

                CentroCosto nuevoCentroCosto =
                        centroCostoDAO.create(
                                centroCosto
                        );

                if (nuevoCentroCosto != null
                        && nuevoCentroCosto.getCentroCostoId() > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Centro de costo creado correctamente.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose();
                }
            } else if (cud == CUD.UPDATE) {

                boolean actualizado =
                        centroCostoDAO.update(
                                centroCosto
                        );

                if (actualizado) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Centro de costo actualizado correctamente.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose();
                }
            } else if (cud == CUD.DELETE) {

                boolean eliminado =
                        centroCostoDAO.delete(
                                centroCosto
                        );

                if (eliminado) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Centro de costo eliminado correctamente.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose();
                }
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
        }
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
        mainPanel.setLayout(new GridLayoutManager(10, 5, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Codigo");
        mainPanel.add(label1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txtCodigo = new JTextField();
        mainPanel.add(txtCodigo, new GridConstraints(3, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Nombre");
        mainPanel.add(label2, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Responsable");
        mainPanel.add(label3, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Presupuesto");
        mainPanel.add(label4, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNombre = new JTextField();
        mainPanel.add(txtNombre, new GridConstraints(4, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtResponsable = new JTextField();
        mainPanel.add(txtResponsable, new GridConstraints(5, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtPresupuesto = new JTextField();
        mainPanel.add(txtPresupuesto, new GridConstraints(6, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cbEstado = new JComboBox();
        mainPanel.add(cbEstado, new GridConstraints(7, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Estado");
        mainPanel.add(label5, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnGuardar = new JButton();
        btnGuardar.setText("Guardar");
        mainPanel.add(btnGuardar, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCancelar = new JButton();
        btnCancelar.setText("Cancelar");
        mainPanel.add(btnCancelar, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(6, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        mainPanel.add(spacer4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$("Fira Code", Font.BOLD, 16, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Centros de Costo");
        mainPanel.add(label6, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        mainPanel.add(spacer5, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        mainPanel.add(spacer6, new GridConstraints(7, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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