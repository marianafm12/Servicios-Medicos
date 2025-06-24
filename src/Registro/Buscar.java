package Registro;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Buscar implements ActionListener {
    private final JTextField[] campos;

    public Buscar(JTextField[] campos) {
        this.campos = campos;
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        String idText = campos[0].getText().trim();
        String nombre = campos[1].getText().trim();
        String apPat = campos[2].getText().trim();
        String apMat = campos[3].getText().trim();

        // Validar que haya al menos ID o Nombre completo
        if ((idText.isEmpty() || !idText.matches("\\d+"))
                && (nombre.isEmpty() || apPat.isEmpty() || apMat.isEmpty())) {
            JOptionPane.showMessageDialog(
                    null,
                    "Debe ingresar un ID válido o Nombre completo para buscar.",
                    "Búsqueda incompleta",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql;
        try (Connection conexion = BaseDeDatos.ConexionSQLite.conectar()) {
            PreparedStatement stmt;

            // Búsqueda por ID
            if (!idText.isEmpty() && idText.matches("\\d+")) {
                sql = "SELECT a.ID, a.Nombre, a.ApellidoPaterno, a.ApellidoMaterno, a.Correo, " +
                        "r.Edad, r.Altura, r.Peso, r.EnfermedadesPreexistentes, r.Medicacion, r.Alergias " +
                        "FROM InformacionAlumno a " +
                        "JOIN Registro r ON a.ID = r.ID " +
                        "WHERE a.ID = ?";
                stmt = conexion.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(idText));

            // Búsqueda por nombre completo
            } else {
                sql = "SELECT a.ID, a.Nombre, a.ApellidoPaterno, a.ApellidoMaterno, a.Correo, " +
                        "r.Edad, r.Altura, r.Peso, r.EnfermedadesPreexistentes, r.Medicacion, r.Alergias " +
                        "FROM InformacionAlumno a " +
                        "JOIN Registro r ON a.ID = r.ID " +
                        "WHERE a.Nombre = ? AND a.ApellidoPaterno = ? AND a.ApellidoMaterno = ?";
                stmt = conexion.prepareStatement(sql);
                stmt.setString(1, nombre);
                stmt.setString(2, apPat);
                stmt.setString(3, apMat);
            }

            // Ejecutar consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Datos personales desde InformacionAlumno
                    campos[0].setText(String.valueOf(rs.getInt("ID")));
                    campos[1].setText(rs.getString("Nombre"));
                    campos[2].setText(rs.getString("ApellidoPaterno"));
                    campos[3].setText(rs.getString("ApellidoMaterno"));
                    campos[4].setText(rs.getString("Correo"));

                    // Datos médicos desde Registros
                    campos[5].setText(String.valueOf(rs.getInt("Edad")));
                    campos[6].setText(String.valueOf(rs.getDouble("Altura")));
                    campos[7].setText(String.valueOf(rs.getDouble("Peso")));
                    campos[8].setText(rs.getString("EnfermedadesPreexistentes"));
                    campos[9].setText(rs.getString("Medicacion"));
                    campos[10].setText(rs.getString("Alergias"));

                    JOptionPane.showMessageDialog(
                            null,
                            "Registro encontrado y cargado.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "No se encontró ningún registro con los datos proporcionados.",
                            "No encontrado",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error al buscar registro: " + e.getMessage(),
                    "Error de Base de Datos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
