package Registro;

import javax.swing.*;
import java.awt.*;
import Utilidades.*;

public class FrameRegistro extends JPanel {
    private final JTextField[] campos;
    private final MensajeErrorUDLAP mensajeGeneral;
    private final String[] etiquetas = {
            "ID:", "Nombre:", "Apellido Paterno:", "Apellido Materno:", "Correo:",
            "Edad:", "Altura (cm):", "Peso (kg):",
            "Enfermedades Preexistentes:", "Medicación:", "Alergias:"
    };

    public FrameRegistro() {
        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);
        campos = new JTextField[etiquetas.length];

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuración de fuente
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titulo = new JLabel("Registro de Pacientes", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(ColoresUDLAP.VERDE_SOLIDO);
        add(titulo, gbc);

        gbc.gridwidth = 1;
        for (int i = 0; i < etiquetas.length; i++) {

            gbc.gridx = 0;
            gbc.gridy = i + 1; // +1 por el título
            gbc.weightx = 0.3;

            JLabel label = new JLabel(etiquetas[i]);
            label.setFont(labelFont);
            label.setForeground(ColoresUDLAP.NEGRO);
            add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;

            campos[i] = new JTextField(20);
            campos[i].setFont(fieldFont);
            campos[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColoresUDLAP.GRIS_HOVER),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            add(campos[i], gbc);
        }

        GridBagConstraints gbcMsg = new GridBagConstraints();
        gbcMsg.gridx = 0;
        gbcMsg.gridy = etiquetas.length + 1; // fila siguiente a los JTextFields
        gbcMsg.gridwidth = 2;
        gbcMsg.fill = GridBagConstraints.HORIZONTAL;
        gbcMsg.weightx = 1.0; // ocupa ambas columnas
        gbcMsg.weighty = 0.0; // no quiere expandirse verticalmente

        mensajeGeneral = new MensajeErrorUDLAP();
        mensajeGeneral.setHorizontalAlignment(SwingConstants.CENTER);
        add(mensajeGeneral, gbcMsg);

        // 2) (Opcional) si quieres empujar todo hacia arriba,
        // añade un glue después de la fila de mensaje:
        GridBagConstraints gbcGlue = new GridBagConstraints();
        gbcGlue.gridx = 0;
        gbcGlue.gridy = etiquetas.length + 2;
        gbcGlue.gridwidth = 2;
        gbcGlue.weighty = 1.0; // toma todo el espacio sobrante
        gbcGlue.fill = GridBagConstraints.BOTH;
        add(Box.createVerticalGlue(), gbcGlue);
    }

    /* Permite al controlador actualizar el mensaje inline */
    public MensajeErrorUDLAP getMensajeGeneral() {
        return mensajeGeneral;
    }

    public JTextField[] obtenerCampos() {
        return campos;
    }
}