package GestionEnfermedades;

import javax.swing.*;
import java.awt.*;
import Utilidades.ColoresUDLAP;
import Utilidades.PanelProvider;

public class PanelEditarDatosPaciente extends JPanel implements PanelProvider {

    private final FormularioGestionEnfermedades formulario;

    public PanelEditarDatosPaciente(boolean esMedico, int idUsuario) {
        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        // Instancia del formulario con rol e ID
        formulario = new FormularioGestionEnfermedades(esMedico, idUsuario);

        // Scroll pane para desplazar contenido si es necesario
        JScrollPane scroll = new JScrollPane(formulario);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public String getPanelName() {
        return "gestionEnfermedades";
    }
}
