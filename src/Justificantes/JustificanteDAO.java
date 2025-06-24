package Justificantes;

import BaseDeDatos.ConexionSQLite;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JustificanteDAO {

    public static boolean guardarJustificante(Justificante j) {
        String sql = "INSERT INTO JustificantePaciente (idPaciente, nombrePaciente, motivo, fechaInicio, fechaFin, diagnostico, rutaArchivo, estado) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, j.getIdPaciente());
            pst.setString(2, j.getNombrePaciente());
            pst.setString(3, j.getMotivo());
            pst.setString(4, j.getFechaInicio().toString());
            pst.setString(5, j.getFechaFin().toString());
            pst.setString(6, j.getDiagnostico());
            pst.setString(7, j.getArchivoReceta() != null ? j.getArchivoReceta().getAbsolutePath() : null);
            pst.setString(8, "Pendiente");

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Optional<Justificante> obtenerPorFolio(int folio) {
        String sql = "SELECT * FROM JustificantePaciente WHERE folio = ?";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, folio);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return Optional.of(mapearDesdeResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static List<Justificante> obtenerTodos() {
        List<Justificante> lista = new ArrayList<>();
        String sql = "SELECT * FROM JustificantePaciente ORDER BY folio DESC";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearDesdeResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static boolean actualizarDiagnosticoYFechas(int folio, String diagnostico, LocalDate inicio, LocalDate fin) {
        String sql = "UPDATE JustificantePaciente SET diagnostico = ?, fechaInicio = ?, fechaFin = ? WHERE folio = ?";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, diagnostico);
            pst.setString(2, inicio.toString());
            pst.setString(3, fin.toString());
            pst.setInt(4, folio);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean aprobarJustificante(int folio, String motivo, String diagnostico, String medicoFirmante,
            LocalDate fechaInicio, LocalDate fechaFin) {

        String sql = "UPDATE JustificantePaciente SET estado = 'Aprobado', " +
                "motivo = ?, diagnostico = ?, resueltoPor = ?, " +
                "fechaResolucion = CURRENT_DATE, " + // Usamos CURRENT_DATE para la fecha actual
                "fechaInicio = ?, fechaFin = ? " +
                "WHERE folio = ?";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, motivo);
            pst.setString(2, diagnostico);
            pst.setString(3, medicoFirmante);
            pst.setString(4, fechaInicio.toString());
            pst.setString(5, fechaFin.toString());
            pst.setInt(6, folio);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean rechazarJustificante(int folio, String medicoFirmante) {
        String sql = "UPDATE JustificantePaciente SET estado = 'Rechazado', resueltoPor = ?, fechaResolucion = ? WHERE folio = ?";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, medicoFirmante);
            pst.setString(2, LocalDate.now().toString());
            pst.setInt(3, folio);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarPorFolio(int folio) {
        String sql = "DELETE FROM JustificantePaciente WHERE folio = ?";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, folio);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Justificante mapearDesdeResultSet(ResultSet rs) throws SQLException {
        int folio = rs.getInt("folio");
        String idPaciente = rs.getString("idPaciente");
        String nombrePaciente = rs.getString("nombrePaciente");
        String motivo = rs.getString("motivo");
        LocalDate fechaInicio = LocalDate.parse(rs.getString("fechaInicio"));
        LocalDate fechaFin = LocalDate.parse(rs.getString("fechaFin"));
        String diagnostico = rs.getString("diagnostico");
        File receta = rs.getString("rutaArchivo") != null ? new File(rs.getString("rutaArchivo")) : null;
        String estado = rs.getString("estado");
        String resueltoPor = rs.getString("resueltoPor");
        LocalDate fechaResolucion = rs.getString("fechaResolucion") != null
                ? LocalDate.parse(rs.getString("fechaResolucion"))
                : null;

        return new Justificante(folio, idPaciente, nombrePaciente, motivo,
                fechaInicio, fechaFin, diagnostico, receta,
                estado, resueltoPor, fechaResolucion);
    }

    public static int contarJustificantesPendientes() {
    String sql = "SELECT COUNT(*) FROM JustificantePaciente WHERE estado IS NULL OR estado = '' OR estado = 'Pendiente'";
    try (Connection con = ConexionSQLite.conectar();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return 0;
}

}
