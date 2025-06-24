package Emergencias;

import javax.swing.*;
import java.awt.*;
import Utilidades.ColoresUDLAP;

public class PanelControlLlamadaEmergencia extends JPanel {
    public PanelControlLlamadaEmergencia(JTextField[] campos) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setBackground(ColoresUDLAP.BLANCO);
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Botón Registrar Emergencia
        JButton botonRegistrar = new JButton("Registrar Emergencia");
        botonRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        botonRegistrar.setBackground(ColoresUDLAP.VERDE_SOLIDO);
        botonRegistrar.setForeground(Color.WHITE);
        botonRegistrar.setPreferredSize(new Dimension(180, 35));
        add(botonRegistrar);

        // Botón Limpiar
        JButton botonLimpiar = new JButton("Limpiar");
        botonLimpiar.setFont(new Font("Arial", Font.BOLD, 14));
        botonLimpiar.setBackground(ColoresUDLAP.ROJO_SOLIDO);
        botonLimpiar.setForeground(Color.WHITE);
        botonLimpiar.setPreferredSize(new Dimension(120, 35));
        add(botonLimpiar);

        // Botón Buscar
        JButton botonBuscar = new JButton("Buscar");
        botonBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        botonBuscar.setBackground(ColoresUDLAP.NARANJA_BARRA);
        botonBuscar.setForeground(Color.WHITE);
        botonBuscar.setPreferredSize(new Dimension(120, 35));
        add(botonBuscar);
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setPreferredSize(new Dimension(120, 35));
        return boton;
    }
}
