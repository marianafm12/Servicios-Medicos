package Inicio;

import javax.swing.*;
import java.awt.*;

public class RecuperarContrasenaFrame extends JFrame {
    private JTextField emailField;
    private JButton enviarBtn;

    public RecuperarContrasenaFrame() {
        setTitle("Recuperar Contraseña");
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 2));

        JLabel emailLabel = new JLabel("Correo Electrónico:");
        emailField = new JTextField();
        enviarBtn = new JButton("Enviar Enlace de Recuperación");
        // Enviar con enter
        getRootPane().setDefaultButton(enviarBtn);

        enviarBtn.addActionListener(e -> {
            String email = emailField.getText();
            if (!email.isEmpty() && email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Se ha enviado un enlace de recuperación a: " + email, "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un correo válido.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        add(emailLabel);
        add(emailField);
        add(new JLabel());
        add(enviarBtn);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RecuperarContrasenaFrame::new);
    }
}