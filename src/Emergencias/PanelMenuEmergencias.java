package Emergencias;

import Utilidades.ColoresUDLAP;
import Utilidades.PanelManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelMenuEmergencias extends JPanel {

    private final PanelManager panelManager;
    private final boolean esMedico;
    private final int userId;

    public PanelMenuEmergencias(PanelManager panelManager, boolean esMedico, int userId) {
        this.panelManager = panelManager;
        this.esMedico = esMedico;
        this.userId = userId;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(ColoresUDLAP.BLANCO);
        setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));

        JLabel titulo = new JLabel("Seleccione una opción de emergencia:");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titulo);
        add(Box.createVerticalStrut(40));

        // 1) Registrar llamada de emergencia (VERDE_SOLIDO)
        JButton btnLlamada = crearBoton(
                "Registrar llamada de emergencia",
                ColoresUDLAP.VERDE_SOLIDO,
                ColoresUDLAP.VERDE_HOVER);
        btnLlamada.addActionListener(e -> {
            FormularioLlamadaEmergencia panel = new FormularioLlamadaEmergencia(esMedico, userId);
            panelManager.mostrarPanelPersonalizado(panel);
        });
        add(btnLlamada);
        add(Box.createVerticalStrut(30));

        // 2) Llenar reporte de accidente (NARANJA_SOLIDO)
        JButton btnAccidente = crearBoton(
                "Llenar reporte de accidente",
                ColoresUDLAP.NARANJA_SOLIDO,
                ColoresUDLAP.NARANJA_HOVER);
        btnAccidente.addActionListener(e -> {
            FormularioAccidenteCompleto panel = new FormularioAccidenteCompleto();
            panelManager.mostrarPanelPersonalizado(panel);
        });
        add(btnAccidente);
        add(Box.createVerticalStrut(30));

        // 3) Ver Emergencias (VERDE_SOLIDO)
        JButton btnVerEmergencias = crearBoton(
                "Ver Emergencias",
                ColoresUDLAP.VERDE_SOLIDO,
                ColoresUDLAP.VERDE_HOVER);
        btnVerEmergencias.addActionListener(e -> panelManager.showPanel("verEmergencias"));
        add(btnVerEmergencias);        
        add(Box.createVerticalStrut(30));

        // 4) Ver Accidentes (NARANJA_SOLIDO)
        JButton btnVerAccidentes = crearBoton(
                "Ver Accidentes",
                ColoresUDLAP.NARANJA_SOLIDO,
                ColoresUDLAP.NARANJA_HOVER);
        btnVerAccidentes.addActionListener(e ->
                panelManager.showPanel("verAccidentes"));
        add(btnVerAccidentes);

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
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.repaint();
            }
        });
        return btn;
    }
}
