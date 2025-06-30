package Emergencias;

import BaseDeDatos.ConexionSQLite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmergenciaDAO {

    /**
     * Lee todas las emergencias de la BD, incluyendo ubicación y nombre del médico responsable.
     */
    public static List<Emergencia> obtenerTodas() {
        List<Emergencia> lista = new ArrayList<>();
        String sql = """
            SELECT e.IDEmergencia,
                   e.IDPaciente,
                   e.Ubicacion,
                   e.TipoDeEmergencia,
                   e.Gravedad,
                   e.Descripcion,
                   e.FechaIncidente,
                   e.FechaRegistro,
                   e.Estado,
                   e.TelefonoContacto,
                   COALESCE(m.Nombre || ' ' || m.ApellidoPaterno || ' ' || m.ApellidoMaterno, '-') AS Medico
              FROM Emergencias e
              LEFT JOIN InformacionMedico m ON e.IDResponsable = m.ID
             ORDER BY CASE e.Estado
                        WHEN 'Pendiente'   THEN 1
                        WHEN 'Transferido' THEN 2
                        ELSE 3
                      END
            """;
        try (Connection c = ConexionSQLite.conectar();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Emergencia(
                    rs.getInt("IDEmergencia"),
                    rs.getInt("IDPaciente"),
                    rs.getString("Ubicacion"),
                    rs.getString("TipoDeEmergencia"),
                    rs.getString("Gravedad"),
                    rs.getString("Descripcion"),
                    rs.getTimestamp("FechaIncidente"),
                    rs.getTimestamp("FechaRegistro"),
                    rs.getString("Estado"),
                    rs.getString("TelefonoContacto"),
                    rs.getString("Medico")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    /**
     * Lee una emergencia específica por su ID.
     */
    public static Emergencia obtenerPorId(int idEmergencia) {
        String sql = """
            SELECT e.IDEmergencia,
                   e.IDPaciente,
                   e.Ubicacion,
                   e.TipoDeEmergencia,
                   e.Gravedad,
                   e.Descripcion,
                   e.FechaIncidente,
                   e.FechaRegistro,
                   e.Estado,
                   e.TelefonoContacto,
                   COALESCE(m.Nombre || ' ' || m.ApellidoPaterno || ' ' || m.ApellidoMaterno, '-') AS Medico
              FROM Emergencias e
              LEFT JOIN InformacionMedico m ON e.IDResponsable = m.ID
             WHERE e.IDEmergencia = ?
            """;
        try (Connection c = ConexionSQLite.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idEmergencia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Emergencia(
                        rs.getInt("IDEmergencia"),
                        rs.getInt("IDPaciente"),
                        rs.getString("Ubicacion"),
                        rs.getString("TipoDeEmergencia"),
                        rs.getString("Gravedad"),
                        rs.getString("Descripcion"),
                        rs.getTimestamp("FechaIncidente"),
                        rs.getTimestamp("FechaRegistro"),
                        rs.getString("Estado"),
                        rs.getString("TelefonoContacto"),
                        rs.getString("Medico")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Actualiza el estado y el médico responsable (por ID) de una emergencia.
     */
    public static boolean actualizarEstadoEmergencia(int idEmergencia, String nuevoEstado, int idResponsable) {
        String sql = "UPDATE Emergencias SET Estado = ?, IDResponsable = ? WHERE IDEmergencia = ?";
        try (Connection c = ConexionSQLite.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idResponsable);
            ps.setInt(3, idEmergencia);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Cuenta cuántas emergencias están pendientes.
     */
    public static int contarPendientes() {
        String sql = "SELECT COUNT(*) FROM Emergencias WHERE Estado = 'Pendiente'";
        try (Connection c = ConexionSQLite.conectar();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
