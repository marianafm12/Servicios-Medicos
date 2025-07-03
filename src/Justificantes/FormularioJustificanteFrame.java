package Justificantes;

import Utilidades.*;

import javax.swing.*;
import javax.swing.border.Border;

import BaseDeDatos.ConexionSQLite;
import Inicio.SesionUsuario;
import Utilidades.PanelManager;

import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class FormularioJustificanteFrame extends JPanel {

    private final PanelManager panelManager;
    private JTextField idField, nombreField, motivoField;
    private DatePickerUDLAP inicioPicker;
    private DatePickerUDLAP finPicker;
    private File archivoPDF = null;
    private JLabel mensajeLabel;

    public FormularioJustificanteFrame(PanelManager panelManager) {
        this.panelManager = panelManager;

        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titulo = new JLabel("Solicitud de Justificante Médico", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(ColoresUDLAP.VERDE_SOLIDO);
        add(titulo, gbc);

        gbc.gridwidth = 1;

        // ID
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(20);
        idField.setFont(fieldFont);
        idField.setBorder(getCampoBorde());
        add(idField, gbc);

        // Nombre
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        nombreField = new JTextField(20);
        nombreField.setFont(fieldFont);
        nombreField.setBorder(getCampoBorde());
        add(nombreField, gbc);

        // Motivo
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 1;
        motivoField = new JTextField(20);
        motivoField.setFont(fieldFont);
        motivoField.setBorder(getCampoBorde());
        add(motivoField, gbc);

        // Fecha de inicio (DatePickerUDLAP)
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Inicio de Reposo:"), gbc);
        gbc.gridx = 1;
        inicioPicker = new DatePickerUDLAP();
        add(inicioPicker, gbc);

        // Fecha de fin (DatePickerUDLAP)
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Fin de Reposo:"), gbc);
        gbc.gridx = 1;
        finPicker = new DatePickerUDLAP();
        add(finPicker, gbc);

        // Label de estado
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mensajeLabel = new JLabel("", SwingConstants.CENTER);
        mensajeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        mensajeLabel.setForeground(Color.RED);
        add(mensajeLabel, gbc);
        gbc.gridwidth = 1;

        // Botones
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(ColoresUDLAP.BLANCO);

        JButton subirPDF = botonTransparente("Subir Receta", ColoresUDLAP.VERDE_SOLIDO, ColoresUDLAP.VERDE_HOVER);
        JButton btnGuardar = botonTransparente("Guardar", ColoresUDLAP.NARANJA_SOLIDO, ColoresUDLAP.NARANJA_HOVER);
        JButton btnRegresar = botonTransparente("Regresar", new Color(150, 150, 150), new Color(120, 120, 120));
        btnRegresar.addActionListener(e -> regresarAMenuJustificantes());

        subirPDF.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                archivoPDF = chooser.getSelectedFile();
                mensajeLabel.setForeground(new Color(0, 100, 0));
                mensajeLabel.setText("Archivo cargado: " + archivoPDF.getName());
            }
        });

        btnGuardar.addActionListener(e -> guardarJustificante());

        panelBotones.add(btnRegresar);
        panelBotones.add(subirPDF);
        panelBotones.add(btnGuardar);
        add(panelBotones, gbc);
    }

    private void guardarJustificante() {
        String id = idField.getText().trim();
        String nombre = nombreField.getText().trim();
        String motivo = motivoField.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || motivo.isEmpty()) {
            mensajeLabel.setForeground(Color.RED);
            mensajeLabel.setText("Completa todos los campos obligatorios.");
            return;
        }

        LocalDate inicio = inicioPicker.getDate();
        LocalDate fin = finPicker.getDate();

        if (fin == null || inicio.isAfter(fin)) {
            mensajeLabel.setForeground(Color.RED);
            mensajeLabel.setText("La fecha de fin debe ser igual o posterior a la de inicio.");
            return;
        }

        Justificante nuevo = new Justificante(id, nombre, motivo, inicio, fin, "", archivoPDF);
        boolean exito = JustificanteDAO.guardarJustificante(nuevo);

        if (exito) {
            mensajeLabel.setForeground(new Color(0, 100, 0));
            mensajeLabel.setText("Justificante guardado correctamente.");
            limpiarFormulario();

            int folioNuevo = obtenerUltimoFolioInsertado();
            if (folioNuevo > 0) {
                CorreosProfesoresPanel panelCorreos = new CorreosProfesoresPanel(folioNuevo, panelManager);
                panelManager.mostrarPanelPersonalizado(panelCorreos);
            } else {
                mensajeLabel.setForeground(Color.RED);
                mensajeLabel.setText("No se pudo obtener el folio para agregar correos.");
            }
        } else {
            mensajeLabel.setForeground(Color.RED);
            mensajeLabel.setText("Error al guardar justificante.");
        }
    }

    private JButton botonTransparente(String texto, Color base, Color hover) {
        JButton button = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private Border getCampoBorde() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_HOVER),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public void setValoresDesdeSesion() {
        int idActual = SesionUsuario.getPacienteActual();
        idField.setText(String.valueOf(idActual));
        idField.setEditable(false);

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT Nombre, ApellidoPaterno, ApellidoMaterno FROM InformacionAlumno WHERE ID = ?")) {
            ps.setInt(1, idActual);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombreCompleto = rs.getString("Nombre") + " " +
                            rs.getString("ApellidoPaterno") + " " + rs.getString("ApellidoMaterno");
                    nombreField.setText(nombreCompleto);
                    nombreField.setEditable(false);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            nombreField.setText("Error al cargar nombre");
        }
    }

    private int obtenerUltimoFolioInsertado() {
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement pst = conn.prepareStatement("SELECT MAX(folio) FROM JustificantePaciente")) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void limpiarFormulario() {
        motivoField.setText("");
        archivoPDF = null;
        mensajeLabel.setText("");
    }

    private void regresarAMenuJustificantes() {
        panelManager.showPanel("justificantesPaciente");
    }
}
