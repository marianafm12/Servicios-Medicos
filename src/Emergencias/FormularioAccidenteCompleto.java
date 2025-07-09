package Emergencias;

import Utilidades.*;
import BaseDeDatos.ConexionSQLite;

import java.io.File;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

/**
 * Formulario completo de reporte de accidente para estudiantes UDLAP.
 * Incluye todas las secciones I–VIII del PDF,
 * validación, persistencia y limpieza.
 */
public class FormularioAccidenteCompleto extends JPanel {
    // Componentes del formulario (I–VIII)

    private JTextField campoIDEmergencia, campoMatricula, campoNombre, campoApellidoPaterno,
            campoApellidoMaterno, campoEdad, campoPrograma, campoSemestre,
            campoCorreoUDLAP, campoTelefonoEst, campoUbicacionExacta, campoCentroAtencion,
            campoSignosVitales, campoDiagnostico, campoIncapacidad, campoLesionSecundaria,
            campoMedicoTratante, campoCedula,
            campoResponsableTraslado, campoMedioTransporte,
            campoNombreContacto, campoRelacionContacto,
            campoTelefonoContacto, campoCorreoContacto,
            campoTestigo1Nombre, campoTestigo1Telefono;

    private JTextField campoFechaRegistro, campoParamedico;

    private JTextArea areaDireccion, areaDescripcion, areaPrimerosAuxilios,
            areaMedicamentos, areaTratamiento, areaDomicilioContacto,
            areaNarrativa;

    private ComboBoxUDLAP<String> comboSexo, comboEscuela, comboDiaSemana,
            comboLugar, comboEnHorario, comboTrayecto,
            comboLesionPrincipal, comboParteCuerpo, comboTriage,
            comboConsciencia, comboLesionesAtribuibles,
            comboRiesgoMuerte, comboHospitalizacion,
            comboHospitalDestino;

    private MensajeErrorUDLAP mensajeEstado;
    // PARA FOTOS
    private JPanel panelFotos;
    private java.util.List<byte[]> fotosAccidente = new ArrayList<>();

    private DatePickerUDLAP datePickerAccidente;
    private ComboBoxUDLAP<String> comboHoraAccidente, comboMinutoAccidente;
    private DatePickerUDLAP datePickerIngreso;
    private ComboBoxUDLAP<String> comboHoraIngreso, comboMinutoIngreso;

    private DatePickerUDLAP datePickerInforme;
    private DatePickerUDLAP datePickerElaboracion;

    public FormularioAccidenteCompleto() {
        initComponentes();
        initListeners();
    }

