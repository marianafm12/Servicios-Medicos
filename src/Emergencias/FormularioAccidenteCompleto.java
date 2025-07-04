package Emergencias;

import Utilidades.ColoresUDLAP;
import Utilidades.ComboBoxUDLAP;
//import BaseDeDatos.ConexionSQLite;
import Registro.ValidadorPaciente;
import java.io.File;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
//import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import Emergencias.Emergencia;
import Emergencias.EmergenciaDAO;
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

    private JPanel panelFechaAccidente, panelFechaIngreso,
            panelFechaInforme, panelFechaElaboracion;

    private JButton btnGuardar, btnLimpiar;
    // PARA FOTOS
    private JButton btnAgregarFotos;
    private JPanel panelFotos;
    private java.util.List<byte[]> fotosAccidente = new ArrayList<>();

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
                //  — nueva fila para FechaRegistro Emergencia —
        campoFechaRegistro = crearCampo(contenido, gbc, row++, "Fecha Registro Emergencia:", fontField);
        campoFechaRegistro.setEditable(false);
        //  — nueva fila para Paramédico Responsable —
        campoParamedico   = crearCampo(contenido, gbc, row++, "Paramédico Responsable:", fontField);
        campoParamedico.setEditable(false);
        campoMatricula = crearCampo(contenido, gbc, row++, "ID Estudiante:", fontField);
        campoNombre = crearCampo(contenido, gbc, row++, "Nombre(s):", fontField);
        campoApellidoPaterno = crearCampo(contenido, gbc, row++, "Apellido Paterno:", fontField);
        campoApellidoMaterno = crearCampo(contenido, gbc, row++, "Apellido Materno:", fontField);
        campoEdad = crearCampo(contenido, gbc, row++, "Edad:", fontField);
        comboSexo = new ComboBoxUDLAP<>("Seleccione", new String[] {
                "Masculino", "Femenino", "No Binario", "Prefiero no decir" });
        addCombo(contenido, gbc, row++, "Sexo*:", comboSexo);
        comboEscuela = new ComboBoxUDLAP<>("Seleccione", new String[] {
                "Ingeniería", "Negocios y Economía", "Ciencias Sociales", "Humanidades",
                "Arquitectura", "Artes y Diseño", "Ciencias", "Derecho", "Otro" });
        addCombo(contenido, gbc, row++, "Escuela*:", comboEscuela);
        campoPrograma = crearCampo(contenido, gbc, row++, "Programa Académico:", fontField);
        campoSemestre = crearCampo(contenido, gbc, row++, "Semestre:", fontField);
        campoCorreoUDLAP = crearCampo(contenido, gbc, row++, "Correo UDLAP:", fontField);
        campoTelefonoEst = crearCampo(contenido, gbc, row++, "Teléfono:", fontField);
        areaDireccion = new JTextArea(3, 20);
        addArea(contenido, gbc, row++, "Dirección:", areaDireccion);

        // II. Info del Accidente
        addSeccion(contenido, gbc, row++, "II. Información del Accidente", fontLabel);
        panelFechaAccidente = crearPanelFechaTime();
        addCustom(contenido, gbc, row++, "Fecha y Hora:", panelFechaAccidente);
        comboDiaSemana = new ComboBoxUDLAP<>("Seleccione", new String[] {
                "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo" });
        addCombo(contenido, gbc, row++, "Día*:", comboDiaSemana);
        comboLugar = new ComboBoxUDLAP<>("Seleccione", new String[] {
                "Aula", "Laboratorio", "Biblioteca", "Residencias", "Gimnasio",
                "Estacionamiento", "Administrativa", "Escaleras", "Deportivas",
                "Comedor", "Vía Pública", "Otro" });
        addCombo(contenido, gbc, row++, "Lugar*:", comboLugar);
        campoUbicacionExacta = crearCampo(contenido, gbc, row++, "Ubicación Exacta:", fontField);
        comboEnHorario = new ComboBoxUDLAP<>("Seleccione", new String[] { "Si", "No" });
        addCombo(contenido, gbc, row++, "En Clase*:", comboEnHorario);
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
        addCombo(contenido, gbc, row++, "Lesión Principal*:", comboLesionPrincipal);
        campoLesionSecundaria = crearCampo(contenido, gbc, row++, "Lesión Secundaria:", fontField);
        comboParteCuerpo = new ComboBoxUDLAP<>("Seleccione", new String[] {
                "Cabeza", "Cuello", "Tronco", "Ext Sup Izq", "Ext Sup Der",
                "Ext Inf Izq", "Ext Inf Der", "Múltiple", "Interno", "Otro" });
        addCombo(contenido, gbc, row++, "Parte Cuerpo*:", comboParteCuerpo);
        comboTriage = new ComboBoxUDLAP<>("Seleccione",
                new String[] { "Rojo", "Naranja", "Amarillo", "Verde", "Azul" });
        addCombo(contenido, gbc, row++, "Triage*:", comboTriage);
        comboConsciencia = new ComboBoxUDLAP<>("Seleccione", new String[] { "Alerta", "Consciente", "Inconsciente" });
        addCombo(contenido, gbc, row++, "Consciencia*:", comboConsciencia);
        campoSignosVitales = crearCampo(contenido, gbc, row++, "Signos Vitales:", fontField);
        areaDescripcion = new JTextArea(3, 20);
        addArea(contenido, gbc, row++, "Descripción*:", areaDescripcion);
        areaPrimerosAuxilios = new JTextArea(3, 20);
        addArea(contenido, gbc, row++, "Primeros Auxilios:", areaPrimerosAuxilios);
        areaMedicamentos = new JTextArea(3, 20);
        addArea(contenido, gbc, row++, "Medicamentos:", areaMedicamentos);
        campoDiagnostico = crearCampo(contenido, gbc, row++, "Diagnóstico:", fontField);

        // IV. Evaluación Médica
        addSeccion(contenido, gbc, row++, "IV. Evaluación Médica", fontLabel);
        comboLesionesAtribuibles = new ComboBoxUDLAP<>("Seleccione", new String[] { "Si", "No" });
        addCombo(contenido, gbc, row++, "Lesiones Atribuibles*:", comboLesionesAtribuibles);
        comboRiesgoMuerte = new ComboBoxUDLAP<>("Seleccione", new String[] { "Si", "No" });
        addCombo(contenido, gbc, row++, "Riesgo Muerte*:", comboRiesgoMuerte);
        campoIncapacidad = crearCampo(contenido, gbc, row++, "Incapacidad (días)*:", fontField);
        comboHospitalizacion = new ComboBoxUDLAP<>("Seleccione", new String[] { "Si", "No" });
        addCombo(contenido, gbc, row++, "Hospitalización*:", comboHospitalizacion);
        areaTratamiento = new JTextArea(3, 20);
        addArea(contenido, gbc, row++, "Tratamiento*:", areaTratamiento);
        campoMedicoTratante = crearCampo(contenido, gbc, row++, "Médico Tratante*:", fontField);
        campoCedula = crearCampo(contenido, gbc, row++, "Cédula*:", fontField);
        panelFechaInforme = crearPanelFecha();
        addCustom(contenido, gbc, row++, "Fecha Informe*:", panelFechaInforme);

        // V. Traslado
        addSeccion(contenido, gbc, row++, "V. Traslado y Seguimiento", fontLabel);
        comboHospitalDestino = new ComboBoxUDLAP<>("Seleccione", new String[] { "Hospital UDLAP", "ABC", "Otro" });
        addCombo(contenido, gbc, row++, "Hospital Destino:", comboHospitalDestino);
        campoResponsableTraslado = crearCampo(contenido, gbc, row++, "Responsable:", fontField);
        campoMedioTransporte = crearCampo(contenido, gbc, row++, "Medio Transporte:", fontField);
        panelFechaIngreso = crearPanelFechaTime();
        addCustom(contenido, gbc, row++, "Fecha Ingreso:", panelFechaIngreso);

        // VI. Contacto Emergencia
        addSeccion(contenido, gbc, row++, "VI. Contacto de Emergencia", fontLabel);
        campoNombreContacto = crearCampo(contenido, gbc, row++, "Nombre Contacto*:", fontField);
        campoRelacionContacto = crearCampo(contenido, gbc, row++, "Relación*:", fontField);
        campoTelefonoContacto = crearCampo(contenido, gbc, row++, "Teléfono*:", fontField);
        campoCorreoContacto = crearCampo(contenido, gbc, row++, "Correo*:", fontField);
        areaDomicilioContacto = new JTextArea(3, 20);
        addArea(contenido, gbc, row++, "Domicilio:", areaDomicilioContacto);

        // VII. Testigos
        addSeccion(contenido, gbc, row++, "VII. Testigos", fontLabel);
        campoTestigo1Nombre = crearCampo(contenido, gbc, row++, "Testigo1 Nombre:", fontField);
        campoTestigo1Telefono = crearCampo(contenido, gbc, row++, "Testigo1 Teléfono:", fontField);

        // VIII. Declaraciones y Firmas
        addSeccion(contenido, gbc, row++, "VIII. Declaraciones y Firmas", fontLabel);
        areaNarrativa = new JTextArea(5, 20);
        addArea(contenido, gbc, row++, "Narrativa*:", areaNarrativa);
        panelFechaElaboracion = crearPanelFecha();
        addCustom(contenido, gbc, row++, "Fecha Elaboración*:", panelFechaElaboracion);


        // (dentro de initComponentes, antes de añadir scroll y panelBotones)
        panelFotos = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelFotos.setBackground(ColoresUDLAP.BLANCO);
        panelFotos.setBorder(BorderFactory.createTitledBorder("Fotos"));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        contenido.add(panelFotos, gbc);
        gbc.gridwidth = 1;
            

        // Botones
        btnGuardar = botonTransparente("Guardar", ColoresUDLAP.NARANJA_SOLIDO, ColoresUDLAP.NARANJA_HOVER);
        btnLimpiar = botonTransparente("Limpiar", ColoresUDLAP.GRIS_SOLIDO, ColoresUDLAP.GRIS_HOVER);
        btnAgregarFotos = botonTransparente("Agregar Fotos", ColoresUDLAP.VERDE_SOLIDO, ColoresUDLAP.VERDE_HOVER);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnAgregarFotos);

        JScrollPane scroll = new JScrollPane(contenido);
        // Quita el border exterior:
        scroll.setBorder(BorderFactory.createEmptyBorder());
        // (Opcional) Quita también cualquier border interno en el viewport:
        scroll.setViewportBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void initListeners() {
        // Limpiar formulario
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // Guardar accidente
        btnGuardar.addActionListener(e -> guardarAccidente());

        // Agregar fotos
        btnAgregarFotos.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setMultiSelectionEnabled(true);
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                for (File f : fc.getSelectedFiles()) {
                    try {
                        byte[] img = Files.readAllBytes(f.toPath());
                        fotosAccidente.add(img);
                        // vista previa
                        ImageIcon ico = new ImageIcon(img);
                        Image imgScale = ico.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        JLabel lbl = new JLabel(new ImageIcon(imgScale));
                        lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        // al hacer clic, eliminar la foto
                        lbl.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent ev) {
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
        });

        // Al perder foco en el campo de ID de Emergencia, cargar fecha de registro y paramédico
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
                        em.getFechaRegistro().toLocalDateTime().format(fmt)
                    );
                    campoParamedico.setText(
                        em.getMedicoResponsable() != null ? em.getMedicoResponsable() : "-"
                    );
                } else {
                    campoFechaRegistro.setText("-");
                    campoParamedico.setText("-");
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
        for (Component c : panelFechaAccidente.getComponents()) {
            if (c instanceof JComboBox<?>) {
                ((JComboBox<?>) c).setSelectedIndex(0);
            }
        }
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
        for (Component c : panelFechaInforme.getComponents()) {
            if (c instanceof JComboBox<?>) {
                ((JComboBox<?>) c).setSelectedIndex(0);
            }
        }

        // V. Traslado y Seguimiento
        comboHospitalDestino.setSelectedIndex(0);
        campoResponsableTraslado.setText("");
        campoMedioTransporte.setText("");
        for (Component c : panelFechaIngreso.getComponents()) {
            if (c instanceof JComboBox<?>) {
                ((JComboBox<?>) c).setSelectedIndex(0);
            }
        }

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
        for (Component c : panelFechaElaboracion.getComponents()) {
            if (c instanceof JComboBox<?>) {
                ((JComboBox<?>) c).setSelectedIndex(0);
            }
        }

        // —————— LIMPIAR LAS FOTOS ——————
        fotosAccidente.clear();
        panelFotos.removeAll();
        panelFotos.revalidate();
        panelFotos.repaint();
    }

    /**
     * Valida todos los campos obligatorios del formulario.
     */
    private boolean validarCampos() {
        // I. Datos del Estudiante
        String txt;
        // ID Emergencia
        txt = campoIDEmergencia.getText().trim();
        if (txt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar el ID de Emergencia.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoIDEmergencia.requestFocus();
            return false;
        }
        try {
            Integer.parseInt(txt);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El ID de Emergencia debe ser numérico.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoIDEmergencia.requestFocus();
            return false;
        }

        // Matrícula: rango [180000–999999]
        txt = campoMatricula.getText().trim();
        if (!ValidadorPaciente.esIDValido(txt)) {
            JOptionPane.showMessageDialog(this,
                    "El ID de estudiante debe ser un número entre 180000 y 999999.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoMatricula.requestFocus();
            return false;
        }
        // Nombre(s)
        if (campoNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del estudiante.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoNombre.requestFocus();
            return false;
        }
        // Apellidos
        if (campoApellidoPaterno.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el apellido paterno.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoApellidoPaterno.requestFocus();
            return false;
        }
        if (campoApellidoMaterno.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el apellido materno.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoApellidoMaterno.requestFocus();
            return false;
        }
        // Edad
        txt = campoEdad.getText().trim();
        if (txt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la edad.", "Validación", JOptionPane.WARNING_MESSAGE);
            campoEdad.requestFocus();
            return false;
        }
        try {
            int edad = Integer.parseInt(txt);
            if (edad < 0 || edad > 120) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número entre 0 y 120.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoEdad.requestFocus();
            return false;
        }
        // Sexo y Escuela
        if (comboSexo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar el sexo.", "Validación", JOptionPane.WARNING_MESSAGE);
            comboSexo.requestFocus();
            return false;
        }
        if (comboEscuela.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar la escuela/departamento.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            comboEscuela.requestFocus();
            return false;
        }
        // Programa y Semestre
        if (campoPrograma.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el programa académico.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoPrograma.requestFocus();
            return false;
        }
        txt = campoSemestre.getText().trim();
        if (txt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el semestre/cuatrimestre.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoSemestre.requestFocus();
            return false;
        }
        try {
            Integer.parseInt(txt);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El semestre/cuatrimestre debe ser numérico.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoSemestre.requestFocus();
            return false;
        }
        // Correo UDLAP
        txt = campoCorreoUDLAP.getText().trim();
        if (txt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el correo institucional UDLAP.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoCorreoUDLAP.requestFocus();
            return false;
        }
        if (!txt.matches("^[A-Za-z0-9._%+-]+@udlap\\.edu\\.mx$")) {
            JOptionPane.showMessageDialog(this, "El correo debe pertenecer al dominio @udlap.edu.mx.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoCorreoUDLAP.requestFocus();
            return false;
        }
        // Teléfono Estudiante
        txt = campoTelefonoEst.getText().trim();
        if (!txt.matches("^\\d{10}$")) {
            JOptionPane.showMessageDialog(this, "El teléfono del estudiante debe tener 10 dígitos.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoTelefonoEst.requestFocus();
            return false;
        }

        // II. Información del Accidente
        if (comboDiaSemana.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar el día de la semana.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            comboDiaSemana.requestFocus();
            return false;
        }
        if (comboLugar.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar el lugar de ocurrencia.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            comboLugar.requestFocus();
            return false;
        }
        if (campoUbicacionExacta.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la ubicación exacta.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoUbicacionExacta.requestFocus();
            return false;
        }
        if (comboEnHorario.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe indicar si fue en horario de clase.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            comboEnHorario.requestFocus();
            return false;
        }
        if (campoCentroAtencion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el centro de atención inicial.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoCentroAtencion.requestFocus();
            return false;
        }

        // III. Lesiones y Daños
        if (comboLesionPrincipal.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar el tipo de lesión principal.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            comboLesionPrincipal.requestFocus();
            return false;
        }
        if (comboParteCuerpo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar la parte del cuerpo afectada.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            comboParteCuerpo.requestFocus();
            return false;
        }
        if (comboTriage.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar la gravedad (triage).", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            comboTriage.requestFocus();
            return false;
        }
        if (comboConsciencia.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar el nivel de consciencia.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            comboConsciencia.requestFocus();
            return false;
        }
        if (areaDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la descripción detallada del accidente.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            areaDescripcion.requestFocus();
            return false;
        }

        // IV. Evaluación Médica
        if (comboLesionesAtribuibles.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe indicar si las lesiones son atribuibles al accidente.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            comboLesionesAtribuibles.requestFocus();
            return false;
        }
        if (comboRiesgoMuerte.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe indicar el riesgo de muerte.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            comboRiesgoMuerte.requestFocus();
            return false;
        }
        txt = campoIncapacidad.getText().trim();
        if (txt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar los días de incapacidad.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoIncapacidad.requestFocus();
            return false;
        }
        try {
            Integer.parseInt(txt);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La incapacidad debe ser un número entero.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoIncapacidad.requestFocus();
            return false;
        }
        if (comboHospitalizacion.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe indicar si requiere hospitalización.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            comboHospitalizacion.requestFocus();
            return false;
        }
        if (areaTratamiento.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe indicar el tratamiento recomendado.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            areaTratamiento.requestFocus();
            return false;
        }
        if (campoMedicoTratante.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del médico tratante.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoMedicoTratante.requestFocus();
            return false;
        }
        if (campoCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la cédula profesional.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoCedula.requestFocus();
            return false;
        }

        // V. Traslado y Seguimiento: ninguno obligatorio salvo condicional, se omite

        // VI. Contacto de Emergencia
        if (campoNombreContacto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del contacto de emergencia.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoNombreContacto.requestFocus();
            return false;
        }
        if (campoRelacionContacto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la relación con el estudiante.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoRelacionContacto.requestFocus();
            return false;
        }
        if (!campoTelefonoContacto.getText().trim().matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "El teléfono de contacto debe tener 10 dígitos.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            campoTelefonoContacto.requestFocus();
            return false;
        }

        // VIII. Declaraciones y Firmas
        if (areaNarrativa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la narrativa detallada del accidente.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            areaNarrativa.requestFocus();
            return false;
        }

        // Todos los datos son válidos
        return true;
    }

    /**
     * Extrae día, mes, año, hora y minuto de un panel creado con
     * crearPanelFechaTime()
     * y devuelve una cadena en formato "YYYY-MM-DD HH:mm:00".
     */
    @SuppressWarnings("unchecked")
    private String formatearFechaHora(JPanel p) {
        Component[] comps = p.getComponents();
        JComboBox<String> cbDia = (JComboBox<String>) comps[0];
        JComboBox<String> cbMes = (JComboBox<String>) comps[1];
        JComboBox<String> cbAnio = (JComboBox<String>) comps[2];
        JComboBox<String> cbHora = (JComboBox<String>) comps[3];
        JComboBox<String> cbMin = (JComboBox<String>) comps[4];

        int dia = Integer.parseInt(cbDia.getSelectedItem().toString());
        int mes = cbMes.getSelectedIndex() + 1; // índices 0–11
        String anio = cbAnio.getSelectedItem().toString();
        String hora = cbHora.getSelectedItem().toString();
        String minuto = cbMin.getSelectedItem().toString();

        return String.format(
                "%s-%02d-%02d %s:%s:00",
                anio, mes, dia, hora, minuto);
    }

    /**
     * Extrae día, mes y año de un panel creado con crearPanelFecha()
     * y devuelve una cadena en formato "YYYY-MM-DD".
     */
    @SuppressWarnings("unchecked")
    private String formatearFecha(JPanel p) {
        Component[] comps = p.getComponents();
        JComboBox<String> cbDia = (JComboBox<String>) comps[0];
        JComboBox<String> cbMes = (JComboBox<String>) comps[1];
        JComboBox<String> cbAnio = (JComboBox<String>) comps[2];

        int dia = Integer.parseInt(cbDia.getSelectedItem().toString());
        int mes = cbMes.getSelectedIndex() + 1; // índices 0–11
        String anio = cbAnio.getSelectedItem().toString();

        return String.format(
                "%s-%02d-%02d",
                anio, mes, dia);
    }

    /**
     * Extrae los valores de todos los campos del formulario, crea un objeto
     * Accidente
     * y lo persiste en la base de datos.
     */
    private void guardarAccidente() {
        // 1) Validar
        if (!validarCampos()) {
            return;
        }

        if (fotosAccidente.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes agregar al menos una foto.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
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
        String fechaAccidente = formatearFechaHora(panelFechaAccidente);
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
        String fechaInformeMedico = formatearFecha(panelFechaInforme);

        // 6) Extraer valores de V. Traslado y Seguimiento
        String hospitalDestino = comboHospitalDestino.getSelectedItem().toString();
        String responsableTraslado = campoResponsableTraslado.getText().trim();
        String medioTransporte = campoMedioTransporte.getText().trim();
        String fechaHoraIngreso = formatearFechaHora(panelFechaIngreso);

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
        String fechaElaboracion = formatearFecha(panelFechaElaboracion);
        String fechaRegistroEmergencia   = campoFechaRegistro.getText().trim();
        String paramedicoResponsable = campoParamedico.getText().trim();
        // 10) Crear modelo y persistir
        Accidente acc = new Accidente(
                idEmergencia,
                fechaRegistroEmergencia,        // ← nuevo
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

        boolean ok = AccidenteDB.guardarAccidenteCompleto(acc);
        JOptionPane.showMessageDialog(
                this,
                ok ? "Accidente guardado con éxito." : "Error al guardar el accidente.",
                "Resultado",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);

        if (ok) {
            limpiarCampos();
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