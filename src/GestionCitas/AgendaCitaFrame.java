package GestionCitas;

import Utilidades.*;
import BaseDeDatos.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class AgendaCitaFrame extends JPanel {

    private final int idPaciente;
    private final PanelManager panelManager;

    private JTextField campoNombre;
    private JTextField campoApellidos;
    private JTextField campoID;
    private ComboBoxUDLAP<String> comboServicio;
    private DatePickerUDLAP datePickerUDLAP;
    private ComboBoxUDLAP<String> comboHora;
    private ComboBoxUDLAP<String> comboMinuto;
    private JLabel errorLabel;

    public AgendaCitaFrame(int idPaciente, PanelManager panelManager) {
        this.idPaciente = idPaciente;
        this.panelManager = panelManager;

        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // — TÍTULO —
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Agendar Cita Médica", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // — ID PACIENTE —
        gbc.gridx = 0;
        JLabel lblID = new JLabel("ID del Paciente:");
        lblID.setFont(labelFont);
        add(lblID, gbc);

        gbc.gridx = 1;
        campoID = new JTextField(String.valueOf(idPaciente), 20);
        campoID.setFont(fieldFont);
        campoID.setEditable(false);
        campoID.setBorder(getCampoBorde());
        add(campoID, gbc);

        // — NOMBRE —
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(labelFont);
        add(lblNombre, gbc);

        gbc.gridx = 1;
        campoNombre = new JTextField(20);
        campoNombre.setFont(fieldFont);
        campoNombre.setEditable(false);
        campoNombre.setBorder(getCampoBorde());
        add(campoNombre, gbc);

        // — APELLIDOS —
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setFont(labelFont);
        add(lblApellidos, gbc);

        gbc.gridx = 1;
        campoApellidos = new JTextField(20);
        campoApellidos.setFont(fieldFont);
        campoApellidos.setEditable(false);
        campoApellidos.setBorder(getCampoBorde());
        add(campoApellidos, gbc);

        // carga datos desde BD
        cargarDatosPersonales(idPaciente);

        // — SERVICIO —
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblServicio = new JLabel("Servicio:");
        lblServicio.setFont(labelFont);
        add(lblServicio, gbc);

        gbc.gridx = 1;
        comboServicio = new ComboBoxUDLAP<>(new String[] { "Consulta", "Enfermería", "Examen Médico" });
        add(comboServicio, gbc);

        // — FECHA (DatePickerUDLAP) —
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblFecha = new JLabel("Fecha de la Cita:");
        lblFecha.setFont(labelFont);
        add(lblFecha, gbc);

        gbc.gridx = 1;
        datePickerUDLAP = new DatePickerUDLAP();
        add(datePickerUDLAP, gbc);

        // — HORA —
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblHora = new JLabel("Hora de la Cita:");
        lblHora.setFont(labelFont);
        add(lblHora, gbc);

        gbc.gridx = 1;
        JPanel panelHora = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelHora.setBackground(ColoresUDLAP.BLANCO);

        comboHora = new ComboBoxUDLAP<>(new String[] {
                "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21"
        });

        comboMinuto = new ComboBoxUDLAP<>(new String[] { "00", "30" });

        panelHora.add(comboHora);
        panelHora.add(new JLabel(":"));
        panelHora.add(comboMinuto);
        add(panelHora, gbc);

        // — MENSAJE DE ERROR —
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        errorLabel.setForeground(Color.RED);
        add(errorLabel, gbc);

        // — BOTONES —
        gbc.gridy++;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(ColoresUDLAP.BLANCO);

        JButton btnConfirmar = botonTransparente("Confirmar",
                ColoresUDLAP.VERDE, ColoresUDLAP.VERDE_HOVER);
        JButton btnCancelar = botonTransparente("Volver",
                ColoresUDLAP.NARANJA, ColoresUDLAP.NARANJA_HOVER);

        btnConfirmar.addActionListener(e -> validarYConfirmarCita());
        btnCancelar.addActionListener(e -> panelManager.showPanel("panelGestionCitas"));

        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);
        add(panelBotones, gbc);
    }

    private void validarYConfirmarCita() {
        LocalDate fechaSeleccionada = datePickerUDLAP.getDate();
        if (fechaSeleccionada == null
                || !ValidacionesCita.esFechaValida(
                        fechaSeleccionada.getDayOfMonth(),
                        fechaSeleccionada.getMonthValue(),
                        fechaSeleccionada.getYear())) {
            errorLabel.setText("Fecha inválida (debe ser futura y válida)");
            errorLabel.setForeground(Color.RED);
            return;
        }
        String fecha = fechaSeleccionada.toString(); // yyyy-MM-dd
        String servicio = (String) comboServicio.getSelectedItem();
        String hora = (String) comboHora.getSelectedItem();
        String minuto = (String) comboMinuto.getSelectedItem();
        String horaFinal = hora + ":" + minuto;

        try (Connection conn = ConexionSQLite.conectar()) {
            // cita ocupada
            if (ValidacionesCita.estaCitaOcupada(fecha, horaFinal, servicio)) {
                int opcion = JOptionPane.showOptionDialog(
                        this,
                        "La cita ya está ocupada. ¿Deseas unirte a la lista de espera?",
                        "Horario ocupado",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[] { "Sí", "No" },
                        "Sí");
                if (opcion == JOptionPane.YES_OPTION) {
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            try {
                                ListaEsperaDAO.registrarEnEspera(
                                        String.valueOf(idPaciente),
                                        fecha, horaFinal, servicio);
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();
                    errorLabel.setForeground(Color.ORANGE);
                    errorLabel.setText("Registrado en lista de espera.");
                }
                return;
            }

            // ya tiene cita para ese servicio
            if (ValidacionesCita.pacienteYaTieneCitaParaServicio(
                    idPaciente, servicio)) {
                errorLabel.setForeground(Color.RED);
                errorLabel.setText("Ya tienes una cita para este servicio.");
                return;
            }

            // INSERT en BD
            String sql = "INSERT INTO CitasMedicas"
                    + "(idPaciente,fecha,hora,servicio) VALUES(?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idPaciente);
                ps.setString(2, fecha);
                ps.setString(3, horaFinal);
                ps.setString(4, servicio);
                ps.executeUpdate();
                errorLabel.setForeground(ColoresUDLAP.VERDE_OSCURO);
                errorLabel.setText("Cita agendada exitosamente.");
            }

        } catch (SQLException ex) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error al registrar cita.");
            ex.printStackTrace();
        }
    }

    private void cargarDatosPersonales(int id) {
        String sql = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno "
                + "FROM InformacionAlumno WHERE ID = ?";
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    campoNombre.setText(
                            rs.getString("Nombre"));
                    campoApellidos.setText(
                            rs.getString("ApellidoPaterno")
                                    + " " +
                                    rs.getString("ApellidoMaterno"));
                } else {
                    campoNombre.setText("Desconocido");
                    campoApellidos.setText("Desconocido");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private JButton botonTransparente(String texto, Color base, Color hover) {
        JButton button = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
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
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
}
