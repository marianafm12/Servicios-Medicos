package Justificantes;

import BaseDeDatos.ConexionSQLite;
import Utilidades.ColoresUDLAP;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class PanelMisJustificantes extends JPanel {
    private final int idAlumno;

    public PanelMisJustificantes(int idAlumno) {
        this.idAlumno = idAlumno;

        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        JLabel titulo = new JLabel("Mis Justificantes", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        titulo.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(listaPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        for (JustificanteItem item : obtenerJustificantes()) {
            listaPanel.add(item);
            listaPanel.add(Box.createVerticalStrut(10));
        }
    }

    private java.util.List<JustificanteItem> obtenerJustificantes() {
        java.util.List<JustificanteItem> lista = new ArrayList<>();
        String sql = """
                    SELECT Fecha, Profesor, Motivo, Estado
                    FROM SolicitudesJustificantes
                    WHERE IDAlumno = ?
                    ORDER BY Fecha DESC
                """;

        try (Connection con = ConexionSQLite.conectar();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAlumno);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String fecha = rs.getString("Fecha");
                String profesor = rs.getString("Profesor");
                String motivo = rs.getString("Motivo");
                String estado = rs.getString("Estado");
                lista.add(new JustificanteItem(fecha, profesor, motivo, estado));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener justificantes:\n" + e.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }

        return lista;
    }

    static class JustificanteItem extends JPanel {
        public JustificanteItem(String fecha, String profesor, String motivo, String estado) {
            setLayout(new BorderLayout(10, 10));
            setBackground(new Color(255, 255, 255));
            setBorder(BorderFactory.createLineBorder(ColoresUDLAP.VERDE_OSCURO, 2));
            setPreferredSize(new Dimension(750, 100));

            JTextArea contenido = new JTextArea(
                    "Fecha: " + fecha +
                            "\nProfesor: " + profesor +
                            "\nMotivo: " + motivo +
                            "\nEstado: " + estado);
            contenido.setFont(new Font("Arial", Font.PLAIN, 14));
            contenido.setLineWrap(true);
            contenido.setWrapStyleWord(true);
            contenido.setEditable(false);
            contenido.setOpaque(false);

            add(contenido, BorderLayout.CENTER);
        }
    }
}
