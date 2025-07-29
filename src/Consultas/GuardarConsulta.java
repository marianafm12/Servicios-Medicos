package Consultas;

import BaseDeDatos.ConexionSQLite;
import Utilidades.MensajeErrorUDLAP;
import javax.swing.*;

import java.awt.Window;
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

    private final MensajeErrorUDLAP mensajeInline;

    public GuardarConsulta(
            int idMedicoSesion,
            JTextField txtIdPaciente,
            JTextField txtEdad, JTextField txtAltura, JTextField txtPeso,
            JTextField txtMedicacion,
            JTextArea taSintomas, JTextArea taMedicamentosRec,
            JTextArea taDiagnostico, JTextField txtFechaConsulta,
            JTextArea taReceta, MensajeErrorUDLAP mensajeInline) {

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
        this.mensajeInline = mensajeInline;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mensajeInline.limpiar(); // Limpiar mensajes previos

        /* 1. Validar ID Paciente */
        String idTxt = txtIdPaciente.getText().trim();
        if (!idTxt.matches("\\d+")) {
            mensajeInline.mostrarError("El ID del paciente debe ser numérico.");
            txtIdPaciente.requestFocus();
            return;
        }
        int idPaciente = Integer.parseInt(idTxt);

        /* 2. Validar campos obligatorios */
        if (taSintomas.getText().trim().isEmpty()
                || taDiagnostico.getText().trim().isEmpty()) {
            mensajeInline.mostrarError("Síntomas y Diagnóstico son obligatorios.");
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
            // Mensaje inline de éxito
            mensajeInline.mostrarExito("Consulta guardada exitosamente.");
            limpiarCampos();

        } catch (SQLException ex) {
            Window owner = SwingUtilities.getWindowAncestor(txtIdPaciente);
            MensajeErrorUDLAP.mostrarVentanaError(
                    owner, "Error de Base de Datos",
                    "No se pudo guardar la consulta. \nError: " + ex.getMessage());
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
        mensajeInline.limpiar();
    }
}
