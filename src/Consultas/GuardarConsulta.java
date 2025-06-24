package Consultas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import BaseDeDatos.ConexionSQLite;

public class GuardarConsulta implements ActionListener {
    private final JTextField[] campos;
    private final JTextArea areaTexto;

    public GuardarConsulta(JTextField[] campos, JTextArea areaTexto) {
        this.campos = campos;
        this.areaTexto = areaTexto;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // campos[0] = ID Paciente
        // campos[1] = Nombre (visual)
        // campos[2] = Edad (visual)
        // campos[3] = Correo (visual)
        // campos[4] = Fecha Consulta
        // campos[5] = Última Consulta (no se guarda)
        // campos[6] = Fecha Inicio Síntomas

        String idPaciente = campos[0].getText().trim();
        String sintomas = campos[4].getText().trim();
        String medicamentos = campos[5].getText().trim();
        String diagnostico = campos[6].getText().trim();
        String fechaConsulta = campos[4].getText().trim();
        String fechaUltima = campos[5].getText().trim();
        String fechaInicio = campos[6].getText().trim();

        String receta = areaTexto.getText().trim();

        if (idPaciente.isEmpty() || sintomas.isEmpty() || medicamentos.isEmpty()
                || diagnostico.isEmpty() || fechaConsulta.isEmpty()
                || fechaInicio.isEmpty() || receta.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Todos los campos deben estar completos.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!idPaciente.matches("\\d+")) {
            JOptionPane.showMessageDialog(null,
                    "El ID del paciente debe ser numérico.",
                    "ID inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = ConexionSQLite.conectar()) {
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM InformacionAlumno WHERE ID = ?");
            checkStmt.setInt(1, Integer.parseInt(idPaciente));
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                JOptionPane.showMessageDialog(null,
                        "No existe un paciente con ese ID.",
                        "Paciente no encontrado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO Consultas (IDPaciente, IDMedico, Sintomas, Medicamentos, Diagnostico, FechaConsulta, FechaUltimaConsulta, FechaInicioSintomas, Receta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(idPaciente));
            ps.setInt(2, 1); // ID del médico (ajustable)
            ps.setString(3, sintomas);
            ps.setString(4, medicamentos);
            ps.setString(5, diagnostico);
            ps.setString(6, fechaConsulta);
            ps.setString(7, fechaUltima); // NUEVO campo
            ps.setString(8, fechaInicio);
            ps.setString(9, receta);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    "Consulta guardada exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al guardar la consulta:\n" + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        for (JTextField campo : campos) {
            campo.setText("");
        }
        areaTexto.setText("");
    }
}
