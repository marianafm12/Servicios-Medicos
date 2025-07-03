package Utilidades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Barra de ventana UDLAP, aplicable tanto a JFrame como a JDialog.
 */
public class BarraVentanaUDLAP extends JPanel {
    public BarraVentanaUDLAP(Window window) {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        setBackground(ColoresUDLAP.NARANJA_SOLIDO);
        setPreferredSize(new Dimension(0, 30));

        // Minimizar y maximizar solo para frames
        if (window instanceof Frame frame) {
            add(makeButton("_", e -> frame.setState(Frame.ICONIFIED)));
            add(makeButton("□", e -> {
                int state = frame.getExtendedState();
                if ((state & Frame.MAXIMIZED_BOTH) == 0)
                    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                else
                    frame.setExtendedState(Frame.NORMAL);
            }));
        }
        // Cerrar (dispose en diálogos, exit en frame)
        add(makeButton("✕", e -> {
            if (window instanceof Frame)
                System.exit(0);
            else
                window.dispose();
        }));

        // Arrastrar la ventana
        final Point[] clickPoint = { null };
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                clickPoint[0] = e.getPoint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (clickPoint[0] != null) {
                    Point loc = window.getLocation();
                    window.setLocation(
                            loc.x + e.getX() - clickPoint[0].x,
                            loc.y + e.getY() - clickPoint[0].y);
                }
            }
        });
    }

    private JButton makeButton(String texto, ActionListener al) {
        JButton b = new JButton(texto);
        b.setPreferredSize(new Dimension(30, 26));
        b.setFocusPainted(false);
        b.setBorder(null);
        b.setFont(new Font("Dialog", Font.BOLD, 15));
        b.setForeground(Color.WHITE);
        b.setBackground(ColoresUDLAP.NARANJA_SOLIDO);
        b.addActionListener(al);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(new Color(230, 80, 0));
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(ColoresUDLAP.NARANJA_SOLIDO);
            }
        });
        return b;
    }
}
