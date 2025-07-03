package GestionCitas;

import Utilidades.*;
import BaseDeDatos.ConexionSQLite;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;

public class ModificarCitaFrame extends JPanel {

    private final int idPaciente;
    private final PanelManager panelManager;

    private JTextField campoNombre;
    private JTextField campoApellidos;
    private JTextField campoID;
    private ComboBoxUDLAP<String> comboCitas;
    private ComboBoxUDLAP<String> comboServicio;
    private DatePickerUDLAP datePickerUDLAP;
    private ComboBoxUDLAP<String> comboHora;
    private ComboBoxUDLAP<String> comboMinuto;
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

        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // — TÍTULO —
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Modificar Cita Médica", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        add(lblTitulo, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;

        // — ID PACIENTE —
        gbc.gridx = 0;
        add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        campoID = new JTextField(String.valueOf(idPaciente), 20);
        campoID.setFont(fieldFont);
        campoID.setEditable(false);
        campoID.setBorder(getCampoBorde());
        add(campoID, gbc);

        // — NOMBRE Y APELLIDOS —
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        campoNombre = new JTextField(20);
        campoNombre.setFont(fieldFont);
        campoNombre.setEditable(false);
        campoNombre.setBorder(getCampoBorde());
        add(campoNombre, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Apellidos:"), gbc);
        gbc.gridx = 1;
        campoApellidos = new JTextField(20);
        campoApellidos.setFont(fieldFont);
        campoApellidos.setEditable(false);
        campoApellidos.setBorder(getCampoBorde());
        add(campoApellidos, gbc);

        // — COMBO CITAS —
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Selecciona tu cita:"), gbc);
        gbc.gridx = 1;
        comboCitas = new ComboBoxUDLAP<>("Seleccione una cita", new String[] {});
        comboCitas.setFont(fieldFont);
        comboCitas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sel = comboCitas.getSelectedItem();
                if (sel != null) {
                    int idCita = Integer.parseInt(sel.split(":")[0].trim());
                    cargarDatosCitaSeleccionada(idCita);
                }
            }
        });
        add(comboCitas, gbc);

        // — SERVICIO —
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Servicio:"), gbc);
        gbc.gridx = 1;
        comboServicio = new ComboBoxUDLAP<>(
                "Seleccione un servicio",
                new String[] { "Consulta", "Enfermería", "Examen Médico" });
        comboServicio.setFont(fieldFont);
        add(comboServicio, gbc);

        // — NUEVA FECHA — (Fines de semana bloqueados)
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Nueva Fecha:"), gbc);
        gbc.gridx = 1;
        datePickerUDLAP = new DatePickerUDLAP();
        datePickerUDLAP.setBlockWeekends(true);
        add(datePickerUDLAP, gbc);

        // — HORA —
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Hora de la Cita:"), gbc);
        gbc.gridx = 1;
        JPanel panelHora = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelHora.setBackground(ColoresUDLAP.BLANCO);
        comboHora = new ComboBoxUDLAP<>(
                "Hora",
                new String[] { "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21" });
        comboHora.setFont(fieldFont);
        comboMinuto = new ComboBoxUDLAP<>(
                "Minuto",
                new String[] { "00", "30" });
        comboMinuto.setFont(fieldFont);
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

        JButton btnModificar = botonTransparente(
                "Modificar Cita", ColoresUDLAP.VERDE, ColoresUDLAP.VERDE_HOVER);
        JButton btnCancelarCita = botonTransparente(
                "Cancelar Cita", ColoresUDLAP.ROJO, ColoresUDLAP.ROJO_HOVER);
        JButton btnVolver = botonTransparente(
                "Volver", ColoresUDLAP.NARANJA, ColoresUDLAP.NARANJA_HOVER);

        btnModificar.addActionListener(e -> modificarCita());
        btnCancelarCita.addActionListener(e -> cancelarCita());
        btnVolver.addActionListener(e -> panelManager.showPanel("panelGestionCitas"));

        panelBotones.add(btnModificar);
        panelBotones.add(btnCancelarCita);
        panelBotones.add(btnVolver);
        add(panelBotones, gbc);

        // Carga inicial de datos
        cargarDatosPersonales();
        cargarCitas();
    }

    /** Llena nombre y apellidos del paciente */
    private void cargarDatosPersonales() {
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT Nombre, ApellidoPaterno, ApellidoMaterno FROM InformacionAlumno WHERE ID = ?")) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    campoNombre.setText(rs.getString("Nombre"));
                    campoApellidos.setText(
                            rs.getString("ApellidoPaterno") + " " + rs.getString("ApellidoMaterno"));
                }
            }
        } catch (SQLException ex) {
            campoNombre.setText("Error");
            campoApellidos.setText("Error");
        }
    }

    /** Carga el combo de citas */
    private void cargarCitas() {
        comboCitas.removeAllItems();
        comboCitas.addItem("Seleccione una cita");
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT idCita, fecha || ' ' || hora || ' - ' || servicio AS desc " +
                                "FROM CitasMedicas WHERE idPaciente = ?")) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String item = rs.getInt("idCita") + ": " + rs.getString("desc");
                    comboCitas.addItem(item);
                }
            }
        } catch (SQLException ex) {
            errorLabel.setText("Error al cargar citas.");
        }
    }

    /** Cuando seleccionas una cita, llena los campos con sus datos */
    private void cargarDatosCitaSeleccionada(int idCita) {
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT servicio, fecha, hora FROM CitasMedicas WHERE idCita = ?")) {
            ps.setInt(1, idCita);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    comboServicio.setSelectedItem(rs.getString("servicio"));
                    LocalDate fecha = LocalDate.parse(rs.getString("fecha"));
                    datePickerUDLAP.setDate(fecha);
                    String[] hm = rs.getString("hora").split(":");
                    comboHora.setSelectedItem(hm[0]);
                    comboMinuto.setSelectedItem(hm[1]);
                    errorLabel.setText("");
                }
            }
        } catch (SQLException ex) {
            errorLabel.setText("Error al cargar datos de la cita.");
        }
    }

    /** Modifica la cita seleccionada, evitando duplicados de servicio */
    private void modificarCita() {
        String sel = comboCitas.getSelectedItem();
        if (sel == null) {
            errorLabel.setText("Seleccione una cita para modificar.");
            return;
        }
        int idCita = Integer.parseInt(sel.split(":")[0].trim());
        String servicio = comboServicio.getSelectedItem();
        LocalDate fechaSel = datePickerUDLAP.getDate();
        String hora = comboHora.getSelectedItem();
        String minuto = comboMinuto.getSelectedItem();

        if (fechaSel == null || !ValidacionesCita.esFechaValida(
                fechaSel.getDayOfMonth(),
                fechaSel.getMonthValue(),
                fechaSel.getYear())) {
            errorLabel.setText("Fecha inválida o no seleccionada.");
            return;
        }

        // ➡️ Validar que no haya otra cita del mismo servicio distinta a ésta
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) FROM CitasMedicas " +
                                "WHERE idPaciente = ? AND servicio = ? AND idCita <> ?")) {
            ps.setInt(1, idPaciente);
            ps.setString(2, servicio);
            ps.setInt(3, idCita);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                if (rs.getInt(1) > 0) {
                    errorLabel.setText(
                            "Ya tienes otra cita de “" + servicio + "”. Sólo una por servicio.");
                    return;
                }
            }
        } catch (SQLException ex) {
            errorLabel.setText("Error al validar duplicados.");
            return;
        }

        String nuevaFecha = fechaSel.toString();
        String nuevaHora = hora + ":" + minuto;

        // Leer cita original para notificar si cambia
        String fechaAnterior = null;
        String horaAnterior = null;
        String servicioAnterior = null;
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT fecha, hora, servicio FROM CitasMedicas WHERE idCita = ?")) {
            ps.setInt(1, idCita);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    fechaAnterior = rs.getString("fecha");
                    horaAnterior = rs.getString("hora");
                    servicioAnterior = rs.getString("servicio");
                }
            }
        } catch (SQLException ex) {
            errorLabel.setText("Error al obtener datos originales.");
            return;
        }

        // Actualizar en BD
        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE CitasMedicas SET fecha = ?, hora = ?, servicio = ? WHERE idCita = ?")) {
            ps.setString(1, nuevaFecha);
            ps.setString(2, nuevaHora);
            ps.setString(3, servicio);
            ps.setInt(4, idCita);
            ps.executeUpdate();

            errorLabel.setForeground(ColoresUDLAP.VERDE_OSCURO);
            errorLabel.setText("Cita modificada correctamente.");
            cargarCitas();

            // Notificar lista de espera si cambió
            if (!nuevaFecha.equals(fechaAnterior)
                    || !nuevaHora.equals(horaAnterior)
                    || !servicio.equals(servicioAnterior)) {
                NotificadorListaEspera.notificarDisponibilidad(
                        fechaAnterior, horaAnterior, servicioAnterior);
            }

        } catch (SQLException ex) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setText("Error al modificar la cita.");
        }
    }

    /** Cancela la cita seleccionada */
    private void cancelarCita() {
        String sel = comboCitas.getSelectedItem();
        if (sel == null) {
            errorLabel.setText("Seleccione una cita para cancelar.");
            return;
        }
        // … tu lógica de cancelación …
    }

    private Border getCampoBorde() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
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
}
