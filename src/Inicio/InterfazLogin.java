package Inicio;

import Utilidades.*;
import BaseDeDatos.ConexionSQLite;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class InterfazLogin extends JFrame {
    private final JTextField txtID = new JTextField(15);
    private final JPasswordField txtPass = new JPasswordField(15);

    public InterfazLogin() {
        super("Servicios Médicos UDLAP - Login");
        setUndecorated(true);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Barra superior con botones personalizados
        BarraVentanaUDLAP barra = new BarraVentanaUDLAP(this, false);
        add(barra, BorderLayout.NORTH);

        // Panel central
        JPanel main = new JPanel();
        main.setBackground(ColoresUDLAP.BLANCO);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        add(main, BorderLayout.CENTER);

        main.add(Box.createVerticalGlue());

        // Título
        JLabel lblTitulo = new JLabel(
                "<html>"
                        + "<span style='font-size:24pt; font-weight:bold; color:#006400;'>Servicios Médicos</span> "
                        + "<span style='font-size:24pt; font-weight:bold; color:#FF6600;'>UDLAP</span>"
                        + "</html>",
                SwingConstants.CENTER);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(lblTitulo);
        main.add(Box.createVerticalStrut(20));

        // Subtítulo
        JLabel lblLogin = new JLabel("Log In", SwingConstants.CENTER);
        lblLogin.setFont(lblLogin.getFont().deriveFont(Font.PLAIN, 18f));
        lblLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(lblLogin);
        main.add(Box.createVerticalStrut(20));

        // Formulario
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(ColoresUDLAP.BLANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        form.add(txtID, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        form.add(txtPass, gbc);

        main.add(form);
        main.add(Box.createVerticalStrut(20));

        // Botones
        JPanel botones = new JPanel();
        botones.setBackground(ColoresUDLAP.BLANCO);
        botones.setLayout(new BoxLayout(botones, BoxLayout.Y_AXIS));

        JButton btnIniciar = new JButton("Iniciar Sesión");
        btnIniciar.setFont(btnIniciar.getFont().deriveFont(Font.BOLD, 14f));
        btnIniciar.setBackground(ColoresUDLAP.NARANJA_SOLIDO);
        btnIniciar.setForeground(ColoresUDLAP.BLANCO);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIniciar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        botones.add(btnIniciar);
        botones.add(Box.createVerticalStrut(10));

        JButton btnRecuperar = new JButton("Recuperar contraseña");
        btnRecuperar.setFont(btnRecuperar.getFont().deriveFont(Font.BOLD, 14f));
        btnRecuperar.setBackground(ColoresUDLAP.VERDE_SOLIDO);
        btnRecuperar.setForeground(ColoresUDLAP.BLANCO);
        btnRecuperar.setFocusPainted(false);
        btnRecuperar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRecuperar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        botones.add(btnRecuperar);

        main.add(botones);
        main.add(Box.createVerticalGlue());

        // Acción de login
        btnIniciar.addActionListener(e -> {
            String idText = txtID.getText().trim();
            String password = new String(txtPass.getPassword());
            if (idText.isEmpty() || password.isEmpty()) {
                MensajeErrorUDLAP.mostrarVentanaError(this,
                        "Error", "Por favor, ingrese sus credenciales.");
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                MensajeErrorUDLAP.mostrarVentanaError(this,
                        "Error", "ID inválido.");
                return;
            }
            try (Connection con = ConexionSQLite.conectar()) {
                // Médicos
                String sqlMed = "SELECT Nombre, ApellidoPaterno FROM InformacionMedico WHERE ID = ? AND Contraseña = ?";
                try (PreparedStatement ps = con.prepareStatement(sqlMed)) {
                    ps.setInt(1, id);
                    ps.setString(2, password);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String nombreCompleto = rs.getString("Nombre")
                                    + " " + rs.getString("ApellidoPaterno");
                            SesionUsuario.iniciarSesionMedico(nombreCompleto);
                            new InterfazMedica(true, id).setVisible(true);
                            dispose();
                            return;
                        }
                    }
                }
                // Pacientes
                String sqlPac = "SELECT ID FROM InformacionAlumno WHERE ID = ? AND Contraseña = ?";
                try (PreparedStatement ps = con.prepareStatement(sqlPac)) {
                    ps.setInt(1, id);
                    ps.setString(2, password);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            SesionUsuario.iniciarSesion(id);
                            new InterfazMedica(false, id).setVisible(true);
                            dispose();
                            return;
                        }
                    }
                }
                // Credenciales incorrectas
                MensajeErrorUDLAP.mostrarVentanaError(this,
                        "Error", "ID o contraseña incorrectos.");
            } catch (SQLException ex) {
                MensajeErrorUDLAP.mostrarVentanaError(this,
                        "Error de Conexión", "Error en la base de datos:\n" + ex.getMessage());
            }
        });

        btnRecuperar.addActionListener(e -> new RecuperarContrasenaFrame().setVisible(true));
        getRootPane().setDefaultButton(btnIniciar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazLogin login = new InterfazLogin();
            login.setVisible(true);
        });
    }
}
