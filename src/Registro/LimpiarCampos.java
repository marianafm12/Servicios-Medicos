package Registro;

import java.awt.event.*;
import javax.swing.*;

public class LimpiarCampos implements ActionListener {
    private JTextField[] campos;

    public LimpiarCampos(JTextField[] campos) {
        this.campos = campos;
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
        JOptionPane.showMessageDialog(null, "🧹 Todos los campos han sido limpiados.");
    }
}
