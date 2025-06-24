package GestionEnfermedades;

import Utilidades.PanelProvider;
import javax.swing.*;

public class PanelGestionEnfermedades implements PanelProvider {

    private final JPanel panel;

    public PanelGestionEnfermedades(boolean esMedico, int idUsuario) {
        this.panel = new FormularioGestionEnfermedades(esMedico, idUsuario);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public String getPanelName() {
        return esPanelDePaciente() ? "verDatosPaciente" : "editarDatosPaciente";
    }

    private boolean esPanelDePaciente() {
        // Determinar el nombre del panel en base al tipo de usuario
        // Este método es útil si en InterfazMedica se quiere alternar el nombre
        // mostrado en el panelManager
        if (panel instanceof FormularioGestionEnfermedades form) {
            return !form.esMedico;
        }
        return false;
    }
}
