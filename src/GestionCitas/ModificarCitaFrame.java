package GestionCitas;

import Utilidades.*;
import Utilidades.PanelBotonesFormulario.BotonConfig;
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
    private MensajeErrorUDLAP mensajeInline;

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
        lblTitulo.setForeground(ColoresUDLAP.VERDE_SOLIDO);
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

        // — MENSAJE DE ERROR UDLAP —
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mensajeInline = new MensajeErrorUDLAP();
        add(mensajeInline, gbc);

        // — BOTONES UDLAP —
        gbc.gridy++;
        PanelBotonesFormulario panelBotones = new PanelBotonesFormulario(
                new BotonConfig("Modificar Cita", BotonConfig.Tipo.PRIMARY),
                new BotonConfig("Cancelar Cita", BotonConfig.Tipo.DANGER),
                new BotonConfig("Volver", BotonConfig.Tipo.BACK));
        panelBotones.setListeners(
                e -> modificarCita(),
                e -> cancelarCita(),
                e -> panelManager.showPanel("panelGestionCitas"));
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
        try {
            mensajeInline.limpiar();
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
                    if (comboCitas.getItemCount() <= 1) {
                        mensajeInline.mostrarInformacion("No tienes citas programadas.");
                    } else {
                        comboCitas.setSelectedIndex(0); // Selecciona el primer elemento
                    }
                }
            }
        } catch (SQLException ex) {
            mensajeInline.mostrarError("Error al cargar citas.");
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
                    mensajeInline.limpiar();
                }
            }
        } catch (SQLException ex) {
            mensajeInline.mostrarError("Error al cargar datos de la cita.");
        }
    }

    /** Modifica la cita seleccionada, evitando duplicados de servicio */
    private void modificarCita() {
        String sel = comboCitas.getSelectedItem();
        if (sel == null) {
            mensajeInline.mostrarAdvertencia("Seleccione una cita para modificar.");
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
            mensajeInline.mostrarAdvertencia("Fecha inválida o no seleccionada.");
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
                    mensajeInline.mostrarError(
                            "Ya tienes otra cita de “" + servicio + "”. Sólo se puede una cita por servicio.");
                    return;
                }
            }
        } catch (SQLException ex) {
            mensajeInline.mostrarError("Error al validar duplicados.");
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
            mensajeInline.mostrarError("Error al obtener datos originales.");
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

            mensajeInline.mostrarExito("Cita modificada correctamente.");
            cargarCitas();

            // Notificar lista de espera si cambió
            if (!nuevaFecha.equals(fechaAnterior)
                    || !nuevaHora.equals(horaAnterior)
                    || !servicio.equals(servicioAnterior)) {
                NotificadorListaEspera.notificarDisponibilidad(
                        fechaAnterior, horaAnterior, servicioAnterior);
            }

        } catch (SQLException ex) {
            mensajeInline.mostrarError("Error al modificar la cita.");
        }
    }

    /** Cancela la cita seleccionada */
    private void cancelarCita() {
        String sel = comboCitas.getSelectedItem();
        if (sel == null) {
            mensajeInline.mostrarAdvertencia("Seleccione una cita para cancelar.");
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
            int idCita = Integer.parseInt(sel.split(":")[0].trim());
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
                mensajeInline.mostrarAdvertencia("Error al obtener cita antes de cancelar.");
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

                mensajeInline.mostrarExito("Cita cancelada.");
                cargarCitas();

            } catch (SQLException ex) {
                mensajeInline.mostrarError("Error al cancelar cita.");
            }

        }
    }

    private Border getCampoBorde() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColoresUDLAP.GRIS_HOVER),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public void reiniciar() {
        // 1) Limpia mensaje
        mensajeInline.limpiar();
        // 2) Reset de controles
        comboServicio.setSelectedIndex(0);
        datePickerUDLAP.setDate(null);
        comboHora.setSelectedIndex(0);
        comboMinuto.setSelectedIndex(0);
        // 3) Recarga datos
        cargarDatosPersonales();
        cargarCitas();
    }

}