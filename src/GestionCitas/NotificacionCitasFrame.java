package GestionCitas;

import Utilidades.ColoresUDLAP;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificacionCitasFrame extends JFrame {
    private JTextArea mensajeNotificacion;
    private JButton aceptarCitaButton, rechazarCitaButton;
    private JLabel estadoLabel;

    private final String fecha;
    private final String hora;
    private final String servicio;
    private final String idPaciente;

    public NotificacionCitasFrame(String fecha, String hora, String servicio, String idPaciente) {
        this.fecha = fecha;
        this.hora = hora;
        this.servicio = servicio;
        this.idPaciente = idPaciente;

        setTitle("Notificación de Cita Disponible");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(ColoresUDLAP.BLANCO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mensajeNotificacion = new JTextArea("Se ha liberado una cita:\n"
                + "Fecha: " + fecha + "\n"
                + "Hora: " + hora + "\n"
                + "Servicio: " + servicio + "\n\n"
                + "¿Desea agendar esta cita?");
        mensajeNotificacion.setEditable(false);
        mensajeNotificacion.setLineWrap(true);
        mensajeNotificacion.setWrapStyleWord(true);
        mensajeNotificacion.setFont(new Font("Arial", Font.PLAIN, 14));
        mensajeNotificacion.setBackground(ColoresUDLAP.BLANCO);
        add(mensajeNotificacion, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        aceptarCitaButton = botonTransparente("Aceptar", ColoresUDLAP.VERDE_SOLIDO, ColoresUDLAP.VERDE_HOVER);
        add(aceptarCitaButton, gbc);

        gbc.gridx = 1;
        rechazarCitaButton = botonTransparente("Rechazar", ColoresUDLAP.NARANJA_SOLIDO, ColoresUDLAP.NARANJA_HOVER);
        add(rechazarCitaButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        estadoLabel = new JLabel("", SwingConstants.CENTER);
        estadoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        estadoLabel.setForeground(Color.BLUE);
        add(estadoLabel, gbc);

        aceptarCitaButton.addActionListener(e -> aceptarCita());
        rechazarCitaButton.addActionListener(e -> rechazarCita());

        setVisible(true);
    }

    private JButton botonTransparente(String texto, Color base, Color hover) {
        JButton button = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void aceptarCita() {
        final int MAX_REINTENTOS = 3;
        int intentos = 0;
        boolean exito = false;

        while (intentos < MAX_REINTENTOS && !exito) {
            try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
                conn.setAutoCommit(false);

                String sqlInsert = "INSERT INTO CitasMedicas(idPaciente, fecha, hora, servicio) VALUES(?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(sqlInsert);
                stmt.setString(1, idPaciente);
                stmt.setString(2, fecha);
                stmt.setString(3, hora);
                stmt.setString(4, servicio);
                stmt.executeUpdate();

                String sqlDelEspera = "DELETE FROM ListaEsperaCitas WHERE idPaciente = ? AND fechaDeseada = ? AND horaDeseada = ? AND servicio = ?";
                PreparedStatement delStmt = conn.prepareStatement(sqlDelEspera);
                delStmt.setString(1, idPaciente);
                delStmt.setString(2, fecha);
                delStmt.setString(3, hora);
                delStmt.setString(4, servicio);
                delStmt.executeUpdate();

                String sqlNotif = "UPDATE Notificaciones SET estado = 'atendida' WHERE idPaciente = ? AND fecha = ? AND hora = ? AND servicio = ?";
                PreparedStatement upStmt = conn.prepareStatement(sqlNotif);
                upStmt.setString(1, idPaciente);
                upStmt.setString(2, fecha);
                upStmt.setString(3, hora);
                upStmt.setString(4, servicio);
                upStmt.executeUpdate();

                conn.commit();

                estadoLabel.setForeground(new Color(0, 128, 0));
                estadoLabel.setText("Cita agendada exitosamente.");
                aceptarCitaButton.setEnabled(false);
                rechazarCitaButton.setEnabled(false);
                exito = true;

            } catch (SQLException ex) {
                if (ex.getMessage().contains("database is locked")) {
                    intentos++;
                    estadoLabel.setForeground(Color.RED);
                    estadoLabel.setText("Reintentando... (" + intentos + "/" + MAX_REINTENTOS + ")");
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    estadoLabel.setForeground(Color.RED);
                    estadoLabel.setText("Error al agendar: " + ex.getMessage());
                    break;
                }
            }
        }

        if (!exito) {
            estadoLabel.setForeground(Color.RED);
            estadoLabel.setText("No se pudo agendar la cita. Intenta más tarde.");
        }
    }

    private void rechazarCita() {
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            String sql = "UPDATE Notificaciones SET estado = 'rechazada' WHERE idPaciente = ? AND fecha = ? AND hora = ? AND servicio = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPaciente);
            stmt.setString(2, fecha);
            stmt.setString(3, hora);
            stmt.setString(4, servicio);
            stmt.executeUpdate();

            estadoLabel.setText("Has rechazado esta cita.");
            aceptarCitaButton.setEnabled(false);
            rechazarCitaButton.setEnabled(false);
        } catch (SQLException ex) {
            estadoLabel.setForeground(Color.RED);
            estadoLabel.setText("Error al rechazar: " + ex.getMessage());
        }
    }
}
