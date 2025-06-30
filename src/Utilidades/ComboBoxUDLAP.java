package Utilidades;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

public class ComboBoxUDLAP<E> extends JComboBox<E> {

    public ComboBoxUDLAP(E[] items) {
        super(agregarTextoInicial(items));
        setFont(new Font("Arial", Font.PLAIN, 14));
        setBackground(Color.WHITE);
        setForeground(ColoresUDLAP.NEGRO);
        setBorder(BorderFactory.createLineBorder(ColoresUDLAP.NARANJA_BARRA, 1));
        setRenderer(new UDLAPRenderer<>());
        setUI(new ComboBoxUDLAPUI());
        setSelectedIndex(0);
    }

    // Agrega la opción "Selecciona una opción" al inicio
    @SuppressWarnings("unchecked")
    private static <E> E[] agregarTextoInicial(E[] items) {
        Object[] nuevo = new Object[items.length + 1];
        nuevo[0] = "Selecciona una opción";
        System.arraycopy(items, 0, nuevo, 1, items.length);
        return (E[]) nuevo;
    }

    // Renderer para color institucional y fondo blanco siempre
    private static class UDLAPRenderer<E> extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JLabel comp = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            comp.setFont(new Font("Arial", Font.PLAIN, 14));
            comp.setOpaque(true);

            // Fondo blanco SIEMPRE en el "campo" (index == -1)
            if (index == -1) {
                comp.setBackground(Color.WHITE);
                if ("Selecciona una opción".equals(value)) {
                    comp.setForeground(ColoresUDLAP.BLANCO);
                } else {
                    comp.setForeground(ColoresUDLAP.NEGRO);
                }
            } else {
                // Lista desplegada
                if ("Selecciona una opción".equals(value)) {
                    comp.setForeground(ColoresUDLAP.GRIS_OSCURO);
                    comp.setBackground(Color.WHITE);
                } else if (isSelected) {
                    comp.setBackground(ColoresUDLAP.NARANJA_HOVER);
                    comp.setForeground(Color.WHITE);
                } else {
                    comp.setBackground(Color.WHITE);
                    comp.setForeground(ColoresUDLAP.NEGRO);
                }
            }
            return comp;
        }
    }

    // UI personalizada solo para la flecha y scrollbar
    private static class ComboBoxUDLAPUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            return new ArrowButtonUDLAP();
        }

        @Override
        protected ComboPopup createPopup() {
            ComboPopup popup = super.createPopup();

            // Encuentra el JScrollPane dentro del popup
            JScrollPane scroll = findScrollPane((Component) popup);
            if (scroll != null) {
                JScrollBar vBar = scroll.getVerticalScrollBar();
                JScrollBar hBar = scroll.getHorizontalScrollBar();
                vBar.setUI(new UDLAPScrollBarUI());
                vBar.setBackground(Color.WHITE);
                vBar.setPreferredSize(new Dimension(14, 60));
                if (hBar != null) {
                    hBar.setUI(new UDLAPScrollBarUI());
                    hBar.setBackground(Color.WHITE);
                    hBar.setPreferredSize(new Dimension(60, 14));
                }
            }
            return popup;
        }

        // Busca recursivamente un JScrollPane dentro del popup
        private JScrollPane findScrollPane(Component comp) {
            if (comp instanceof JScrollPane)
                return (JScrollPane) comp;
            if (comp instanceof Container) {
                for (Component child : ((Container) comp).getComponents()) {
                    JScrollPane found = findScrollPane(child);
                    if (found != null)
                        return found;
                }
            }
            return null;
        }
    }

    // Flecha institucional: blanca con fondo naranja
    private static class ArrowButtonUDLAP extends JButton {
        private boolean hovered = false;

        public ArrowButtonUDLAP() {
            setPreferredSize(new Dimension(28, 24));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder());
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    hovered = true;
                    repaint();
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hovered ? ColoresUDLAP.NARANJA_HOVER : ColoresUDLAP.NARANJA_BARRA);
            g2.fillRect(0, 0, getWidth(), getHeight());
            int w = getWidth(), h = getHeight();
            int size = Math.min(w, h) / 2 + 2;
            int x = w / 2, y = h / 2 + 1;
            Polygon flecha = new Polygon(
                    new int[] { x - size / 2, x + size / 2, x },
                    new int[] { y - size / 4, y - size / 4, y + size / 2 },
                    3);
            g2.setColor(Color.WHITE);
            g2.fillPolygon(flecha);
            g2.dispose();
        }
    }

    // ScrollBar institucional: verde hover
    private static class UDLAPScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = ColoresUDLAP.VERDE_HOVER;
            this.thumbHighlightColor = ColoresUDLAP.VERDE_HOVER.darker();
            this.thumbDarkShadowColor = ColoresUDLAP.VERDE_HOVER;
            this.thumbLightShadowColor = ColoresUDLAP.VERDE_HOVER;
            this.trackColor = Color.WHITE;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            btn.setMinimumSize(new Dimension(0, 0));
            btn.setMaximumSize(new Dimension(0, 0));
            btn.setVisible(false);
            return btn;
        }
    }

    // Devuelve null si está en la opción inicial
    public E getValorSeleccionado() {
        int idx = getSelectedIndex();
        if (idx <= 0)
            return null;
        return (E) getItemAt(idx);
    }
}
