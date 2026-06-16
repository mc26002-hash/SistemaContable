package esfe.presentacion;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class CrearCuentaForm extends JDialog {

    private JPanel mainPanel;
    private JTextField txtCodigo;  // Visual/Ignorado en el INSERT (porque es autoincrementable)
    private JTextField txtNombre;  // Mapea a NombreTipo
    private JTextField txtTipo;    // Mapea a Naturaleza ('D' o 'H')
    private JTextField txtEstado;  // Visual/Ignorado
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JLabel lblTitulo;

    public CrearCuentaForm(TipoCuenta padre) {
        super(padre, true);
        setTitle("Nueva Cuenta");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(padre);

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardarRegistro());
    }

    private void guardarRegistro() {
        String nombre = txtNombre.getText().trim();
        String tipoStr = txtTipo.getText().trim();

        // ─── CORREGIDO: Ya no es obligatorio validar el código aquí ───
        if (nombre.isEmpty() || tipoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete los campos Nombre y Tipo.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String url = "jdbc:sqlserver://ContabilidadESFE.mssql.somee.com:1433;databaseName=ContabilidadESFE;encrypt=true;trustServerCertificate=true;";
        String usuario = "ContabilidadGab_SQLLogin_1";
        String password = "a7l6kuot7x";

        // ─── CORREGIDO: Quitamos TipoCuentaId para que SQL Server lo cree solo ───
        String sql = "INSERT INTO TiposCuenta (NombreTipo, Naturaleza) VALUES (?, ?)";

        try {
            // Mapeo automático de Naturaleza ('D' o 'H')
            char naturalezaChar = 'D';
            if (!tipoStr.isEmpty()) {
                char inicial = Character.toUpperCase(tipoStr.charAt(0));
                if (inicial == 'H' || inicial == 'P' || inicial == 'I' || inicial == 'A') {
                    naturalezaChar = 'H';
                }
            }

            Connection con = DriverManager.getConnection(url, usuario, password);
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);

                // ─── CORREGIDO: Ahora solo enviamos 2 parámetros ───
                ps.setString(1, nombre);
                ps.setString(2, String.valueOf(naturalezaChar));

                int filasAfectadas = ps.executeUpdate();
                ps.close();
                con.close();

                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(this, "¡Registro guardado con éxito en Somee!");
                    dispose();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de SQL Server: " + ex.getMessage(), "Error al Guardar", JOptionPane.ERROR_MESSAGE);
        }
    }
}