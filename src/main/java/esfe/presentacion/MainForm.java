package esfe.presentacion;

import esfe.dominio.CentroCosto;
import esfe.dominio.Usuario;
import esfe.presentacion.contabilidad.DocumentoFiscalForm;
import esfe.presentacion.contabilidad.TercerosForm;
import esfe.presentacion.contabilidad.TipoDocumentoFiscalForm;
import esfe.presentacion.centroscosto.CentroCostoReadingForm;
import esfe.presentacion.centroscosto.CentroCostoWriteForm;
import esfe.presentacion.contabilidad.TipoPartidaForm;
import esfe.presentacion.contabilidad.tipocuenta.TipoCuenta;
import esfe.presentacion.usuario.ChangePasswordForm;
import esfe.presentacion.usuario.RolListadoForm;
import esfe.presentacion.usuario.UsuarioReadingForm;
import esfe.utils.CUD;

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

    //CON LOGIN
//    public MainForm(Usuario usuarioAutenticado) {
//        this.usuarioAutenticado = usuarioAutenticado;
//
//        setTitle("Panel Principal - Sistema Contable");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(1100, 600);
//        setLocationRelativeTo(null);
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//
//        createMenu();
//        createMainPanel();
//    }

    //SIN LOGIN
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

        itemInicio.addActionListener(e -> mostrarModuloPendiente());
        itemSalir.addActionListener(e -> System.exit(0));

        // =========================
        // MENÚ USUARIOS
        // =========================
        JMenu menuUsuarios = new JMenu("Usuarios");
        menuBar.add(menuUsuarios);

        JMenuItem itemUsuarios = new JMenuItem("Gestión de Usuarios");
        JMenuItem itemRoles = new JMenuItem("Roles");

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

        JMenuItem itemTerceros = new JMenuItem("Terceros");
        JMenuItem itemTipoDocumentoFiscal = new JMenuItem("Tipos de Documento Fiscal");
        JMenuItem itemTipoPartida = new JMenuItem("Tipos de Partida");

        menuCatalogos.add(itemTerceros);
        menuCatalogos.add(itemTipoDocumentoFiscal);
        menuCatalogos.add(itemTipoPartida);

        JMenuItem itemTipoCuenta = new JMenuItem("Tipos de Cuenta");
        JMenuItem itemCrearCuenta = new JMenuItem("Crear Cuenta");

        menuCuentas.add(itemTipoCuenta);
        menuCuentas.add(itemCrearCuenta);

        JMenuItem itemDocumentosFiscales = new JMenuItem("Gestión de Documentos Fiscales");

        menuDocumentos.add(itemDocumentosFiscales);

        menuContabilidad.addSeparator();

        JMenuItem itemAsientos = new JMenuItem("Asientos Contables");
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

        itemCrearCuenta.addActionListener(e -> {
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

        JMenuItem itemVerCentrosCosto = new JMenuItem("Ver Centros de Costo");
        JMenuItem itemCrearCentroCosto = new JMenuItem("Crear Centro de Costo");

        menuCentrosCosto.add(itemVerCentrosCosto);
        menuCentrosCosto.add(itemCrearCentroCosto);

        itemVerCentrosCosto.addActionListener(e -> {
            CentroCostoReadingForm form = new CentroCostoReadingForm();
            form.setVisible(true);
        });

        itemCrearCentroCosto.addActionListener(e -> {
            CentroCosto centroCosto = new CentroCosto();
            CentroCostoWriteForm form = new CentroCostoWriteForm(CUD.CREATE, centroCosto);
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

        JButton btnUsuarios = new JButton("Usuarios");
        JButton btnDocumentos = new JButton("Documentos Fiscales");
        JButton btnCentrosCosto = new JButton("Centros de Costo");
        JButton btnReportes = new JButton("Reportes");

        btnUsuarios.addActionListener(e -> {
            UsuarioReadingForm form = new UsuarioReadingForm();
            form.setVisible(true);
        });

        btnDocumentos.addActionListener(e -> {
            DocumentoFiscalForm form = new DocumentoFiscalForm(this);
            form.setVisible(true);
        });

        btnCentrosCosto.addActionListener(e -> {
            CentroCostoReadingForm form = new CentroCostoReadingForm();
            form.setVisible(true);
        });

        btnReportes.addActionListener(e -> mostrarModuloPendiente());

        panelBotones.add(btnUsuarios);
        panelBotones.add(btnDocumentos);
        panelBotones.add(btnCentrosCosto);
        panelBotones.add(btnReportes);

        panel.add(panelBotones, BorderLayout.CENTER);

        setContentPane(panel);
    }
}