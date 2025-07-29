package Emergencias;

import javax.swing.*;
import java.awt.*;
import Utilidades.ColoresUDLAP;
import Utilidades.MensajeErrorUDLAP;
import Utilidades.PanelProvider;

public class PanelReportarEmergencia extends JPanel implements PanelProvider {

    public PanelReportarEmergencia() {
        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        // Título
        JLabel titulo = new JLabel("Reportar Emergencia", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(ColoresUDLAP.ROJO_SOLIDO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        // Botones centrales
        JPanel centro = new JPanel(new GridLayout(2, 1, 20, 20));
        centro.setBackground(ColoresUDLAP.BLANCO);
        centro.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));

        JButton btnMedicos = crearBotonEmergencia("Llamar a Servicios Médicos", ColoresUDLAP.VERDE_SOLIDO);
        btnMedicos.addActionListener(e -> mostrarLlamada("Servicios Médicos"));

        JButton btnSeguridad = crearBotonEmergencia("Llamar a Seguridad UDLAP", ColoresUDLAP.NARANJA_SOLIDO);
        btnSeguridad.addActionListener(e -> mostrarLlamada("Seguridad UDLAP"));

        centro.add(btnMedicos);
        centro.add(btnSeguridad);
        add(centro, BorderLayout.CENTER);
    }

    private JButton crearBotonEmergencia(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(280, 50));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void mostrarLlamada(String destino) {
        MensajeErrorUDLAP.mostrarVentanaInformacion(SwingUtilities.getWindowAncestor(this), "Llamada en curso",
                "Llamando a " + destino + "...\n\nEspere mientras se procesa la emergencia.");
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public String getPanelName() {
        return "reportarEmergencia";
    }
}
