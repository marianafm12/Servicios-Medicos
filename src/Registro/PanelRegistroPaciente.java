// src/Registro/PanelRegistroPaciente.java
package Registro;

import javax.swing.*;
import java.awt.*;
import Utilidades.*;

/**
 * Panel que combina el formulario de registro y su panel de control.
 */
public class PanelRegistroPaciente extends JPanel {

    /**
     * @param owner Ventana propietaria (normalmente el JFrame que contiene este
     *              panel).
     */
    public PanelRegistroPaciente(Window owner) {
        super(new GridBagLayout());
        setOpaque(true);
        setBackground(ColoresUDLAP.BLANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;

        // 1) Creamos el formulario
        FrameRegistro form = new FrameRegistro();

        // 2) Obtenemos los campos y el componente de mensaje inline
        JTextField[] campos = form.obtenerCampos();
        MensajeErrorUDLAP mensaje = form.getMensajeGeneral();

        // 3) Creamos el panel de control, inyectando las dependencias
        PanelControl control = new PanelControl(campos, mensaje, owner);

        // 4) Añadimos ambos al GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.9;
        add(form, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.1;
        add(control, gbc);

    }

    /**
     * Método main opcional para pruebas unitarias de este panel de forma
     * independiente
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Registro de Pacientes");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.getContentPane().add(new PanelRegistroPaciente(ventana));
            ventana.pack();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}
