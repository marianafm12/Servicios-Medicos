package Utilidades;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.EmptyBorder;

/**
 * Componente para mostrar mensajes de error, advertencia o éxito inline,
 * y lanzar un diálogo modal de error/advertencia con estilo UDLAP.
 */
public class MensajeErrorUDLAP extends JLabel {
    private static final Icon ICON_ERROR = UIManager.getIcon("OptionPane.errorIcon");
    private static final Icon ICON_WARN = UIManager.getIcon("OptionPane.warningIcon");
    private static final Icon ICON_INFO = UIManager.getIcon("OptionPane.informationIcon");
    private static final Icon ICON_SUCCESS = UIManager.getIcon("OptionPane.successIcon");

    public MensajeErrorUDLAP() {
        super("");
        setFont(new Font("Arial", Font.BOLD, 13));
        setOpaque(false);
    }

    /** Muestra un mensaje de error inline. */
    public void mostrarError(String texto) {
        setText(texto);
        setIcon(ICON_ERROR);
        setForeground(ColoresUDLAP.ROJO_SOLIDO);
    }

    /** Muestra un mensaje de advertencia inline. */
    public void mostrarAdvertencia(String texto) {
        setText(texto);
        setIcon(ICON_WARN);
        setForeground(ColoresUDLAP.NARANJA_HOVER);
    }

    /** Muestra un mensaje de información inline. */
    public void mostrarInformacion(String texto) {
        setText(texto);
        setIcon(ICON_INFO);
        setForeground(ColoresUDLAP.AZUL_SOLIDO);
    }

    /** Muestra un mensaje de éxito inline. */
    public void mostrarExito(String texto) {
        setText(texto);
        setIcon(ICON_SUCCESS);
        setForeground(ColoresUDLAP.VERDE_SOLIDO);
    }

    /** Limpia el texto y el ícono. */
    public void limpiar() {
        setText("");
        setIcon(null);
    }

    /**
     * Lanza un diálogo modal de error con barra roja, borde alrededor
     * y botón de cierre.
     */
    public static void mostrarVentanaError(Window owner, String titulo, String mensaje) {
        VentanaErrorUDLAP dialog = new VentanaErrorUDLAP(owner, titulo, mensaje);
        dialog.setVisible(true);
    }
}

/**
 * Diálogo modal con barra superior roja, borde exterior y botón de cierre.
 */
class VentanaErrorUDLAP extends JDialog {
    private static final int ARC = 12; // radio de las cuatro esquinas
    private static final int BORDE = 2; // grosor del borde rojo

    public VentanaErrorUDLAP(Window owner, String titulo, String mensaje) {
        super(owner, titulo, ModalityType.APPLICATION_MODAL);
        // 1) Sin decoración nativa
        setUndecorated(true);
        // 2) Fondo transparente para que se vean las esquinas “recortadas”
        setBackground(new Color(0, 0, 0, 0));

        // 3) Panel raíz que pinta fondo blanco + borde rojo, ambos redondeados
        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();
                // Fondo blanco redondeado
                g2.setColor(ColoresUDLAP.BLANCO);
                g2.fillRoundRect(0, 0, w, h, ARC, ARC);

                // Borde rojo
                g2.setStroke(new BasicStroke(BORDE));
                g2.setColor(ColoresUDLAP.ROJO_SOLIDO);
                g2.drawRoundRect(
                        BORDE / 2, BORDE / 2,
                        w - BORDE, h - BORDE,
                        ARC, ARC);
                g2.dispose();
            }
        };
        root.setOpaque(false);
        // Margen interior para separar contenido del borde
        root.setBorder(new EmptyBorder(0, 0, 0, 0));

        // ——— Barra superior roja ————————————————
        root.add(new BarraVentanaUDLAP(this, true), BorderLayout.NORTH);

        // ——— Mensaje central —————————————————————
        JLabel lblMsg = new JLabel(mensaje, SwingConstants.CENTER);
        lblMsg.setFont(new Font("Arial", Font.PLAIN, 14));
        lblMsg.setBorder(new EmptyBorder(20, 20, 20, 20));
        // fondo transparente para dejar ver el panel raíz
        lblMsg.setOpaque(false);
        root.add(lblMsg, BorderLayout.CENTER);

        // ——— Botón “Cerrar” al pie ——————————————————
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(btnCerrar.getFont().deriveFont(Font.BOLD, 14f));
        btnCerrar.setForeground(ColoresUDLAP.BLANCO);
        btnCerrar.setBackground(ColoresUDLAP.ROJO_SOLIDO);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.setPreferredSize(new Dimension(100, 32));
        btnCerrar.addActionListener(e -> dispose());

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBtn.setOpaque(false);
        pnlBtn.setBorder(new EmptyBorder(0, 0, 20, 0));
        pnlBtn.add(btnCerrar);
        root.add(pnlBtn, BorderLayout.SOUTH);

        setContentPane(root);
        pack();

        // 4) Aplicar Shape al propio diálogo para recortar las esquinas
        setShape(new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), ARC, ARC));

        setLocationRelativeTo(owner);
    }
}