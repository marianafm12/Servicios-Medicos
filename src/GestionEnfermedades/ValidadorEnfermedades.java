package GestionEnfermedades;

import javax.swing.*;

import Utilidades.MensajeErrorUDLAP;

public class ValidadorEnfermedades {
    public static boolean validar(JTextField id, JTextArea enf, JTextArea alg, JTextArea med,
            MensajeErrorUDLAP mensajeInline) {
        if (id.getText().trim().isEmpty()) {
            mensajeInline.mostrarAdvertencia("El ID del paciente es obligatorio.");
            return false;
        }
        if (enf.getText().trim().isEmpty() &&
                alg.getText().trim().isEmpty() &&
                med.getText().trim().isEmpty()) {
            mensajeInline.mostrarAdvertencia("Debe llenar al menos un campo médico.");
            return false;
        }
        return true;
    }
}
