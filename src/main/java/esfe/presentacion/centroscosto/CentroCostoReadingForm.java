package esfe.presentacion.centroscosto;

import esfe.dominio.CentroCosto;
import esfe.persistencia.CentroCostoDAO;
import esfe.utils.CUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

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

        pack();
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
}