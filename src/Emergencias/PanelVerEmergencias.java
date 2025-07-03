package Emergencias;

import Utilidades.ColoresUDLAP;
import Utilidades.PanelManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import Utilidades.PanelProvider;


/**
 * Panel para ver y actualizar el estado de las emergencias.
 */
public class PanelVerEmergencias extends JPanel {
    private final PanelManager panelManager;
    private final boolean esMedico;
    private final int userId;

    private JPanel panelCentro;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JPanel panelBotones;
    private JButton btnRegresar;
    private JButton btnVerInformacion;
    private JButton btnCambiarEstado;


    public PanelVerEmergencias(PanelManager panelManager, boolean esMedico, int userId) {
        this.panelManager = panelManager;
        this.esMedico     = esMedico;
        this.userId       = userId;
        initUI();
        cargarDatosDesdeBD();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColoresUDLAP.BLANCO);

        // --- Título ---
        JLabel titulo = new JLabel("Emergencias Registradas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        // --- Centro: tabla dentro de panelCentro ---
        panelCentro = new JPanel(new BorderLayout());
        add(panelCentro, BorderLayout.CENTER);
        crearTabla();

        // --- Botones inferior ---
        panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        JButton btnRegresar = botonTransparente("Regresar",
                    ColoresUDLAP.VERDE,    // color base
                    ColoresUDLAP.VERDE_HOVER    // color hover
                );
        btnRegresar.addActionListener(e ->
            panelManager.showPanel("menuEmergencias")
        );
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);

    // — después de crear btnRegresar —
        btnVerInformacion = botonTransparente(
            "Ver información",
            ColoresUDLAP.NARANJA,       // color base
            ColoresUDLAP.NARANJA_HOVER  // color hover
        );
        btnVerInformacion.setVisible(false); 

    btnVerInformacion.addActionListener(e -> {
        int filaVista = tabla.getSelectedRow();
        if (filaVista == -1) return;

        int filaModelo    = tabla.convertRowIndexToModel(filaVista);
        int idEmergencia  = (int) modelo.getValueAt(filaModelo, 0);
        Emergencia em     = EmergenciaDAO.obtenerPorId(idEmergencia);

        // clave única: id + milisegundos
        String keyDetalle = "detalleEmergencia_" + idEmergencia + "_" + System.currentTimeMillis();

        panelManager.registerPanel(new PanelProvider() {
            @Override public JPanel getPanel()     {
                return new PanelDetalleEmergencia(panelManager, em);
            }
            @Override public String getPanelName() {
                return keyDetalle;
            }
        });

        panelManager.showPanel(keyDetalle);
    });




    btnCambiarEstado = botonTransparente(
        "Cambiar estado",
        ColoresUDLAP.ROJO,       // color base
        ColoresUDLAP.ROJO_HOVER  // color hover
    );
    btnCambiarEstado.setVisible(false);
    btnCambiarEstado.addActionListener(e -> {
        int filaVista = tabla.getSelectedRow();
        if (filaVista != -1) {
            int filaModelo = tabla.convertRowIndexToModel(filaVista);
            int idEmergencia = (int) modelo.getValueAt(filaModelo, 0);
            String[] nuevosEstados = {"Transferido", "Completo"};

            // guardamos textos originales
            String oldOk     = UIManager.getString("OptionPane.okButtonText");
            String oldCancel = UIManager.getString("OptionPane.cancelButtonText");
            UIManager.put("OptionPane.okButtonText",     "Aceptar");
            UIManager.put("OptionPane.cancelButtonText", "Cancelar");

            String nuevoEstado = (String) JOptionPane.showInputDialog(
                PanelVerEmergencias.this,
                "Nuevo estado:",
                "Actualizar Emergencia",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nuevosEstados,
                nuevosEstados[0]
            );

            // restauramos UIManager
            UIManager.put("OptionPane.okButtonText",     oldOk);
            UIManager.put("OptionPane.cancelButtonText", oldCancel);

            if (nuevoEstado != null) {
                boolean ok = EmergenciaDAO.actualizarEstadoEmergencia(idEmergencia, nuevoEstado, userId);
                if (ok) {
                    cargarDatosDesdeBD();
                } else {
                    JOptionPane.showMessageDialog(
                        PanelVerEmergencias.this,
                        "No se pudo actualizar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    });

    // añadimos ambos botones antes o después de btnRegresar
    panelBotones.add(btnVerInformacion);
    panelBotones.add(btnCambiarEstado);
    panelBotones.revalidate();
    panelBotones.repaint();


    }




    private void crearTabla() {
        // Inicializamos JTable
        tabla = new JTable();
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabla.setRowHeight(28);
        tabla.setGridColor(new Color(230, 230, 230));
        tabla.setFillsViewportHeight(true);

        // Encabezado
        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setBackground(ColoresUDLAP.VERDE_SOLIDO);
        encabezado.setForeground(Color.WHITE);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        encabezado.setPreferredSize(new Dimension(100, 40));
        encabezado.setReorderingAllowed(false);

        // Scrollpane con márgenes
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        panelCentro.removeAll();
        panelCentro.add(scroll, BorderLayout.CENTER);

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
                    int filaVista = tabla.getSelectedRow();
                    int filaModelo = tabla.convertRowIndexToModel(filaVista);
                    String estadoActual = modelo.getValueAt(filaModelo, 3).toString();

                    // siempre mostramos “Ver información”
                    btnVerInformacion.setVisible(true);
                    // “Cambiar estado” solo si estaba pendiente
                    btnCambiarEstado.setVisible("Pendiente".equalsIgnoreCase(estadoActual));

                    // refrescar layout de los botones
                    panelBotones.revalidate();
                    panelBotones.repaint();
                }
            }
        });

    }

    private void cargarDatosDesdeBD() {

        btnVerInformacion.setVisible(false);
        btnCambiarEstado.setVisible(false);

        // Nuevo modelo de tabla
        modelo = new DefaultTableModel() {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.setColumnIdentifiers(new String[]{
            "ID", "ID Paciente", "Fecha Incidente", "Estado", "Médico Responsable"
        });

        // Consultamos BD
        List<Emergencia> lista = EmergenciaDAO.obtenerTodas();
        // Orden personalizado: Pendiente > Transferido > Completo
        lista.sort(Comparator.comparingInt(e -> {
            switch (e.getEstado()) {
                case "Pendiente":   return 0;
                case "Transferido": return 1;
                default:            return 2;
            }
        }));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Emergencia e : lista) {
            // aquí chequeamos null y mostramos "-" si no hay paciente
            Object idPacCell = e.getIdPaciente() != null
                            ? e.getIdPaciente()
                            : "-";

            modelo.addRow(new Object[]{
                e.getId(),                                  // IDEmergencia
                idPacCell,                                  // IDPaciente o "-"
                e.getFechaIncidente().toLocalDateTime().format(fmt),
                e.getEstado(),
                e.getMedicoResponsable() != null
                    ? e.getMedicoResponsable()
                    : "-"
            });
        }


        tabla.setModel(modelo);
        panelCentro.revalidate();
        panelCentro.repaint();
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            // Oculta botones y recarga toda la tabla
            btnVerInformacion.setVisible(false);
            btnCambiarEstado.setVisible(false);
            cargarDatosDesdeBD();
        }
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
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

}
