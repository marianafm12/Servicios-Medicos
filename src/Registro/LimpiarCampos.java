package Registro;

import java.awt.event.*;
import javax.swing.*;

import Utilidades.MensajeErrorUDLAP;

public class LimpiarCampos implements ActionListener {
    private JTextField[] campos;
    private final MensajeErrorUDLAP mensajeInline;

    public LimpiarCampos(JTextField[] campos, MensajeErrorUDLAP mensajeInline) {
        this.campos = campos;
        this.mensajeInline = mensajeInline;
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
        mensajeInline.mostrarAdvertencia("Todos los campos han sido limpiados.");
    }
}
