package Utilidades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Componente para mostrar mensajes de error, advertencia o éxito inline,
 * y ventana modal de error/advertencia con estilo UDLAP.
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

    /** Limpia texto e ícono. */
    public void limpiar() {
        setText("");
        setIcon(null);
    }

    /**
     * Lanza una ventana modal de error con barra y botón de cierre.
     */
    public static void mostrarVentanaError(Frame owner, String titulo, String mensaje) {
        VentanaErrorUDLAP dialog = new VentanaErrorUDLAP(owner, titulo, mensaje);
        dialog.setVisible(true);
    }
}

/**
 * Diálogo modal con barra personalizada y botón estilo formulario.
 */
class VentanaErrorUDLAP extends JDialog {
    public VentanaErrorUDLAP(Frame owner, String titulo, String mensaje) {
        super(owner, titulo, true);
        setUndecorated(true);
        setLayout(new BorderLayout());
        // Barra superior
        add(new BarraVentanaUDLAP(this), BorderLayout.NORTH);

        // Contenido central
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(ColoresUDLAP.BLANCO);
        content.setBorder(BorderFactory.createLineBorder(ColoresUDLAP.GRIS_HOVER));

        JLabel lblMsg = new JLabel(mensaje, SwingConstants.CENTER);
        lblMsg.setFont(new Font("Arial", Font.PLAIN, 14));
        lblMsg.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.add(lblMsg, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(owner);
    }
}
