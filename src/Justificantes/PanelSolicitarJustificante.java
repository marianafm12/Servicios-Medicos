package Justificantes;

import BaseDeDatos.ConexionSQLite;
import Utilidades.ColoresUDLAP;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PanelSolicitarJustificante extends JPanel {
    private final JTextField campoFecha;
    private final JTextField campoProfesor;
    private final JTextArea campoMotivo;
    private final int idAlumno;

    public PanelSolicitarJustificante(int idAlumno) {
        this.idAlumno = idAlumno;
        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        JLabel titulo = new JLabel("Solicitar Justificante", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoFecha = new JTextField(20);
        campoProfesor = new JTextField(20);
        campoMotivo = new JTextArea(5, 20);
        campoMotivo.setLineWrap(true);
        campoMotivo.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(campoMotivo);

        agregarCampo(formulario, gbc, "Fecha (YYYY-MM-DD):", campoFecha, 0);
        agregarCampo(formulario, gbc, "Profesor:", campoProfesor, 1);
        agregarCampo(formulario, gbc, "Motivo:", scroll, 2);

        add(formulario, BorderLayout.CENTER);

        JButton btnEnviar = new JButton("Enviar Solicitud");
        btnEnviar.setBackground(ColoresUDLAP.VERDE);
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFocusPainted(false);
        btnEnviar.addActionListener(e -> enviarSolicitud());
        JPanel abajo = new JPanel();
        abajo.setOpaque(false);
        abajo.add(btnEnviar);
        add(abajo, BorderLayout.SOUTH);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, Component campo, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        panel.add(campo, gbc);
    }

    private void enviarSolicitud() {
        String fecha = campoFecha.getText().trim();
        String profesor = campoProfesor.getText().trim();
        String motivo = campoMotivo.getText().trim();

        if (fecha.isEmpty() || profesor.isEmpty() || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.", "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection con = ConexionSQLite.conectar()) {
            String sql = "INSERT INTO SolicitudesJustificantes (IDAlumno, Fecha, Profesor, Motivo) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idAlumno);
            ps.setString(2, fecha);
            ps.setString(3, profesor);
            ps.setString(4, motivo);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Solicitud enviada correctamente.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al enviar la solicitud:\n" + e.getMessage(), "Error de BD",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        campoFecha.setText("");
        campoProfesor.setText("");
        campoMotivo.setText("");
    }
}
