package esfe.utils;

import java.awt.Window;

public class WindowConfig {

    public static final int WIDTH = 650;
    public static final int HEIGHT = 500;

    public static void configurarVentana(Window window) {
        window.setSize(WIDTH, HEIGHT);
        window.setLocationRelativeTo(null);
    }
}