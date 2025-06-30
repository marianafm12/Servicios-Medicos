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
        btnRegresar = new JButton("Regresar");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 15));
        btnRegresar.setBackground(Color.GRAY);
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.addActionListener(e ->
            panelManager.showPanel("menuEmergencias")
        );
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
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

        // Doble click para actualizar estado
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && esMedico) {
                    int fila = tabla.getSelectedRow();
                    String estadoActual = (String) modelo.getValueAt(fila, 3);
                    if ("Pendiente".equalsIgnoreCase(estadoActual)) {
                        int idEmergencia = (int) modelo.getValueAt(fila, 0);
                        String[] opciones = {"Transferido", "Completo"};

                        // Interceptar textos de botones
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
                            opciones,
                            opciones[0]
                        );

                        // Restaurar textos originales
                        UIManager.put("OptionPane.okButtonText",     oldOk);
                        UIManager.put("OptionPane.cancelButtonText", oldCancel);

                        if (nuevoEstado != null) {
                            boolean ok = EmergenciaDAO.actualizarEstadoEmergencia(
                                idEmergencia,
                                nuevoEstado,
                                userId
                            );
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
                }
            }
        });
    }

    private void cargarDatosDesdeBD() {
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
            modelo.addRow(new Object[]{
                e.getId(),
                e.getIdPaciente(),
                e.getFechaIncidente().toLocalDateTime().format(fmt),
                e.getEstado(),
                e.getMedicoResponsable() != null ? e.getMedicoResponsable() : "-"
            });
        }

        tabla.setModel(modelo);
        panelCentro.revalidate();
        panelCentro.repaint();
    }
}
