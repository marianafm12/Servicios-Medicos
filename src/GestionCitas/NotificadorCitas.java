package GestionCitas;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase central que mantiene una lista de observadores y los notifica
 * cuando se libera una cita.
 */
public class NotificadorCitas {

    private static final List<ObservadorCita> observadores = new ArrayList<>();

    public static void registrarObservador(ObservadorCita obs) {
        observadores.add(obs);
    }

    public static void notificarCitaLiberada(String fecha, String hora, String servicio) {
        for (ObservadorCita obs : observadores) {
            obs.citaDisponible(fecha, hora, servicio);
        }
    }
}