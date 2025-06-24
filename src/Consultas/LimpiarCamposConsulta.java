package Consultas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LimpiarCamposConsulta implements ActionListener {
    private final JTextField[] campos;
    private final JTextArea areaTexto;

    public LimpiarCamposConsulta(JTextField[] campos, JTextArea areaTexto) {
        this.campos = campos;
        this.areaTexto = areaTexto;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
        areaTexto.setText("");
    }
}