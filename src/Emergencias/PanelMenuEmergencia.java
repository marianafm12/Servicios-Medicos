package Emergencias;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel que reemplaza al antiguo MenuEmergenciaFrame.
 * Muestra dos opciones: registrar llamada y reporte de accidente,
 * más un botón de volver.
 */
public class PanelMenuEmergencia extends JPanel {
    private final JButton btnRegistrarLlamada;
    private final JButton btnReporteAccidente;
    private final JButton btnVolver;

    /**
     * @param menuListener ActionListener que recibirá los comandos:
     *                     "registrarLlamada", "reporteAccidente" y "volver"
     */
    public PanelMenuEmergencia(ActionListener menuListener) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ——— Encabezado ———
        JLabel lblTitulo = new JLabel("Menú de Emergencia", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // ——— Cuerpo central con los dos botones grandes ———
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Botón “Registrar Llamada de Emergencia”
        gbc.gridy = 0;
        btnRegistrarLlamada = createMenuButton("Registrar Llamada de Emergencia");
        btnRegistrarLlamada.setActionCommand("registrarLlamada");
        btnRegistrarLlamada.addActionListener(menuListener);
        centro.add(btnRegistrarLlamada, gbc);

        // Botón “Llenar Reporte de Accidente”
        gbc.gridy = 1;
        btnReporteAccidente = createMenuButton("Llenar Reporte de Accidente");
        btnReporteAccidente.setActionCommand("reporteAccidente");
        btnReporteAccidente.addActionListener(menuListener);
        centro.add(btnReporteAccidente, gbc);

        add(centro, BorderLayout.CENTER);

        // ——— Pie con botón “Volver” ———
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pie.setBackground(Color.WHITE);
        btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 14));
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.setActionCommand("volver");
        btnVolver.addActionListener(menuListener);
        pie.add(btnVolver);
        pie.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
        add(pie, BorderLayout.SOUTH);
    }

    /**
     * Crea un JButton de gran tamaño y estilo consistente.
     */
    private JButton createMenuButton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(320, 60));
        btn.setBackground(new Color(0, 123, 200));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
