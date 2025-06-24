package Registro;

import javax.swing.*;
import Utilidades.*;
import java.awt.*;

public class PanelRegistroPaciente extends JPanel implements PanelProvider {
    private FrameRegistro panelDatos;
    private PanelControl panelBotones;

    public PanelRegistroPaciente() {
        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        panelDatos = new FrameRegistro();
        JScrollPane scrollPane = new JScrollPane(panelDatos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        panelBotones = new PanelControl(panelDatos.obtenerCampos());
        add(panelBotones, BorderLayout.SOUTH);
    }

    public JTextField[] obtenerCampos() {
        return panelDatos.obtenerCampos();
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public String getPanelName() {
        return "formularioRegistro";
    }
}