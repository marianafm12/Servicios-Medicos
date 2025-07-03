package Utilidades;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * JComboBox con estilo UDLAP:
 * - fondo blanco y borde gris claro
 * - placeholder en gris claro e itálica
 * - flecha blanca sobre NARANJA_SOLIDO
 * - hover en NARANJA_HOVER con texto blanco/negrita
 * - scroll de lista en VERDE_HOVER
 * - altura igual a un JTextField estándar
 */
public class ComboBoxUDLAP<T> extends JComboBox<T> {
    private final T placeholder;
    private final CustomRenderer renderer;

    public ComboBoxUDLAP(T placeholder, T[] items) {
        super();
        this.placeholder = placeholder;
        addItem(placeholder);
        for (T item : items) {
            addItem(item);
        }
        this.renderer = new CustomRenderer(placeholder.toString(), ColoresUDLAP.NARANJA_HOVER);
        initUI();
    }

    public ComboBoxUDLAP(T[] items) {
        super(items);
        this.placeholder = null;
        this.renderer = new CustomRenderer(null, ColoresUDLAP.NARANJA_HOVER);
        initUI();
    }

    private void initUI() {
        if (placeholder != null) {
            setSelectedIndex(0);
        }
        setBackground(Color.WHITE);
        setRenderer(renderer);
        setUI(new CustomComboBoxUI(ColoresUDLAP.NARANJA_SOLIDO, renderer));

        addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                // al abrir, si el primer elemento es el placeholder, lo quitamos
                if (placeholder != null
                        && getItemCount() > 0
                        && placeholder.equals(getItemAt(0))) {
                    removeItemAt(0);
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // al cerrar, si no está, lo insertamos de nuevo
                if (placeholder != null
                        && (getItemCount() == 0 || !placeholder.equals(getItemAt(0)))) {
                    insertItemAt(placeholder, 0);
                }
                // si no hay selección válida, marcamos el placeholder
                if (getSelectedItem() == null) {
                    setSelectedIndex(0);
                }
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });

        int baseHeight = new JTextField().getPreferredSize().height;
        int baseWidth = new JTextField().getPreferredSize().width;
        setPreferredSize(new Dimension(baseWidth + 60, baseHeight + 8));

