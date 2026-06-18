package esfe.utils;

import java.nio.charset.StandardCharsets; // Clase que define juegos de caracteres estándar, como UTF-8, utilizado para codificar la contraseña antes de hashearla.
import java.security.MessageDigest;      // Clase que proporciona funcionalidades para algoritmos de resumen de mensajes criptográficos, como SHA-256, para hashear contraseñas.
import java.security.NoSuchAlgorithmException; // Clase para manejar excepciones que ocurren cuando un algoritmo criptográfico solicitado no está disponible en el entorno.
import java.util.Base64;                 // Clase utilitaria para codificar y decodificar datos en formato Base64, aunque en este contexto (hasheo de contraseñas) no se utiliza directamente para el hash en sí, sino que podría usarse para codificar el hash resultante para su almacenamiento.

public class PasswordHasher {

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    public static byte[] hashPasswordBytes(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
}