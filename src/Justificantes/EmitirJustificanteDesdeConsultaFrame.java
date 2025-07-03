package Justificantes;

import Utilidades.ColoresUDLAP;
import Utilidades.DatePickerUDLAP;
import Utilidades.PanelManager;

import BaseDeDatos.ConexionSQLite;
import Inicio.SesionUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.awt.Desktop;
import java.io.IOException;

public class EmitirJustificanteDesdeConsultaFrame extends JPanel {

    private final PanelManager panelManager;
    private JButton guardarBtn;
    private final JTextField idField;
    private final JTextField nombreField;
    private final JTextField motivoField;
    private DatePickerUDLAP inicioPicker;
    private DatePickerUDLAP finPicker;
    private final JTextArea diagnosticoArea;
    private final JLabel mensajeError;
    private File archivoReceta;

    public EmitirJustificanteDesdeConsultaFrame(PanelManager panelManager) {
        this.panelManager = panelManager;

        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Font titleFont = new Font("Arial", Font.BOLD, 18);

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titulo = new JLabel("Emitir Justificante Médico", SwingConstants.CENTER);
        titulo.setFont(titleFont);
        titulo.setForeground(ColoresUDLAP.VERDE_SOLIDO);
        add(titulo, gbc);
        gbc.gridwidth = 1;

        // ID
        gbc.gridy++;
        gbc.gridx = 0;
        add(label("ID del Paciente:", labelFont), gbc);
        gbc.gridx = 1;
        idField = campoTexto(fieldFont);
        add(idField, gbc);

        // Nombre
        gbc.gridy++;
        gbc.gridx = 0;
        add(label("Nombre del Paciente:", labelFont), gbc);
        gbc.gridx = 1;
        nombreField = campoTexto(fieldFont);
        nombreField.setEditable(false);
        add(nombreField, gbc);

        // Autocompletar nombre
        idField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = idField.getText().trim();
                if (input.matches("\\d{6}")) {
                    int id = Integer.parseInt(input);
                    try (Connection conn = ConexionSQLite.conectar();
                            PreparedStatement pst = conn.prepareStatement(
                                    "SELECT Nombre, ApellidoPaterno, ApellidoMaterno FROM InformacionAlumno WHERE ID = ?")) {
                        pst.setInt(1, id);
                        try (ResultSet rs = pst.executeQuery()) {
                            if (rs.next()) {
                                String full = rs.getString("Nombre") + " "
                                        + rs.getString("ApellidoPaterno") + " "
                                        + rs.getString("ApellidoMaterno");
                                nombreField.setText(full);
                                mensajeError.setText("");
                            } else {
                                nombreField.setText("");
                                mensajeError.setText("⚠️ Paciente no registrado.");
                            }
                        }
                    } catch (SQLException ex) {
                        nombreField.setText("");
                        mensajeError.setText("Error BD.");
                    }
                } else {
                    nombreField.setText("");
                }
            }
        });

        // Motivo
        gbc.gridy++;
        gbc.gridx = 0;
        add(label("Motivo:", labelFont), gbc);
        gbc.gridx = 1;
        motivoField = campoTexto(fieldFont);
        add(motivoField, gbc);

        // Inicio de Reposo
        gbc.gridy++;
        gbc.gridx = 0;
        add(label("Inicio de Reposo:", labelFont), gbc);
        gbc.gridx = 1;
        inicioPicker = new DatePickerUDLAP();
        add(inicioPicker, gbc);

        // Fin de Reposo
        gbc.gridy++;
        gbc.gridx = 0;
        add(label("Fin de Reposo:", labelFont), gbc);
        gbc.gridx = 1;
        finPicker = new DatePickerUDLAP();
        add(finPicker, gbc);

        // Diagnóstico
        gbc.gridy++;
        gbc.gridx = 0;
        add(label("Diagnóstico:", labelFont), gbc);
        gbc.gridx = 1;
        diagnosticoArea = new JTextArea(4, 20);
        diagnosticoArea.setFont(fieldFont);
        diagnosticoArea.setLineWrap(true);
        diagnosticoArea.setWrapStyleWord(true);
        diagnosticoArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_HOVER),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(new JScrollPane(diagnosticoArea), gbc);

        // Mensaje error
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mensajeError = new JLabel();
        mensajeError.setForeground(Color.RED);
        mensajeError.setFont(new Font("Arial", Font.BOLD, 13));
        add(mensajeError, gbc);
        gbc.gridwidth = 1;

        // Botones
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(ColoresUDLAP.BLANCO);

        JButton subirArchivoBtn = botonTransparente("Subir Receta", ColoresUDLAP.AZUL_SOLIDO, ColoresUDLAP.AZUL_HOVER);
        subirArchivoBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                archivoReceta = fc.getSelectedFile();
            }
        });

        guardarBtn = botonTransparente("Emitir Justificante", ColoresUDLAP.VERDE_SOLIDO, ColoresUDLAP.VERDE_SOLIDO);
        guardarBtn.addActionListener(e -> guardar());

        JButton cancelarBtn = botonTransparente("Cancelar", ColoresUDLAP.ROJO_SOLIDO, ColoresUDLAP.ROJO_HOVER);
        cancelarBtn.addActionListener(e -> panelManager.showPanel("justificantes"));

        panelBotones.add(subirArchivoBtn);
        panelBotones.add(guardarBtn);
        panelBotones.add(cancelarBtn);
        add(panelBotones, gbc);
    }

    private void guardar() {
        String id = idField.getText().trim();
        String nombre = nombreField.getText().trim();
        String motivo = motivoField.getText().trim();
        String diagnostico = diagnosticoArea.getText().trim();
        LocalDate inicio = inicioPicker.getDate();
        LocalDate fin = finPicker.getDate();

        if (id.isEmpty() || nombre.isEmpty() || motivo.isEmpty() || diagnostico.isEmpty()) {
            mensajeError.setText("⚠️ Todos los campos son obligatorios.");
            return;
        }
        if (inicio == null || !inicio.isAfter(LocalDate.now())) {
            mensajeError.setText("⚠️ Fecha inicio debe ser futura.");
            return;
        }
        if (fin == null || inicio.isAfter(fin)) {
            mensajeError.setText("⚠️ Fecha fin inválida.");
            return;
        }

        Justificante j = new Justificante(id, nombre, motivo, inicio, fin, diagnostico, archivoReceta);
        j.setEstado("Aprobado");
        j.setResueltoPor(SesionUsuario.getMedicoActual());
        j.setFechaResolucion(LocalDate.now());

        boolean ok = JustificanteDAO.guardarJustificante(j);
        if (ok) {
            mensajeError.setForeground(new Color(0, 153, 0));
            mensajeError.setText("Justificante emitido correctamente.");
            guardarBtn.setEnabled(false);
            // Generar y abrir PDF
            Justificante justi = JustificanteDAO.obtenerPorFolio(j.getFolio()).orElse(null);
            if (justi != null) {
                File pdf = GeneradorPDFJustificante.generar(justi);
                if (pdf != null && pdf.exists()) {
                    try {
                        Desktop.getDesktop().open(pdf);
                    } catch (IOException ex) {
                        /* ignorar */ }
                }
            }
        } else {
            mensajeError.setForeground(Color.RED);
            mensajeError.setText("Error al guardar justificante.");
        }
    }

    private JLabel label(String texto, Font font) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(font);
        lbl.setForeground(ColoresUDLAP.NEGRO);
        return lbl;
    }

    private JTextField campoTexto(Font font) {
        JTextField t = new JTextField(20);
        t.setFont(font);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_HOVER),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return t;
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
}