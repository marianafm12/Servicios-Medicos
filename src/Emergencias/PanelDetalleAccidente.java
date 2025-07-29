package Emergencias;

import Utilidades.ColoresUDLAP;
import Utilidades.PanelManager;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;
//import java.time.format.DateTimeFormatter;

/**
 * Muestra en detalle todos los datos de un Accidente.
 */
public class PanelDetalleAccidente extends JPanel {
    private final PanelManager panelManager;
    private final Accidente accidente;

    public PanelDetalleAccidente(PanelManager panelManager, Accidente accidente) {
        this.panelManager = panelManager;
        this.accidente = accidente;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBackground(ColoresUDLAP.BLANCO);

        // Título
        JLabel titulo = new JLabel("Detalle del Accidente", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(ColoresUDLAP.NARANJA_SOLIDO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        // Datos en GridLayout
        JPanel centro = new JPanel(new GridLayout(0, 1, 0, 0));
        centro.setBackground(ColoresUDLAP.BLANCO);
        centro.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        /*
         * DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
         * DateTimeFormatter dFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
         */
        centro.add(makeRow("ID Accidente: ", String.valueOf(accidente.getIdEmergencia())));
        centro.add(makeRow("Fecha Registro Emergencia: ", accidente.getFechaRegistro()));
        centro.add(makeRow("Paramédico Responsable: ", accidente.getParamedicoResponsable()));

        centro.add(makeRow("ID Estudiante: ", String.valueOf(accidente.getMatricula())));
        centro.add(makeRow("Nombre Estudiante: ",
                accidente.getNombreEstudiante() + " " +
                        accidente.getApellidoPaterno() + " " +
                        accidente.getApellidoMaterno()));
        centro.add(makeRow("Edad: ", String.valueOf(accidente.getEdad())));
        centro.add(makeRow("Sexo: ", accidente.getSexo()));
        centro.add(makeRow("Escuela: ", accidente.getEscuela()));
        centro.add(makeRow("Programa: ", accidente.getProgramaAcademico()));
        centro.add(makeRow("Semestre: ", String.valueOf(accidente.getSemestre())));
        centro.add(makeRow("Correo UDLAP: ", accidente.getCorreoUDLAP()));
        centro.add(makeRow("Teléfono Estudiante: ", accidente.getTelefonoEstudiante()));

        centro.add(makeRow("Dirección: ", accidente.getDireccion()));
        centro.add(makeRow("Fecha Accidente: ",
                accidente.getFechaAccidente()));
        centro.add(makeRow("Día Semana: ", accidente.getDiaSemana()));
        centro.add(makeRow("Lugar Ocurrencia: ", accidente.getLugarOcurrencia()));
        centro.add(makeRow("Ubicación Exacta: ", accidente.getUbicacionExacta()));
        centro.add(makeRow("En Horario Clase: ", accidente.getEnHorarioClase()));

        centro.add(makeRow("Lesión Principal: ", accidente.getLesionPrincipal()));
        centro.add(makeRow("Lesión Secundaria: ", accidente.getLesionSecundaria()));
        centro.add(makeRow("Parte Cuerpo: ", accidente.getParteCuerpo()));
        centro.add(makeRow("Gravedad: ", accidente.getGravedadTriage()));
        centro.add(makeRow("Nivel Consciencia: ", accidente.getNivelConsciencia()));
        centro.add(makeRow("Signos Vitales: ", accidente.getSignosVitales()));
        centro.add(makeRow("Descripción Detallada: ", accidente.getDescripcionDetallada()));
        centro.add(makeRow("Primeros Auxilios: ", accidente.getPrimerosAuxilios()));
        centro.add(makeRow("Medicamentos: ", accidente.getMedicamentos()));
        centro.add(makeRow("Diagnóstico: ", accidente.getDiagnosticoCIE10()));

        centro.add(makeRow("Lesiones Atribuibles: ", accidente.getLesionesAtribuibles()));
        centro.add(makeRow("Riesgo Muerte: ", accidente.getRiesgoMuerte()));
        centro.add(makeRow("Incapacidad Días: ", String.valueOf(accidente.getIncapacidadDias())));
        centro.add(makeRow("Requiere Hospitalización: ", accidente.getRequiereHospitalizacion()));
        centro.add(makeRow("Tratamiento Recomendado: ", accidente.getTratamientoRecomendado()));
        centro.add(makeRow("Médico Tratante: ", accidente.getMedicoTratante()));
        centro.add(makeRow("Cédula Profesional: ", accidente.getCedulaProfesional()));

        centro.add(makeRow("Hospital Destino: ", accidente.getHospitalDestino()));
        centro.add(makeRow("Responsable Traslado: ", accidente.getResponsableTraslado()));
        centro.add(makeRow("Medio Transporte: ", accidente.getMedioTransporte()));
        centro.add(makeRow("Fecha Hora Ingreso: ", accidente.getFechaHoraIngreso()));

        centro.add(makeRow("Nombre Contacto: ", accidente.getNombreContacto()));
        centro.add(makeRow("Relación Contacto: ", accidente.getRelacionContacto()));
        centro.add(makeRow("Teléfono Contacto: ", accidente.getTelefonoContacto()));
        centro.add(makeRow("Correo Contacto: ", accidente.getCorreoContacto()));
        centro.add(makeRow("Domicilio Contacto: ", accidente.getDomicilioContacto()));

        centro.add(makeRow("Testigo 1 Nombre: ", accidente.getTestigo1Nombre()));
        centro.add(makeRow("Testigo 1 Teléfono: ", accidente.getTestigo1Telefono()));

        centro.add(makeRow("Narrativa Detallada: ", accidente.getNarrativaDetallada()));
        centro.add(makeRow("Fecha Elaboración: ", accidente.getFechaElaboracion()));

        if (accidente.getFotos() != null && !accidente.getFotos().isEmpty()) {
            JPanel rowFotos = new JPanel(new BorderLayout(1, 0));
            rowFotos.setBackground(ColoresUDLAP.BLANCO);
            JLabel lblFotos = new JLabel("Fotos:");
            lblFotos.setFont(new Font("Arial", Font.BOLD, 16));
            rowFotos.add(lblFotos, BorderLayout.WEST);
            centro.add(rowFotos);
            JPanel fotosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            fotosPanel.setBackground(ColoresUDLAP.BLANCO);

            for (byte[] imgBytes : accidente.getFotos()) {
                ImageIcon icon = new ImageIcon(imgBytes);
                // Miniatura de 80×80
                Image thumb = icon.getImage()
                        .getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JLabel lblFoto = new JLabel(new ImageIcon(thumb));
                lblFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                lblFoto.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                // Al hacer click, muestra la foto grande en un JOptionPane
                lblFoto.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ImageIcon fullIcon = new ImageIcon(imgBytes);
                        // Ajusta la escala que necesites
                        Image fullImg = fullIcon.getImage()
                                .getScaledInstance(400, 400, Image.SCALE_SMOOTH);
                        JLabel lblFull = new JLabel(new ImageIcon(fullImg));
                        JOptionPane.showMessageDialog(
                                PanelDetalleAccidente.this,
                                lblFull,
                                "Foto",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                });

                fotosPanel.add(lblFoto);
            }
            centro.add(fotosPanel);
        }

        JScrollPane scroll = new JScrollPane(centro);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        // Botón Regresar
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBackground(ColoresUDLAP.BLANCO);
        JButton btnRegresar = botonTransparente("Regresar",
                ColoresUDLAP.NARANJA_SOLIDO,
                ColoresUDLAP.NARANJA_HOVER);
        btnRegresar.addActionListener(e -> panelManager.showPanel("verAccidentes"));
        botones.add(btnRegresar);
        add(botones, BorderLayout.SOUTH);
    }

    private JPanel makeRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(0, 0));
        row.setBackground(ColoresUDLAP.BLANCO);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel val = new JLabel(value != null && !value.isEmpty() ? value : "-");
        val.setFont(new Font("Arial", Font.PLAIN, 16));
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        return row;
    }

    private JButton botonTransparente(String texto, Color base, Color hover) {
        JButton button = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
}
