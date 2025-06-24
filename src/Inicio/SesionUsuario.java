package Inicio;

public class SesionUsuario {
    private static int idPaciente;
    private static String nombreMedico; // nuevo
    private static boolean esMedico = false; // nuevo

    // --- PACIENTE ---
    public static void iniciarSesionPaciente(int id) {
        idPaciente = id;
        esMedico = false;
    }

    public static void iniciarSesion(int id) {
        idPaciente = id;
    }

    public static int getPacienteActual() {
        return idPaciente;
    }

    // --- MÉDICO ---
    public static void iniciarSesionMedico(String nombre) {
        nombreMedico = nombre;
        esMedico = true;
    }

    public static String getMedicoActual() {
        return nombreMedico;
    }

    public static boolean isMedico() {
        return esMedico;
    }

    public static void cerrarSesion() {
        idPaciente = 0;
        nombreMedico = null;
        esMedico = false;
    }
}
