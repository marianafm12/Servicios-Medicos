package Utilidades;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PanelManager {
    private final JPanel contentPanel;
    private final CardLayout cardLayout;
    private final Map<String, PanelProvider> panels = new HashMap<>();
    private int dynamicPanelCounter = 0; // Para paneles personalizados

    public PanelManager(JPanel contentPanel) {
        this.contentPanel = contentPanel;
        this.cardLayout = (CardLayout) contentPanel.getLayout();
    }

    // Registrar paneles estáticos (con nombre fijo)
    public void registerPanel(PanelProvider provider) {
        String panelName = provider.getPanelName();
        if (!panels.containsKey(panelName)) {
            panels.put(panelName, provider);
            contentPanel.add(provider.getPanel(), panelName);
        }
    }

    // Mostrar un panel registrado
    public void showPanel(String panelName) {
        if (panels.containsKey(panelName)) {
            cardLayout.show(contentPanel, panelName);
        }
    }

    // Mostrar un panel dinámico (como CorreosProfesoresPanel) sin registrarlo de forma fija
    public void mostrarPanelPersonalizado(JPanel panel) {
        String panelName = "dinamico_" + (++dynamicPanelCounter);
        contentPanel.add(panel, panelName);
        cardLayout.show(contentPanel, panelName);
    }
}
