package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;

public class HistorialMedicoItem extends JPanel {
    public HistorialMedicoItem(String fecha, String diagnostico, String sintomas, String medicamentos, String receta) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JTextArea area = new JTextArea(String.format("""
                Fecha: %s
                Diagnóstico: %s
                Síntomas: %s
                Medicamentos: %s
                Receta: %s
                """, fecha, diagnostico, sintomas, medicamentos, receta));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(getBackground());
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        add(area, BorderLayout.CENTER);
    }
}
