package BaseDeDatos;

import java.sql.*;

public class PacienteDB {

    public static String obtenerNombrePorID(String id) {
        String sql = "SELECT nombre, apellidoPaterno, apellidoMaterno FROM Pacientes WHERE id = ?";
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre") + " " +
                        rs.getString("apellidoPaterno") + " " +
                        rs.getString("apellidoMaterno");
            }
        } catch (SQLException e) {
            System.err.println("Error \nbuscando \npaciente: " + e.getMessage());
        }
        return null;
    }
}
