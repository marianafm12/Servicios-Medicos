package Registro;

import javax.swing.*;
import java.awt.*;
import Utilidades.*;

/**
 * Panel de control para registro de pacientes, usando PanelBotonesFormulario
 * UDLAP.
 */
public class PanelControl extends JPanel {

    public PanelControl(JTextField[] campos) {
        // Layout y estilo del panel
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setBackground(ColoresUDLAP.BLANCO);
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Configuración de los botones: Agregar (VERDE_SOLIDO), Buscar
        // (NARANJA_SOLIDO),
        // Limpiar
        // (ROJO_SOLIDO)
        PanelBotonesFormulario botones = new PanelBotonesFormulario(
                new PanelBotonesFormulario.BotonConfig("Agregar", PanelBotonesFormulario.BotonConfig.Tipo.PRIMARY),
                new PanelBotonesFormulario.BotonConfig("Buscar", PanelBotonesFormulario.BotonConfig.Tipo.SECONDARY),
                new PanelBotonesFormulario.BotonConfig("Limpiar", PanelBotonesFormulario.BotonConfig.Tipo.DANGER));

        // Asignar listeners a cada botón de acuerdo a su orden
        botones.setListeners(
                new AgregarRegistro(campos), // Primer botón -> Agregar
                new Buscar(campos), // Segundo botón -> Buscar
                new LimpiarCampos(campos) // Tercer botón -> Limpiar
        );

        // Agregar el panel de botones al panel de control
        add(botones);
    }
}
