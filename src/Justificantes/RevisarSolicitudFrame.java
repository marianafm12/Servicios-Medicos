package Justificantes;

import Utilidades.ColoresUDLAP;
import Utilidades.DatePickerUDLAP;
import Utilidades.PanelManager;

import Inicio.InterfazMedica;
import Inicio.SesionUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class RevisarSolicitudFrame extends JPanel {

    private JTextField motivoField;
    private DatePickerUDLAP inicioPicker;
    private DatePickerUDLAP finPicker;
    private JTextArea diagnosticoArea;
    private JLabel lblEstado;
    private File archivoReceta;
    private final int folio;
    private final Justificante justificante;
    private final String medicoFirmante = SesionUsuario.getMedicoActual();

    private final PanelManager panelManager;
    private final InterfazMedica interfazMedica;

    public RevisarSolicitudFrame(int folio, ActionListener volverAction,
            PanelManager panelManager, InterfazMedica interfazMedica) {
        this.folio = folio;
        this.panelManager = panelManager;
        this.interfazMedica = interfazMedica;
        this.justificante = JustificanteDAO.obtenerPorFolio(folio).orElse(null);

        if (justificante == null) {
            JOptionPane.showMessageDialog(this, "Justificante no encontrado.");
            return;
        }

        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Font titleFont = new Font("Arial", Font.BOLD, 16);

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Revisión de Solicitud de Justificante", SwingConstants.CENTER);
        lblTitulo.setFont(titleFont);
        lblTitulo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Estado
        gbc.gridx = 0;
        lblEstado = new JLabel("Estado:");
        lblEstado.setFont(labelFont);
        lblEstado.setForeground(ColoresUDLAP.NEGRO);
        add(lblEstado, gbc);

        gbc.gridx = 1;
        JLabel lblEstadoValor = new JLabel(justificante.getEstado());
        lblEstadoValor.setFont(fieldFont);
        add(lblEstadoValor, gbc);

        // Motivo
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 1;
        motivoField = new JTextField(justificante.getMotivo());
        motivoField.setFont(fieldFont);
        motivoField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(motivoField, gbc);

        // Inicio de Reposo
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Inicio de Reposo:"), gbc);
        gbc.gridx = 1;
        inicioPicker = new DatePickerUDLAP();
        inicioPicker.setBlockWeekends(false); // permitir fines de semana si se desea
        inicioPicker.setDate(justificante.getFechaInicio());
        add(inicioPicker, gbc);

        // Fin de Reposo
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Fin de Reposo:"), gbc);
        gbc.gridx = 1;
        finPicker = new DatePickerUDLAP();
        finPicker.setBlockWeekends(false);
        finPicker.setDate(justificante.getFechaFin());
        add(finPicker, gbc);

        // Diagnóstico
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Diagnóstico:"), gbc);
        gbc.gridx = 1;
        diagnosticoArea = new JTextArea(4, 30);
        diagnosticoArea.setFont(fieldFont);
        diagnosticoArea.setText(justificante.getDiagnostico() != null
                ? justificante.getDiagnostico()
                : "");
        diagnosticoArea.setWrapStyleWord(true);
        diagnosticoArea.setLineWrap(true);
        diagnosticoArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(new JScrollPane(diagnosticoArea), gbc);

        // Botones aprobar/rechazar/limpiar
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel filaBotones1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        filaBotones1.setBackground(ColoresUDLAP.BLANCO);
        JButton btnAprobar = botonTransparente("Aprobar", ColoresUDLAP.VERDE, ColoresUDLAP.VERDE_HOVER);
        JButton btnRechazar = botonTransparente("Rechazar", ColoresUDLAP.ROJO, ColoresUDLAP.ROJO_HOVER);
        JButton limpiarBtn = botonTransparente("Limpiar", ColoresUDLAP.NARANJA, ColoresUDLAP.NARANJA_HOVER);
        filaBotones1.add(btnAprobar);
        filaBotones1.add(btnRechazar);
        filaBotones1.add(limpiarBtn);
        add(filaBotones1, gbc);

        // Botones archivo/pdf/volver
        gbc.gridy++;
        JPanel filaBotones2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        filaBotones2.setBackground(ColoresUDLAP.BLANCO);
        JButton abrirArchivoBtn = botonTransparente("Abrir Archivo",
                ColoresUDLAP.AZUL, ColoresUDLAP.AZUL);
        JButton vistaBtn = botonTransparente("Vista preliminar del Justificante",
                ColoresUDLAP.VERDE_OSCURO, ColoresUDLAP.VERDE_OSCURO);
        JButton volverBtn = botonTransparente("Volver",
                ColoresUDLAP.GRIS_CLARO, ColoresUDLAP.GRIS_CLARO);
        filaBotones2.add(abrirArchivoBtn);
        filaBotones2.add(vistaBtn);
        filaBotones2.add(volverBtn);
        add(filaBotones2, gbc);

        // Listeners de aprobación y rechazo
        btnAprobar.addActionListener(e -> {
            LocalDate inicio = inicioPicker.getDate();
            LocalDate fin = finPicker.getDate();
            if (inicio == null || fin == null)
                return;
            if (inicio.isAfter(fin)) {
                JOptionPane.showMessageDialog(this,
                        "La fecha de inicio no puede ser posterior a la fecha de fin.",
                        "Error de Fecha", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String motivo = motivoField.getText().trim();
            String diag = diagnosticoArea.getText().trim();

            boolean ok = JustificanteDAO.aprobarJustificante(
                    folio, motivo, diag, medicoFirmante, inicio, fin);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Justificante aprobado.");

                // Generar y abrir PDF automáticamente
                Justificante justiActualizado = JustificanteDAO.obtenerPorFolio(folio).orElse(null);
                if (justiActualizado != null) {
                    File pdf = GeneradorPDFJustificante.generar(justiActualizado);
                    if (pdf != null && pdf.exists()) {
                        try {
                            Desktop.getDesktop().open(pdf);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(this, "Error al abrir el PDF.");
                        }
                    }
                }

                // Deshabilitar UI tras aprobación
                motivoField.setEditable(false);
                diagnosticoArea.setEditable(false);
                inicioPicker.setEnabled(false);
                finPicker.setEnabled(false);
                btnAprobar.setEnabled(false);
                btnRechazar.setEnabled(false);
                limpiarBtn.setEnabled(false);

                volverAction.actionPerformed(null);
                interfazMedica.checkNotifications();
            }
        });

        btnRechazar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que deseas rechazar esta solicitud?",
                    "Confirmar rechazo", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (JustificanteDAO.rechazarJustificante(folio, medicoFirmante)) {
                    JOptionPane.showMessageDialog(this, "Justificante rechazado.");
                    volverAction.actionPerformed(null);
                }
            }
            interfazMedica.checkNotifications();
        });

        limpiarBtn.addActionListener(e -> {
            diagnosticoArea.setText("");
            motivoField.setText(justificante.getMotivo());
        });

        abrirArchivoBtn.addActionListener(e -> {
            if (archivoReceta != null && archivoReceta.exists()) {
                try {
                    Desktop.getDesktop().open(archivoReceta);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al abrir el archivo.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No hay archivo disponible.");
            }
        });

        vistaBtn.addActionListener(e -> {
            File pdf = GeneradorPDFJustificante.generar(justificante);
            try {
                if (pdf != null && pdf.exists())
                    Desktop.getDesktop().open(pdf);
                else
                    JOptionPane.showMessageDialog(this, "No se pudo generar el PDF.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir el PDF.");
            }
        });

        volverBtn.addActionListener(volverAction);
    }

    private JButton botonTransparente(String texto, Color base, Color hover) {
        JButton button = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
}
