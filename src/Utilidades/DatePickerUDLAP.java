package Utilidades;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class DatePickerUDLAP extends JPanel {
    private final JTextField campoFecha;
    private final JButton btnCalendario;
    private final JPopupMenu popup;
    private final CalendarioUDLAP calendario;

    public DatePickerUDLAP() {
        setLayout(new BorderLayout(4, 0));
        setOpaque(false);

        campoFecha = new JTextField(12);
        campoFecha.setEditable(false);
        campoFecha.setBackground(ColoresUDLAP.BLANCO);
        campoFecha.setFont(new Font("Arial", Font.PLAIN, 14));
        campoFecha.setBorder(BorderFactory.createLineBorder(ColoresUDLAP.GRIS_CLARO));
        campoFecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnCalendario = new JButton("\uD83D\uDCC5"); // Icono calendario 📅 (puedes usar uno propio)
        btnCalendario.setFocusable(false);
        btnCalendario.setFont(new Font("Arial", Font.PLAIN, 16));
        btnCalendario.setBackground(ColoresUDLAP.BLANCO);
        btnCalendario.setBorder(BorderFactory.createLineBorder(ColoresUDLAP.NARANJA_BARRA));
        btnCalendario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCalendario.setPreferredSize(new Dimension(38, 30));

        popup = new JPopupMenu();
        popup.setLayout(new BorderLayout());
        calendario = new CalendarioUDLAP();
        popup.add(calendario, BorderLayout.CENTER);

        MouseListener mostrarPopup = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostrarCalendario();
            }
        };
        campoFecha.addMouseListener(mostrarPopup);
        btnCalendario.addMouseListener(mostrarPopup);

        calendario.setDateSelectionListener((fecha) -> {
            if (fecha != null) {
                campoFecha.setText(fecha.toString());
            } else {
                campoFecha.setText("");
            }
            popup.setVisible(false);
        });

        popup.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });

        add(campoFecha, BorderLayout.CENTER);
        add(btnCalendario, BorderLayout.EAST);
    }

    private void mostrarCalendario() {
        calendario.setPreferredSize(new Dimension(430, 350));
        popup.pack();
        popup.show(this, 0, getHeight());
    }

    public LocalDate getDate() {
        String txt = campoFecha.getText();
        if (txt == null || txt.isEmpty())
            return null;
        try {
            return LocalDate.parse(txt);
        } catch (Exception ex) {
            return null;
        }
    }

    public void setDate(LocalDate fecha) {
        calendario.setSelectedDate(fecha);
        campoFecha.setText(fecha != null ? fecha.toString() : "");
    }

    public JTextField getTextField() {
        return campoFecha;
    }
}
