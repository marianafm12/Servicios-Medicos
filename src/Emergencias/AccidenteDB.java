package Emergencias;

import BaseDeDatos.ConexionSQLite;
import java.sql.*;

public class AccidenteDB {

public static boolean guardarAccidenteCompleto(Accidente accidente) {
    String sql = "INSERT INTO Accidentes (" +
            "IDEmergencia, FechaAccidente, GeneroPaciente, PresionArterial, RitmoCardiaco, RitmoRespiratorio, " +
            "Consciencia, DescripcionAccidente, Observaciones, IDContacto, NombreContacto, ApellidosContacto, " +
            "CorreoContacto, TelefonoContacto, EstadoEmergencia) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = ConexionSQLite.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, accidente.getIdEmergencia());
        ps.setString(2, accidente.getFecha());
        ps.setString(3, accidente.getGenero());
        ps.setString(4, accidente.getPresionArterial());
        ps.setInt(5, accidente.getRitmoCardiaco());
        ps.setInt(6, accidente.getRitmoRespiratorio());
        ps.setString(7, accidente.getConsciencia());
        ps.setString(8, accidente.getDescripcion());
        ps.setString(9, accidente.getObservaciones());

        if (accidente.getIdContacto() != null)
            ps.setInt(10, accidente.getIdContacto());
        else
            ps.setNull(10, Types.INTEGER);

        ps.setString(11, accidente.getNombreContacto());
        ps.setString(12, accidente.getApellidosContacto());
        ps.setString(13, accidente.getCorreoContacto());
        ps.setString(14, accidente.getTelefonoContacto());
        ps.setString(15, accidente.getEstado());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


public static Accidente buscarAccidente(String id) {
    String sql = "SELECT * FROM Accidentes WHERE IDEmergencia = ?";
    try (Connection conn = ConexionSQLite.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int idEmergencia = rs.getInt("IDEmergencia");
            String fecha = rs.getString("FechaAccidente");
            String genero = rs.getString("GeneroPaciente");
            String presion = rs.getString("PresionArterial");
            int ritmoCardiaco = rs.getInt("RitmoCardiaco");
            int ritmoRespiratorio = rs.getInt("RitmoRespiratorio");
            String consciencia = rs.getString("Consciencia");
            String descripcion = rs.getString("DescripcionAccidente");
            String observaciones = rs.getString("Observaciones");
            Integer idContacto = rs.getObject("IDContacto") != null ? rs.getInt("IDContacto") : null;
            String nombreContacto = rs.getString("NombreContacto");
            String apellidosContacto = rs.getString("ApellidosContacto");
            String correoContacto = rs.getString("CorreoContacto");
            String telefonoContacto = rs.getString("TelefonoContacto");
            String estado = rs.getString("EstadoEmergencia");

            return new Accidente(
                    idEmergencia, fecha, genero, presion,
                    ritmoCardiaco, ritmoRespiratorio, consciencia,
                    descripcion, observaciones, idContacto,
                    nombreContacto, apellidosContacto,
                    correoContacto, telefonoContacto, estado
            );
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

}
