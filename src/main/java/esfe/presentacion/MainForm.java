package esfe.presentacion;

import esfe.dominio.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {

    private Usuario usuarioAutenticado;

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public void setUsuarioAutenticado(Usuario usuarioAutenticado) {
        this.usuarioAutenticado = usuarioAutenticado;
    }

    public MainForm() {
        setTitle("Panel Principal - Sistema Contable");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        createMenu();
        createMainPanel();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // =========================
        // MENÚ ARCHIVO
        // =========================
        JMenu menuArchivo = new JMenu("Archivo");
        menuBar.add(menuArchivo);

        JMenuItem itemInicio = new JMenuItem("Inicio");
        JMenuItem itemSalir = new JMenuItem("Salir");

        menuArchivo.add(itemInicio);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        itemSalir.addActionListener(e -> System.exit(0));

        // =========================
        // MENÚ USUARIOS
        // =========================
        JMenu menuUsuarios = new JMenu("Usuarios");
        menuBar.add(menuUsuarios);

        JMenuItem itemGestionUsuarios = new JMenuItem("Gestión de Usuarios");

        menuUsuarios.add(itemGestionUsuarios);

        itemGestionUsuarios.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Este módulo se integrará después.",
                    "Módulo pendiente",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // =========================
        // MENÚ CONTABILIDAD
        // =========================
        JMenu menuContabilidad = new JMenu("Contabilidad");
        menuBar.add(menuContabilidad);

        // Submenú Catálogos
        JMenu menuCatalogos = new JMenu("Catálogos");
        menuContabilidad.add(menuCatalogos);

        // Items de Catálogos
        JMenuItem itemCatalogoCuentas = new JMenuItem("Catálogo de Cuentas");
        JMenuItem itemTipoDocumentoFiscal = new JMenuItem("Tipos de Documento Fiscal");

        menuCatalogos.add(itemCatalogoCuentas);
        menuCatalogos.add(itemTipoDocumentoFiscal);

        // Otros items de Contabilidad
        JMenuItem itemAsientos = new JMenuItem("Asientos Contables");
        JMenuItem itemPeriodos = new JMenuItem("Cierre de Periodos");

        menuContabilidad.addSeparator();
        menuContabilidad.add(itemAsientos);
        menuContabilidad.add(itemPeriodos);

        // Eventos
        itemCatalogoCuentas.addActionListener(e -> mostrarModuloPendiente());
        itemTipoDocumentoFiscal.addActionListener(e -> mostrarModuloPendiente());
        itemAsientos.addActionListener(e -> mostrarModuloPendiente());
        itemPeriodos.addActionListener(e -> mostrarModuloPendiente());

        // =========================
        // MENÚ REPORTES
        // =========================
        JMenu menuReportes = new JMenu("Reportes");
        menuBar.add(menuReportes);

        JMenuItem itemBalanceComprobacion = new JMenuItem("Balance de Comprobación");
        JMenuItem itemReportesFinancieros = new JMenuItem("Reportes Financieros");
        JMenuItem itemExportacionIVA = new JMenuItem("Exportación de IVA");

        menuReportes.add(itemBalanceComprobacion);
        menuReportes.add(itemReportesFinancieros);
        menuReportes.add(itemExportacionIVA);

        itemBalanceComprobacion.addActionListener(e -> mostrarModuloPendiente());
        itemReportesFinancieros.addActionListener(e -> mostrarModuloPendiente());
        itemExportacionIVA.addActionListener(e -> mostrarModuloPendiente());

        // =========================
        // MENÚ HERRAMIENTAS
        // =========================
        JMenu menuHerramientas = new JMenu("Herramientas");
        menuBar.add(menuHerramientas);

        JMenuItem itemCierrePeriodos = new JMenuItem("Cierre de Periodos");
        JMenuItem itemCalculadora = new JMenuItem("Calculadora");

        menuHerramientas.add(itemCierrePeriodos);
        menuHerramientas.add(itemCalculadora);

        itemCierrePeriodos.addActionListener(e -> mostrarModuloPendiente());
        itemCalculadora.addActionListener(e -> mostrarModuloPendiente());

        // =========================
        // MENÚ AYUDA
        // =========================
        JMenu menuAyuda = new JMenu("Ayuda");
        menuBar.add(menuAyuda);

        JMenuItem itemManualUsuario = new JMenuItem("Manual de Usuario");

        menuAyuda.add(itemManualUsuario);

        itemManualUsuario.addActionListener(e -> mostrarModuloPendiente());
    }

    private void mostrarModuloPendiente() {
        JOptionPane.showMessageDialog(this,
                "Este módulo se integrará después.",
                "Módulo pendiente",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        JLabel lblTitulo = new JLabel("Bienvenido al Sistema de Contabilidad");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(1, 4, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panelBotones.setBackground(Color.LIGHT_GRAY);

        JButton btnUsuarios = new JButton("Gestión de Usuarios");
        JButton btnCatalogo = new JButton("Catálogo de Cuentas");
        JButton btnReportes = new JButton("Reportes Financieros");
        JButton btnCierre = new JButton("Cierre de Periodos");

        panelBotones.add(btnUsuarios);
        panelBotones.add(btnCatalogo);
        panelBotones.add(btnReportes);
        panelBotones.add(btnCierre);

        panel.add(panelBotones, BorderLayout.CENTER);

        setContentPane(panel);
    }
}