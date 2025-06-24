package Inicio;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de bienvenida o portada.
 */
public class PanelPortada extends JPanel {
    public PanelPortada() {
        setLayout(new BorderLayout());
        JLabel lbl = new JLabel("Bienvenido a Servicios Médicos UDLAP", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 28));
        add(lbl, BorderLayout.CENTER);
    }
}
