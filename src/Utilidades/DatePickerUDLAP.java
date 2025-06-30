package Utilidades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class DatePickerUDLAP extends JPanel {
        private JTextField txtFecha;
        private JButton btnCalendar;
        private CalendarioUDLAP calendario;
        private JPopupMenu popup;
        private LocalDate selectedDate;

        public DatePickerUDLAP() {
                setLayout(new BorderLayout(0, 0));
                txtFecha = new JTextField(10);
                txtFecha.setEditable(false);
                txtFecha.setBackground(Color.WHITE);
                txtFecha.setFont(new Font("Arial", Font.PLAIN, 14));

                btnCalendar = new JButton("📅");
                btnCalendar.setFocusable(false);
                btnCalendar.setMargin(new Insets(0, 6, 0, 6));
                btnCalendar.setFont(new Font("Arial", Font.PLAIN, 16));
                btnCalendar.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

                calendario = new CalendarioUDLAP();
                calendario.setOpaque(true);

                popup = new JPopupMenu();
                popup.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
                popup.setLayout(new BorderLayout());
                popup.add(calendario, BorderLayout.CENTER);

                add(txtFecha, BorderLayout.CENTER);
                add(btnCalendar, BorderLayout.EAST);

                // Mostrar calendario al hacer click en el botón o campo de texto
                btnCalendar.addActionListener(e -> mostrarCalendario());
                txtFecha.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                                mostrarCalendario();
                        }
                });

                // Listener de selección de fecha (incluye limpiar y hoy)
                calendario.setDateSelectionListener(date -> {
                        selectedDate = date;
                        if (date == null) {
                                txtFecha.setText("");
                        } else {
                                txtFecha.setText(formato(date));
                        }
                        popup.setVisible(false);
                });

                // Cerrar popup solo si se hace click FUERA del calendario y del DatePicker
                Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
                        if (popup.isVisible() && event instanceof MouseEvent) {
                                MouseEvent me = (MouseEvent) event;
                                if (me.getID() == MouseEvent.MOUSE_PRESSED) {
                                        Component comp = me.getComponent();
                                        boolean esPopup = SwingUtilities.isDescendingFrom(comp, popup);
                                        boolean esBoton = SwingUtilities.isDescendingFrom(comp, btnCalendar) ||
                                                        SwingUtilities.isDescendingFrom(comp, txtFecha);
                                        if (!esPopup && !esBoton) {
                                                popup.setVisible(false);
                                        }
                                }
                        }
                }, AWTEvent.MOUSE_EVENT_MASK);
        }

        /** Muestra el calendario debajo del campo de texto */
        private void mostrarCalendario() {
                if (selectedDate != null) {
                        calendario.setSelectedDate(selectedDate);
                } else {
                        calendario.setSelectedDate(LocalDate.now());
                }
                popup.show(this, 0, getHeight());
        }

        /** Formato de fecha (puedes personalizarlo) */
        private String formato(LocalDate fecha) {
                if (fecha == null)
                        return "";
                return String.format("%02d/%02d/%04d", fecha.getDayOfMonth(), fecha.getMonthValue(), fecha.getYear());
        }

        public LocalDate getSelectedDate() {
                return selectedDate;
        }

        /** Si necesitas establecer fecha desde fuera */
        public void setSelectedDate(LocalDate date) {
                this.selectedDate = date;
                calendario.setSelectedDate(date);
                txtFecha.setText(formato(date));
        }

        /** Si quieres saber si está abierto el calendario */
        public boolean isCalendarVisible() {
                return popup.isVisible();
        }
}
