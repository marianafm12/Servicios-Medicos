package GestionCitas;

import BaseDeDatos.ConexionSQLite;
import java.sql.*;

/**
 * DAO para todo lo que tenga que ver con las citas médicas.
 */
public class CitasDAO {

    /**
     * Cuenta cuántas citas futuras tiene pendientes un paciente.
     */
    public static int contarProximas(int idPaciente) {
        String sql = """
            SELECT COUNT(*) 
              FROM CitasMedicas 
             WHERE idPaciente = ? 
               AND fecha >= date('now')
            """;
        try (Connection c = ConexionSQLite.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
