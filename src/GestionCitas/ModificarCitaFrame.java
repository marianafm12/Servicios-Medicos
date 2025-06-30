package GestionCitas;

import Utilidades.ColoresUDLAP;
import Utilidades.PanelManager;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import BaseDeDatos.ConexionSQLite;

public class ModificarCitaFrame extends JPanel {

    private final int idPaciente;
    private final PanelManager panelManager;

    private JTextField campoNombre;
    private JTextField campoApellidos;
    private JTextField campoID;
    private JComboBox<String> comboCitas;
    private JComboBox<String> comboServicio;
    private JComboBox<Integer> comboDia;
    private JComboBox<String> comboMes;
    private JComboBox<Integer> comboAño;
    private JComboBox<String> comboHora;
    private JComboBox<String> comboMinuto;
    private JLabel errorLabel;

    public ModificarCitaFrame(int idPaciente, PanelManager panelManager) {
        this.idPaciente = idPaciente;
        this.panelManager = panelManager;

        setLayout(new GridBagLayout());
        setBackground(ColoresUDLAP.BLANCO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Modificar Cita Médica", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // ID
        gbc.gridx = 0;
        add(new JLabel("ID:"), gbc);

        gbc.gridx = 1;
        campoID = new JTextField(String.valueOf(idPaciente), 20);
        campoID.setFont(fieldFont);
        campoID.setEditable(false);
        campoID.setBorder(getCampoBorde());
        add(campoID, gbc);

        // Nombre
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        campoNombre = new JTextField("No editable", 20);
        campoNombre.setFont(fieldFont);
        campoNombre.setEditable(false);
        campoNombre.setForeground(Color.GRAY);
        campoNombre.setBorder(getCampoBorde());
        add(campoNombre, gbc);

        // Apellidos
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Apellidos:"), gbc);

        gbc.gridx = 1;
        campoApellidos = new JTextField("No editable", 20);
        campoApellidos.setFont(fieldFont);
        campoApellidos.setEditable(false);
        campoApellidos.setForeground(Color.GRAY);
        campoApellidos.setBorder(getCampoBorde());
        add(campoApellidos, gbc);

        // Botón buscar citas
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton btnBuscar = new JButton("Buscar Citas");
        btnBuscar.addActionListener(e -> cargarCitas());
        add(btnBuscar, gbc);
        gbc.gridwidth = 1;

        // Combo de citas
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Selecciona tu cita:"), gbc);
        gbc.gridx = 1;
        comboCitas = new JComboBox<>();
        comboCitas.setFont(fieldFont);
        comboCitas.setBackground(Color.WHITE);
        add(comboCitas, gbc);

        // Servicio
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Servicio:"), gbc);

        gbc.gridx = 1;
        comboServicio = new JComboBox<>(new String[] { "Consulta", "Enfermería", "Examen Médico" });
        comboServicio.setFont(fieldFont);
        comboServicio.setBackground(Color.WHITE);
        add(comboServicio, gbc);

        // Fecha
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Nueva Fecha:"), gbc);

        gbc.gridx = 1;
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelFecha.setBackground(ColoresUDLAP.BLANCO);

        comboDia = new JComboBox<>(crearRango(1, 31));
        comboDia.setFont(fieldFont);
        comboMes = new JComboBox<>(new String[] { "Ene", "Feb", "Mar", "Abr", "May", "Jun",
                "Jul", "Ago", "Sep", "Oct", "Nov", "Dic" });
        comboMes.setFont(fieldFont);
        comboAño = new JComboBox<>(crearRango(LocalDate.now().getYear(), 2030));
        comboAño.setFont(fieldFont);

        panelFecha.add(comboDia);
        panelFecha.add(comboMes);
        panelFecha.add(comboAño);
        add(panelFecha, gbc);

        // Hora
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Hora de la Cita:"), gbc);

        gbc.gridx = 1;
        JPanel panelHora = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelHora.setBackground(ColoresUDLAP.BLANCO);

        comboHora = new JComboBox<>(new String[] { "08", "09", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21" });
        comboHora.setFont(fieldFont);
        comboMinuto = new JComboBox<>(new String[] { "00", "30" });
        comboMinuto.setFont(fieldFont);

        panelHora.add(comboHora);
        panelHora.add(new JLabel(":"));
        panelHora.add(comboMinuto);
        add(panelHora, gbc);

        // Error label
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        errorLabel.setForeground(Color.RED);
        add(errorLabel, gbc);

        // Botones
        gbc.gridy++;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(ColoresUDLAP.BLANCO);

        JButton btnModificar = botonTransparente("Modificar Cita", ColoresUDLAP.VERDE, ColoresUDLAP.VERDE_HOVER);
        JButton btnCancelarCita = botonTransparente("Cancelar Cita", ColoresUDLAP.ROJO, ColoresUDLAP.ROJO_HOVER);
        JButton btnVolver = botonTransparente("Volver", ColoresUDLAP.NARANJA, ColoresUDLAP.NARANJA_HOVER);

        btnModificar.addActionListener(e -> modificarCita());

        btnCancelarCita.addActionListener(e -> {
            String seleccion = (String) comboCitas.getSelectedItem();
            if (seleccion == null) {
                errorLabel.setText("Seleccione una cita para cancelar.");
                return;
            }

            Object[] opciones = { "Sí", "No" };
            int confirm = JOptionPane.showOptionDialog(
                    this,
                    "¿Estás seguro de cancelar esta cita?",
                    "Confirmar cancelación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0] // opción predeterminada
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int idCita = Integer.parseInt(seleccion.split(":")[0].trim());
                String fechaLiberada = null;
                String horaLiberada = null;
                String servicioLiberado = null;

                try (Connection conn = ConexionSQLite.conectar();
                        PreparedStatement ps = conn
                                .prepareStatement("SELECT fecha, hora, servicio FROM CitasMedicas WHERE idCita = ?")) {
                    ps.setInt(1, idCita);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            fechaLiberada = rs.getString("fecha");
                            horaLiberada = rs.getString("hora");
                            servicioLiberado = rs.getString("servicio");

                            if (horaLiberada.length() == 8) {
                                horaLiberada = horaLiberada.substring(0, 5);
                            } else if (!horaLiberada.contains(":")) {
                                horaLiberada += ":00";
                            }
                        }
                    }
                } catch (SQLException ex) {
                    errorLabel.setText("Error al obtener cita antes de cancelar.");
                    return;
                }

                // 🔧 Normalizar hora
                if (horaLiberada.length() == 8) {
                    horaLiberada = horaLiberada.substring(0, 5); // de 10:30:00 a 10:30
                } else if (!horaLiberada.contains(":")) {
                    horaLiberada += ":00";
                }

                try {
                    NotificadorListaEspera.notificarDisponibilidad(fechaLiberada, horaLiberada, servicioLiberado);

                    try (Connection conn = ConexionSQLite.conectar();
                            PreparedStatement ps = conn.prepareStatement("DELETE FROM CitasMedicas WHERE idCita=?")) {
                        ps.setInt(1, idCita);
                        ps.executeUpdate();
                    }

                    errorLabel.setForeground(new Color(0, 128, 0));
                    errorLabel.setText("Cita cancelada.");
                    cargarCitas();

                } catch (SQLException ex) {
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setText("Error al cancelar cita.");
                }

            }
        }); // ← ✅ cierre correcto del listener

        btnVolver.addActionListener(e -> panelManager.showPanel("panelGestionCitas"));

        panelBotones.add(btnModificar);
        panelBotones.add(btnCancelarCita);
        panelBotones.add(btnVolver);

        add(panelBotones, gbc);

        // Inicial
        cargarDatosPersonales();
        cargarCitas();
    }

    private void cargarDatosPersonales() {
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT Nombre, ApellidoPaterno, ApellidoMaterno FROM InformacionAlumno WHERE ID = ?")) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    campoNombre.setText(rs.getString("Nombre"));
                    campoNombre.setForeground(Color.BLACK);
                    campoApellidos.setText(rs.getString("ApellidoPaterno") + " " + rs.getString("ApellidoMaterno"));
                    campoApellidos.setForeground(Color.BLACK);
                }
            }
        } catch (SQLException ex) {
            campoNombre.setText("Error");
            campoApellidos.setText("Error");
        }
    }

    private void cargarCitas() {
        comboCitas.removeAllItems();
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT idCita, fecha || ' ' || hora || ' - ' || servicio AS desc FROM CitasMedicas WHERE idPaciente = ?")) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    comboCitas.addItem(rs.getInt("idCita") + ": " + rs.getString("desc"));
                }
                if (!found)
                    errorLabel.setText("No hay citas para este paciente.");
                else
                    errorLabel.setText("");
            }
        } catch (SQLException ex) {
            errorLabel.setText("Error al cargar citas.");
        }
    }

    private void modificarCita() {
        String seleccion = (String) comboCitas.getSelectedItem();
        if (seleccion == null) {
            errorLabel.setText("Seleccione una cita para modificar.");
            return;
        }

        int idCita = Integer.parseInt(seleccion.split(":")[0].trim());
        String servicio = (String) comboServicio.getSelectedItem();
        int dia = (Integer) comboDia.getSelectedItem();
        int mes = comboMes.getSelectedIndex() + 1;
        int año = (Integer) comboAño.getSelectedItem();
        String hora = (String) comboHora.getSelectedItem();
        String minuto = (String) comboMinuto.getSelectedItem();

        // ✅ Validar fecha válida y futura
        if (!ValidacionesCita.esFechaValida(dia, mes, año)) {
            errorLabel.setText("Fecha inválida.");
            return;
        }

        String nuevaFecha = String.format("%04d-%02d-%02d", año, mes, dia);
        String nuevaHora = hora + ":" + minuto;

        // 📦 Obtener cita anterior (sin cambio)
        String fechaAnterior = null, horaAnterior = null, servicioAnterior = null;
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn
                        .prepareStatement("SELECT fecha, hora, servicio FROM CitasMedicas WHERE idCita=?")) {
            ps.setInt(1, idCita);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    fechaAnterior = rs.getString("fecha");
                    horaAnterior = rs.getString("hora");
                    servicioAnterior = rs.getString("servicio");
                }
                if (!horaAnterior.contains(":"))
                    horaAnterior += ":00";
            }
        } catch (SQLException ex) {
            errorLabel.setText("Error al obtener cita original.");
            return;
        }

        try (Connection conn = ConexionSQLite.conectar()) {

            // ✅ Validar conflicto de horario con otras citas (excepto esta misma)
            if (ValidacionesCita.existeConflictoConOtraCita(idCita, nuevaFecha, nuevaHora, servicio)) {
                errorLabel.setText("Ya existe otra cita en ese horario.");
                return;
            }

            // 🔄 Actualizar cita
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE CitasMedicas SET fecha=?, hora=?, servicio=? WHERE idCita=?")) {
                ps.setString(1, nuevaFecha);
                ps.setString(2, nuevaHora);
                ps.setString(3, servicio);
                ps.setInt(4, idCita);
                ps.executeUpdate();
                errorLabel.setForeground(new Color(0, 128, 0));
                errorLabel.setText("Cita modificada correctamente.");
                cargarCitas();
            }

            // 🔔 Notificar si se liberó la anterior
            if (!nuevaFecha.equals(fechaAnterior) || !nuevaHora.equals(horaAnterior)
                    || !servicio.equals(servicioAnterior)) {
                NotificadorListaEspera.notificarDisponibilidad(fechaAnterior, horaAnterior, servicioAnterior);
            }

        } catch (SQLException ex) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error al modificar la cita.");
        }
    }

    private Border getCampoBorde() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_OSCURO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private Integer[] crearRango(int desde, int hasta) {
        Integer[] arr = new Integer[hasta - desde + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = desde + i;
        }
        return arr;
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
}