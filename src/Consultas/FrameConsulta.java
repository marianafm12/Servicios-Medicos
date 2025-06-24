package Consultas;

import javax.swing.*;
import java.awt.*;
import Utilidades.ColoresUDLAP;

public class FrameConsulta extends JPanel {

    private final int idMedico;
    private final String nombreMedico;

    public FrameConsulta(int idMedico, String nombreMedico) {
        this.idMedico = idMedico;
        this.nombreMedico = nombreMedico;

        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        PanelConsultaNueva panelConsulta = new PanelConsultaNueva(idMedico, nombreMedico);
        add(panelConsulta, BorderLayout.CENTER);
    }
}
