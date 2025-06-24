package GestionCitas;

import java.util.List;
import Modelo.Espera;

public class NotificadorListaEspera {

    public static void notificarDisponibilidad(String fecha, String hora, String servicio) {
        try {
            List<Espera> solicitudes = ListaEsperaDAO.obtenerSolicitudesPara(fecha, hora, servicio);

            for (Espera espera : solicitudes) {
                // 🔔 Generar notificación persistente para el paciente
                NotificacionDAO.agregarNotificacion(
                        espera.getIdPaciente(),
                        fecha,
                        hora,
                        servicio);

                // 🟢 Marcar como notificada en la tabla de lista de espera
                ListaEsperaDAO.marcarNotificada(
                        espera.getIdPaciente(),
                        fecha,
                        hora,
                        servicio);
            }
        } catch (Exception e) {
            System.err.println("Error al notificar disponibilidad: " + e.getMessage());
        }
    }
}