        Border line = BorderFactory.createLineBorder(ColoresUDLAP.GRIS_HOVER);
        Border empty = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        setBorder(BorderFactory.createCompoundBorder(line, empty));
    }

    /**
     * Antes de mostrar el popup, elimina el placeholder de los ítems
     * para que no aparezca en la lista desplegable.
     */
    @Override
    public void showPopup() {
        if (placeholder != null && getItemCount() > 0 && placeholder.equals(getItemAt(0))) {
            removeItemAt(0);
        }
        super.showPopup();
    }

    /**
     * Después de cerrar el popup, reinserta el placeholder en la posición 0.
     * Si no hay selección (getSelectedItem()==null), lo marca como seleccionado.
     */
    @Override
    public void hidePopup() {
        super.hidePopup();
        if (placeholder != null && (getItemCount() == 0 || !placeholder.equals(getItemAt(0)))) {
            insertItemAt(placeholder, 0);
        }
        if (getSelectedItem() == null) {
            setSelectedIndex(0);
        }
    }

    /**
     * Si el elemento seleccionado coincide con el placeholder,
     * devuelve null en lugar de dicho placeholder.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T getSelectedItem() {
        Object sel = super.getSelectedItem();
        if (sel != null && placeholder != null && placeholder.equals(sel)) {
            return null;
        }
        return (T) sel;
    }

    // -------------------------
    // Custom UI y Renderers
    // -------------------------

    private static class CustomComboBoxUI extends BasicComboBoxUI {
        private final Color arrowBg;
        private final CustomRenderer renderer;

        public CustomComboBoxUI(Color arrowBg, CustomRenderer renderer) {
            this.arrowBg = arrowBg;
            this.renderer = renderer;
        }

        @Override
        protected JButton createArrowButton() {
            JButton btn = new JButton(new ArrowIcon(12, 12, arrowBg, Color.WHITE));
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setContentAreaFilled(true);
            btn.setBackground(arrowBg);
            return btn;
        }

        @Override
        protected void installListeners() {
            super.installListeners();

            BasicComboPopup popup = (BasicComboPopup) this.popup;
            JList<?> list = popup.getList();
            list.setCellRenderer(renderer);

            // Hover en lista
            list.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int idx = list.locationToIndex(e.getPoint());
                    renderer.setRolloverIndex(idx);
                    list.repaint();
                }
            });

            // Personalizamos el scroll del popup
            SwingUtilities.invokeLater(() -> {
                JScrollPane scroll = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, list);
                if (scroll != null) {
                    JScrollBar vsb = scroll.getVerticalScrollBar();
                    if (vsb != null) {
                        vsb.setUI(new CustomScrollBarUI(ColoresUDLAP.VERDE_HOVER));
                    }
                    JScrollBar hsb = scroll.getHorizontalScrollBar();
                    if (hsb != null) {
                        hsb.setUI(new CustomScrollBarUI(ColoresUDLAP.VERDE_HOVER));
                    }
                }
            });
        }

        @Override
        public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(Color.WHITE);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            super.paintCurrentValue(g, bounds, false);
        }
    }

    private static class CustomScrollBarUI extends BasicScrollBarUI {
        private final Color thumbColor;

        public CustomScrollBarUI(Color thumbColor) {
            this.thumbColor = thumbColor;
        }

        @Override
        protected void configureScrollBarColors() {
            this.thumbHighlightColor = thumbColor.brighter();
            this.thumbDarkShadowColor = thumbColor.darker();
            this.trackColor = Color.WHITE;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y,
                    thumbBounds.width, thumbBounds.height, 4, 4);
            g2.dispose();
        }

        @Override
        protected Dimension getMinimumThumbSize() {
            return new Dimension(20, 20);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            return btn;
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            return btn;
        }
    }

    private static class ArrowIcon implements Icon {
        private final int w, h;
        private final Color bg, fg;

        public ArrowIcon(int w, int h, Color bg, Color fg) {
            this.w = w;
            this.h = h;
            this.bg = bg;
            this.fg = fg;
        }

        @Override
        public int getIconWidth() {
            return w;
        }

        @Override
        public int getIconHeight() {
            return h;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRect(x, y, w, h);
            g2.setColor(fg);
            Polygon p = new Polygon();
            p.addPoint(x + w / 4, y + h / 3);
            p.addPoint(x + w * 3 / 4, y + h / 3);
            p.addPoint(x + w / 2, y + h * 2 / 3);
            g2.fill(p);
            g2.dispose();
        }
    }

    private static class CustomRenderer extends DefaultListCellRenderer {
        private final String placeholder;
        private final Color hoverBg;
        private int rolloverIndex = -1;

        public CustomRenderer(String placeholder, Color hoverBg) {
            this.placeholder = placeholder;
            this.hoverBg = hoverBg;
        }

        public void setRolloverIndex(int idx) {
            this.rolloverIndex = idx;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, false, cellHasFocus);

            if (index == -1) {
                // Combo cerrado
                if (placeholder != null && placeholder.equals(value)) {
                    setForeground(Color.GRAY);
                    setFont(getFont().deriveFont(Font.ITALIC));
                } else {
                    setForeground(Color.BLACK);
                    setFont(getFont().deriveFont(Font.PLAIN));
                }
            } else {
                // Lista desplegada
                setBackground(list.getBackground());
                setForeground(list.getForeground());
                setFont(getFont().deriveFont(Font.PLAIN));

                if (placeholder != null && placeholder.equals(value)) {
                    setForeground(Color.GRAY);
                    setFont(getFont().deriveFont(Font.ITALIC));
                }
                if (index == rolloverIndex) {
                    setBackground(hoverBg);
                    setForeground(Color.WHITE);
                    setFont(getFont().deriveFont(Font.BOLD));
                }
            }
            return this;
        }
    }
}
