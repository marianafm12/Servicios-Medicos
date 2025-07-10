package Utilidades;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Componente personalizado de selector de fecha para UDLAP.
 * Permite mostrar un calendario emergente y notificar cambios de fecha.
 */
public class DatePickerUDLAP extends JPanel {
    private final JTextField campoFecha;
    private final JButton btnCalendario;
    private final JPopupMenu popup;
    private final CalendarioUDLAP calendario;

    /** Lista de callbacks a invocar al cambiar la fecha */
    private final List<Consumer<LocalDate>> dateChangeListeners = new ArrayList<>();

    public DatePickerUDLAP() {
        setLayout(new BorderLayout(4, 0));
        setOpaque(false);

        // campo de texto (no editable por defecto)
        campoFecha = new JTextField(12);
        campoFecha.setEditable(false);
        campoFecha.setBackground(ColoresUDLAP.BLANCO);
        campoFecha.setFont(new Font("Arial", Font.PLAIN, 14));
        campoFecha.setBorder(BorderFactory.createLineBorder(ColoresUDLAP.GRIS_HOVER));
        campoFecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // botón que dispara el calendario
        btnCalendario = new JButton("\uD83D\uDCC6"); // 📅
        btnCalendario.setFocusable(false);
        btnCalendario.setFont(new Font("JoyPixels", Font.PLAIN, 14));
        btnCalendario.setBackground(ColoresUDLAP.BLANCO);
        btnCalendario.setBorder(BorderFactory.createLineBorder(ColoresUDLAP.NARANJA_SOLIDO));
        btnCalendario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCalendario.setPreferredSize(new Dimension(38, 30));

        // popup con el calendario
        popup = new JPopupMenu();
        popup.setLayout(new BorderLayout());
        calendario = new CalendarioUDLAP();
        popup.add(calendario, BorderLayout.CENTER);

        // listener para mostrar el popup
        MouseListener mostrarPopup = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostrarCalendario();
            }
        };
        campoFecha.addMouseListener(mostrarPopup);
        btnCalendario.addMouseListener(mostrarPopup);

        // cuando el usuario elige una fecha en el calendario
        calendario.setDateSelectionListener((fecha) -> {
            campoFecha.setText(fecha != null ? fecha.toString() : "");
            popup.setVisible(false);
            for (Consumer<LocalDate> listener : dateChangeListeners) {
                listener.accept(fecha);
            }
        });

        popup.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) { }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { }
            public void popupMenuCanceled(PopupMenuEvent e) { }
        });

        add(campoFecha, BorderLayout.CENTER);
        add(btnCalendario, BorderLayout.EAST);
    }

    /**
     * Muestra el calendario sólo si el componente está habilitado.
     */
    private void mostrarCalendario() {
        if (!isEnabled()) {
            return;
        }
        calendario.setPreferredSize(new Dimension(430, 350));
        popup.pack();
        popup.show(this, 0, getHeight());
    }

    /** Devuelve la fecha actualmente seleccionada, o null si no hay ninguna */
    public LocalDate getDate() {
        String txt = campoFecha.getText();
        if (txt == null || txt.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(txt);
        } catch (Exception ex) {
            return null;
        }
    }

    /** Establece la fecha visible y seleccionada en el calendario */
    public void setDate(LocalDate fecha) {
        calendario.setSelectedDate(fecha);
        campoFecha.setText(fecha != null ? fecha.toString() : "");
        for (Consumer<LocalDate> listener : dateChangeListeners) {
            listener.accept(fecha);
        }
    }

    /** Permite bloquear fines de semana (delegado a CalendarioUDLAP) */
    public void setBlockWeekends(boolean block) {
        calendario.setBlockWeekends(block);
    }

    /** Retorna el JTextField interno */
    public JTextField getTextField() {
        return campoFecha;
    }

    /**
     * Registra un listener que se invocará cada vez que cambie la fecha.
     * @param listener callback que recibe la nueva LocalDate (o null)
     */
    public void addDateChangeListener(Consumer<LocalDate> listener) {
        dateChangeListeners.add(listener);
    }

    /**
     * Al deshabilitar/el habilitar este DatePicker,
     * propagamos ese estado al campo de texto y al botón.
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        campoFecha.setEnabled(enabled);
        btnCalendario.setEnabled(enabled);
    }
}
