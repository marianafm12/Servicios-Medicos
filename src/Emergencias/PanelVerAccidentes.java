package Emergencias;

import BaseDeDatos.ConexionSQLite;
import Utilidades.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Panel para ver y navegar entre los accidentes registrados,
 * con buscador por prefijo de ID Paciente y ordenado por Fecha Elaboración
 * (descendente).
 */
public class PanelVerAccidentes extends JPanel {
    private final PanelManager panelManager;
    private final MensajeErrorUDLAP mensajeInline;

    private JTextField txtBuscar;
    private JPanel panelCentro;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JPanel panelBotones;
    private JButton btnRegresar;
    private JButton btnVerDetalle;

    public PanelVerAccidentes(PanelManager panelManager) {
        this.panelManager = panelManager;
        this.mensajeInline = new MensajeErrorUDLAP();
        initUI();
        cargarDatosDesdeBD();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColoresUDLAP.BLANCO);

        // --- Cabecera: Título y buscador ---
        JLabel titulo = new JLabel("Accidentes Registrados", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(ColoresUDLAP.NARANJA_SOLIDO);

        JPanel header = new JPanel(new BorderLayout(5, 5));
        header.setBackground(ColoresUDLAP.BLANCO);
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        header.add(titulo, BorderLayout.NORTH);

        mensajeInline.setOpaque(false);
        mensajeInline.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(mensajeInline, BorderLayout.CENTER);

        JPanel pnlBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBuscar.setBackground(ColoresUDLAP.BLANCO);
        pnlBuscar.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        pnlBuscar.add(new JLabel("Buscar ID Paciente:"));
        txtBuscar = new JTextField(10);
        pnlBuscar.add(txtBuscar);
        header.add(pnlBuscar, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        // Refrescar tabla al cambiar texto de búsqueda
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

        // --- Centro: tabla de resultados ---
        panelCentro = new JPanel(new BorderLayout());
        add(panelCentro, BorderLayout.CENTER);
        crearTabla();

        // --- Pie: botones ---
        panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        btnRegresar = botonTransparente("Regresar",
                ColoresUDLAP.NARANJA_SOLIDO, ColoresUDLAP.NARANJA_HOVER);
        btnRegresar.addActionListener(e -> panelManager.showPanel("menuEmergencias"));
        panelBotones.add(btnRegresar);

        btnVerDetalle = botonTransparente("Ver detalle",
                ColoresUDLAP.VERDE_SOLIDO, ColoresUDLAP.VERDE_HOVER);
        btnVerDetalle.setVisible(false);
        btnVerDetalle.addActionListener(e -> mostrarDetalle());
        panelBotones.add(btnVerDetalle);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void crearTabla() {
        tabla = new JTable();
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabla.setRowHeight(28);
        tabla.setGridColor(new Color(230, 230, 230));
        tabla.setFillsViewportHeight(true);

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(ColoresUDLAP.NARANJA_SOLIDO);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        scroll.setBackground(ColoresUDLAP.BLANCO);
        scroll.getViewport().setBackground(ColoresUDLAP.BLANCO);

        panelCentro.removeAll();
        panelCentro.add(scroll, BorderLayout.CENTER);

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                btnVerDetalle.setVisible(tabla.getSelectedRow() != -1);
                panelBotones.revalidate();
            }
        });
    }

    private void cargarDatosDesdeBD() {
        btnVerDetalle.setVisible(false);

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        modelo.setColumnIdentifiers(new String[] {
                "IDEmergencia", "ID Paciente", "Paciente", "Médico", "Gravedad", "Fecha Accidente", "Fecha Elaboración"
        });

        String prefijo = txtBuscar.getText().trim();
        String sql = """
                    SELECT
                        IDEmergencia,
                        Matricula                               AS IDPaciente,
                        NombreEstudiante || ' ' || ApellidoPaterno || ' ' || ApellidoMaterno
                                                               AS Paciente,
                        ParamedicoResponsable                   AS Medico,
                        GravedadTriage                          AS Gravedad,
                        FechaAccidente,
                        FechaElaboracion
                    FROM Accidentes
                """;
        if (!prefijo.isEmpty()) {
            sql += " WHERE CAST(Matricula AS TEXT) LIKE ? ";
        }
        sql += " ORDER BY FechaElaboracion DESC";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!prefijo.isEmpty()) {
                ps.setString(1, prefijo + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modelo.addRow(new Object[] {
                            rs.getInt("IDEmergencia"),
                            rs.getInt("IDPaciente"),
                            rs.getString("Paciente"),
                            rs.getString("Medico"),
                            rs.getString("Gravedad"),
                            rs.getString("FechaAccidente"),
                            rs.getString("FechaElaboracion")
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            mensajeInline.mostrarError(
                    "Error al cargar accidentes: " + ex.getMessage());
        }

        tabla.setModel(modelo);
        tabla.removeColumn(tabla.getColumnModel().getColumn(0));
        panelCentro.revalidate();
    }

    private void mostrarDetalle() {
        int filaVista = tabla.getSelectedRow();
        if (filaVista == -1)
            return;
        int filaModelo = tabla.convertRowIndexToModel(filaVista);
        int idPaciente = (int) modelo.getValueAt(filaModelo, 0);

        String key = "detalleAccidente_" + idPaciente + "_" + System.currentTimeMillis();
        panelManager.registerPanel(new PanelProvider() {
            @Override
            public JPanel getPanel() {
                return new PanelDetalleAccidente(
                        panelManager,
                        AccidenteDB.buscarAccidente(idPaciente));
            }

            @Override
            public String getPanelName() {
                return key;
            }
        });
        panelManager.showPanel(key);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            cargarDatosDesdeBD();
            btnVerDetalle.setVisible(false);
        }
    }

    private JButton botonTransparente(String texto, Color base, Color hover) {
        JButton b = new JButton(texto) {
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
