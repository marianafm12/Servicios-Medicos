package Emergencias;

import Utilidades.ColoresUDLAP;
import Utilidades.PanelManager;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Muestra en detalle todos los datos de una Emergencia.
 */
public class PanelDetalleEmergencia extends JPanel {
    private final PanelManager panelManager;
    private final Emergencia emergencia;

    public PanelDetalleEmergencia(PanelManager panelManager, Emergencia emergencia) {
        this.panelManager = panelManager;
        this.emergencia = emergencia;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColoresUDLAP.BLANCO);

        // --- Título ---
        JLabel titulo = new JLabel("Información de la Emergencia", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(ColoresUDLAP.VERDE_SOLIDO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        // --- Centro: datos en GridLayout ---
        JPanel centro = new JPanel(new GridLayout(0, 1, 5, 5));
        centro.setBackground(ColoresUDLAP.BLANCO);
        centro.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        centro.add(makeRow("ID Emergencia: ", String.valueOf(emergencia.getId())));
        centro.add(makeRow(
            "ID Paciente: ",
            emergencia.getIdPaciente() != null 
                ? emergencia.getIdPaciente().toString() 
                : "-"
        ));
        centro.add(makeRow("Ubicación: ", emergencia.getUbicacion()));
        centro.add(makeRow("Tipo de Emergencia: ", emergencia.getTipoDeEmergencia()));
        centro.add(makeRow("Gravedad: ", emergencia.getGravedad()));
        centro.add(makeRow("Descripción: ", emergencia.getDescripcion()));
        centro.add(makeRow("Fecha Incidente: ",
                emergencia.getFechaIncidente().toLocalDateTime().format(fmt)));
        centro.add(makeRow("Fecha Registro: ",
                emergencia.getFechaRegistro().toLocalDateTime().format(fmt)));
        centro.add(makeRow("Estado: ", emergencia.getEstado()));
        centro.add(makeRow("Teléfono Contacto: ",
                emergencia.getTelefonoContacto() != null
                        ? emergencia.getTelefonoContacto()
                        : "-"));
        centro.add(makeRow("Paramédico Responsable: ",
                emergencia.getMedicoResponsable() != null
                        ? emergencia.getMedicoResponsable()
                        : "-"));

        JScrollPane scroll = new JScrollPane(centro);
        scroll.setBorder(BorderFactory.createEmptyBorder());  // quita el marco
        add(scroll, BorderLayout.CENTER);


        // --- Botón Regresar ---
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBackground(ColoresUDLAP.BLANCO);
<<<<<<< Updated upstream
        JButton btnRegresar = botonTransparente("Regresar",
            ColoresUDLAP.VERDE,    // color base
            ColoresUDLAP.VERDE_HOVER    // color hover
        );


        btnRegresar.addActionListener(e ->
            panelManager.showPanel("verEmergencias")
        );
=======
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 15));
        btnRegresar.setBackground(Color.GRAY);
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.addActionListener(e -> panelManager.showPanel("verEmergencias"));
>>>>>>> Stashed changes
        botones.add(btnRegresar);
        add(botones, BorderLayout.SOUTH);
    }

    private JPanel makeRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(5, 0));
        row.setBackground(ColoresUDLAP.BLANCO);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.PLAIN, 16));

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        return row;
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
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

}
