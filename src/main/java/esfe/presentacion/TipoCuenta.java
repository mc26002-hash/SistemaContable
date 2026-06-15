package esfe.presentacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TipoCuenta extends JDialog {

    // Componentes vinculados automáticamente con tu archivo .form
    private JPanel mainPanel;
    private JPanel panelPrincipal;
    private JTable table1;
    private JButton nuevaCuentaButton;
    private JButton editarButton;
    private JButton deshabilitarButton;
    private JLabel lblTitulo2;

    // Constructor de la pantalla
    public TipoCuenta(JFrame padre) {
        super(padre, true); // La hace modal para que resalte sobre el formulario principal

        setTitle("Catálogo de Cuentas Contables");
        setContentPane(mainPanel); // Vincula el panel raíz de tu diseño
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(padre); // Centra la ventana en la pantalla

        // 1. Cargamos los datos en la tabla al abrir la ventana
        llenarTabla();

        // 2. EVENTO: Al dar clic en "Nueva Cuenta", abre la ventana de creación


        // EVENTO: Botón Editar
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí irá la lógica para modificar una cuenta seleccionada
            }
        });
        // <-- El bloque vacío que estaba aquí ya fue eliminado con éxito
        nuevaCuentaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    // Método para llenar la JTable con los datos de ejemplo
    private void llenarTabla() {
        String[] columnas = {"Código", "Nombre de Cuenta", "Tipo de Cuenta", "Estado"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);

        try {
            // Ejemplo de fila manual para probar el diseño visual:
            Object[] filaEjemplo = {"1101", "Caja General", "Activo", "Vigente"};
            modeloTabla.addRow(filaEjemplo);

            table1.setModel(modeloTabla);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos desde la base de datos: " + ex.getMessage(),
                    "Error de Carga",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}