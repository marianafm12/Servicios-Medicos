package Inicio;

import Emergencias.EmergenciaDAO;
import Justificantes.JustificanteDAO;
import Justificantes.SolicitudesJustificantesFrame;
import Utilidades.*;

import javax.swing.*;
import java.awt.*;

public class PanelNotificaciones extends JPanel {
    private final PanelManager panelManager;
    private JButton btnEmergencias;
    private JButton btnJustificantes;
    private final MensajeErrorUDLAP mensajeInline = new MensajeErrorUDLAP();

    public PanelNotificaciones(PanelManager panelManager) {
        this.panelManager = panelManager;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(ColoresUDLAP.BLANCO);
        setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));

        JLabel titulo = new JLabel("Notificaciones Pendientes");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titulo);
        add(Box.createVerticalStrut(40));

        // creamos los botones pero sin fijar aún su texto
        btnEmergencias = crearBoton("", ColoresUDLAP.VERDE_SOLIDO, ColoresUDLAP.VERDE_HOVER);
        btnEmergencias.addActionListener(e -> panelManager.showPanel("verEmergencias"));
        add(btnEmergencias);
        add(Box.createVerticalStrut(30));

        btnJustificantes = crearBoton("", ColoresUDLAP.NARANJA_SOLIDO, ColoresUDLAP.NARANJA_HOVER);
        btnJustificantes.addActionListener(evt -> panelManager.mostrarPanelPersonalizado(
                new SolicitudesJustificantesFrame(panelManager, mensajeInline)));
        add(btnJustificantes);
    }

    private void updateCounts() {
        int emPend = EmergenciaDAO.contarPendientes();
        int justPend = JustificanteDAO.contarJustificantesPendientes();

        btnEmergencias.setText("Emergencias pendientes: " + emPend);
        btnJustificantes.setText("Justificantes pendientes: " + justPend);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            updateCounts();
        }
    }

    private JButton crearBoton(String texto, Color base, Color hover) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(350, 60));
        btn.setFont(new Font("Arial", Font.BOLD, 19));
        btn.setForeground(ColoresUDLAP.BLANCO);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.repaint();
            }
        });
        return btn;
    }
}