    /** Construye y distribuye todos los componentes en el panel. */
    private void initComponentes() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);

        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setBackground(ColoresUDLAP.BLANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Font fontLabel = new Font("Arial", Font.BOLD, 14);
        Font fontField = new Font("Arial", Font.PLAIN, 14);

        int row = 0;
        // I. Datos del Estudiante
        addSeccion(contenido, gbc, row++, "I. Datos del Estudiante", fontLabel);
        campoIDEmergencia = crearCampo(contenido, gbc, row++, "ID Emergencia:", fontField);
        // — nueva fila para FechaRegistro Emergencia —
        campoFechaRegistro = crearCampo(contenido, gbc, row++, "Fecha Registro Emergencia:", fontField);
        campoFechaRegistro.setEditable(false);
        // — nueva fila para Paramédico Responsable —
        campoParamedico = crearCampo(contenido, gbc, row++, "Paramédico Responsable:", fontField);
        campoParamedico.setEditable(false);
        campoMatricula = crearCampo(contenido, gbc, row++, "ID Estudiante:", fontField);
        campoNombre = crearCampo(contenido, gbc, row++, "Nombre(s):", fontField);
        campoApellidoPaterno = crearCampo(contenido, gbc, row++, "Apellido Paterno:", fontField);
        campoApellidoMaterno = crearCampo(contenido, gbc, row++, "Apellido Materno:", fontField);
        campoEdad = crearCampo(contenido, gbc, row++, "Edad:", fontField);
        comboSexo = new ComboBoxUDLAP<>("Seleccione", new String[] {
                "Masculino", "Femenino", "No Binario", "Prefiero no decir" });
        addCombo(contenido, gbc, row++, "Sexo:", comboSexo);
        comboEscuela = new ComboBoxUDLAP<>("Seleccione", new String[] {
                "Ingeniería", "Negocios y Economía", "Ciencias Sociales", "Artes y Humanidades", "Ciencias" });
        addCombo(contenido, gbc, row++, "Escuela:", comboEscuela);
        campoPrograma = crearCampo(contenido, gbc, row++, "Programa Académico:", fontField);
        campoSemestre = crearCampo(contenido, gbc, row++, "Semestre:", fontField);
        campoCorreoUDLAP = crearCampo(contenido, gbc, row++, "Correo UDLAP:", fontField);
        campoTelefonoEst = crearCampo(contenido, gbc, row++, "Teléfono:", fontField);
        areaDireccion = new JTextArea(3, 20);
        addArea(contenido, gbc, row++, "Dirección:", areaDireccion);

        // II. Info del Accidente
        addSeccion(contenido, gbc, row++, "II. Información del Accidente", fontLabel);
        // Implementación de DatePickerUDLAP para seleccionar fecha y hora
        datePickerAccidente = new DatePickerUDLAP();
        datePickerAccidente.setBlockWeekends(false);
        comboHoraAccidente = new ComboBoxUDLAP<>("HH", new String[] {
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
                "17", "18", "19", "20", "21", "22", "23" });
        comboMinutoAccidente = new ComboBoxUDLAP<>("MM", new String[] {
                "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55" });
        JPanel panelAcc = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelAcc.setBackground(ColoresUDLAP.BLANCO);
        panelAcc.add(datePickerAccidente);
        panelAcc.add(comboHoraAccidente);
        panelAcc.add(new JLabel(":"));
        panelAcc.add(comboMinutoAccidente);
        addCustom(contenido, gbc, row++, "Fecha y Hora del Accidente:", panelAcc);

        comboDiaSemana = new ComboBoxUDLAP<>("Seleccione", new String[] {
                "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo" });
        addCombo(contenido, gbc, row++, "Día:", comboDiaSemana);
        comboLugar = new ComboBoxUDLAP<>("Seleccione", new String[] {
                "Aula", "Laboratorio", "Biblioteca", "Residencias", "Gimnasio",
                "Estacionamiento", "Administrativa", "Escaleras", "Deportivas",
                "Comedor", "Vía Pública", "Otro" });
        addCombo(contenido, gbc, row++, "Lugar:", comboLugar);
        campoUbicacionExacta = crearCampo(contenido, gbc, row++, "Ubicación Exacta:", fontField);
        comboEnHorario = new ComboBoxUDLAP<>("Seleccione", new String[] { "Si", "No" });
        addCombo(contenido, gbc, row++, "En Clase:", comboEnHorario);
        comboTrayecto = new ComboBoxUDLAP<>("Seleccione", new String[] {
                "Dentro campus", "Entre edificios", "Campus-Residencias",
                "Extra-campus", "Otro" });
        addCombo(contenido, gbc, row++, "Trayecto:", comboTrayecto);
        campoCentroAtencion = crearCampo(contenido, gbc, row++, "Centro Atención:", fontField);

        // III. Lesiones y Daños
        addSeccion(contenido, gbc, row++, "III. Lesiones y Daños", fontLabel);
        comboLesionPrincipal = new ComboBoxUDLAP<>("Seleccione", new String[] {
                "Fractura", "Esguince", "Luxación", "Quemadura", "Contusión", "Laceración",
                "Asfixia", "Intoxicación", "Conmoción", "Otros" });
        addCombo(contenido, gbc, row++, "Lesión Principal:", comboLesionPrincipal);
        campoLesionSecundaria = crearCampo(contenido, gbc, row++, "Lesión Secundaria:", fontField);
        comboParteCuerpo = new ComboBoxUDLAP<>("Seleccione", new String[] {
                "Cabeza", "Cuello", "Tronco", "Extremidad Superior Izquierda", "Extremidad Superior Derecha",
                "Extremidad Inferior Izquierda", "Extremidad Inferior Derecha", "Múltiple", "Interno", "Otro" });
        addCombo(contenido, gbc, row++, "Parte Cuerpo:", comboParteCuerpo);
        comboTriage = new ComboBoxUDLAP<>("Seleccione",
                new String[] { "Rojo", "Naranja", "Amarillo", "Verde", "Azul" });
        addCombo(contenido, gbc, row++, "Gravedad:", comboTriage);
        comboConsciencia = new ComboBoxUDLAP<>("Seleccione", new String[] { "Alerta", "Consciente", "Inconsciente" });
        addCombo(contenido, gbc, row++, "Consciencia:", comboConsciencia);
        campoSignosVitales = crearCampo(contenido, gbc, row++, "Signos Vitales:", fontField);
        areaDescripcion = new JTextArea(3, 20);
        addArea(contenido, gbc, row++, "Descripción:", areaDescripcion);
        areaPrimerosAuxilios = new JTextArea(3, 20);
        addArea(contenido, gbc, row++, "Primeros Auxilios:", areaPrimerosAuxilios);
        areaMedicamentos = new JTextArea(3, 20);
        addArea(contenido, gbc, row++, "Medicamentos:", areaMedicamentos);
        campoDiagnostico = crearCampo(contenido, gbc, row++, "Diagnóstico:", fontField);

        // IV. Evaluación Médica
        addSeccion(contenido, gbc, row++, "IV. Evaluación Médica", fontLabel);
        comboLesionesAtribuibles = new ComboBoxUDLAP<>("Seleccione", new String[] { "Si", "No" });
        addCombo(contenido, gbc, row++, "Lesiones Atribuibles:", comboLesionesAtribuibles);
        comboRiesgoMuerte = new ComboBoxUDLAP<>("Seleccione", new String[] { "Si", "No" });
        addCombo(contenido, gbc, row++, "Riesgo Muerte:", comboRiesgoMuerte);
        campoIncapacidad = crearCampo(contenido, gbc, row++, "Incapacidad (días):", fontField);
        comboHospitalizacion = new ComboBoxUDLAP<>("Seleccione", new String[] { "Si", "No" });
        addCombo(contenido, gbc, row++, "Hospitalización:", comboHospitalizacion);
        areaTratamiento = new JTextArea(3, 20);
        addArea(contenido, gbc, row++, "Tratamiento:", areaTratamiento);
        campoMedicoTratante = crearCampo(contenido, gbc, row++, "Médico Tratante:", fontField);
        campoCedula = crearCampo(contenido, gbc, row++, "Cédula:", fontField);

        // Fecha informe médico con DatePickerUDLAP
        datePickerInforme = new DatePickerUDLAP();
        datePickerInforme.setBlockWeekends(false);
        addCustom(contenido, gbc, row++, "Fecha Informe Médico:", datePickerInforme);

        // V. Traslado
        addSeccion(contenido, gbc, row++, "V. Traslado y Seguimiento", fontLabel);
        comboHospitalDestino = new ComboBoxUDLAP<>("Seleccione", new String[] { "Hospital UDLAP", "ABC", "Otro" });
        addCombo(contenido, gbc, row++, "Hospital Destino:", comboHospitalDestino);
        campoResponsableTraslado = crearCampo(contenido, gbc, row++, "Responsable:", fontField);
        campoMedioTransporte = crearCampo(contenido, gbc, row++, "Medio Transporte:", fontField);

        // Fecha de ingreso al hospital con DatePickerUDLAP
        datePickerIngreso = new DatePickerUDLAP();
        datePickerIngreso.setBlockWeekends(false);
        comboHoraIngreso = new ComboBoxUDLAP<>("HH", new String[] {
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" });
        comboMinutoIngreso = new ComboBoxUDLAP<>("MM", new String[] {
                "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55" });
        JPanel panelIng = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelIng.setBackground(ColoresUDLAP.BLANCO);
        panelIng.add(datePickerIngreso);
        panelIng.add(comboHoraIngreso);
        panelIng.add(new JLabel(":"));
        panelIng.add(comboMinutoIngreso);
        addCustom(contenido, gbc, row++, "Fecha y Hora Ingreso Hospital:", panelIng);

        // VI. Contacto Emergencia
        addSeccion(contenido, gbc, row++, "VI. Contacto de Emergencia", fontLabel);
        campoNombreContacto = crearCampo(contenido, gbc, row++, "Nombre Contacto:", fontField);
        campoRelacionContacto = crearCampo(contenido, gbc, row++, "Relación:", fontField);
        campoTelefonoContacto = crearCampo(contenido, gbc, row++, "Teléfono:", fontField);
        campoCorreoContacto = crearCampo(contenido, gbc, row++, "Correo:", fontField);
        areaDomicilioContacto = new JTextArea(3, 20);
        addArea(contenido, gbc, row++, "Domicilio:", areaDomicilioContacto);

        // VII. Testigos
        addSeccion(contenido, gbc, row++, "VII. Testigos", fontLabel);
        campoTestigo1Nombre = crearCampo(contenido, gbc, row++, "Testigo 1 Nombre:", fontField);
        campoTestigo1Telefono = crearCampo(contenido, gbc, row++, "Testigo 1 Teléfono:", fontField);

        // VIII. Declaraciones y Firmas
        addSeccion(contenido, gbc, row++, "VIII. Declaraciones y Firmas", fontLabel);
        areaNarrativa = new JTextArea(5, 20);
        addArea(contenido, gbc, row++, "Narrativa:", areaNarrativa);

        // Fecha de elaboración del informe con DatePickerUDLAP
        datePickerElaboracion = new DatePickerUDLAP();
        datePickerElaboracion.setBlockWeekends(false);
        addCustom(contenido, gbc, row++, "Fecha Elaboración Informe:", datePickerElaboracion);

        // (dentro de initComponentes, antes de añadir scroll y panelBotones)
        panelFotos = new JPanel();
        panelFotos.setLayout(new BoxLayout(panelFotos, BoxLayout.Y_AXIS));
        panelFotos.setBackground(ColoresUDLAP.BLANCO);
        panelFotos.setBorder(BorderFactory.createTitledBorder("Fotos"));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        contenido.add(panelFotos, gbc);
        gbc.gridwidth = 1;

        // Botones
        // 1) Envolvemos 'contenido' en un scroll y lo agregamos al centro:
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setViewportBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        // 2) Luego construimos y agregamos el panelInferior con botones y mensaje:
        PanelBotonesFormulario panelBotones = new PanelBotonesFormulario(
                new PanelBotonesFormulario.BotonConfig("Guardar", PanelBotonesFormulario.BotonConfig.Tipo.SECONDARY),
                new PanelBotonesFormulario.BotonConfig("Limpiar", PanelBotonesFormulario.BotonConfig.Tipo.BACK),
                new PanelBotonesFormulario.BotonConfig("Foto(s)",
                        PanelBotonesFormulario.BotonConfig.Tipo.PRIMARY));
        mensajeEstado = new MensajeErrorUDLAP();

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(ColoresUDLAP.BLANCO);
        panelInferior.add(mensajeEstado, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        panelBotones.setListeners(
                e -> guardarAccidente(),
                e -> limpiarCampos(),
                e -> abrirSelectorFotos());

    }

    private void initListeners() {

        // Al perder foco en el campo de ID de Emergencia, cargar fecha de registro y
        // paramédico
        campoIDEmergencia.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                String txt = campoIDEmergencia.getText().trim();
                if (!txt.matches("\\d+")) {
                    return;
                }
                int idEmergencia = Integer.parseInt(txt);
                Emergencia em = EmergenciaDAO.obtenerPorId(idEmergencia);
                if (em != null) {
                    // Formateador para mostrar "yyyy-MM-dd HH:mm"
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    campoFechaRegistro.setText(
                            em.getFechaRegistro().toLocalDateTime().format(fmt));
                    campoParamedico.setText(
                            em.getMedicoResponsable() != null ? em.getMedicoResponsable() : "-");
                } else {
                    campoFechaRegistro.setText("-");
                    campoParamedico.setText("-");
                }
            }
        });

        campoMatricula.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String txt = campoMatricula.getText().trim();
                if (!txt.matches("\\d+"))
                    return;
                int idAlumno = Integer.parseInt(txt);

                // Primero limpio todo
                campoNombre.setText("");
                campoApellidoPaterno.setText("");
                campoApellidoMaterno.setText("");
                campoCorreoUDLAP.setText("");
                campoEdad.setText("");
                // y las dejo editables para poder volver a limpiar correctamente
                campoNombre.setEditable(true);
                campoApellidoPaterno.setEditable(true);
                campoApellidoMaterno.setEditable(true);
                campoCorreoUDLAP.setEditable(true);
                campoEdad.setEditable(true);

                String sqlA = """
                            SELECT Nombre, ApellidoPaterno, ApellidoMaterno, Correo
                            FROM InformacionAlumno
                            WHERE ID = ?
                        """;
                String sqlR = """
                            SELECT Edad
                            FROM Registro
                            WHERE ID = ?
                        """;

                try (Connection conn = ConexionSQLite.conectar();
                        PreparedStatement psA = conn.prepareStatement(sqlA);
                        PreparedStatement psR = conn.prepareStatement(sqlR)) {

                    // datos de InformacionAlumno
                    psA.setInt(1, idAlumno);
                    try (ResultSet rs = psA.executeQuery()) {
                        if (rs.next()) {
                            campoNombre.setText(rs.getString("Nombre"));
                            campoApellidoPaterno.setText(rs.getString("ApellidoPaterno"));
                            campoApellidoMaterno.setText(rs.getString("ApellidoMaterno"));
                            campoCorreoUDLAP.setText(rs.getString("Correo"));

                            // ¡Aquí deshabilitas la edición!
                            campoNombre.setEditable(false);
                            campoApellidoPaterno.setEditable(false);
                            campoApellidoMaterno.setEditable(false);
                            campoCorreoUDLAP.setEditable(false);
                        }
                    }

                    // datos de Registro
                    psR.setInt(1, idAlumno);
                    try (ResultSet rs = psR.executeQuery()) {
                        if (rs.next()) {
                            campoEdad.setText(String.valueOf(rs.getInt("Edad")));
                            campoEdad.setEditable(false);
                        }
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            FormularioAccidenteCompleto.this,
                            "Error al cargar datos del estudiante:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    // Auxiliares UI
    private void addSeccion(JPanel panel, GridBagConstraints gbc, int row, String texto, Font font) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JLabel lbl = new JLabel(texto);
        lbl.setFont(font);
        lbl.setForeground(ColoresUDLAP.NARANJA_SOLIDO);
        panel.add(lbl, gbc);
        gbc.gridwidth = 1;
    }

    private JTextField crearCampo(JPanel p, GridBagConstraints gbc, int row, String label, Font f) {
        gbc.gridx = 0;
        gbc.gridy = row;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        JTextField t = new JTextField(20);
        t.setFont(f);
        p.add(t, gbc);
        return t;
    }

    private void addCombo(JPanel p, GridBagConstraints gbc, int row, String label, JComboBox<?> c) {
        gbc.gridx = 0;
        gbc.gridy = row;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        p.add(c, gbc);
    }

    private void addArea(JPanel p, GridBagConstraints gbc, int row, String label, JTextArea a) {
        gbc.gridx = 0;
        gbc.gridy = row;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        p.add(new JScrollPane(a), gbc);
    }

    private void addCustom(JPanel p, GridBagConstraints gbc, int row, String label, JPanel c) {
        gbc.gridx = 0;
        gbc.gridy = row;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        p.add(c, gbc);
    }

    private JPanel crearPanelFecha() {
        JComboBox<String> d = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            d.addItem(String.valueOf(i));
        }
        JComboBox<String> m = new JComboBox<>(new String[] {
                "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
        });
        JComboBox<String> a = new JComboBox<>();
        int ya = LocalDate.now().getYear();
        for (int i = ya; i <= ya + 5; i++) {
            a.addItem(String.valueOf(i));
        }
        JPanel p = new JPanel();
        p.add(d);
        p.add(m);
        p.add(a);
        return p;
    }

    private JPanel crearPanelFechaTime() {
        JPanel p = crearPanelFecha();
        JComboBox<String> h = new JComboBox<>(), min = new JComboBox<>();
        for (int i = 0; i < 24; i++)
            h.addItem(String.format("%02d", i));
        for (int i = 0; i < 60; i++)
            min.addItem(String.format("%02d", i));
        p.add(h);
        p.add(min);
        return p;
    }

    /**
     * Limpia y restaura todos los campos del formulario a su estado inicial.
     */
    private void limpiarCampos() {
        // I. Datos del Estudiante
        campoIDEmergencia.setText("");
        campoMatricula.setText("");
        campoNombre.setText("");
        campoApellidoPaterno.setText("");
        campoApellidoMaterno.setText("");
        campoEdad.setText("");
        comboSexo.setSelectedIndex(0);
        comboEscuela.setSelectedIndex(0);
        campoPrograma.setText("");
        campoSemestre.setText("");
        campoCorreoUDLAP.setText("");
        campoTelefonoEst.setText("");
        areaDireccion.setText("");

        // II. Información del Accidente
        // Fecha y hora
        datePickerAccidente.setDate(null);
        comboHoraAccidente.setSelectedIndex(0);
        comboMinutoAccidente.setSelectedIndex(0);
        comboDiaSemana.setSelectedIndex(0);
        comboLugar.setSelectedIndex(0);
        campoUbicacionExacta.setText("");
        comboEnHorario.setSelectedIndex(0);
        comboTrayecto.setSelectedIndex(0);
        campoCentroAtencion.setText("");

        // III. Lesiones y Daños
        comboLesionPrincipal.setSelectedIndex(0);
        campoLesionSecundaria.setText("");
        comboParteCuerpo.setSelectedIndex(0);
        comboTriage.setSelectedIndex(0);
        comboConsciencia.setSelectedIndex(0);
        campoSignosVitales.setText("");
        areaDescripcion.setText("");
        areaPrimerosAuxilios.setText("");
        areaMedicamentos.setText("");
        campoDiagnostico.setText("");

        // IV. Evaluación Médica
        comboLesionesAtribuibles.setSelectedIndex(0);
        comboRiesgoMuerte.setSelectedIndex(0);
        campoIncapacidad.setText("");
        comboHospitalizacion.setSelectedIndex(0);
        areaTratamiento.setText("");
        campoMedicoTratante.setText("");
        campoCedula.setText("");
        datePickerInforme.setDate(null);

        // V. Traslado y Seguimiento
        comboHospitalDestino.setSelectedIndex(0);
        campoResponsableTraslado.setText("");
        campoMedioTransporte.setText("");
        datePickerIngreso.setDate(null);
        comboHoraIngreso.setSelectedIndex(0);
        comboMinutoIngreso.setSelectedIndex(0);

        // VI. Contacto de Emergencia
        campoNombreContacto.setText("");
        campoRelacionContacto.setText("");
        campoTelefonoContacto.setText("");
        campoCorreoContacto.setText("");
        areaDomicilioContacto.setText("");

        // VII. Testigos
        campoTestigo1Nombre.setText("");
        campoTestigo1Telefono.setText("");

        // VIII. Declaraciones y Firmas
        areaNarrativa.setText("");
        datePickerElaboracion.setDate(null);

        // —————— LIMPIAR LAS FOTOS ——————
        fotosAccidente.clear();
        panelFotos.removeAll();
        panelFotos.revalidate();
        panelFotos.repaint();
    }

    /**
     * Abre el JFileChooser para agregar fotos y las muestra en panelFotos.
     */
    private void abrirSelectorFotos() {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            for (File f : fc.getSelectedFiles()) {
                try {
                    // ---- Reemplázalo por esto ----
                    byte[] img = Files.readAllBytes(f.toPath());
                    fotosAccidente.add(img);
                    // vista previa
                    javax.swing.ImageIcon ico = new javax.swing.ImageIcon(img);
                    // Usa java.awt.Image y su constante completamente cualificada
                    java.awt.Image imgScale = ico.getImage()
                            .getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
                    // Construye el JLabel con el ImageIcon plenamente cualificado
                    JLabel lbl = new JLabel(new javax.swing.ImageIcon(imgScale));
                    // Usa la fábrica de bordes de Swing y Color de AWT plenamente cualificados
                    lbl.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));
                    // al hacer clic, eliminar la foto
                    lbl.addMouseListener(new MouseAdapter() {
                        // Quita @Override si recibes error de compatibilidad
                        public void mouseClicked(java.awt.event.MouseEvent ev) {
                            fotosAccidente.remove(img);
                            panelFotos.remove(lbl);
                            panelFotos.revalidate();
                            panelFotos.repaint();
                        }
                    });
                    panelFotos.add(lbl);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            panelFotos.revalidate();
            panelFotos.repaint();
        }
    }

    /**
     * Extrae día, mes, año, hora y minuto de un panel creado con
     * crearPanelFechaTime()
     * y devuelve una cadena en formato "YYYY-MM-DD HH:mm:00".
     */
    private String formatearFechaHoraUDLAP(DatePickerUDLAP dp, ComboBoxUDLAP<String> ch, ComboBoxUDLAP<String> cm) {
        LocalDate fecha = dp.getDate();
        String hora = (String) ch.getSelectedItem();
        String minuto = (String) cm.getSelectedItem();
        if (fecha == null || hora == null || minuto == null)
            return null;
        return fecha.toString() + " " + hora + ":" + minuto + ":00";
    }

    private String formatearFechaUDLAP(DatePickerUDLAP dp) {
        LocalDate fecha = dp.getDate();
        return (fecha != null) ? fecha.toString() : null;
    }

    /**
     * Extrae los valores de todos los campos del formulario, crea un objeto
     * Accidente
     * y lo persiste en la base de datos.
     */
    private void guardarAccidente() {
        // 1) Validación externa UDLAP
        Window owner = SwingUtilities.getWindowAncestor(this);
        if (!ValidacionesAccidente.validarCampos(
                owner,
                // I. Datos del Estudiante
                campoIDEmergencia.getText().trim(),
                campoMatricula.getText().trim(),
                campoNombre.getText().trim(),
                campoApellidoPaterno.getText().trim(),
                campoApellidoMaterno.getText().trim(),
                campoEdad.getText().trim(),
                (String) comboSexo.getSelectedItem(),
                (String) comboEscuela.getSelectedItem(),
                campoPrograma.getText().trim(),
                campoSemestre.getText().trim(),
                campoCorreoUDLAP.getText().trim(),
                campoTelefonoEst.getText().trim(),

                // II. Información del Accidente
                datePickerAccidente.getDate(),
                (String) comboHoraAccidente.getSelectedItem(),
                (String) comboMinutoAccidente.getSelectedItem(),
                (String) comboDiaSemana.getSelectedItem(),
                (String) comboLugar.getSelectedItem(),
                campoUbicacionExacta.getText().trim(),
                (String) comboEnHorario.getSelectedItem(),
                (String) comboTrayecto.getSelectedItem(),
                campoCentroAtencion.getText().trim(),

                // III. Lesiones y Daños
                (String) comboLesionPrincipal.getSelectedItem(),
                campoLesionSecundaria.getText().trim(),
                (String) comboParteCuerpo.getSelectedItem(),
                (String) comboTriage.getSelectedItem(),
                (String) comboConsciencia.getSelectedItem(),
                areaDescripcion.getText().trim(),
                areaPrimerosAuxilios.getText().trim(),
                areaMedicamentos.getText().trim(),
                campoDiagnostico.getText().trim(),

                // IV. Evaluación Médica
                (String) comboLesionesAtribuibles.getSelectedItem(),
                (String) comboRiesgoMuerte.getSelectedItem(),
                campoIncapacidad.getText().trim(),
                (String) comboHospitalizacion.getSelectedItem(),
                areaTratamiento.getText().trim(),
                campoMedicoTratante.getText().trim(),
                campoCedula.getText().trim(),

                // Fechas UDLAP
                datePickerInforme.getDate(),
                datePickerIngreso.getDate(),
                (String) comboHoraIngreso.getSelectedItem(),
                (String) comboMinutoIngreso.getSelectedItem(),

                // VI. Contacto de Emergencia
                campoNombreContacto.getText().trim(),
                campoRelacionContacto.getText().trim(),
                campoTelefonoContacto.getText().trim(),
                campoCorreoContacto.getText().trim(),
                areaDomicilioContacto.getText().trim(),

                // VII. Testigos
                campoTestigo1Nombre.getText().trim(),
                campoTestigo1Telefono.getText().trim(),

                // VIII. Narrativa y elaboración
                areaNarrativa.getText().trim(),
                datePickerElaboracion.getDate(),

                // Fotos
                fotosAccidente)) {
            // Si falla la validación, ya se mostró el diálogo; salimos
            return;
        }

        // 2) Extraer valores de I. Datos del Estudiante
        int idEmergencia = Integer.parseInt(campoIDEmergencia.getText().trim());
        int matricula = Integer.parseInt(campoMatricula.getText().trim());
        String nombreEstudiante = campoNombre.getText().trim();
        String apPaterno = campoApellidoPaterno.getText().trim();
        String apMaterno = campoApellidoMaterno.getText().trim();
        int edad = Integer.parseInt(campoEdad.getText().trim());
        String sexo = comboSexo.getSelectedItem().toString();
        String escuela = comboEscuela.getSelectedItem().toString();
        String programa = campoPrograma.getText().trim();
        int semestre = Integer.parseInt(campoSemestre.getText().trim());
        String correoUDLAP = campoCorreoUDLAP.getText().trim();
        String telEstudiante = campoTelefonoEst.getText().trim();
        String direccion = areaDireccion.getText().trim();

        // 3) Extraer valores de II. Información del Accidente
        String fechaAccidente = formatearFechaHoraUDLAP(datePickerAccidente, comboHoraAccidente, comboMinutoAccidente);
        String diaSemana = comboDiaSemana.getSelectedItem().toString();
        String lugarOcurrencia = comboLugar.getSelectedItem().toString();
        String ubicacionExacta = campoUbicacionExacta.getText().trim();
        String enHorario = comboEnHorario.getSelectedItem().toString();
        String tipoTrayecto = comboTrayecto.getSelectedItem().toString();
        String centroAtencion = campoCentroAtencion.getText().trim();

        // 4) Extraer valores de III. Lesiones y Daños
        String lesionPrincipal = comboLesionPrincipal.getSelectedItem().toString();
        String lesionSecundaria = campoLesionSecundaria.getText().trim();
        String parteCuerpo = comboParteCuerpo.getSelectedItem().toString();
        String gravedadTriage = comboTriage.getSelectedItem().toString();
        String nivelConsciencia = comboConsciencia.getSelectedItem().toString();
        String signosVitales = campoSignosVitales.getText().trim();
        String descripcionDetallada = areaDescripcion.getText().trim();
        String primerosAuxilios = areaPrimerosAuxilios.getText().trim();
        String medicamentos = areaMedicamentos.getText().trim();
        String diagnosticoCIE10 = campoDiagnostico.getText().trim();

        // 5) Extraer valores de IV. Evaluación Médica
        String lesionesAtribuibles = comboLesionesAtribuibles.getSelectedItem().toString();
        String riesgoMuerte = comboRiesgoMuerte.getSelectedItem().toString();
        int incapacidadDias = Integer.parseInt(campoIncapacidad.getText().trim());
        String requiereHosp = comboHospitalizacion.getSelectedItem().toString();
        String tratamientoRec = areaTratamiento.getText().trim();
        String medicoTratante = campoMedicoTratante.getText().trim();
        String cedulaProfesional = campoCedula.getText().trim();
        String fechaInformeMedico = formatearFechaUDLAP(datePickerInforme);

        // 6) Extraer valores de V. Traslado y Seguimiento
        String hospitalDestino = comboHospitalDestino.getSelectedItem().toString();
        String responsableTraslado = campoResponsableTraslado.getText().trim();
        String medioTransporte = campoMedioTransporte.getText().trim();
        String fechaHoraIngreso = formatearFechaHoraUDLAP(datePickerIngreso, comboHoraIngreso, comboMinutoIngreso);

        // 7) Extraer valores de VI. Contacto de Emergencia
        String nombreContacto = campoNombreContacto.getText().trim();
        String relacionContacto = campoRelacionContacto.getText().trim();
        String telefonoContacto = campoTelefonoContacto.getText().trim();
        String correoContacto = campoCorreoContacto.getText().trim();
        String domicilioContacto = areaDomicilioContacto.getText().trim();

        // 8) Extraer valores de VII. Testigos
        String testigo1Nombre = campoTestigo1Nombre.getText().trim();
        String testigo1Telefono = campoTestigo1Telefono.getText().trim();

        // 9) Extraer valores de VIII. Declaraciones y Firmas
        String narrativaDetallada = areaNarrativa.getText().trim();
        String fechaElaboracion = formatearFechaUDLAP(datePickerElaboracion);
        String fechaRegistro = campoFechaRegistro.getText().trim();
        String paramedicoResponsable = campoParamedico.getText().trim();
        // 10) Crear modelo y persistir
        Accidente acc = new Accidente(
                idEmergencia,
                fechaRegistro, // ← nuevo
                paramedicoResponsable,
                matricula,
                nombreEstudiante,
                apPaterno,
                apMaterno,
                edad,
                sexo,
                escuela,
                programa,
                semestre,
                correoUDLAP,
                telEstudiante,
                direccion,
                fechaAccidente,
                diaSemana,
                lugarOcurrencia,
                ubicacionExacta,
                enHorario,
                tipoTrayecto,
                centroAtencion,
                lesionPrincipal,
                lesionSecundaria,
                parteCuerpo,
                gravedadTriage,
                nivelConsciencia,
                signosVitales,
                descripcionDetallada,
                primerosAuxilios,
                medicamentos,
                diagnosticoCIE10,
                lesionesAtribuibles,
                riesgoMuerte,
                incapacidadDias,
                requiereHosp,
                tratamientoRec,
                medicoTratante,
                cedulaProfesional,
                fechaInformeMedico,
                hospitalDestino,
                responsableTraslado,
                medioTransporte,
                fechaHoraIngreso,
                nombreContacto,
                relacionContacto,
                telefonoContacto,
                correoContacto,
                domicilioContacto,
                testigo1Nombre,
                testigo1Telefono,
                narrativaDetallada,
                fechaElaboracion,
                fotosAccidente);

        try {
            boolean ok = AccidenteDB.guardarAccidenteCompleto(acc);
            if (!ok) {
                MensajeErrorUDLAP.mostrarVentanaError(owner,
                        "Error",
                        "Error al guardar el accidente.");
            } else {
                mensajeEstado.mostrarExito("Accidente guardado con éxito.");
                limpiarCampos();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            MensajeErrorUDLAP.mostrarVentanaError(owner,
                    "Error SQL",
                    "Error al guardar: " + ex.getMessage());
        }

    }

    /**
     * Crea un botón con fondo redondeado, pintado en el color base
     * y que cambia a hover cuando pasas el ratón.
     */
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
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

}