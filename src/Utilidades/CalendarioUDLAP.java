package Utilidades;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.function.Consumer;

public class CalendarioUDLAP extends JPanel {
    private int year, month;
    private LocalDate selectedDate = null;
    private BotonUDLAP[][] dayButtons = new BotonUDLAP[6][7];
    private JLabel lblMonth, lblYear;
    private Consumer<LocalDate> dateSelectionListener;
    private BotonUDLAP btnHoy, btnLimpiar;
    private boolean blockWeekends = false;

    public CalendarioUDLAP() {
        this(false);
    }

    /**
     * @param blockWeekends si true, los fines de semana quedan deshabilitados
     */
    public CalendarioUDLAP(boolean blockWeekends) {
        this.blockWeekends = blockWeekends;
        LocalDate now = LocalDate.now();
        this.year = now.getYear();
        this.month = now.getMonthValue();

        setLayout(new BorderLayout());
        setBackground(ColoresUDLAP.BLANCO);
        setPreferredSize(new Dimension(300, 250));
        setMinimumSize(new Dimension(300, 260));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createCalendarPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
        updateCalendar();
    }

    /**
     * Permite habilitar o deshabilitar bloqueos de fin de semana en tiempo de
     * ejecución
     */
    public void setBlockWeekends(boolean block) {
        this.blockWeekends = block;
        updateCalendar();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ColoresUDLAP.BLANCO);
        GridBagConstraints c = new GridBagConstraints();

        BotonUDLAP prevYear = createFlechaButton("<<");
        BotonUDLAP prevMonth = createFlechaButton("<");
        BotonUDLAP nextMonth = createFlechaButton(">");
        BotonUDLAP nextYear = createFlechaButton(">>");
        prevYear.addActionListener(e -> {
            year--;
            updateCalendar();
        });
        prevMonth.addActionListener(e -> previousMonth());
        nextMonth.addActionListener(e -> nextMonth());
        nextYear.addActionListener(e -> {
            year++;
            updateCalendar();
        });

        lblMonth = new JLabel("", SwingConstants.CENTER);
        lblMonth.setForeground(ColoresUDLAP.NARANJA_BARRA);
        lblMonth.setFont(new Font("Arial", Font.BOLD, 15));
        lblYear = new JLabel("", SwingConstants.CENTER);
        lblYear.setForeground(ColoresUDLAP.NARANJA_BARRA);
        lblYear.setFont(new Font("Arial", Font.BOLD, 15));

        c.insets = new Insets(2, 3, 2, 3);
        c.gridy = 0;
        c.gridx = 0;
        panel.add(prevYear, c);
        c.gridx = 1;
        panel.add(prevMonth, c);

        c.gridx = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(lblMonth);
        centerPanel.add(lblYear);
        panel.add(centerPanel, c);

        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 3;
        panel.add(nextMonth, c);
        c.gridx = 4;
        panel.add(nextYear, c);

