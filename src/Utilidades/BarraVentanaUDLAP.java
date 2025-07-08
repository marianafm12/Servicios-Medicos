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
 * • Hover completo: el fondo del botón se vuelve colorHover
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

        btnMinimizar = crearBoton("_");
        btnMaximizar = crearBoton("□");
        btnCerrar = crearBoton("X");

        add(btnMinimizar);
        add(btnMaximizar);
        add(btnCerrar);
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

    /**
     * Crea un JButton que pinta todo su fondo con colorHover en rollover,
     * y transparente en estado normal.
     */
    private JButton crearBoton(String simbolo) {
        JButton b = new JButton(simbolo) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // si el modelo está en rollover, pintamos todo el fondo
                if (getModel().isRollover()) {
                    g2.setColor(colorHover);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                            ARC_BTN, ARC_BTN);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        b.setFont(b.getFont().deriveFont(Font.BOLD, 14f));
        b.setForeground(ColoresUDLAP.BLANCO);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Importante: asegurar que el L&F no pinte el botón por debajo
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);

        // Margen interno para darle tamaño uniforme
        b.setBorder(new EmptyBorder(4, 8, 4, 8));

        // Habilitamos rollover (por defecto ya suele ser true, pero por si acaso)
        b.setRolloverEnabled(true);

        return b;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(colorFondo);

        int w = getWidth(), h = getHeight();
        // esquinas superiores redondeadas sólo cuando no está maximizado
        RoundRectangle2D top = new RoundRectangle2D.Float(
                0, 0, w, ARC * 2, ARC * 2, ARC * 2);
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

    // Aplica el recorte de ventana (redondeo vs. rectangular)
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
