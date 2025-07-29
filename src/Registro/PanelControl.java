// src/Registro/PanelControl.java
package Registro;

import javax.swing.*;
import java.awt.*;
import Utilidades.*;

/**
 * Panel de control para registro de pacientes, usando PanelBotonesFormulario
 * UDLAP.
 * Ahora ancla los botones al borde inferior.
 */
public class PanelControl extends JPanel {

        /**
         * @param campos        Array de JTextField que maneja el formulario de
         *                      registro.
         * @param mensajeInline Componente donde se mostrarán los mensajes de
         *                      validación.
         * @param owner         Ventana propietaria (para diálogos, etc.).
         */
        public PanelControl(JTextField[] campos,
                        MensajeErrorUDLAP mensajeInline,
                        Window owner) {
                // Usamos BorderLayout para posicionar el panel de botones al SOUTH:
                super(new BorderLayout());
                setBackground(ColoresUDLAP.BLANCO);
                // Margen interior: 10px arriba y abajo
                setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

                // Creamos el panel estándar de botones:
                PanelBotonesFormulario botones = new PanelBotonesFormulario(
                                new PanelBotonesFormulario.BotonConfig("Agregar",
                                                PanelBotonesFormulario.BotonConfig.Tipo.PRIMARY),
                                new PanelBotonesFormulario.BotonConfig("Buscar",
                                                PanelBotonesFormulario.BotonConfig.Tipo.SECONDARY),
                                new PanelBotonesFormulario.BotonConfig("Limpiar",
                                                PanelBotonesFormulario.BotonConfig.Tipo.DANGER));

                // Inyectamos los listeners:
                botones.setListeners(
                                new AgregarRegistro(campos, mensajeInline, owner),
                                new Buscar(campos, mensajeInline),
                                new LimpiarCampos(campos, mensajeInline));

                // Se anclan los botones hasta abajo del panel
                add(botones, BorderLayout.SOUTH);
        }
}
