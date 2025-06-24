package Justificantes;

import java.awt.event.ActionListener;
import BaseDeDatos.ConexionSQLite;
import Utilidades.ColoresUDLAP;
import Utilidades.PanelManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class SolicitudesJustificantesFrame extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JPanel panelCentro;
    private final PanelManager panelManager;

    public SolicitudesJustificantesFrame(PanelManager panelManager) {
        this.panelManager = panelManager;

        setTitle("Solicitudes de Justificantes");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setBackground(ColoresUDLAP.BLANCO);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Solicitudes de Justificantes", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 102, 0));
        titulo.setBackground(ColoresUDLAP.BLANCO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        panelCentro = new JPanel(new BorderLayout());
        add(panelCentro, BorderLayout.CENTER);

        crearTabla();
        cargarDatosDesdeBD();

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));

        JButton btnVer = new JButton("Ver Seleccionado");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRegresar = new JButton("Regresar");

        btnVer.setFont(new Font("Arial", Font.BOLD, 15));
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 15));
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 15));

        btnVer.setBackground(new Color(0, 102, 0));
        btnVer.setForeground(Color.WHITE);
        btnVer.setFocusPainted(false);

        btnEliminar.setBackground(new Color(221, 71, 66));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);

        btnRegresar.setBackground(Color.GRAY);
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);

        btnVer.addActionListener(e -> verSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnRegresar.addActionListener(e -> {
            panelManager.showPanel("menuJustificantes");
            dispose();
        });

        panelBotones.add(btnRegresar);
        panelBotones.add(btnVer);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void crearTabla() {
        tabla = new JTable();
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

        panelCentro.removeAll();
        panelCentro.setLayout(new BorderLayout());
        panelCentro.add(scroll, BorderLayout.CENTER);
        panelCentro.revalidate();
        panelCentro.repaint();
    }

    private void cargarDatosDesdeBD() {
        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[] {
                "Folio", "ID Paciente", "Nombre", "Motivo", "Fecha Inicio", "Fecha Fin", "Estado"
        });

 String sql = "SELECT folio, idPaciente, nombrePaciente, motivo, fechaInicio, fechaFin, estado " +
             "FROM JustificantePaciente " +
             "ORDER BY CASE estado " +
             "  WHEN 'Pendiente' THEN 1 " +
             "  WHEN 'Aprobado' THEN 2 " +
             "  WHEN 'Rechazado' THEN 3 " +
             "  ELSE 4 END";


        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int folio = rs.getInt("folio");
                String id = rs.getString("idPaciente");
                String nombre = rs.getString("nombrePaciente");
                String motivo = rs.getString("motivo");
                String inicio = rs.getString("fechaInicio");
                String fin = rs.getString("fechaFin");
                String estado = rs.getString("estado");

                if (estado == null || estado.trim().isEmpty()) {
                    estado = "Pendiente";
                }

                modelo.addRow(new Object[] { folio, id, nombre, motivo, inicio, fin, estado });
            }

            tabla.setModel(modelo);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar las solicitudes de justificantes.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para revisar.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int folio = (int) modelo.getValueAt(fila, 0);

        // Acción que ejecutará el botón "Volver"
        ActionListener volverAction = e -> {
            crearTabla();
            cargarDatosDesdeBD();
        };

        panelCentro.removeAll();
        panelCentro.setLayout(new BorderLayout());
        panelCentro.add(new RevisarSolicitudFrame(folio, volverAction, panelManager), BorderLayout.CENTER);

        panelCentro.revalidate();
        panelCentro.repaint();
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] opciones = { "Sí", "No" };
        int confirmacion = JOptionPane.showOptionDialog(
                this,
                "¿Está seguro de que desea eliminar esta solicitud?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[1]);

        if (confirmacion != JOptionPane.YES_OPTION)
            return;

        int folio = (int) modelo.getValueAt(fila, 0);

        String sql = "DELETE FROM JustificantePaciente WHERE folio = ?";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, folio);
            int eliminado = ps.executeUpdate();

            if (eliminado > 0) {
                modelo.removeRow(fila);
                JOptionPane.showMessageDialog(this, "Solicitud eliminada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la solicitud.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar de la base de datos.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Si deseas probarlo desde aquí, puedes inicializar con null
        SwingUtilities.invokeLater(() -> new SolicitudesJustificantesFrame(null).setVisible(true));
    }
}
