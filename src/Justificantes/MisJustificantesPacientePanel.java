package Justificantes;

import Utilidades.ColoresUDLAP;
import Utilidades.PanelManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MisJustificantesPacientePanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private String idPaciente;
    private final PanelManager panelManager;

    public MisJustificantesPacientePanel(String idPaciente, PanelManager panelManager) {
        this.idPaciente = idPaciente;
        this.panelManager = panelManager;

        setLayout(new BorderLayout(10, 10));
        setBackground(ColoresUDLAP.BLANCO);

        JLabel titulo = new JLabel("Mis Justificantes Médicos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 102, 0));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{
                "Folio", "Motivo", "Inicio", "Fin", "Estado", "Médico"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabla.setRowHeight(28);
        tabla.setGridColor(new Color(230, 230, 230));
        tabla.setFillsViewportHeight(true);

        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setBackground(new Color(255, 102, 0));
        encabezado.setForeground(Color.WHITE);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        encabezado.setPreferredSize(new Dimension(100, 40));
        encabezado.setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);

        cargarJustificantes();

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panelBotones.setBackground(ColoresUDLAP.BLANCO);

        JButton btnAbrirPDF = new JButton("Abrir Justificante PDF");
        JButton btnRegresar = new JButton("Regresar");

        btnAbrirPDF.setFont(new Font("Arial", Font.BOLD, 15));
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 15));

        btnAbrirPDF.setBackground(new Color(0, 102, 0));
        btnAbrirPDF.setForeground(Color.WHITE);
        btnAbrirPDF.setFocusPainted(false);

        btnRegresar.setBackground(new Color(221, 71, 66));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);

        btnAbrirPDF.addActionListener(e -> abrirSeleccionado());
        btnRegresar.addActionListener(e -> regresarAMenuJustificantes());

        panelBotones.add(btnAbrirPDF);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarJustificantes() {
        List<Justificante> lista = JustificanteDAO.obtenerTodos().stream()
                .filter(j -> j.getIdPaciente().equals(idPaciente))
                .collect(Collectors.toList());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Justificante j : lista) {
            modelo.addRow(new Object[]{
                    j.getFolio(),
                    j.getMotivo(),
                    j.getFechaInicio().format(fmt),
                    j.getFechaFin().format(fmt),
                    j.getEstado(),
                    j.getResueltoPor() != null ? j.getResueltoPor() : "-"
            });
        }
    }

    private void abrirSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un justificante.");
            return;
        }

        String estado = (String) modelo.getValueAt(fila, 4);
        if (!estado.equalsIgnoreCase("Aprobado")) {
            JOptionPane.showMessageDialog(this, "Solo puedes abrir justificantes aprobados.");
            return;
        }

        int folio = (int) modelo.getValueAt(fila, 0);
        Justificante j = JustificanteDAO.obtenerPorFolio(folio).orElse(null);
        if (j != null) {
            File pdf = GeneradorPDFJustificante.generar(j);
            if (pdf != null && pdf.exists()) {
                try {
                    Desktop.getDesktop().open(pdf);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al abrir el PDF.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "El justificante no tiene PDF generado.");
            }
        }
    }

    private void regresarAMenuJustificantes() {
        panelManager.showPanel("justificantesPaciente");
    }
}
