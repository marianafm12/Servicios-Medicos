package Justificantes;

import Utilidades.PanelProvider;
import Utilidades.PanelManager;

import javax.swing.*;

public class PanelJustificantesProvider implements PanelProvider {
    private final PanelManager panelManager;

    public PanelJustificantesProvider(PanelManager panelManager) {
        this.panelManager = panelManager;
    }

    @Override
    public JPanel getPanel() {
        return new PanelMenuJustificantes(panelManager); // ✅ PASA EL PARAMETRO
    }

    @Override
    public String getPanelName() {
        return "menuJustificantes";
    }
}

