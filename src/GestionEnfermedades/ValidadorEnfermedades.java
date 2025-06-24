package GestionEnfermedades;

import javax.swing.*;

public class ValidadorEnfermedades {
    public static boolean validar(JTextField id, JTextArea enf, JTextArea alg, JTextArea med) {
        if (id.getText().trim().isEmpty()) {
            show("El ID del paciente es obligatorio.");
            return false;
        }
        if (enf.getText().trim().isEmpty() &&
                alg.getText().trim().isEmpty() &&
                med.getText().trim().isEmpty()) {
            show("Debe llenar al menos un campo médico.");
            return false;
        }
        return true;
    }

    private static void show(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Validación", JOptionPane.WARNING_MESSAGE);
    }
}
