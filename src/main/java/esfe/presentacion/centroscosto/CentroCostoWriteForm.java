package esfe.presentacion.centroscosto;

import esfe.dominio.CentroCosto;
import esfe.persistencia.CentroCostoDAO;
import esfe.utils.CUD;

import javax.swing.*;
import java.math.BigDecimal;

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

        pack();
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
            }

            else if (cud == CUD.UPDATE) {

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
            }

            else if (cud == CUD.DELETE) {

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
}