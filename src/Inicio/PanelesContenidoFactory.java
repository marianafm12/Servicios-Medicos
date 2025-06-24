package Inicio;

import javax.swing.*;
import java.awt.*;

public class PanelesContenidoFactory {

    // Panel de inicio o bienvenida
    public static JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Selecciona una opción del menú", SwingConstants.CENTER);
        label.setFont(new Font("Dialog", Font.ITALIC, 22));
        panel.setBackground(Color.WHITE);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    // Ejemplo: Panel para 'Consulta Nueva' (solo médicos)
    public static JPanel crearPanelRegistrarPacienteNuevo() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Formulario del Paciente");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Dialog", Font.BOLD, 28));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.EAST;

        String[] labels = {
                "ID Paciente:", "Nombre Completo del Paciente:", "Edad:", "Correo Electrónico:",
                "Síntomas:", "Medicamento(s) administrado(s):", "Diagnóstico:", "ID Expediente Médico:",
                "Fecha (dd/MM/yyyy):", "Última Consulta (dd/MM/yyyy):", "Fecha de inicio de síntomas (dd/MM/yyyy):",
                "Receta Médica:"
        };

        Component[] fields = new Component[labels.length];
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.fill = GridBagConstraints.NONE;
            JLabel l = new JLabel(labels[i], SwingConstants.RIGHT);
            l.setFont(new Font("Dialog", Font.PLAIN, 16));
            form.add(l, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            if (i == labels.length - 1) { // Receta médica es un area
                JTextArea area = new JTextArea(3, 22);
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                fields[i] = new JScrollPane(area);
                form.add(fields[i], gbc);
            } else {
                JTextField tf = new JTextField(22);
                fields[i] = tf;
                form.add(tf, gbc);
            }
        }

        panel.add(form);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton btnGuardar = new JButton("Guardar Datos");
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnGuardar);

        return panel;
    }

    public static JPanel crearPanelConsultaNueva() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Formulario para registrar nueva consulta:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField txtPaciente = new JTextField("Nombre del paciente");
        JTextArea txtDiagnostico = new JTextArea("Diagnóstico...");
        txtDiagnostico.setLineWrap(true);
        txtDiagnostico.setWrapStyleWord(true);
        JButton btnGuardar = new JButton("Guardar Consulta");
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(txtPaciente);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JScrollPane(txtDiagnostico));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnGuardar);
        return panel;
    }

    // Ejemplo: Panel para 'Editar Datos del Paciente' (solo médicos)
    public static JPanel crearPanelEditarDatos() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Nombre:"));
        panel.add(new JTextField());
        panel.add(new JLabel("Apellido:"));
        panel.add(new JTextField());
        panel.add(new JLabel("Edad:"));
        panel.add(new JTextField());
        panel.add(new JLabel("Teléfono:"));
        panel.add(new JTextField());
        panel.add(new JLabel(""));
        panel.add(new JButton("Guardar Cambios"));
        return panel;
    }

    // Ejemplo: Panel para 'Justificantes Médicos' (solo médicos)
    public static JPanel crearPanelJustificantesMedicos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel("Lista de justificantes médicos generados", SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);
        String[] cols = { "ID", "Paciente", "Fecha", "Motivo" };
        Object[][] datos = { { 1, "Juan Pérez", "2025-05-10", "Reposo" } };
        JTable table = new JTable(datos, cols);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // Ejemplo: Panel para 'Registrar Llamada de Emergencia' (solo médicos)
    public static JPanel crearPanelLlamadaEmergencia() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Paciente:"));
        panel.add(new JTextField());
        panel.add(new JLabel("Motivo:"));
        panel.add(new JTextField());
        panel.add(new JLabel(""));
        panel.add(new JButton("Registrar llamada"));
        return panel;
    }

    // Ejemplo: Panel para 'Llenar Reporte de Accidente' (solo médicos)
    public static JPanel crearPanelReporteAccidente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel("Reporte de Accidente", SwingConstants.CENTER);
        JTextArea area = new JTextArea("Describa el accidente...");
        JButton btnEnviar = new JButton("Enviar Reporte");
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(btnEnviar, BorderLayout.SOUTH);
        return panel;
    }

    // Ejemplo: Panel para 'Gestión de Citas' (solo pacientes)
    public static JPanel crearPanelGestionCitas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel("Mis Citas Programadas", SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);
        String[] cols = { "Fecha", "Hora", "Médico", "Estado" };
        Object[][] datos = { { "2025-05-15", "09:00", "Dr. Gómez", "Pendiente" } };
        JTable table = new JTable(datos, cols);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton btnAgendar = new JButton("Agendar Nueva Cita");
        panel.add(btnAgendar, BorderLayout.SOUTH);
        return panel;
    }

    // Ejemplo: Panel para 'Historial Médico' (solo pacientes)
    public static JPanel crearPanelHistorialMedico() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel("Historial Médico del Paciente", SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);
        String[] cols = { "Fecha", "Consulta", "Médico" };
        Object[][] datos = { { "2025-01-10", "Revisión General", "Dra. Ruiz" } };
        JTable table = new JTable(datos, cols);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // Ejemplo: Panel para 'Solicitar Justificante' (solo pacientes)
    public static JPanel crearPanelSolicitarJustificante() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel("Solicitar Justificante Médico", SwingConstants.CENTER);
        JTextArea area = new JTextArea("Describe el motivo de tu solicitud...");
        JButton btnSolicitar = new JButton("Solicitar");
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(btnSolicitar, BorderLayout.SOUTH);
        return panel;
    }

    // Ejemplo: Panel para 'Mis Justificantes' (solo pacientes)
    public static JPanel crearPanelMisJustificantes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel("Mis justificantes médicos", SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);
        String[] cols = { "ID", "Fecha", "Motivo", "Estado" };
        Object[][] datos = { { 1, "2025-04-30", "Reposo", "Aprobado" } };
        JTable table = new JTable(datos, cols);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // Ejemplo: Panel para 'Reportar Emergencia' (solo pacientes)
    public static JPanel crearPanelReportarEmergencia() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel("Reportar Emergencia", SwingConstants.CENTER);
        JTextArea area = new JTextArea("Describe la emergencia...");
        JButton btnReportar = new JButton("Reportar");
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(btnReportar, BorderLayout.SOUTH);
        return panel;
    }
}
