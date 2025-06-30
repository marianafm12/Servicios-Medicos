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
        this.emergencia    = emergencia;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10,10));
        setBackground(ColoresUDLAP.BLANCO);

        // --- Título ---
        JLabel titulo = new JLabel("Detalle de Emergencia", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        // --- Centro: datos en GridLayout ---
        JPanel centro = new JPanel(new GridLayout(0,1,5,5));
        centro.setBackground(ColoresUDLAP.BLANCO);
        centro.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        centro.add(makeRow("ID Emergencia: ", String.valueOf(emergencia.getId())));
        centro.add(makeRow("ID Paciente: ", String.valueOf(emergencia.getIdPaciente())));
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
        centro.add(makeRow("Médico Responsable: ",
            emergencia.getMedicoResponsable() != null
                ? emergencia.getMedicoResponsable()
                : "-"));

        add(new JScrollPane(centro), BorderLayout.CENTER);

        // --- Botón Regresar ---
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBackground(ColoresUDLAP.BLANCO);
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 15));
        btnRegresar.setBackground(Color.GRAY);
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.addActionListener(e ->
            panelManager.showPanel("verEmergencias")
        );
        botones.add(btnRegresar);
        add(botones, BorderLayout.SOUTH);
    }

    private JPanel makeRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(5,0));
        row.setBackground(ColoresUDLAP.BLANCO);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.PLAIN, 16));

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        return row;
    }
}
