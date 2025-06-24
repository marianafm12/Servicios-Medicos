package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import BaseDeDatos.ConexionSQLite;
import Utilidades.ColoresUDLAP;
import Utilidades.PanelProvider;

public class PanelHistorialMedico extends JPanel implements PanelProvider {
    private final int idPaciente;

    public PanelHistorialMedico(int idPaciente) {
        this.idPaciente = idPaciente;
        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        // Título
        JLabel titulo = new JLabel("Expediente Médico del Paciente", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(ColoresUDLAP.VERDE_SOLIDO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        // Contenedor de todo
        JPanel contenedorPrincipal = new JPanel();
        contenedorPrincipal.setLayout(new BoxLayout(contenedorPrincipal, BoxLayout.Y_AXIS));
        contenedorPrincipal.setBackground(Color.WHITE);

        // Información del paciente
        JPanel panelInfo = obtenerDatosGenerales();
        if (panelInfo != null) {
            contenedorPrincipal.add(panelInfo);
        }

        contenedorPrincipal.add(Box.createVerticalStrut(15));

        // Historial de consultas
        for (HistorialMedicoItem item : obtenerHistorialConsultas()) {
            contenedorPrincipal.add(item);
            contenedorPrincipal.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(contenedorPrincipal);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel obtenerDatosGenerales() {
        String sql = """
                    SELECT a.Nombre, a.ApellidoPaterno, a.ApellidoMaterno,
                    r.Edad, r.Altura, r.Peso, r.EnfermedadesPreexistentes,
                    r.Medicacion, r.Alergias
                    FROM Registro r
                    JOIN InformacionAlumno a ON r.ID = a.ID
                    WHERE r.ID = ?
                """;

        try (Connection con = ConexionSQLite.conectar();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("Nombre") + " " + rs.getString("ApellidoPaterno") + " "
                        + rs.getString("ApellidoMaterno");
                int edad = rs.getInt("Edad");
                float altura = rs.getFloat("Altura");
                float peso = rs.getFloat("Peso");
                String enf = rs.getString("EnfermedadesPreexistentes");
                String meds = rs.getString("Medicacion");
                String alerg = rs.getString("Alergias");

                JTextArea area = new JTextArea();
                area.setEditable(false);
                area.setBackground(ColoresUDLAP.BLANCO);
                area.setFont(new Font("Arial", Font.PLAIN, 14));
                area.setText(String.format("""
                        Nombre completo: %s
                        Edad: %d años
                        Altura: %.2f m    Peso: %.2f kg

                        Enfermedades preexistentes: %s
                        Medicación actual: %s
                        Alergias: %s
                        """, nombre, edad, altura, peso, enf, meds, alerg));

                JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground(Color.WHITE);
                panel.setBorder(BorderFactory.createTitledBorder("Datos Generales"));
                panel.add(area, BorderLayout.CENTER);
                return panel;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener información general:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    private ArrayList<HistorialMedicoItem> obtenerHistorialConsultas() {
        ArrayList<HistorialMedicoItem> lista = new ArrayList<>();
        String sql = """
                    SELECT FechaConsulta, Sintomas, MedicamentosRecetados, Diagnostico, RecetaMedica
                    FROM Consultas
                    WHERE IDPaciente = ?
                    ORDER BY FechaConsulta DESC
                """;

        try (Connection con = ConexionSQLite.conectar();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String fecha = rs.getString("FechaConsulta");
                String sintomas = rs.getString("Sintomas");
                String medicamentos = rs.getString("MedicamentosRecetados");
                String diagnostico = rs.getString("Diagnostico");
                String receta = rs.getString("RecetaMedica");

                lista.add(new HistorialMedicoItem(fecha, diagnostico, sintomas, medicamentos, receta));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener el historial médico:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return lista;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public String getPanelName() {
        return "historialMedico";
    }
}