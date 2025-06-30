package Inicio;

public class SesionUsuario {
    private static int idPaciente;
    private static String nombreMedico;           // nombre del médico logueado
    private static boolean esMedico = false;      // indica si el usuario es médico
    private static int emergenciaSeleccionada;    // ID de la emergencia seleccionada

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

    // --- EMERGENCIA SELECCIONADA ---
    /**
     * Guarda el ID de la emergencia que el usuario ha seleccionado
     * para luego mostrar su detalle.
     */
    public static void setEmergenciaSeleccionada(int idEmergencia) {
        emergenciaSeleccionada = idEmergencia;
    }

    /**
     * Devuelve el ID de la emergencia previamente guardada.
     */
    public static int getEmergenciaSeleccionada() {
        return emergenciaSeleccionada;
    }

    // --- CERRAR SESIÓN ---
    public static void cerrarSesion() {
        idPaciente = 0;
        nombreMedico = null;
        esMedico = false;
        emergenciaSeleccionada = 0;
    }
}
