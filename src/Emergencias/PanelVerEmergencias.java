package Emergencias;

import Utilidades.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Inicio.InterfazMedica;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class PanelVerEmergencias extends JPanel {
    private final PanelManager panelManager;
    private final boolean esMedico;
    private final int userId;

    private JTextField txtBuscar;
    private JPanel panelCentro;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JPanel panelBotones;
    private JButton btnVerInformacion;
    private JButton btnCambiarEstado;

    private final MensajeErrorUDLAP mensajeInline;

    public PanelVerEmergencias(PanelManager panelManager, boolean esMedico, int userId) {
        this.panelManager = panelManager;
        this.esMedico = esMedico;
        this.userId = userId;

        mensajeInline = new MensajeErrorUDLAP();
        mensajeInline.limpiar();
        initUI();
        cargarDatosDesdeBD();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColoresUDLAP.BLANCO);

        // --- Cabecera: Título + buscador ---
        JLabel titulo = new JLabel("Emergencias Registradas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(ColoresUDLAP.VERDE_SOLIDO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));

        // panel de búsqueda
        JPanel pnlBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        pnlBuscar.setBackground(ColoresUDLAP.BLANCO);
        pnlBuscar.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        pnlBuscar.add(new JLabel("Buscar ID Estudiante:"));
        txtBuscar = new JTextField(10);
        pnlBuscar.add(txtBuscar);

        // envolvemos ambos en un panel vertical
        JPanel norte = new JPanel(new BorderLayout());
        norte.setBackground(ColoresUDLAP.BLANCO);
        norte.add(titulo, BorderLayout.NORTH);
        norte.add(pnlBuscar, BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);

        // cada vez que cambie el texto, recargamos
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            private void actualizar() {
                cargarDatosDesdeBD();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizar();
            }
        });

        // --- Centro: tabla ---
        panelCentro = new JPanel(new BorderLayout());
        add(panelCentro, BorderLayout.CENTER);
        crearTabla();

        // --- Pie: botones ---
        panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        JButton btnRegresar = botonTransparente("Regresar",
                ColoresUDLAP.VERDE_SOLIDO, ColoresUDLAP.VERDE_HOVER);
        btnRegresar.addActionListener(e -> panelManager.showPanel("menuEmergencias"));
        panelBotones.add(btnRegresar);

        btnVerInformacion = botonTransparente("Ver información",
                ColoresUDLAP.NARANJA_SOLIDO, ColoresUDLAP.NARANJA_HOVER);
        btnVerInformacion.setVisible(false);
        btnVerInformacion.addActionListener(e -> mostrarDetalle());
        panelBotones.add(btnVerInformacion);

        btnCambiarEstado = botonTransparente("Cambiar estado",
                ColoresUDLAP.ROJO_SOLIDO, ColoresUDLAP.ROJO_HOVER);
        btnCambiarEstado.setVisible(false);
        btnCambiarEstado.addActionListener(e -> cambiarEstado());
        panelBotones.add(btnCambiarEstado);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void crearTabla() {
        tabla = new JTable();
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabla.setRowHeight(28);
        tabla.setGridColor(new Color(230, 230, 230));
        tabla.setFillsViewportHeight(true);

        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setBackground(ColoresUDLAP.VERDE_SOLIDO);
        encabezado.setForeground(Color.WHITE);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        encabezado.setPreferredSize(new Dimension(100, 40));
        encabezado.setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        panelCentro.removeAll();
        panelCentro.add(scroll, BorderLayout.CENTER);

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tabla.getSelectedRow() != -1) {
                    btnVerInformacion.setVisible(true);
                    // solo permitimos cambiar si estaba "Pendiente"
                    String estado = modelo.getValueAt(tabla.convertRowIndexToModel(tabla.getSelectedRow()), 3)
                            .toString();
                    btnCambiarEstado.setVisible("Pendiente".equalsIgnoreCase(estado));
                    panelBotones.revalidate();
                }
            }
        });
    }

    private void cargarDatosDesdeBD() {
        btnVerInformacion.setVisible(false);
        btnCambiarEstado.setVisible(false);

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        modelo.setColumnIdentifiers(new String[] {
                "ID", "ID Paciente", "Fecha Incidente", "Estado", "Médico Responsable"
        });

        // sacamos todas, ordenamos y luego filtramos en memoria por prefijo de
        // matrícula
        List<Emergencia> lista = EmergenciaDAO.obtenerTodas();
        lista.sort(Comparator.comparingInt(e -> {
            switch (e.getEstado()) {
                case "Pendiente":
                    return 0;
                case "Transferido":
                    return 1;
                default:
                    return 2;
            }
        }));
        String prefijo = txtBuscar.getText().trim();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Emergencia e : lista) {
            // convertir idPaciente a cadena
            String idPac = e.getIdPaciente() != null ? e.getIdPaciente().toString() : "";
            if (!prefijo.isEmpty() && !idPac.startsWith(prefijo)) {
                continue;
            }
            modelo.addRow(new Object[] {
                    e.getId(),
                    idPac.isEmpty() ? "-" : idPac,
                    e.getFechaIncidente().toLocalDateTime().format(fmt),
                    e.getEstado(),
                    e.getMedicoResponsable() != null ? e.getMedicoResponsable() : "-"
            });
        }

        tabla.setModel(modelo);
        panelCentro.revalidate();
        panelCentro.repaint();
    }

    private void mostrarDetalle() {
        int fila = tabla.getSelectedRow();
        if (fila == -1)
            return;
        int idEmergencia = (int) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
        Emergencia em = EmergenciaDAO.obtenerPorId(idEmergencia);
        String key = "detalleEmergencia_" + idEmergencia + "_" + System.currentTimeMillis();
        panelManager.registerPanel((new PanelProvider() {
            @Override
            public JPanel getPanel() {
                return new PanelDetalleEmergencia(panelManager, em);
            }

            @Override
            public String getPanelName() {
                return key;
            }
        }));
        panelManager.showPanel(key);
    }

    private void cambiarEstado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1)
            return;
        int idEmergencia = (int) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);

        String[] estados = { "Transferido", "Completo" };
        // configuración de diálogo omitida por brevedad...
        String nuevo = (String) JOptionPane.showInputDialog(
                this, "Nuevo estado:", "Actualizar Emergencia",
                JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]);
        if (nuevo != null && EmergenciaDAO.actualizarEstadoEmergencia(idEmergencia, nuevo, userId)) {
            // 1) recarga la tabla
            cargarDatosDesdeBD();

            // 2) notifica a la ventana principal (InterfazMedica) para que actualice la
            // campana
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof InterfazMedica) {
                ((InterfazMedica) w).checkNotifications();
            }
        } else if (nuevo != null) {
            mensajeInline.mostrarAdvertencia("No se pudo actualizar.");
        }
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            cargarDatosDesdeBD();
            btnVerInformacion.setVisible(false);
            btnCambiarEstado.setVisible(false);
        }
    }

    private JButton botonTransparente(String texto, Color base, Color hover) {
        JButton b = new JButton(texto) {
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
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 15));
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