        return panel;
    }

    private BotonUDLAP createFlechaButton(String text) {
        return new BotonUDLAP(
                text,
                ColoresUDLAP.BLANCO,
                ColoresUDLAP.NARANJA_HOVER,
                ColoresUDLAP.NARANJA_BARRA,
                ColoresUDLAP.BLANCO,
                new Font("Arial", Font.BOLD, 17), 20) {
            {
                setPreferredSize(new Dimension(36, 30));
                setMaximumSize(new Dimension(36, 30));
                setMinimumSize(new Dimension(30, 30));
                setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
            }
        };
    }

    private JPanel createCalendarPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 7));
        panel.setBackground(ColoresUDLAP.BLANCO);
        panel.setPreferredSize(new Dimension(430, 230));

        String[] days = { "Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa" };
        for (String day : days) {
            JLabel lbl = new JLabel(day, SwingConstants.CENTER);
            lbl.setForeground(ColoresUDLAP.VERDE_OSCURO);
            lbl.setFont(new Font("Arial", Font.BOLD, 12));
            lbl.setOpaque(true);
            lbl.setBackground(ColoresUDLAP.BLANCO);
            panel.add(lbl);
        }
        Font diaFont = new Font("Arial", Font.BOLD, 14);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                BotonUDLAP btn = new BotonUDLAP("", ColoresUDLAP.BLANCO, ColoresUDLAP.VERDE_HOVER,
                        ColoresUDLAP.NEGRO, ColoresUDLAP.BLANCO, diaFont, 18);
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createLineBorder(ColoresUDLAP.BLANCO));
                btn.setPreferredSize(new Dimension(48, 36));
                int row = i, col = j;
                btn.addActionListener(e -> dayClicked(row, col));
                dayButtons[i][j] = btn;
                panel.add(btn);
            }
        }
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        panel.setBackground(ColoresUDLAP.BLANCO);

        btnHoy = new BotonUDLAP("Hoy", ColoresUDLAP.VERDE_SOLIDO, ColoresUDLAP.VERDE_HOVER,
                ColoresUDLAP.BLANCO, ColoresUDLAP.BLANCO, new Font("Arial", Font.BOLD, 13), 22);
        btnHoy.setFocusPainted(false);
        btnHoy.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));
        btnHoy.addActionListener(e -> {
            LocalDate hoy = LocalDate.now();
            selectedDate = hoy;
            year = hoy.getYear();
            month = hoy.getMonthValue();
            if (dateSelectionListener != null)
                dateSelectionListener.accept(selectedDate);
            updateCalendar();
        });

        btnLimpiar = new BotonUDLAP("Limpiar", ColoresUDLAP.ROJO_SOLIDO, ColoresUDLAP.ROJO_HOVER,
                ColoresUDLAP.BLANCO, ColoresUDLAP.BLANCO, new Font("Arial", Font.BOLD, 13), 22);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));
        btnLimpiar.addActionListener(e -> {
            selectedDate = null;
            if (dateSelectionListener != null)
                dateSelectionListener.accept(null);
            updateCalendar();
        });

        panel.add(btnHoy);
        panel.add(btnLimpiar);
        return panel;
    }

    private void updateCalendar() {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        int startDay = firstDayOfMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = yearMonth.lengthOfMonth();

        lblMonth.setText(getMonthName(month));
        lblYear.setText(String.valueOf(year));

        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 7; j++)
                dayButtons[i][j].setDia(null, false, false, false, false);

        int dayCounter = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if ((i == 0 && j < startDay) || dayCounter > daysInMonth)
                    continue;
                BotonUDLAP btn = dayButtons[i][j];
                LocalDate btnDate = LocalDate.of(year, month, dayCounter);
                boolean esHoy = btnDate.equals(LocalDate.now());
                boolean esSeleccionado = selectedDate != null && btnDate.equals(selectedDate);
                boolean esFinDeSemana = (j == 0 || j == 6);
                boolean habil = !blockWeekends || !esFinDeSemana;
                btn.setDia(dayCounter, esHoy, esSeleccionado, esFinDeSemana, habil);
                dayCounter++;
            }
        }
    }

    private void dayClicked(int row, int col) {
        BotonUDLAP btn = dayButtons[row][col];
        if (!btn.isDiaHabil())
            return;
        if (btn.getNumeroDia() == null)
            return;
        selectedDate = LocalDate.of(year, month, btn.getNumeroDia());
        if (dateSelectionListener != null)
            dateSelectionListener.accept(selectedDate);
        updateCalendar();
    }

    private void previousMonth() {
        month--;
        if (month < 1) {
            month = 12;
            year--;
        }
        updateCalendar();
    }

    private void nextMonth() {
        month++;
        if (month > 12) {
            month = 1;
            year++;
        }
        updateCalendar();
    }

    private String getMonthName(int month) {
        String[] meses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" };
        return meses[month - 1];
    }

    public void setDateSelectionListener(Consumer<LocalDate> listener) {
        this.dateSelectionListener = listener;
    }

    public void setSelectedDate(LocalDate date) {
        this.selectedDate = date;
        if (date != null) {
            year = date.getYear();
            month = date.getMonthValue();
        }
        updateCalendar();
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    private static class BotonUDLAP extends JButton {
        private final Color baseColor, hoverColor, fgBase, fgHover;
        private Integer numeroDia;
        private boolean esHoy, esSeleccionado, esFinDeSemana, habil;
        private final Font fuente;
        private final int arc;

        public BotonUDLAP(String texto, Color base, Color hover, Color fgBase, Color fgHover, Font font, int arc) {
            super(texto);
            this.baseColor = base;
            this.hoverColor = hover;
            this.fgBase = fgBase;
            this.fgHover = fgHover;
            this.fuente = font;
            this.arc = arc;
            setContentAreaFilled(false);
            setOpaque(false);
            setBorderPainted(true);
            setFocusPainted(false);
            setFont(font);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setForeground(fgBase);
        }

        public void setDia(Integer dia, boolean hoy, boolean seleccionado, boolean finDeSemana, boolean habilitado) {
            this.numeroDia = dia;
            this.esHoy = hoy;
            this.esSeleccionado = seleccionado;
            this.esFinDeSemana = finDeSemana;
            this.habil = habilitado;
            setText(dia == null ? "" : String.valueOf(dia));
            setEnabled(habil);
            repaint();
        }

        public Integer getNumeroDia() {
            return numeroDia;
        }

        public boolean isDiaHabil() {
            return habil;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color colorFondo = baseColor;
            Color colorLetra = fgBase;

            if (esSeleccionado) {
                colorFondo = ColoresUDLAP.NARANJA_BARRA;
                colorLetra = ColoresUDLAP.BLANCO;
            } else if (esHoy) {
                colorFondo = ColoresUDLAP.NARANJA;
                colorLetra = ColoresUDLAP.NEGRO;
            } else if (getModel().isRollover() && isEnabled()) {
                colorFondo = hoverColor;
                colorLetra = fgHover;
            }

            g2.setColor(colorFondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            setForeground(colorLetra);
            setFont(fuente);
            super.paintComponent(g);
            g2.dispose();
        }
    }
}
