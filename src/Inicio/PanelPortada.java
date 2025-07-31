package Inicio;

import Utilidades.ColoresUDLAP;
import Utilidades.PanelManager;
import Emergencias.EmergenciaDAO;
import GestionCitas.CitasDAO;
import Justificantes.JustificanteDAO;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de bienvenida o portada.
 */
public class PanelPortada extends JPanel {
    private final PanelManager panelManager;
    private final boolean esMedico;
    private final int userId;

    // Constructor recibe los tres parámetros
    public PanelPortada(PanelManager panelManager, boolean esMedico, int userId) {
        this.panelManager = panelManager;
        this.esMedico     = esMedico;
        this.userId       = userId;
        initUI();
    }

    private void initUI() {
        // TODO: si quieres usar tu blanco UDLAP, en lugar de Color.WHITE pon ColoresUDLAP.BLANCO
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // --- Título ---
        JLabel lbl = new JLabel("Bienvenido a Servicios Médicos UDLAP", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 28));
        lbl.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));
        add(lbl, BorderLayout.NORTH);

        // --- Instrucción central ---
        JLabel instr = new JLabel("Seleccione una opción de la izquierda", SwingConstants.CENTER);
        instr.setFont(new Font("Arial", Font.PLAIN, 18));
        instr.setForeground(new Color(80, 80, 80));
        add(instr, BorderLayout.CENTER);

        // --- Dashboard al pie ---
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        south.setBackground(Color.WHITE);
        south.setOpaque(true);

        if (!esMedico) {
            int proximas = CitasDAO.contarProximas(userId);
            south.add(createCard("Próximas Citas", String.valueOf(proximas)));
        } else {
            int emPend   = EmergenciaDAO.contarPendientes();
            int justPend = JustificanteDAO.contarJustificantesPendientes();
            south.add(createCard("Emergencias Pendientes",   String.valueOf(emPend)));
            south.add(createCard("Justificantes Pendientes", String.valueOf(justPend)));
        }

        add(south, BorderLayout.SOUTH);
    }

    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setOpaque(true);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColoresUDLAP.VERDE_SOLIDO, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Arial", Font.PLAIN, 14));
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(5));
        card.add(lblValue);

        return card;
    }
}
