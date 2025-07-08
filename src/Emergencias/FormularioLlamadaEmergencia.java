
package Emergencias;

import Utilidades.ColoresUDLAP;
import Utilidades.ComboBoxUDLAP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.Border;
import BaseDeDatos.ConexionSQLite;

import java.sql.*;

public class FormularioLlamadaEmergencia extends JPanel {

    private JTextField campoNombrePaciente;
    private JTextField campoIDPaciente;
    private JTextField campoUbicacion;
    private JTextField campoTelefonoContacto;
    private ComboBoxUDLAP<String> comboTipoEmergencia;
    private JRadioButton rbRojo, rbNaranja, rbAmarillo, rbVerde, rbAzul;
    private ButtonGroup grupoGravedad;
    private JTextArea areaDescripcion;
    private JLabel errorLabel;
    private boolean esMedico;
    private int idUsuario;

    public FormularioLlamadaEmergencia(boolean esMedico, int idUsuario) {
        this.esMedico = esMedico;
        this.idUsuario = idUsuario;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Formulario de Emergencia", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(ColoresUDLAP.VERDE_SOLIDO);
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblNombre = new JLabel("Nombre del Paciente:");
        lblNombre.setFont(labelFont);
        add(lblNombre, gbc);

        gbc.gridx = 1;
        campoNombrePaciente = new JTextField(20);
        campoNombrePaciente.setFont(fieldFont);
        campoNombrePaciente.setEditable(false);
        campoNombrePaciente.setBorder(getCampoBorde());
        campoNombrePaciente.setForeground(Color.GRAY);
        add(campoNombrePaciente, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblID = new JLabel("ID del Paciente:");
        lblID.setFont(labelFont);
        add(lblID, gbc);

        gbc.gridx = 1;
        campoIDPaciente = new JTextField(10);
        campoIDPaciente.setFont(fieldFont);
        campoIDPaciente.setBorder(getCampoBorde());
        add(campoIDPaciente, gbc);

        campoIDPaciente.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String textoID = campoIDPaciente.getText().trim();
                if (!textoID.matches("\\d+")) {
                    campoNombrePaciente.setText("ID inválido");
                    return;
                }

                int id = Integer.parseInt(textoID);
                String sql = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno FROM InformacionAlumno WHERE ID = ?";

                try (Connection conn = BaseDeDatos.ConexionSQLite.conectar();
                        PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            campoNombrePaciente.setText(
                                    rs.getString("Nombre") + " " +
                                            rs.getString("ApellidoPaterno") + " " +
                                            rs.getString("ApellidoMaterno"));
                        } else {
                            campoNombrePaciente.setText("No encontrado");
                        }
                    }
                } catch (SQLException ex) {
                    campoNombrePaciente.setText("Error BD");
                    ex.printStackTrace();
                }
            }
        });

        gbc.gridy++;
        gbc.gridx = 0;

        JLabel lblResp = new JLabel("Médico Responsable:");
        lblResp.setFont(labelFont);
        add(lblResp, gbc);

        gbc.gridx = 1;
        // justo antes de crear el JLabel
        String nombreMedico = obtenerNombreMedico(idUsuario);
        JLabel lblMedicoActual = new JLabel(nombreMedico);
        lblMedicoActual.setFont(fieldFont);
        add(lblMedicoActual, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblUbi = new JLabel("Ubicación:");
        lblUbi.setFont(labelFont);
        add(lblUbi, gbc);

        gbc.gridx = 1;
        campoUbicacion = new JTextField(20);
        campoUbicacion.setFont(fieldFont);
        campoUbicacion.setBorder(getCampoBorde());
        add(campoUbicacion, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblTipo = new JLabel("Tipo de Emergencia:");
        lblTipo.setFont(labelFont);
        add(lblTipo, gbc);

        gbc.gridx = 1;
        comboTipoEmergencia = new ComboBoxUDLAP<>("Seleccione", new String[] {
                "Caída/Golpe", "Corte", "Quemadura", "Atragantamiento",
                "Intoxicación Alimentaria", "Accidente Eléctrico",
                "Accidente Deportivo", "Accidente Automovilístico",
                "Otra"
        });
        comboTipoEmergencia.setFont(fieldFont);
        add(comboTipoEmergencia, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblGrav = new JLabel("Gravedad:");
        lblGrav.setFont(labelFont);
        add(lblGrav, gbc);

        gbc.gridx = 1;
        JPanel panelGravedad = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelGravedad.setBackground(ColoresUDLAP.BLANCO);
        rbRojo = new JRadioButton("Rojo");
        rbNaranja = new JRadioButton("Naranja");
        rbAmarillo = new JRadioButton("Amarillo");
        rbVerde = new JRadioButton("Verde");
        rbAzul = new JRadioButton("Azul");

        grupoGravedad = new ButtonGroup();
        grupoGravedad.add(rbRojo);
        grupoGravedad.add(rbNaranja);
        grupoGravedad.add(rbAmarillo);
        grupoGravedad.add(rbVerde);
        grupoGravedad.add(rbAzul);

        panelGravedad.add(rbRojo);
        panelGravedad.add(rbNaranja);
        panelGravedad.add(rbAmarillo);
        panelGravedad.add(rbVerde);
        panelGravedad.add(rbAzul);
        add(panelGravedad, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblTel = new JLabel("Teléfono de Contacto:");
        lblTel.setFont(labelFont);
        add(lblTel, gbc);

        gbc.gridx = 1;
        campoTelefonoContacto = new JTextField(15);
        campoTelefonoContacto.setFont(fieldFont);
        campoTelefonoContacto.setBorder(getCampoBorde());
        add(campoTelefonoContacto, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setFont(labelFont);
        add(lblDesc, gbc);

        gbc.gridx = 1;
        areaDescripcion = new JTextArea(5, 20);
        areaDescripcion.setBorder(getCampoBorde());
        add(new JScrollPane(areaDescripcion), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setFont(fieldFont);
        errorLabel.setForeground(Color.RED);
        add(errorLabel, gbc);

        gbc.gridy++;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(ColoresUDLAP.BLANCO);

        JButton btnRegistrar = botonTransparente("Registrar Emergencia",
                ColoresUDLAP.VERDE_SOLIDO, ColoresUDLAP.VERDE_HOVER);
        panelBotones.add(btnRegistrar);
        add(panelBotones, gbc);

        btnRegistrar.addActionListener(e -> registrarEmergencia());

    }

    private JButton botonTransparente(String texto, Color base, Color hover) {
        JButton button = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private Border getCampoBorde() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_HOVER),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void registrarEmergencia() {
        // 1) Validar ubicación
        String ubicacion = campoUbicacion.getText().trim();
        if (ubicacion.isEmpty()) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("La ubicación es obligatoria.");
            return;
        }

        // 2) Validar tipo de emergencia (ComboBoxUDLAP arranca en índice 0 con
        // "Seleccione")
        if (comboTipoEmergencia.getSelectedIndex() == 0) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Debe seleccionar el tipo de emergencia.");
            return;
        }
        String tipo = (String) comboTipoEmergencia.getSelectedItem();

        // 3) Validar gravedad (radio buttons)
        if (grupoGravedad.getSelection() == null) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Debe seleccionar la gravedad.");
            return;
        }
        String gravedad = rbRojo.isSelected() ? "Rojo"
                : rbNaranja.isSelected() ? "Naranja"
                        : rbAmarillo.isSelected() ? "Amarillo"
                                : rbVerde.isSelected() ? "Verde"
                                        : "Azul";

        try {
            // 4) Validar y extraer ID de paciente (opcional)
            int idPaciente = 0;
            String inputId = campoIDPaciente.getText().trim();
            if (!inputId.isEmpty()) {
                if (!inputId.matches("\\d+")) {
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setText("El ID del paciente debe ser numérico.");
                    return;
                }
                idPaciente = Integer.parseInt(inputId);
                if (!pacienteExiste(idPaciente)) {
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setText("El ID del paciente no existe.");
                    return;
                }
            }

            // 5) Fecha de incidente
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            // 6) Descripción (opcional)
            String descripcion = areaDescripcion.getText().trim();
            if (descripcion.isEmpty()) {
                descripcion = null;
            }

            // 7) Teléfono contacto (opcional, 10–15 dígitos)
            String telefono = campoTelefonoContacto.getText().trim();
            if (!telefono.isEmpty()) {
                if (!telefono.matches("\\d{10,15}")) {
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setText("El teléfono debe tener entre 10 y 15 dígitos.");
                    return;
                }
            } else {
                telefono = null;
            }

            // 8) Responsable = usuario logueado
            int idResponsable = idUsuario;

            // 9) Guardar en BD
            boolean exito = EmergenciaDB.guardarEmergencia(
                    idPaciente > 0 ? Integer.valueOf(idPaciente) : null,
                    ubicacion,
                    tipo,
                    gravedad,
                    descripcion,
                    timestamp,
                    telefono,
                    idResponsable);

            if (exito) {
                errorLabel.setForeground(new Color(0, 128, 0));
                errorLabel.setText("Emergencia registrada correctamente.");

                // 10) Limpiar formulario
                campoUbicacion.setText("");
                campoIDPaciente.setText("");
                areaDescripcion.setText("");
                campoTelefonoContacto.setText("");
                grupoGravedad.clearSelection();
                comboTipoEmergencia.setSelectedIndex(0);
            } else {
                errorLabel.setForeground(Color.RED);
                errorLabel.setText("No se pudo registrar la emergencia.");
            }

        } catch (Exception e) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error al registrar emergencia: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean pacienteExiste(int idPaciente) {
        String sql = "SELECT 1 FROM InformacionAlumno WHERE ID = ?";
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private String obtenerNombreMedico(int idMedico) {
        String sql = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno "
                + "FROM InformacionMedico WHERE ID = ?";
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMedico);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Nombre") + " "
                            + rs.getString("ApellidoPaterno") + " "
                            + rs.getString("ApellidoMaterno");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "Desconocido";
    }
}