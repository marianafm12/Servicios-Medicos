package Consultas;

import javax.swing.*;
import java.awt.*;
import Utilidades.ColoresUDLAP;

public class PanelControlConsultas extends JPanel {
    public PanelControlConsultas(JTextField[] campos, JTextArea areaTexto) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setBackground(ColoresUDLAP.BLANCO);
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton botonGuardar = crearBoton("Guardar", ColoresUDLAP.VERDE_SOLIDO);
        botonGuardar.addActionListener(new GuardarConsulta(campos, areaTexto));
        add(botonGuardar);

        JButton botonBuscar = crearBoton("Buscar", ColoresUDLAP.NARANJA_BARRA);
        botonBuscar.addActionListener(new BuscarPaciente(campos));
        add(botonBuscar);

        JButton botonLimpiar = crearBoton("Limpiar", ColoresUDLAP.ROJO_SOLIDO);
        botonLimpiar.addActionListener(new LimpiarCamposConsulta(campos, areaTexto));
        add(botonLimpiar);
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