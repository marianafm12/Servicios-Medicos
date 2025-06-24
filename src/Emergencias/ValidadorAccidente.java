// ValidadorAccidente.java
package Emergencias;

import javax.swing.*;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidadorAccidente {

    public static String validarCampos(
            String idEmergencia,
            String idPaciente,
            String nombrePaciente,
            String correoPaciente,
            String telefonoPaciente,
            String presionArterial,
            String ritmoCardiaco,
            String ritmoRespiratorio,
            ButtonGroup conscienciaGroup,
            String descripcion,
            String correoContacto,
            String observaciones,
            int dia, int mes, int anio,
            String idContacto,
            String nombreContacto,
            String telefonoContacto) {
        if (!idEmergencia.matches("\\d+"))
            return "ID Emergencia debe ser numérico";
        if (!idPaciente.matches("\\d+"))
            return "ID Paciente debe ser numérico";
        if (nombrePaciente.trim().isEmpty())
            return "Nombre del paciente es obligatorio";
        if (!correoPaciente.contains("@"))
            return "El correo del paciente debe contener '@'";
        if (!telefonoPaciente.matches("\\d{10,15}"))
            return "El teléfono del paciente debe tener entre 10 y 15 dígitos";

        try {
            LocalDate fecha = LocalDate.of(anio, mes, dia);
            if (fecha.isBefore(LocalDate.now()))
                return "La fecha del accidente no puede ser anterior a hoy";
        } catch (Exception e) {
            return "La fecha ingresada no es válida";
        }

        if (!idContacto.matches("\\d+"))
            return "ID del contacto debe ser numérico";
        if (nombreContacto.trim().isEmpty())
            return "El nombre del contacto es obligatorio";
        if (!correoContacto.contains("@"))
            return "El correo del contacto debe contener '@'";
        if (!telefonoContacto.matches("\\d{10,15}"))
            return "El teléfono del contacto debe tener entre 10 y 15 dígitos";

        if (!presionArterial.matches("\\d{2,3}/\\d{2,3}"))
            return "Presión arterial debe estar en formato ##/##";
        if (!ritmoCardiaco.matches("\\d+"))
            return "Ritmo cardíaco debe ser numérico";
        if (!ritmoRespiratorio.matches("\\d+"))
            return "Ritmo respiratorio debe ser numérico";

        if (conscienciaGroup.getSelection() == null)
            return "Seleccione nivel de consciencia";
        if (descripcion.trim().isEmpty())
            return "La descripción del accidente es obligatoria";
        if (observaciones.trim().isEmpty())
            return "Las observaciones no pueden estar vacías";

        return null;
    }

    public static boolean idEmergenciaExiste(String idEmergencia) {
        String query = "SELECT 1 FROM Accidentes WHERE IDEmergencia = ?";
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, Integer.parseInt(idEmergencia));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

}
