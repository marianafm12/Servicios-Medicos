package Emergencias;

import BaseDeDatos.ConexionSQLite;
import java.sql.*;

public class EmergenciaDB {

    public static boolean guardarEmergencia(
            Integer idPaciente, String ubicacion, String tipo, String gravedad,
            String descripcion, Timestamp fechaIncidente,
            String telefono, Integer idResponsable
    ) {
        String sql = "INSERT INTO Emergencias (" +
                "IDPaciente, Ubicacion, TipoDeEmergencia, Gravedad, " +
                "Descripcion, FechaIncidente, TelefonoContacto, IDResponsable" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (idPaciente != null) ps.setInt(1, idPaciente);
            else ps.setNull(1, Types.INTEGER);

            ps.setString(2, ubicacion);

            if (tipo != null) ps.setString(3, tipo);
            else ps.setNull(3, Types.VARCHAR);

            if (gravedad != null) ps.setString(4, gravedad);
            else ps.setNull(4, Types.VARCHAR);

            if (descripcion != null) ps.setString(5, descripcion);
            else ps.setNull(5, Types.VARCHAR);

            ps.setTimestamp(6, fechaIncidente);

            if (telefono != null) ps.setString(7, telefono);
            else ps.setNull(7, Types.VARCHAR);

            if (idResponsable != null) ps.setInt(8, idResponsable);
            else ps.setNull(8, Types.INTEGER);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
