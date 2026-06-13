package esfe.presentacion;

import esfe.presentacion.CrearCuentaForm; // Asegúrate de que el nombre de tu otra pantalla sea este
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JDialog {

    // Componentes vinculados automáticamente con tu archivo .form
    private JPanel mainPanel;
    private JPanel panelPrincipal;
    private JTable table1;
    private JButton nuevaCuentaButton;
    private JButton editarButton;
    private JButton deshabilitarButton;
    private JLabel lblTitulo2;

    // Constructor de la pantalla
    public LoginForm(JFrame padre) {
        super(padre, true); // La hace modal para que resalte sobre el formulario principal

        setTitle("Catálogo de Cuentas Contables");
        setContentPane(mainPanel); // Vincula el panel raíz de tu diseño
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(padre); // Centra la ventana en la pantalla

        // 1. Cargamos los datos de SQL Server en la tabla al abrir la ventana
        llenarTabla();

        // 2. EVENTO: Al dar clic en "Nueva Cuenta", abre la ventana de creación
        nuevaCuentaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Le pasamos 'LoginForm.this' para que actúe como ventana padre legítima
                CrearCuentaForm pantallaCrear = new CrearCuentaForm(LoginForm.this);
                pantallaCrear.setVisible(true);
            }
        });

        // EVENTO: Botón Editar (opcional por ahora)
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí irá la lógica para modificar una cuenta seleccionada
            }
        });
    }

    // Método para llenar la JTable con los datos de tu BD
    private void llenarTabla() {
        // Creamos las columnas que llevará nuestra tabla
        String[] columnas = {"Código", "Nombre de Cuenta", "Tipo de Cuenta", "Estado"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);

        try {
            // TODO: Aquí llamarás a tu capa de datos (TipoCuentaDAO)
            // Ejemplo de cómo agregar una fila manual para probar que pinte en pantalla:
            Object[] filaEjemplo = {"1101", "Caja General", "Activo", "Vigente"};
            modeloTabla.addRow(filaEjemplo);

            // Le inyectamos el modelo con los datos a tu componente table1
            table1.setModel(modeloTabla);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos desde la base de datos: " + ex.getMessage(),
                    "Error de Carga",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}