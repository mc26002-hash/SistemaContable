package esfe.presentacion;

import esfe.dominio.Usuario;
import esfe.presentacion.contabilidad.DocumentoFiscalForm;
import esfe.presentacion.contabilidad.TercerosForm;
import esfe.presentacion.contabilidad.TipoDocumentoFiscalForm;
import esfe.presentacion.centroscosto.CentroCostoReadingForm;
import esfe.presentacion.contabilidad.TipoPartidaForm;
import esfe.presentacion.contabilidad.tipocuenta.TipoCuenta;
import esfe.presentacion.usuario.RolListadoForm;
import esfe.presentacion.usuario.UsuarioReadingForm;
import java.awt.*;


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

//    CON LOGIN
    public MainForm(Usuario usuarioAutenticado) {
        this.usuarioAutenticado = usuarioAutenticado;

        setTitle("Panel Principal - Sistema Contable");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        createMenu();
        createMainPanel();
    }

    //SIN LOGIN
//    public MainForm() {
//        setTitle("Panel Principal - Sistema Contable");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(1100, 600);
//        setLocationRelativeTo(null);
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//
//        createMenu();
//        createMainPanel();
//    }

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

        itemInicio.addActionListener(e -> mostrarModuloPendiente());
        itemSalir.addActionListener(e -> System.exit(0));

        // =========================
        // MENÚ USUARIOS
        // =========================
        JMenu menuUsuarios = new JMenu("Usuarios");
        menuBar.add(menuUsuarios);

        JMenuItem itemUsuarios = new JMenuItem("Gestión de Usuarios");
        JMenuItem itemRoles = new JMenuItem("Gestión de Roles");

        menuUsuarios.add(itemUsuarios);
        menuUsuarios.add(itemRoles);

        itemUsuarios.addActionListener(e -> {
            UsuarioReadingForm form = new UsuarioReadingForm();
            form.setVisible(true);
        });

        itemRoles.addActionListener(e -> {
            RolListadoForm form = new RolListadoForm();
            form.setVisible(true);
        });

        // =========================
        // MENÚ CONTABILIDAD
        // =========================
        JMenu menuContabilidad = new JMenu("Contabilidad");
        menuBar.add(menuContabilidad);

        JMenu menuCatalogos = new JMenu("Catálogos");
        JMenu menuCuentas = new JMenu("Cuentas Contables");
        JMenu menuDocumentos = new JMenu("Documentos Fiscales");

        menuContabilidad.add(menuCatalogos);
        menuContabilidad.add(menuCuentas);
        menuContabilidad.add(menuDocumentos);

        JMenuItem itemTerceros = new JMenuItem("Gestión de Terceros");
        JMenuItem itemTipoDocumentoFiscal = new JMenuItem("Gestión de Tipos de Documento Fiscal");
        JMenuItem itemTipoPartida = new JMenuItem("Gestión de Tipos de Partida");

        menuCatalogos.add(itemTerceros);
        menuCatalogos.add(itemTipoDocumentoFiscal);
        menuCatalogos.add(itemTipoPartida);

        JMenuItem itemTipoCuenta = new JMenuItem("Gestión de Tipos de Cuenta");
        menuCuentas.add(itemTipoCuenta);

        JMenuItem itemDocumentosFiscales = new JMenuItem("Gestión de Documentos Fiscales");
        menuDocumentos.add(itemDocumentosFiscales);

        menuContabilidad.addSeparator();

        JMenuItem itemAsientos = new JMenuItem("Gestión de Asientos Contables");
        JMenuItem itemPeriodos = new JMenuItem("Cierre de Periodos");

        menuContabilidad.add(itemAsientos);
        menuContabilidad.add(itemPeriodos);

        itemTerceros.addActionListener(e -> {
            TercerosForm form = new TercerosForm(this);
            form.setVisible(true);
        });

        itemTipoDocumentoFiscal.addActionListener(e -> {
            TipoDocumentoFiscalForm form = new TipoDocumentoFiscalForm(this);
            form.setVisible(true);
        });

        itemTipoPartida.addActionListener(e -> {
            TipoPartidaForm form = new TipoPartidaForm(this);
            form.setVisible(true);
        });

        itemTipoCuenta.addActionListener(e -> {
            TipoCuenta form = new TipoCuenta(this);
            form.setVisible(true);
        });

        itemDocumentosFiscales.addActionListener(e -> {
            DocumentoFiscalForm form = new DocumentoFiscalForm(this);
            form.setVisible(true);
        });

        itemAsientos.addActionListener(e -> mostrarModuloPendiente());
        itemPeriodos.addActionListener(e -> mostrarModuloPendiente());

        // =========================
        // MENÚ CENTROS DE COSTO
        // =========================
        JMenu menuCentrosCosto = new JMenu("Centros de Costo");
        menuBar.add(menuCentrosCosto);

        JMenuItem itemCentrosCosto = new JMenuItem("Gestión de Centros de Costo");
        menuCentrosCosto.add(itemCentrosCosto);

        itemCentrosCosto.addActionListener(e -> {
            CentroCostoReadingForm form = new CentroCostoReadingForm();
            form.setVisible(true);
        });

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

        JMenuItem itemCalculadora = new JMenuItem("Calculadora");
        menuHerramientas.add(itemCalculadora);

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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(35, 45, 65));
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel lblTitulo = new JLabel("Sistema de Contabilidad");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblUsuario = new JLabel("Usuario: Administrador");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 13));
        lblUsuario.setForeground(new Color(220, 220, 220));

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(lblUsuario, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);

        // CONTENIDO
        JPanel contenido = new JPanel(new BorderLayout(20, 20));
        contenido.setBackground(new Color(245, 245, 245));
        contenido.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));

        JPanel accesos = new JPanel(new GridLayout(2, 2, 18, 18));
        accesos.setBackground(new Color(245, 245, 245));

        JButton btnUsuarios = crearTarjetaMenu("Usuarios");
        JButton btnDocumentos = crearTarjetaMenu("Documentos\nFiscales");
        JButton btnCentrosCosto = crearTarjetaMenu("Centros\nde Costo");
        JButton btnReportes = crearTarjetaMenu("Reportes");

        btnUsuarios.addActionListener(e -> new UsuarioReadingForm().setVisible(true));
        btnDocumentos.addActionListener(e -> new DocumentoFiscalForm(this).setVisible(true));
        btnCentrosCosto.addActionListener(e -> new CentroCostoReadingForm().setVisible(true));
        btnReportes.addActionListener(e -> mostrarModuloPendiente());

        accesos.add(btnUsuarios);
        accesos.add(btnDocumentos);
        accesos.add(btnCentrosCosto);
        accesos.add(btnReportes);

        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBackground(Color.WHITE);
        lateral.setPreferredSize(new Dimension(230, 0));
        lateral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblResumen = new JLabel("Resumen del sistema");
        lblResumen.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel lbl1 = new JLabel("• Base de datos conectada");
        JLabel lbl2 = new JLabel("• Módulos activos");
        JLabel lbl3 = new JLabel("• Sistema listo para operar");
        JLabel lbl4 = new JLabel("• Versión 1.0");

        lateral.add(lblResumen);
        lateral.add(Box.createVerticalStrut(15));
        lateral.add(lbl1);
        lateral.add(Box.createVerticalStrut(8));
        lateral.add(lbl2);
        lateral.add(Box.createVerticalStrut(8));
        lateral.add(lbl3);
        lateral.add(Box.createVerticalStrut(8));
        lateral.add(lbl4);

        contenido.add(accesos, BorderLayout.CENTER);
        contenido.add(lateral, BorderLayout.EAST);

        panel.add(contenido, BorderLayout.CENTER);

        // FOOTER
        JLabel footer = new JLabel("ESFE AGAPE 2026  |  Sistema Contable v1.0", JLabel.CENTER);
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.setForeground(new Color(100, 100, 100));
        footer.setBorder(BorderFactory.createEmptyBorder(8, 8, 12, 8));

        panel.add(footer, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private JButton crearTarjetaMenu(String texto) {
        JButton boton = new JButton("<html><center>" + texto.replace("\n", "<br>") + "</center></html>");

        boton.setPreferredSize(new Dimension(150, 90));
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.setBackground(Color.WHITE);
        boton.setForeground(new Color(40, 40, 40));

        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        return boton;
    }
}