package Consultas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import BaseDeDatos.ConexionSQLite;
import java.sql.*;
import java.awt.Window;
import Utilidades.*;

/**
 * Busca los datos del paciente únicamente por ID.
 * Rellena Nombre, Correo, Edad, Altura, Peso y Medicación.
 */
public class BuscarPaciente implements ActionListener {
    private final MensajeErrorUDLAP mensajeInline;
    private final Window owner; // ventana propietaria para diálogos

    private final JTextField[] campos; // arreglo con los 7 JTextField mencionados

    public BuscarPaciente(JTextField[] campos, MensajeErrorUDLAP mensajeInline, Window owner) {
        mensajeInline.limpiar();
        if (campos.length < 7)
            throw new IllegalArgumentException(
                    "Se requieren al menos 7 JTextField (ID, Nombre, Correo, Edad, Altura, Peso, Medicación)");
        this.campos = campos;
        this.mensajeInline = mensajeInline;
        this.owner = owner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        /* 1. Validar ID numérico */
        String idText = campos[0].getText().trim();
        if (idText.isEmpty() || !idText.matches("\\d+")) {
            mensajeInline.mostrarError("Debe ingresar un ID numérico válido.");
            return;
        }
        int id = Integer.parseInt(idText);

        /* 2. Consulta única */
        String sql = "SELECT IA.Nombre, IA.ApellidoPaterno, IA.ApellidoMaterno, IA.Correo, " +
                "       R.Edad, R.Altura, R.Peso, R.Medicacion " +
                "  FROM InformacionAlumno IA " +
                "  JOIN Registro R ON IA.ID = R.ID " +
                " WHERE IA.ID = ?";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                /* 2.1 Rellenar controles */
                String nombreCompleto = rs.getString("Nombre") + " " +
                        rs.getString("ApellidoPaterno") + " " +
                        rs.getString("ApellidoMaterno");
                campos[1].setText(nombreCompleto);
                campos[2].setText(rs.getString("Correo"));
                campos[3].setText(rs.getString("Edad"));
                campos[4].setText(rs.getString("Altura"));
                campos[5].setText(rs.getString("Peso"));
                campos[6].setText(rs.getString("Medicacion"));
            } else {
                mensajeInline.mostrarAdvertencia("Paciente no encontrado con ID: " + id);
            }

        } catch (SQLException ex) {
            MensajeErrorUDLAP.mostrarVentanaError(owner,
                    "Error al buscar paciente:\n" + ex.getMessage(),
                    "Error de BD");
        }
    }
}
