package Registro;

public class ValidadorPaciente {

    /**
     * Valida que el ID sea un número entre 180000 y 999999.
     */
    public static boolean esIDValido(String id) {
        try {
            int valor = Integer.parseInt(id);
            return valor >= 180000 && valor <= 999999;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valida que un texto contenga únicamente letras y espacios.
     */
    public static boolean esTextoAlfabetico(String texto) {
        return texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+");
    }

    /**
     * Valida que un correo tenga el formato usuario@dominio.com.
     */
    public static boolean esCorreoValido(String correo) {
        return correo.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    }

    /**
     * Valida que un campo contenga únicamente números o decimales positivos.
     */
    public static boolean esNumerico(String dato) {
        return dato.matches("^\\d+(\\.\\d+)?$");
    }

    /**
     * Valida que el texto contenga letras, números y signos de puntuación básicos.
     */
    public static boolean esAlfanumerico(String texto) {
        return texto.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.,;:!¿?()\\-]*");
    }

    public static boolean estanTodosLlenos(String... campos) {
        for (String campo : campos) {
            if (campo == null || campo.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
