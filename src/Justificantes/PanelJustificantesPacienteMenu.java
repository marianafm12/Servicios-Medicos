package Justificantes;

import Inicio.SesionUsuario;
import Utilidades.ColoresUDLAP;
import Utilidades.PanelManager;

import javax.swing.*;
import java.awt.*;

public class PanelJustificantesPacienteMenu extends JPanel {

    private final PanelManager panelManager;

    public PanelJustificantesPacienteMenu(PanelManager panelManager) {
        this.panelManager = panelManager;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        Box verticalBox = Box.createVerticalBox();
        verticalBox.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        verticalBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("Gestión de Justificantes Médicos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton btnSolicitar = crearBoton("Solicitar Justificante", ColoresUDLAP.VERDE, ColoresUDLAP.VERDE_HOVER);
        JButton btnVerJustificantes = crearBoton("Mis Justificantes", ColoresUDLAP.NARANJA, ColoresUDLAP.NARANJA_HOVER);

btnSolicitar.addActionListener(e -> {
    FormularioJustificanteFrame panel = new FormularioJustificanteFrame(panelManager);
    panel.setValoresDesdeSesion();
    panelManager.mostrarPanelPersonalizado(panel);
});


btnVerJustificantes.addActionListener(e -> {
    MisJustificantesPacientePanel panel = new MisJustificantesPacientePanel(
            String.valueOf(SesionUsuario.getPacienteActual()),
            panelManager
    );
    panelManager.mostrarPanelPersonalizado(panel);
});


        btnSolicitar.setMaximumSize(new Dimension(300, 50));
        btnVerJustificantes.setMaximumSize(new Dimension(300, 50));

        verticalBox.add(titulo);
        verticalBox.add(Box.createVerticalStrut(20));
        verticalBox.add(btnSolicitar);
        verticalBox.add(Box.createVerticalStrut(20));
        verticalBox.add(btnVerJustificantes);

        add(verticalBox, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto, Color base, Color hover) {
        JButton button = new JButton(texto) {
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
        button.setFont(new Font("Arial", Font.BOLD, 17));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}
