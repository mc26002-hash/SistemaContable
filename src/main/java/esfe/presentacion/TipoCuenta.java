package esfe.presentacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class TipoCuenta extends JDialog {

    private JPanel mainPanel;
    private JPanel panelPrincipal;
    private JTable table1;
    private JButton nuevaCuentaButton;
    private JButton editarButton;
    private JButton deshabilitarButton;
    private JLabel lblTitulo2;

    public TipoCuenta(JFrame padre) {
        super(padre, true);
        setTitle("Catálogo de Cuentas Contables");
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(padre);

        llenarTabla();

        nuevaCuentaButton.addActionListener(e -> {
            CrearCuentaForm pantallaCrear = new CrearCuentaForm(TipoCuenta.this);
            pantallaCrear.setVisible(true);
            llenarTabla(); // Se refresca la tabla automáticamente al cerrar la creación
        });
    }

    private void llenarTabla() {
        // ─── CORREGIDO: Solo 3 columnas exactas de la BD ───
        String[] columnas = {"Código (Id)", "Nombre de Tipo", "Naturaleza"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);

        String url = "jdbc:sqlserver://ContabilidadESFE.mssql.somee.com:1433;databaseName=ContabilidadESFE;encrypt=true;trustServerCertificate=true;";
        String usuario = "ContabilidadGab_SQLLogin_1";
        String password = "a7l6kuot7x";
        String sql = "SELECT TipoCuentaId, NombreTipo, Naturaleza FROM TiposCuenta";

        try {
            Connection con = DriverManager.getConnection(url, usuario, password);
            if (con != null) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    Object[] filaReal = new Object[3];
                    filaReal[0] = rs.getInt("TipoCuentaId");
                    filaReal[1] = rs.getString("NombreTipo");

                    // Traducimos 'D' y 'H' para que el usuario lo entienda mejor
                    String nat = rs.getString("Naturaleza");
                    filaReal[2] = (nat != null && nat.equalsIgnoreCase("H")) ? "Haber (Pasivo/Patr./Ing.)" : "Debe (Activo/Gasto)";

                    modeloTabla.addRow(filaReal);
                }
                rs.close();
                stmt.close();
                con.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar desde Somee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // Respaldo rápido de 3 columnas
            modeloTabla.addRow(new Object[]{"1", "Activo", "Debe (D)"});
        }
        table1.setModel(modeloTabla);
    }
}