package GestionCitas;

/**
 * Interfaz para los componentes que desean ser notificados cuando una cita se
 * libera.
 */
public interface ObservadorCita {
    void citaDisponible(String fecha, String hora, String servicio);
}