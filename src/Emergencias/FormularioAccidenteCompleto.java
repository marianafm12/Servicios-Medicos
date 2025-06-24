package Emergencias;

import Utilidades.ColoresUDLAP;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class FormularioAccidenteCompleto extends JPanel {

    private JTextField campoIDEmergencia, campoIDPaciente, campoNombrePaciente,
            campoCorreoPaciente, campoTelefonoPaciente,
            campoPresionArterial, campoRitmoCardiaco, campoRitmoRespiratorio,
            campoIDContacto, campoNombreContacto, campoCorreoContacto, campoTelefonoContacto;
    private JComboBox<String> comboGenero, comboDia, comboMes, comboAnio, comboHora, comboMinuto, comboEstado;
    private JTextArea areaDescripcion, areaObservaciones;
    private JRadioButton rbAlerta, rbConsciente, rbInconsciente;
    private ButtonGroup grupoConsciencia;
    private JLabel mensajeLabel;

    public FormularioAccidenteCompleto() {
        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setBackground(ColoresUDLAP.BLANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontField = new Font("Arial", Font.PLAIN, 14);

        int row = 0;
        campoIDEmergencia = crearCampo(contenido, gbc, row++, "ID Emergencia:", fontField);
        campoIDPaciente = crearCampo(contenido, gbc, row++, "ID Paciente:", fontField);

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

        campoNombrePaciente = crearCampo(contenido, gbc, row++, "Nombre Paciente:", fontField);
        campoNombrePaciente.setEditable(false);

        campoCorreoPaciente = crearCampo(contenido, gbc, row++, "Correo Paciente:", fontField);
        campoTelefonoPaciente = crearCampo(contenido, gbc, row++, "Teléfono Paciente:", fontField);

        comboDia = new JComboBox<>();
        comboMes = new JComboBox<>(new String[] {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" });
        comboAnio = new JComboBox<>();
        comboHora = new JComboBox<>();
        comboMinuto = new JComboBox<>();
        for (int i = 1; i <= 31; i++)
            comboDia.addItem(String.valueOf(i));
        for (int y = LocalDate.now().getYear(); y <= 2030; y++)
            comboAnio.addItem(String.valueOf(y));
        for (int h = 0; h < 24; h++)
            comboHora.addItem(String.format("%02d", h));
        for (int m = 0; m < 60; m++)
            comboMinuto.addItem(String.format("%02d", m));

        gbc.gridx = 0;
        gbc.gridy = row;
        contenido.add(new JLabel("Fecha del Accidente:"), gbc);
        gbc.gridx = 1;
        JPanel panelFecha = new JPanel();
        panelFecha.add(comboDia);
        panelFecha.add(comboMes);
        panelFecha.add(comboAnio);
        panelFecha.add(comboHora);
        panelFecha.add(comboMinuto);
        contenido.add(panelFecha, gbc);
        row++;

        campoPresionArterial = crearCampo(contenido, gbc, row++, "Presión Arterial:", fontField);
        campoRitmoCardiaco = crearCampo(contenido, gbc, row++, "Ritmo Cardíaco:", fontField);
        campoRitmoRespiratorio = crearCampo(contenido, gbc, row++, "Ritmo Respiratorio:", fontField);

        gbc.gridx = 0;
        gbc.gridy = row;
        contenido.add(new JLabel("Consciencia:"), gbc);
        gbc.gridx = 1;
        rbAlerta = new JRadioButton("Alerta");
        rbConsciente = new JRadioButton("Consciente");
        rbInconsciente = new JRadioButton("Inconsciente");
        grupoConsciencia = new ButtonGroup();
        grupoConsciencia.add(rbAlerta);
        grupoConsciencia.add(rbConsciente);
        grupoConsciencia.add(rbInconsciente);
        JPanel panelCons = new JPanel();
        panelCons.add(rbAlerta);
        panelCons.add(rbConsciente);
        panelCons.add(rbInconsciente);
        contenido.add(panelCons, gbc);
        row++;

        comboGenero = new JComboBox<>(new String[] { "Masculino", "Femenino", "Otro" });
        gbc.gridx = 0;
        gbc.gridy = row;
        contenido.add(new JLabel("Género:"), gbc);
        gbc.gridx = 1;
        contenido.add(comboGenero, gbc);
        row++;

        areaDescripcion = new JTextArea(3, 20);
        gbc.gridy = row;
        gbc.gridx = 0;
        contenido.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        contenido.add(new JScrollPane(areaDescripcion), gbc);
        row++;

        areaObservaciones = new JTextArea(3, 20);
        gbc.gridy = row;
        gbc.gridx = 0;
        contenido.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        contenido.add(new JScrollPane(areaObservaciones), gbc);
        row++;

        campoIDContacto = crearCampo(contenido, gbc, row++, "ID Contacto:", fontField);

        campoIDContacto.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String textoID = campoIDContacto.getText().trim(); // ← Correcto

                if (!textoID.matches("\\d+")) {
                    campoNombreContacto.setText("ID inválido");

                    return;
                }

                int id = Integer.parseInt(textoID);
                String sql = "SELECT Nombre, ApellidoPaterno, ApellidoMaterno FROM InformacionAlumno WHERE ID = ?";

                try (Connection conn = BaseDeDatos.ConexionSQLite.conectar();
                        PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            campoNombreContacto.setText(
                                    rs.getString("Nombre") + " " +
                                            rs.getString("ApellidoPaterno") + " " +
                                            rs.getString("ApellidoMaterno"));

                        } else {
                            campoNombreContacto.setText("No encontrado");

                        }
                    }
                } catch (SQLException ex) {
                    campoNombreContacto.setText("Error BD");
                    ex.printStackTrace();
                }
            }
        });

        campoNombreContacto = crearCampo(contenido, gbc, row++, "Nombre Contacto:", fontField);

        campoNombreContacto.setEditable(true); //

        campoCorreoContacto = crearCampo(contenido, gbc, row++, "Correo Contacto:", fontField);
        campoTelefonoContacto = crearCampo(contenido, gbc, row++, "Teléfono Contacto:", fontField);

        comboEstado = new JComboBox<>(new String[] { "Pendiente", "Completo", "Transferido" });
        gbc.gridx = 0;
        gbc.gridy = row;
        contenido.add(new JLabel("Estado de Emergencia:"), gbc);
        gbc.gridx = 1;
        contenido.add(comboEstado, gbc);
        row++;

        mensajeLabel = new JLabel("", SwingConstants.CENTER);
        mensajeLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        contenido.add(mensajeLabel, gbc);

        JButton btnRegistrar = new JButton("Registrar Accidente");
        btnRegistrar.addActionListener(e -> validarFormulario());
        gbc.gridy = row;
        contenido.add(btnRegistrar, gbc);

        JScrollPane scrollPane = new JScrollPane(contenido);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JTextField crearCampo(JPanel panel, GridBagConstraints gbc, int row, String label, Font font) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        JTextField field = new JTextField(20);
        field.setFont(font);
        panel.add(field, gbc);
        return field;
    }

    private void validarFormulario() {
        try {
            // Validación de campos
            String error = ValidadorAccidente.validarCampos(
                    campoIDEmergencia.getText(),
                    campoIDPaciente.getText(),
                    campoNombrePaciente.getText(),
                    campoCorreoPaciente.getText(),
                    campoTelefonoPaciente.getText(),
                    campoPresionArterial.getText(),
                    campoRitmoCardiaco.getText(),
                    campoRitmoRespiratorio.getText(),
                    grupoConsciencia,
                    areaDescripcion.getText(),
                    campoCorreoContacto.getText(),
                    areaObservaciones.getText(),
                    Integer.parseInt((String) comboDia.getSelectedItem()),
                    comboMes.getSelectedIndex() + 1,
                    Integer.parseInt((String) comboAnio.getSelectedItem()),
                    campoIDContacto.getText(),
                    campoNombreContacto.getText(),
                    campoTelefonoContacto.getText());

            if (error != null) {
                mostrarError(error);
                return;
            }

            if (ValidadorAccidente.idEmergenciaExiste(campoIDEmergencia.getText())) {
                mostrarError("Ya se encuentra registrado ese ID de emergencia, utilice otro.");
                return;
            }

            guardarEnBaseDeDatos();

        } catch (Exception ex) {
            mostrarError("Error inesperado: " + ex.getMessage());
        }
    }

    private void mostrarError(String msg) {
        mensajeLabel.setForeground(Color.RED);
        mensajeLabel.setText(msg);
    }

    private void guardarEnBaseDeDatos() {
        try {

            int idEmergencia = Integer.parseInt(campoIDEmergencia.getText().trim());

            int dia = Integer.parseInt((String) comboDia.getSelectedItem());
            int mes = comboMes.getSelectedIndex() + 1;
            int anio = Integer.parseInt((String) comboAnio.getSelectedItem());
            int hora = Integer.parseInt((String) comboHora.getSelectedItem());
            int minuto = Integer.parseInt((String) comboMinuto.getSelectedItem());

            String fecha = String.format("%04d-%02d-%02d %02d:%02d:00", anio, mes, dia, hora, minuto);
            String genero = (String) comboGenero.getSelectedItem();
            String presion = campoPresionArterial.getText().trim();
            int ritmoCardiaco = Integer.parseInt(campoRitmoCardiaco.getText().trim());
            int ritmoRespiratorio = Integer.parseInt(campoRitmoRespiratorio.getText().trim());

            String consciencia = rbAlerta.isSelected() ? "Alerta"
                    : rbConsciente.isSelected() ? "Consciente"
                            : "Inconsciente";

            String descripcion = areaDescripcion.getText().trim();
            String observaciones = areaObservaciones.getText().trim();

            String idCont = campoIDContacto.getText().trim();
            Integer idContacto = (!idCont.isEmpty() && idCont.matches("\\d+")) ? Integer.parseInt(idCont) : null;

            String nombreContacto = campoNombreContacto.getText().trim();
            String apellidosContacto = nombreContacto.contains(" ") ? nombreContacto.split(" ", 2)[1] : null;
            String correoContacto = campoCorreoContacto.getText().trim();
            String telefonoContacto = campoTelefonoContacto.getText().trim();
            String estado = (String) comboEstado.getSelectedItem();

            Accidente accidente = new Accidente(
                    idEmergencia, fecha, genero, presion, ritmoCardiaco, ritmoRespiratorio,
                    consciencia, descripcion, observaciones, idContacto,
                    nombreContacto, apellidosContacto, correoContacto,
                    telefonoContacto, estado);

            boolean exito = AccidenteDB.guardarAccidenteCompleto(accidente);

            // 3. Retroalimentación visual
            if (exito) {
                mensajeLabel.setForeground(new Color(0, 128, 0));
                mensajeLabel.setText("Accidente registrado correctamente.");
                limpiarCampos();
            } else {

                mensajeLabel.setForeground(Color.RED);
                mensajeLabel.setText("No se pudo registrar el accidente.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            mensajeLabel.setForeground(Color.RED);
            mensajeLabel.setText("Error inesperado: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        campoIDEmergencia.setText("");
        campoIDPaciente.setText("");
        campoNombrePaciente.setText("");
        campoCorreoPaciente.setText("");
        campoTelefonoPaciente.setText("");
        campoPresionArterial.setText("");
        campoRitmoCardiaco.setText("");
        campoRitmoRespiratorio.setText("");
        campoIDContacto.setText("");
        campoNombreContacto.setText("");
        campoCorreoContacto.setText("");
        campoTelefonoContacto.setText("");
        areaDescripcion.setText("");
        areaObservaciones.setText("");

        comboGenero.setSelectedIndex(0);
        comboDia.setSelectedIndex(0);
        comboMes.setSelectedIndex(0);
        comboAnio.setSelectedIndex(0);
        comboHora.setSelectedIndex(0);
        comboMinuto.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);

        grupoConsciencia.clearSelection();
    }

}
