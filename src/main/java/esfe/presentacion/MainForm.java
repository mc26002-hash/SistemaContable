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

        //MENU DE OPCIONES
        JMenu menuArchivo = new JMenu("Archivo");
        JMenu menuUsuarios = new JMenu("Usuarios");
        JMenu menuContabilidad = new JMenu("Contabilidad");
        JMenu menuReportes = new JMenu("Reportes");
        JMenu menuHerramientas = new JMenu("Herramientas");
        JMenu menuAyuda = new JMenu("Ayuda");

        menuBar.add(menuArchivo);
        menuBar.add(menuUsuarios);
        menuBar.add(menuContabilidad);
        menuBar.add(menuReportes);
        menuBar.add(menuHerramientas);
        menuBar.add(menuAyuda);

        //OPCIONES SELECT DE ARCHIVO
        JMenuItem itemInicio = new JMenuItem("Inicio");
        JMenuItem itemCatalogoDeCuentas = new JMenuItem("Catalogo de Cuentas");
        JMenuItem itemNuevoAsientoContable = new JMenuItem("Nuevo Asiento Contable");
        JMenuItem itemSalir = new JMenuItem("Salir");

        menuArchivo.add(itemInicio);
        menuArchivo.add(itemCatalogoDeCuentas);
        menuArchivo.add(itemNuevoAsientoContable);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        //LOGICA PARA ABRIR LA NUEVA VENTADA DE LA SECCIÓN
        itemCatalogoDeCuentas.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                            "Este módulo se integrará después.",
                            "Módulo pendiente",
                            JOptionPane.INFORMATION_MESSAGE);
            });


        itemSalir.addActionListener(e -> System.exit(0));

        //OPCIONES SELECT DE USUARIOS
        JMenuItem itemGestionUsuarios = new JMenuItem("Gestión de Usuarios");

        menuUsuarios.add(itemGestionUsuarios);

        //LOGICA PARA ABRIR LA NUEVA VENTADA DE LA SECCIÓN
        itemGestionUsuarios.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Este módulo se integrará después.",
                    "Módulo pendiente",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        //OPCIONES SELECT DE CONTABILIDAD
        JMenuItem itemCatalogoCuentas = new JMenuItem("Catálogo de Cuentas");
        JMenuItem itemAsientos = new JMenuItem("Asientos Contables");
        JMenuItem itemPeriodos = new JMenuItem("Cierre de Periodos");

        menuContabilidad.add(itemCatalogoCuentas);
        menuContabilidad.add(itemAsientos);
        menuContabilidad.add(itemPeriodos);

        //OPCIONES SELECT DE REPORTES
        JMenuItem itemBalanceComprobacion = new JMenuItem("Balance de Comprobación");
        JMenuItem itemReportesFinancieros = new JMenuItem("Reportes Financieros");
        JMenuItem itemExportacionIVA = new JMenuItem("Exportación de IVA");

        menuReportes.add(itemBalanceComprobacion);
        menuReportes.add(itemReportesFinancieros);
        menuReportes.add(itemExportacionIVA);

        //OPCIONES SELECT DE HERRAMIENTAS
        JMenuItem itemCierrePeriodos = new JMenuItem("Cierre de Periodos");
        JMenuItem itemCalculadora = new JMenuItem("Calculadora");

        menuHerramientas.add(itemCierrePeriodos);
        menuHerramientas.add(itemCalculadora);

        //OPCIONES SELECT DE AYUDA
        JMenuItem itemManualUsuario = new JMenuItem("Manual de Usuario");
        menuAyuda.add(itemManualUsuario);
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