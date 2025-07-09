package Utilidades;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Barra de control personalizada UDLAP.
 * • Altura fija
 * • Esquinas superiores redondeadas (solo en estado no-maximizado)
 * • Rectangular al maximizar
 * • Botones de minimizar, maximizar/restaurar y cerrar
 * • Transición suave entre color sólido y color hover
 * • Arrastre de la ventana al pulsar la barra
 */
public class BarraVentanaUDLAP extends JPanel {

    private static final int ALTURA = 30; // alto de la barra
    private static final int ARC = 8; // radio esquinas superiores
    private static final int ARC_BTN = 6; // radio esquinas de cada botón

    private final Color colorFondo;
    private final Color colorHover;
    private final JButton btnMinimizar;
    private final JButton btnMaximizar;
    private final JButton btnCerrar;

    /** Constructor “solo colores” */
    public BarraVentanaUDLAP(boolean esError) {
        // Selección de paleta
        if (esError) {
            colorFondo = ColoresUDLAP.ROJO_SOLIDO;
            colorHover = ColoresUDLAP.ROJO_HOVER;
        } else {
            colorFondo = ColoresUDLAP.NARANJA_SOLIDO;
            colorHover = ColoresUDLAP.NARANJA_HOVER;
        }

        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        setBorder(new EmptyBorder(0, 8, 0, 8));
        setPreferredSize(new Dimension(0, ALTURA));

        // Creación de los botones con animación
        btnMinimizar = crearBoton("_");
        btnMaximizar = crearBoton("□");
        btnCerrar = crearBoton("X");

        // Si es modo error, solo agregamos cerrar; si no, los tres
        if (esError) {
            add(btnCerrar);
        } else {
            add(btnMinimizar);
            add(btnMaximizar);
            add(btnCerrar);
        }
        btnCerrar.addActionListener(e -> {
            Window ventana = SwingUtilities.getWindowAncestor(BarraVentanaUDLAP.this);
            if (ventana != null) {
                ventana.dispose();
            }
        });
    }

    /** Constructor “completo”: colores + integración con Window */
    public BarraVentanaUDLAP(Window ventana, boolean esError) {
        this(esError);
        instalarControlVentana(ventana);
        habilitarArrastre(ventana);

        // Cuando redimensiones o cambies estado, reaplicamos la forma
        if (ventana instanceof Frame) {
            Frame f = (Frame) ventana;
            f.addWindowStateListener(e -> aplicarShape(f));
            f.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    aplicarShape(f);
                }
            });
            aplicarShape(f);
        }
    }

    /** Crea un JButton animado que interpola entre colorFondo y colorHover */
    private JButton crearBoton(String simbolo) {
        return new AnimatedButton(simbolo);
    }

    /** Botón con transición suave entre colorFondo y colorHover */
    private class AnimatedButton extends JButton {
        private Timer hoverTimer;
        private float progress = 0f; // 0 = colorFondo, 1 = colorHover
        private boolean hovering = false;

        public AnimatedButton(String simbolo) {
            super(simbolo);
            setFont(getFont().deriveFont(Font.BOLD, 14f));
            setForeground(ColoresUDLAP.BLANCO);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(4, 8, 4, 8));
            setRolloverEnabled(true);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovering = true;
                    startHoverAnimation();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovering = false;
                    startHoverAnimation();
                }
            });
        }

        private void startHoverAnimation() {
            if (hoverTimer != null && hoverTimer.isRunning()) {
                hoverTimer.stop();
            }
            hoverTimer = new Timer(40, evt -> {
                float step = 0.1f;
                if (hovering) {
                    progress = Math.min(1f, progress + step);
                } else {
                    progress = Math.max(0f, progress - step);
                }
                repaint();
                if (progress == 0f || progress == 1f) {
                    hoverTimer.stop();
                }
            });
            hoverTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            // Interpolación lineal entre colorFondo y colorHover
            int r = (int) (colorFondo.getRed() + (colorHover.getRed() - colorFondo.getRed()) * progress);
            int g_ = (int) (colorFondo.getGreen() + (colorHover.getGreen() - colorFondo.getGreen()) * progress);
            int b = (int) (colorFondo.getBlue() + (colorHover.getBlue() - colorFondo.getBlue()) * progress);
            Color curr = new Color(r, g_, b);
            g2.setColor(curr);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_BTN, ARC_BTN);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(colorFondo);

        int w = getWidth(), h = getHeight();
        // esquinas superiores redondeadas sólo cuando no está maximizado
        RoundRectangle2D top = new RoundRectangle2D.Float(0, 0, w, ARC * 2, ARC * 2, ARC * 2);
        Rectangle rect = new Rectangle(0, ARC, w, h - ARC);
        Area shape = new Area(top);
        shape.add(new Area(rect));
        g2.fill(shape);
        g2.dispose();
    }

    // ——— Control de minimizar, maximizar/restaurar y cerrar —————————

    private void instalarControlVentana(Window ventana) {
        btnCerrar.addActionListener(e -> ventana.dispose());
        if (ventana instanceof Frame) {
            Frame f = (Frame) ventana;
            btnMinimizar.addActionListener(e -> f.setExtendedState(f.getExtendedState() | Frame.ICONIFIED));
            btnMaximizar.addActionListener(e -> {
                int estado = f.getExtendedState();
                if ((estado & Frame.MAXIMIZED_BOTH) != 0) {
                    f.setExtendedState(Frame.NORMAL);
                } else {
                    f.setExtendedState(Frame.MAXIMIZED_BOTH);
                }
            });
        }
    }

    private void aplicarShape(Frame f) {
        boolean maximizado = (f.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0;
        if (maximizado) {
            f.setShape(null);
        } else {
            f.setShape(new RoundRectangle2D.Float(
                    0, 0, f.getWidth(), f.getHeight(), ARC, ARC));
        }
    }

    // ——— Arrastre de la ventana —————————————————————————————

    private Point startDrag;

    private void habilitarArrastre(Window ventana) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startDrag = e.getPoint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = ventana.getLocation();
                ventana.setLocation(
                        p.x + e.getX() - startDrag.x,
                        p.y + e.getY() - startDrag.y);
            }
        });
    }

    // ——— Getters por si necesitas listeners adicionales ——————————

    public JButton getBtnMinimizar() {
        return btnMinimizar;
    }

    public JButton getBtnMaximizar() {
        return btnMaximizar;
    }

    public JButton getBtnCerrar() {
        return btnCerrar;
    }

}
