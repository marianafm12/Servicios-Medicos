package Emergencias;

import Utilidades.PanelProvider;
import javax.swing.*;

public class PanelLlamadaEmergencia implements PanelProvider {

    private final JPanel panel;
    public boolean esMedico;

    public PanelLlamadaEmergencia(boolean esMedico, int idUsuario) {
        this.esMedico = esMedico;
        this.panel = new FormularioLlamadaEmergencia(esMedico, idUsuario);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public String getPanelName() {
        return esMedico ? "llamadaEmergencia" : "reportarEmergencia";
    }
}
