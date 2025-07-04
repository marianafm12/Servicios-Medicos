package Utilidades;

import javax.swing.*;
import java.awt.*;

/**
 * Componente para mostrar mensajes de error, advertencia o éxito inline,
 * y lanzar un diálogo modal de error/advertencia con estilo UDLAP.
 */
public class MensajeErrorUDLAP extends JLabel {
    private static final Icon ICON_ERROR = UIManager.getIcon("OptionPane.errorIcon");
    private static final Icon ICON_WARN = UIManager.getIcon("OptionPane.warningIcon");
    private static final Icon ICON_INFO = UIManager.getIcon("OptionPane.informationIcon");

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

    /** Muestra un mensaje de éxito inline. */
    public void mostrarExito(String texto) {
        setText(texto);
        setIcon(ICON_INFO);
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
    public VentanaErrorUDLAP(Window owner, String titulo, String mensaje) {
        super(owner, titulo, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setLayout(new BorderLayout());

        // Barra superior personalizada (roja)
        add(new BarraVentanaUDLAP(true), BorderLayout.NORTH);

        // Borde rojo alrededor de toda la ventana
        getRootPane().setBorder(BorderFactory.createLineBorder(ColoresUDLAP.ROJO_SOLIDO, 2));

        // Mensaje central sin línea intermedia
        JLabel lblMsg = new JLabel(mensaje, SwingConstants.CENTER);
        lblMsg.setFont(new Font("Arial", Font.PLAIN, 14));
        lblMsg.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(ColoresUDLAP.BLANCO);
        content.add(lblMsg, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        // Botón de cierre al pie
        PanelBotonesFormulario panelBot = new PanelBotonesFormulario(
                new PanelBotonesFormulario.BotonConfig("Cerrar", PanelBotonesFormulario.BotonConfig.Tipo.DANGER));
        panelBot.setListeners(e -> dispose());
        add(panelBot, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }
}