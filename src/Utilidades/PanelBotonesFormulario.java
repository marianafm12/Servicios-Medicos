package Utilidades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanelBotonesFormulario extends JPanel {

    public final JButton btnGuardar;
    public final JButton btnBuscar;
    public final JButton btnLimpiar;

    public PanelBotonesFormulario(String textoGuardar, String textoBuscar, String textoLimpiar) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setBackground(ColoresUDLAP.BLANCO);

        Dimension size = new Dimension(100, 30);

        btnGuardar = new JButton(textoGuardar);
        btnBuscar = new JButton(textoBuscar);
        btnLimpiar = new JButton(textoLimpiar);

        configurarBoton(btnGuardar, ColoresUDLAP.VERDE_SOLIDO, size);
        configurarBoton(btnBuscar, ColoresUDLAP.NARANJA_BARRA, size);
        configurarBoton(btnLimpiar, ColoresUDLAP.ROJO_SOLIDO, size);

        add(btnGuardar);
        add(btnBuscar);
        add(btnLimpiar);
    }

    private void configurarBoton(JButton boton, Color color, Dimension size) {
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(true);
        boton.setOpaque(true);
        boton.setPreferredSize(size);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
    }

    public void setListeners(ActionListener guardar, ActionListener buscar, ActionListener limpiar) {
        btnGuardar.addActionListener(guardar);
        btnBuscar.addActionListener(buscar);
        btnLimpiar.addActionListener(limpiar);
    }
}
