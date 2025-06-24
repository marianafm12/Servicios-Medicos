package GestionCitas;

import BaseDeDatos.ConexionSQLite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDAO {

    public static class Notificacion {
        public int idNotificacion;
        public int idPaciente;
        public String fecha;
        public String hora;
        public String servicio;

        public Notificacion(int idNotificacion, int idPaciente, String fecha, String hora, String servicio) {
            this.idNotificacion = idNotificacion;
            this.idPaciente = idPaciente;
            this.fecha = fecha;
            this.hora = hora;
            this.servicio = servicio;
        }
    }

    public synchronized static void agregarNotificacion(String idPaciente, String fecha, String hora, String servicio)
            throws SQLException {
        final int MAX_REINTENTOS = 3;
        int intentos = 0;
        boolean completado = false;

        while (intentos < MAX_REINTENTOS && !completado) {
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "INSERT INTO Notificaciones (idPaciente, mensaje, estado, fecha, hora, servicio) " +
                        "VALUES (?, ?, 'pendiente', ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, idPaciente);
                    stmt.setString(2, "Se ha liberado una cita para " + servicio + " el " + fecha + " a las " + hora);
                    stmt.setString(3, fecha);
                    stmt.setString(4, hora);
                    stmt.setString(5, servicio);
                    stmt.executeUpdate();
                    completado = true;
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("database is locked")) {
                    intentos++;
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    System.err.println("Error al agregar notificación: " + e.getMessage());
                    throw e;
                }
            }
        }

        if (!completado) {
            throw new SQLException("No se pudo agregar la notificación tras varios intentos.");
        }
    }

    public static List<Notificacion> obtenerNotificaciones(int idPaciente) {
        List<Notificacion> notificaciones = new ArrayList<>();

        try (Connection conn = ConexionSQLite.conectar()) {
            String sql = "SELECT * FROM Notificaciones WHERE idPaciente = ? AND estado = 'pendiente'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idPaciente);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        notificaciones.add(new Notificacion(
                                rs.getInt("idNotificacion"),
                                rs.getInt("idPaciente"),
                                rs.getString("fecha"),
                                rs.getString("hora"),
                                rs.getString("servicio")));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener notificaciones: " + e.getMessage());
        }

        return notificaciones;
    }

    public static boolean tieneNotificacionesNoLeidas(int idPaciente) {
        boolean tiene = false;
        String sql = "SELECT COUNT(*) FROM Notificaciones WHERE idPaciente = ? AND estado = 'pendiente'";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt(1);
                    tiene = total > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar notificaciones no leídas: " + e.getMessage());
        }

        return tiene;
    }

    public synchronized static void eliminarNotificacion(int idNotificacion) {
        final int MAX_REINTENTOS = 3;
        int intentos = 0;
        boolean completado = false;

        while (intentos < MAX_REINTENTOS && !completado) {
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "DELETE FROM Notificaciones WHERE idNotificacion = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, idNotificacion);
                    stmt.executeUpdate();
                    completado = true;
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("database is locked")) {
                    intentos++;
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    System.err.println("Error al eliminar notificación: " + e.getMessage());
                    break;
                }
            }
        }

        if (!completado) {
            System.err.println("No se pudo eliminar notificación tras varios intentos.");
        }
    }

    public synchronized static void marcarComoAtendida(int idNotificacion) {
        final int MAX_REINTENTOS = 3;
        int intentos = 0;
        boolean completado = false;

        while (intentos < MAX_REINTENTOS && !completado) {
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "UPDATE Notificaciones SET estado = 'atendida' WHERE idNotificacion = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, idNotificacion);
                    stmt.executeUpdate();
                    completado = true;
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("database is locked")) {
                    intentos++;
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    System.err.println("Error al marcar como atendida: " + e.getMessage());
                    break;
                }
            }
        }

        if (!completado) {
            System.err.println("No se pudo marcar notificación como atendida tras varios intentos.");
        }
    }
}