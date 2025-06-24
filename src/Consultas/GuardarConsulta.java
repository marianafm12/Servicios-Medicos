package Consultas;

import BaseDeDatos.ConexionSQLite;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Guarda una consulta médica:
 * – Actualiza datos antropométricos en Registro
 * – Inserta la consulta en Consultas, incluyendo IDMedico
 */
public class GuardarConsulta implements ActionListener {

    /* ─── Datos fijos de la sesión ─── */
    private final int idMedico;

    /* ─── Controles de la UI ─── */
    private final JTextField txtIdPaciente, txtEdad, txtAltura, txtPeso,
            txtMedicacion, txtFechaConsulta;
    private final JTextArea taSintomas, taMedicamentosRec,
            taDiagnostico, taReceta;

    public GuardarConsulta(
            int idMedicoSesion,
            JTextField txtIdPaciente,
            JTextField txtEdad, JTextField txtAltura, JTextField txtPeso,
            JTextField txtMedicacion,
            JTextArea taSintomas, JTextArea taMedicamentosRec,
            JTextArea taDiagnostico, JTextField txtFechaConsulta,
            JTextArea taReceta) {

        this.idMedico = idMedicoSesion; // ← valor que viene del login
        this.txtIdPaciente = txtIdPaciente;
        this.txtEdad = txtEdad;
        this.txtAltura = txtAltura;
        this.txtPeso = txtPeso;
        this.txtMedicacion = txtMedicacion;
        this.taSintomas = taSintomas;
        this.taMedicamentosRec = taMedicamentosRec;
        this.taDiagnostico = taDiagnostico;
        this.txtFechaConsulta = txtFechaConsulta;
        this.taReceta = taReceta;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        /* 1. Validar ID Paciente */
        String idTxt = txtIdPaciente.getText().trim();
        if (!idTxt.matches("\\d+")) {
            JOptionPane.showMessageDialog(null,
                    "El ID del paciente debe ser numérico.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            txtIdPaciente.requestFocus();
            return;
        }
        int idPaciente = Integer.parseInt(idTxt);

        /* 2. Validar campos obligatorios */
        if (taSintomas.getText().trim().isEmpty()
                || taDiagnostico.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Síntomas y Diagnóstico son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        /* 3. SQL */
        final String sqlUpdateRegistro = "UPDATE Registro SET Edad = ?, Altura = ?, Peso = ?, Medicacion = ? WHERE ID = ?";

        final String sqlInsertConsulta = "INSERT INTO Consultas " +
                "(IDMedico, IDPaciente, Sintomas, MedicamentosRecetados, Diagnostico, FechaConsulta, RecetaMedica) " +
                "VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = ConexionSQLite.conectar()) {
            conn.setAutoCommit(false);

            /* 3.1 Actualizar Registro */
            try (PreparedStatement psUpd = conn.prepareStatement(sqlUpdateRegistro)) {
                psUpd.setString(1, txtEdad.getText().trim());
                psUpd.setString(2, txtAltura.getText().trim());
                psUpd.setString(3, txtPeso.getText().trim());
                psUpd.setString(4, txtMedicacion.getText().trim());
                psUpd.setInt(5, idPaciente);
                psUpd.executeUpdate();
            }

            /* 3.2 Insertar Consulta */
            try (PreparedStatement psIns = conn.prepareStatement(sqlInsertConsulta)) {
                psIns.setInt(1, idMedico); // ★ nuevo
                psIns.setInt(2, idPaciente);
                psIns.setString(3, taSintomas.getText().trim());
                psIns.setString(4, taMedicamentosRec.getText().trim());
                psIns.setString(5, taDiagnostico.getText().trim());
                psIns.setString(6, txtFechaConsulta.getText().trim());
                psIns.setString(7, taReceta.getText().trim());
                psIns.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(null,
                    "Consulta guardada exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al guardar consulta:\n" + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtEdad.setText("");
        txtAltura.setText("");
        txtPeso.setText("");
        txtMedicacion.setText("");
        taSintomas.setText("");
        taMedicamentosRec.setText("");
        taDiagnostico.setText("");
        taReceta.setText("");
    }
}
