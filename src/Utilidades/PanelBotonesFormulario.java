package Utilidades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel genérico de botones para formularios UDLAP.
 * Botones con esquinas redondeadas, colores sólidos y efecto hover.
 * Tipos:
 * - PRIMARY (VERDE_SOLIDO, hover VERDE_HOVER)
 * - SECONDARY (NARANJA_SOLIDO, hover NARANJA_HOVER)
 * - DANGER (ROJO_SOLIDO, hover ROJO_HOVER)
 * - BACK (GRIS_SOLIDO, hover GRIS_HOVER)
 * - INFO (AZUL_SOLIDO, hover AZUL_HOVER)
 */
public class PanelBotonesFormulario extends JPanel {
        /**
         * Configuración de un botón: texto y tipo de estilo.
         */
        public static class BotonConfig {
                public enum Tipo {
                        PRIMARY, SECONDARY, DANGER, BACK, INFO
                }

                public final String texto;
                public final Tipo tipo;

                public BotonConfig(String texto, Tipo tipo) {
                        this.texto = texto;
                        this.tipo = tipo;
                }
        }

        private final List<JButton> botones = new ArrayList<>();

        /**
         * Crea el panel con los botones según las configuraciones dadas.
         */
        public PanelBotonesFormulario(BotonConfig... configs) {
                setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
                setBackground(ColoresUDLAP.BLANCO);

                for (BotonConfig cfg : configs) {
                        JButton btn = new JButton(cfg.texto) {
                                @Override
                                protected void paintComponent(Graphics g) {
                                        Graphics2D g2 = (Graphics2D) g.create();
                                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                        RenderingHints.VALUE_ANTIALIAS_ON);
                                        g2.setColor(getBackground());
                                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                                        g2.dispose();
                                        super.paintComponent(g);
                                }
                        };

                        // Selección de color base y hover según tipo
                        Color base, hover;
                        switch (cfg.tipo) {
                                case PRIMARY:
                                        base = ColoresUDLAP.VERDE_SOLIDO;
                                        hover = ColoresUDLAP.VERDE_HOVER;
                                        break;
                                case SECONDARY:
                                        base = ColoresUDLAP.NARANJA_SOLIDO;
                                        hover = ColoresUDLAP.NARANJA_HOVER;
                                        break;
                                case DANGER:
                                        base = ColoresUDLAP.ROJO_SOLIDO;
                                        hover = ColoresUDLAP.ROJO_HOVER;
                                        break;
                                case BACK:
                                        base = ColoresUDLAP.GRIS_SOLIDO;
                                        hover = ColoresUDLAP.GRIS_HOVER;
                                        break;
                                case INFO:
                                        base = ColoresUDLAP.AZUL_SOLIDO;
                                        hover = ColoresUDLAP.AZUL_HOVER;
                                        break;
                                default:
                                        base = ColoresUDLAP.GRIS_SOLIDO;
                                        hover = ColoresUDLAP.GRIS_HOVER;
                                        break;
                        }

                        btn.setBackground(base);
                        btn.setForeground(ColoresUDLAP.BLANCO);
                        btn.setFont(new Font("Arial", Font.BOLD, 14));
                        btn.setFocusPainted(false);
                        btn.setBorderPainted(false);
                        btn.setContentAreaFilled(false);
                        btn.setOpaque(false);
                        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

                        btn.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseEntered(MouseEvent e) {
                                        btn.setBackground(hover);
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {
                                        btn.setBackground(base);
                                }
                        });

                        botones.add(btn);
                        add(btn);
                }
        }

        /**
         * Asigna ActionListeners de forma secuencial a cada botón creado.
         */
        public void setListeners(ActionListener... listeners) {
                for (int i = 0; i < listeners.length && i < botones.size(); i++) {
                        botones.get(i).addActionListener(listeners[i]);
                }
        }

        /**
         * Retorna la lista de botones (para control adicional si se requiere).
         */
        public List<JButton> getBotones() {
                return botones;
        }
}
