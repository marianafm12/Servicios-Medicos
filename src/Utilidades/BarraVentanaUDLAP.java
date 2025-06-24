package Utilidades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BarraVentanaUDLAP extends JPanel {
    public BarraVentanaUDLAP(JFrame frame) {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        setBackground(ColoresUDLAP.NARANJA_BARRA);
        setPreferredSize(new Dimension(0, 34));

        add(makeButton("_", e -> frame.setState(Frame.ICONIFIED)));
        add(makeButton("□", e -> {
            int state = frame.getExtendedState();
            if ((state & Frame.MAXIMIZED_BOTH) == 0)
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            else
                frame.setExtendedState(Frame.NORMAL);
        }));
        add(makeButton("✕", e -> System.exit(0)));

        // Permite arrastrar la ventana por la barra
        final Point[] clickPoint = { null };
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                clickPoint[0] = e.getPoint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (clickPoint[0] != null)
                    frame.setLocation(
                            frame.getX() + e.getX() - clickPoint[0].x,
                            frame.getY() + e.getY() - clickPoint[0].y);
            }
        });
    }

    private JButton makeButton(String texto, ActionListener al) {
        JButton b = new JButton(texto);
        b.setPreferredSize(new Dimension(38, 34));
        b.setFocusPainted(false);
        b.setBorder(null);
        b.setFont(new Font("Dialog", Font.BOLD, 15));
        b.setForeground(Color.WHITE);
        b.setBackground(ColoresUDLAP.NARANJA_BARRA);
        b.addActionListener(al);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(new Color(230, 80, 0));
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(ColoresUDLAP.NARANJA_BARRA);
            }
        });
        return b;
    }
}