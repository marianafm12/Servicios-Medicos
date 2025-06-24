package Consultas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import BaseDeDatos.ConexionSQLite;
import java.sql.*;

/**
 * Busca los datos del paciente únicamente por ID.
 * Rellena Nombre, Correo, Edad, Altura, Peso y Medicación.
 */
public class BuscarPaciente implements ActionListener {

    private final JTextField[] campos; // arreglo con los 7 JTextField mencionados

    public BuscarPaciente(JTextField[] campos) {
        if (campos.length < 7)
            throw new IllegalArgumentException(
                    "Se requieren al menos 7 JTextField (ID, Nombre, Correo, Edad, Altura, Peso, Medicación)");
        this.campos = campos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        /* 1. Validar ID numérico */
        String idText = campos[0].getText().trim();
        if (idText.isEmpty() || !idText.matches("\\d+")) {
            JOptionPane.showMessageDialog(null,
                    "Debe ingresar un ID numérico válido.",
                    "Entrada inválida", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(null,
                        "Paciente no encontrado.",
                        "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al buscar paciente:\n" + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
