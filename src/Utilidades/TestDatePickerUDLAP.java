package Utilidades;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import java.awt.FlowLayout;

public class TestDatePickerUDLAP {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Demo DatePicker UDLAP");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());
            frame.setSize(340, 100);

            DatePickerUDLAP picker = new DatePickerUDLAP();
            frame.add(picker);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
