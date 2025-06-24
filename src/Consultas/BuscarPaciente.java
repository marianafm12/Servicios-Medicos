package Consultas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import BaseDeDatos.ConexionSQLite;
import java.sql.*;

public class BuscarPaciente implements ActionListener {
    private final JTextField[] campos;

    public BuscarPaciente(JTextField[] campos) {
        this.campos = campos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String idText = campos[0].getText().trim();
        String nombreCompleto = campos[1].getText().trim();

        try (Connection conn = ConexionSQLite.conectar()) {
            if (!idText.isEmpty() && idText.matches("\\d+")) {
                buscarPorID(conn, Integer.parseInt(idText));
            } else if (!nombreCompleto.isBlank()) {
                buscarPorNombreCompleto(conn, nombreCompleto);
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar un ID válido o nombre completo.",
                        "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al buscar paciente:\n" + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPorID(Connection conn, int id) throws SQLException {
        String sqlInfo = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno, Correo FROM InformacionAlumno WHERE ID = ?";
        String sqlRegistro = "SELECT Edad FROM Registro WHERE ID = ?";

        try (
                PreparedStatement ps1 = conn.prepareStatement(sqlInfo);
                PreparedStatement ps2 = conn.prepareStatement(sqlRegistro)) {
            ps1.setInt(1, id);
            ps2.setInt(1, id);

            ResultSet rsInfo = ps1.executeQuery();
            ResultSet rsReg = ps2.executeQuery();

            boolean infoEncontrado = rsInfo.next();
            boolean regEncontrado = rsReg.next();

            if (infoEncontrado) {
                String nombre = rsInfo.getString("Nombre") + " " +
                        rsInfo.getString("ApellidoPaterno") + " " +
                        rsInfo.getString("ApellidoMaterno");
                campos[1].setText(nombre);
                campos[3].setText(rsInfo.getString("Correo"));
            }

            if (regEncontrado) {
                campos[2].setText(rsReg.getString("Edad"));
            }

            if (!infoEncontrado && !regEncontrado) {
                JOptionPane.showMessageDialog(null,
                        "Paciente no encontrado", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void buscarPorNombreCompleto(Connection conn, String nombreComp) throws SQLException {
        String[] partes = nombreComp.split("\\s+");
        if (partes.length < 3) {
            JOptionPane.showMessageDialog(null,
                    "Debe ingresar nombre y apellidos completos (Nombre ApellidoPaterno ApellidoMaterno)",
                    "Formato incorrecto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "SELECT IA.ID, IA.Correo, IA.Nombre, IA.ApellidoPaterno, IA.ApellidoMaterno, R.Edad " +
                "FROM InformacionAlumno IA " +
                "LEFT JOIN Registro R ON IA.ID = R.ID " +
                "WHERE IA.Nombre = ? AND IA.ApellidoPaterno = ? AND IA.ApellidoMaterno = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, partes[0]);
            ps.setString(2, partes[1]);
            ps.setString(3, partes[2]);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                campos[0].setText(String.valueOf(rs.getInt("ID")));
                campos[2].setText(rs.getString("Edad"));
                campos[3].setText(rs.getString("Correo"));
            } else {
                JOptionPane.showMessageDialog(null, "Paciente no encontrado", "Búsqueda",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
