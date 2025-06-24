package GestionCitas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import BaseDeDatos.ConexionSQLite;
import Modelo.Espera;

public class ListaEsperaDAO {

    public synchronized static void registrarEnEspera(String idPaciente, String fecha, String hora, String servicio)
            throws SQLException {

        // Normaliza formato de hora a HH:mm
        if (!hora.contains(":")) {
            hora = hora + ":00";
        }

        if (yaRegistradoEnLista(idPaciente, fecha, hora, servicio)) {
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null,
                    "Ya estás en lista de espera para esta cita.",
                    "Registro duplicado",
                    JOptionPane.WARNING_MESSAGE)
            );
            return;
        }

        final int MAX_REINTENTOS = 5;
        int intentos = 0;
        boolean completado = false;

        while (intentos < MAX_REINTENTOS && !completado) {
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "INSERT INTO ListaEsperaCitas(idPaciente, fechaDeseada, horaDeseada, servicio, estado) " +
                             "VALUES (?, ?, ?, ?, 'pendiente')";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, Integer.parseInt(idPaciente.trim()));
                    stmt.setString(2, fecha);
                    stmt.setString(3, hora);
                    stmt.setString(4, servicio);
                    stmt.executeUpdate();
                    completado = true;
                }

                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null,
                        "Registrado correctamente en lista de espera.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE)
                );

            } catch (SQLException e) {
                if (e.getMessage().contains("database is locked")) {
                    intentos++;
                    try {
                        Thread.sleep(500); // espera medio segundo antes de reintentar
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    mostrarError("Error al insertar en lista de espera:\n" + e.getMessage());
                    throw e;
                }
            }
        }

        if (!completado) {
            mostrarError("No se pudo registrar en lista de espera tras varios intentos.");
            throw new SQLException("No se pudo registrar en lista de espera tras varios intentos.");
        }
    }

    private static void mostrarError(String mensaje) {
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(null,
                mensaje,
                "Error SQL",
                JOptionPane.ERROR_MESSAGE)
        );
    }

    private static boolean yaRegistradoEnLista(String idPaciente, String fecha, String hora, String servicio) {
        // Normaliza formato de hora
        if (!hora.contains(":")) {
            hora = hora + ":00";
        }

        String sql = "SELECT COUNT(*) FROM ListaEsperaCitas " +
                     "WHERE idPaciente = ? AND fechaDeseada = ? AND horaDeseada = ? AND servicio = ? AND estado = 'pendiente'";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idPaciente);
            stmt.setString(2, fecha);
            stmt.setString(3, hora);
            stmt.setString(4, servicio);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en verificación de duplicado: " + e.getMessage());
        }
        return false;
    }

    public static List<Espera> obtenerSolicitudesPara(String fecha, String hora, String servicio) throws SQLException {
        // Normaliza formato de hora
        if (!hora.contains(":")) {
            hora = hora + ":00";
        }

        List<Espera> lista = new ArrayList<>();
        try (Connection conn = ConexionSQLite.conectar()) {
            String sql = "SELECT idPaciente FROM ListaEsperaCitas " +
                         "WHERE fechaDeseada = ? AND horaDeseada = ? AND servicio = ? AND estado = 'pendiente'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, fecha);
                stmt.setString(2, hora);
                stmt.setString(3, servicio);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        lista.add(new Espera(rs.getString("idPaciente"), fecha, hora, servicio));
                    }
                }
            }
        }
        return lista;
    }

    public static void marcarNotificada(String idPaciente, String fecha, String hora, String servicio)
            throws SQLException {
        // Normaliza formato de hora
        if (!hora.contains(":")) {
            hora = hora + ":00";
        }

        final int MAX_REINTENTOS = 3;
        int intentos = 0;
        boolean completado = false;

        while (intentos < MAX_REINTENTOS && !completado) {
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "UPDATE ListaEsperaCitas SET estado = 'notificada' " +
                             "WHERE idPaciente = ? AND fechaDeseada = ? AND horaDeseada = ? AND servicio = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, idPaciente);
                    stmt.setString(2, fecha);
                    stmt.setString(3, hora);
                    stmt.setString(4, servicio);
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
                    throw e;
                }
            }
        }

        if (!completado) {
            throw new SQLException("No se pudo marcar como notificada tras varios intentos.");
        }
    }

    public static void generarNotificacionParaListaEspera(String fecha, String hora, String servicio) {
        // Normaliza formato de hora
        if (!hora.contains(":")) {
            hora = hora + ":00";
        }

        try {
            List<Espera> solicitudes = obtenerSolicitudesPara(fecha, hora, servicio);
            for (Espera espera : solicitudes) {
                try (Connection conn = ConexionSQLite.conectar()) {
                    String mensaje = "Se ha liberado una cita el " + fecha + " a las " + hora + " para " + servicio;
                    String sql = "INSERT INTO Notificaciones(idPaciente, mensaje, estado, fecha, hora, servicio) " +
                                 "VALUES (?, ?, 'pendiente', ?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, espera.getIdPaciente());
                        stmt.setString(2, mensaje);
                        stmt.setString(3, espera.getFecha());
                        stmt.setString(4, espera.getHora());
                        stmt.setString(5, espera.getServicio());
                        stmt.executeUpdate();
                    }
                }
                marcarNotificada(espera.getIdPaciente(), fecha, hora, servicio);
            }
        } catch (SQLException e) {
            System.err.println("Error al generar notificaciones para lista de espera: " + e.getMessage());
        }
    }
}
