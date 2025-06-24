package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import BaseDeDatos.ConexionSQLite;

public class PanelHistorialMedicoEditable extends JPanel {
    private JTextField campoID;
    private JPanel panelDatos;
    private JPanel panelHistorial;

    public PanelHistorialMedicoEditable() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Parte superior con campo para ID
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTop.setBackground(Color.WHITE);
        JLabel label = new JLabel("ID del Paciente:");
        campoID = new JTextField(10);
        JButton botonBuscar = new JButton("Consultar");

        // Color institucional NARANJA UDLAP
        botonBuscar.setBackground(new Color(255, 102, 0));
        botonBuscar.setForeground(Color.WHITE);
        botonBuscar.setFocusPainted(false);
        botonBuscar.setBorderPainted(false);
        botonBuscar.setOpaque(true);

        panelTop.add(label);
        panelTop.add(campoID);
        panelTop.add(botonBuscar);

        add(panelTop, BorderLayout.NORTH);

        // Panel donde irán los resultados
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(Color.WHITE);

        panelDatos = new JPanel();
        panelDatos.setBackground(Color.WHITE);
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));

        panelHistorial = new JPanel();
        panelHistorial.setBackground(Color.WHITE);
        panelHistorial.setLayout(new BoxLayout(panelHistorial, BoxLayout.Y_AXIS));

        panelCentral.add(panelDatos);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(panelHistorial);

        JScrollPane scroll = new JScrollPane(panelCentral);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // Acción del botón
        botonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idTexto = campoID.getText().trim();
                if (!idTexto.matches("\\d+")) {
                    mostrarMensaje("Ingrese un ID numérico válido.");
                    return;
                }
                int idPaciente = Integer.parseInt(idTexto);
                mostrarExpediente(idPaciente);
            }
        });
    }

    private void mostrarExpediente(int id) {
        panelDatos.removeAll();
        panelHistorial.removeAll();

        try (Connection conn = ConexionSQLite.conectar()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT Nombre, ApellidoPaterno, ApellidoMaterno, Correo FROM InformacionAlumno WHERE ID = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                panelDatos.add(new JLabel("ID: " + id));
                panelDatos.add(new JLabel("Nombre: " + rs.getString("Nombre") + " " + rs.getString("ApellidoPaterno")
                        + " " + rs.getString("ApellidoMaterno")));
                panelDatos.add(new JLabel("Correo: " + rs.getString("Correo")));
            } else {
                mostrarMensaje("No se encontró un alumno con ID: " + id);
                revalidate();
                repaint();
                return;
            }

            // Datos clínicos del paciente desde la tabla Registro
            PreparedStatement psRegistro = conn.prepareStatement(
                    "SELECT Edad, Altura, Peso, EnfermedadesPreexistentes, Medicacion, Alergias FROM Registro WHERE ID = ?");
            psRegistro.setInt(1, id);
            ResultSet rsRegistro = psRegistro.executeQuery();

            if (rsRegistro.next()) {
                panelDatos.add(new JLabel("Edad: " + rsRegistro.getInt("Edad")));
                panelDatos.add(new JLabel("Altura: " + rsRegistro.getDouble("Altura") + " cm"));
                panelDatos.add(new JLabel("Peso: " + rsRegistro.getDouble("Peso") + " kg"));
                panelDatos.add(
                        new JLabel("Enfermedades Preexistentes: " + rsRegistro.getString("EnfermedadesPreexistentes")));
                panelDatos.add(new JLabel("Medicación: " + rsRegistro.getString("Medicacion")));
                panelDatos.add(new JLabel("Alergias: " + rsRegistro.getString("Alergias")));
            } else {
                panelDatos.add(new JLabel("No hay datos clínicos registrados para este paciente."));
            }

            // Consultar historial
            PreparedStatement ps2 = conn.prepareStatement(
                    "SELECT FechaConsulta, Diagnostico, Sintomas, MedicamentosRecetados, RecetaMedica FROM Consultas WHERE IDPaciente = ? ORDER BY FechaConsulta DESC");
            ps2.setInt(1, id);
            ResultSet rs2 = ps2.executeQuery();

            boolean tieneConsultas = false;
            while (rs2.next()) {
                tieneConsultas = true;
                JTextArea area = new JTextArea();
                area.setEditable(false);
                area.setText(
                        "Fecha: " + rs2.getString("FechaConsulta") +
                                "\nDiagnóstico: " + rs2.getString("Diagnostico") +
                                "\nSíntomas: " + rs2.getString("Sintomas") +
                                "\nMedicamentos Recetados: " + rs2.getString("MedicamentosRecetados") +
                                "\nReceta Médica: " + rs2.getString("RecetaMedica"));
                area.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                panelHistorial.add(area);
                panelHistorial.add(Box.createVerticalStrut(10));
            }

            if (!tieneConsultas) {
                panelHistorial.add(new JLabel("Este paciente no tiene historial médico registrado."));
            }

        } catch (SQLException ex) {
            mostrarMensaje("Error al obtener el expediente:\n" + ex.getMessage());
        }

        revalidate();
        repaint();
    }

    private void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Historial Médico", JOptionPane.INFORMATION_MESSAGE);
    }
}