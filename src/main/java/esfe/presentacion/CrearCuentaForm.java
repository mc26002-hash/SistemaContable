package esfe.presentacion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrearCuentaForm extends JDialog {

    // Componentes vinculados automáticamente con tu archivo .form
    private JPanel mainPanel;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtTipo;
    private JTextField txtEstado;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JLabel lblTitulo;

    // Quité las variables genéricas (textField1, etc.) para que tu código esté más limpio

    // MODIFICADO: Ahora recibe directamente a TipoCuenta como ventana padre
    public CrearCuentaForm(TipoCuenta padre) {
        super(padre, true); // Sigue siendo modal (bloquea la de atrás)

        setTitle("Nueva Cuenta");
        setContentPane(mainPanel); // Vincula tu diseño visual
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();

        if (padre != null) {
            setLocationRelativeTo(padre); // Se centra justo encima del catálogo
        } else {
            setLocationRelativeTo(null); // Se centra en el medio de la pantalla
        }

        // EVENTO: Botón Cancelar
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra esta ventana flotante sin hacer nada
            }
        });

        // EVENTO: Botón Guardar
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarRegistro();
            }
        });
    }

    // Método para procesar los datos ingresados
    private void guardarRegistro() {
        String codigo = txtCodigo.getText();
        String nombre = txtNombre.getText();
        String tipo = txtTipo.getText();
        String estado = txtEstado.getText();

        // Validación de que no dejen campos en blanco
        if (codigo.isEmpty() || nombre.isEmpty() || tipo.isEmpty() || estado.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos obligatorios.",
                    "Campos Vacíos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // TODO: Aquí conectarás con tu capa de negocio/datos para hacer el INSERT en SQL Server

        JOptionPane.showMessageDialog(this, "¡Cuenta guardada exitosamente!");
        dispose(); // Cierra la pantalla al terminar
    }
}
