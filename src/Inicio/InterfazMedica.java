package Inicio;

import Utilidades.*;
import BaseDeDatos.ConexionSQLite;
import Consultas.PanelConsultaNueva;
import Emergencias.PanelLlamadaEmergencia;
import Emergencias.PanelReportarEmergencia;
import GestionCitas.NotificacionDAO;
import GestionCitas.PanelGestionCitas;
import GestionCitas.AgendaCitaFrame;
import GestionCitas.ModificarCitaFrame;
import GestionCitas.NotificacionCitasFrame;
import GestionEnfermedades.PanelHistorialMedico;
import GestionEnfermedades.PanelHistorialMedicoEditable;
import Justificantes.PanelJustificantesProvider;
import Justificantes.PanelMenuJustificantes;
import Justificantes.PanelJustificantesPacienteMenu;
import Registro.PanelRegistroPaciente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;
import java.util.List;

public class InterfazMedica extends JFrame {
    private JPanel contentPanel;
    private final boolean esMedico;
    private final int userId;
    private String nombreUsuario;
    private PanelManager panelManager;
    private JLabel notificationIcon;
    private ImageIcon iconDefault, iconNew;
    private boolean hasNewNotification = false;

    public InterfazMedica(boolean esMedico, int userId) {
        this.esMedico = esMedico;
        this.userId = userId;
        this.nombreUsuario = fetchNombreUsuario();
        loadNotificationIcons();
        initUI();
        checkNotifications();
    }

    private void loadNotificationIcons() {
        try {
            URL u1 = getClass().getResource("/icons/bell.png");
            URL u2 = getClass().getResource("/icons/bell_new.png");
            iconDefault = new ImageIcon(new ImageIcon(u1).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
            iconNew = new ImageIcon(new ImageIcon(u2).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            iconDefault = new ImageIcon();
            iconNew = new ImageIcon();
        }
    }

    private void checkNotifications() {
        if (!esMedico) {
            hasNewNotification = NotificacionDAO.tieneNotificacionesNoLeidas(userId);
            System.out.println("¿Tiene notificaciones pendientes? " + hasNewNotification);
        }

        if (notificationIcon != null) {
            notificationIcon.setIcon(hasNewNotification ? iconNew : iconDefault);
        }
    }

    private void mostrarNotificaciones() {
        if (esMedico) {
            JOptionPane.showMessageDialog(this,
                    "Funcionalidad de notificaciones para médicos en desarrollo.",
                    "Notificaciones Médicas",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<NotificacionDAO.Notificacion> lista = NotificacionDAO.obtenerNotificaciones(userId);
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay nuevas notificaciones.",
                    "Notificaciones", JOptionPane.INFORMATION_MESSAGE);
            notificationIcon.setIcon(iconDefault);
        } else {
            for (NotificacionDAO.Notificacion n : lista) {
                new NotificacionCitasFrame(n.fecha, n.hora, n.servicio, String.valueOf(userId));
            }
            hasNewNotification = false;
            notificationIcon.setIcon(iconDefault);
        }
    }

    private void initUI() {
        setUndecorated(true);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel contenedorBarras = new JPanel();
        contenedorBarras.setLayout(new BoxLayout(contenedorBarras, BoxLayout.Y_AXIS));
        contenedorBarras.add(new BarraVentanaUDLAP(this));
        contenedorBarras.add(crearTopPanel());
        add(contenedorBarras, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColoresUDLAP.BLANCO);
        add(mainPanel, BorderLayout.CENTER);

        JPanel menuPanel = crearMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.WEST);

        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(Color.WHITE);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        panelManager = new PanelManager(contentPanel);
        registrarPaneles();
        panelManager.showPanel("panel0");
    }

private JPanel crearMenuPanel() {
        JPanel menu = new JPanel();
        menu.setBackground(ColoresUDLAP.BLANCO);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(15, 8, 8, 8));

        String[] items = esMedico
                ? new String[] { "Registrar Paciente", "Consulta Nueva", "Historial Médico", "Justificantes",
                        "Emergencias", "Accidente" }
                : new String[] { "Mis Citas", "Historial Médico", "Justificantes", "Reportar Emergencia" };

        Font btnFont = new Font("Arial", Font.BOLD, 20);

        for (int i = 0; i < items.length; i++) {
            String textoHtml = "<html><div style='text-align:center;'>"
                    + items[i].replace("\n", "<br>")
                    + "</div></html>";

            // Determinamos color base y hover
            Color baseColor;
            Color hoverColor;

            // Si es la opción de reportar emergencia (usuarios)
            if (!esMedico && "Reportar Emergencia".equals(items[i])) {
                baseColor = ColoresUDLAP.ROJO;
                hoverColor = ColoresUDLAP.ROJO_HOVER;
            }

            else {
                boolean par = (i % 2 == 0);
                baseColor = par ? ColoresUDLAP.VERDE : ColoresUDLAP.NARANJA;
                hoverColor = par ? ColoresUDLAP.VERDE_HOVER : ColoresUDLAP.NARANJA_HOVER;
            }

            JButton boton = botonTransparente(textoHtml, baseColor, hoverColor, btnFont);
            int idx = i;
            boton.addActionListener(e -> manejarClick(idx));
            boton.setMaximumSize(new Dimension(260, 80));

            menu.add(boton);
            menu.add(Box.createVerticalStrut(10));
        }

        return menu;
    }

    private void manejarClick(int idx) {
        String[] medicoKeys = { "formularioRegistro", "consultaNueva", "historialMedico", "justificantes",
                "llamadaEmergencia", "reporteAccidente" };
        String[] pacienteKeys = { "panelGestionCitas", "historialMedico", "justificantesPaciente",
                "reportarEmergencia" };
        String key = esMedico ? medicoKeys[idx] : pacienteKeys[idx];
        panelManager.showPanel(key);
    }

    // Fragmento de InterfazMedica.java con registrarPaneles() actualizado para usar
    // PanelHistorialMedicoEditable

    private void registrarPaneles() {
        if (esMedico) {
            panelManager.registerPanel(new PanelRegistroPaciente());
            panelManager.registerPanel(new PanelConsultaNueva(userId, nombreUsuario));

            // Mostrar historial médico editable con campo ID fijo
            panelManager.registerPanel(new PanelProvider() {
                public JPanel getPanel() {
                    return new PanelHistorialMedicoEditable();
                }

                public String getPanelName() {
                    return "historialMedico";
                }
            });

            panelManager.registerPanel(new PanelProvider() {
                public JPanel getPanel() {
                    return new PanelMenuJustificantes(panelManager); // ✅ se pasa el PanelManager
                }
                public String getPanelName() {
                    return "justificantes";
                }
            });


            panelManager.registerPanel(new PanelLlamadaEmergencia(esMedico, userId));

           panelManager.registerPanel(new PanelProvider() {
    public JPanel getPanel() {
        return new Emergencias.FormularioAccidenteCompleto();  // Usa tu nueva clase aquí
    }

    public String getPanelName() {
        return "reporteAccidente";
    }
});


        } else {
            // Paneles para el paciente (sin cambios)
            panelManager.registerPanel(new PanelHistorialMedico(userId));

            panelManager.registerPanel(new PanelProvider() {
                public JPanel getPanel() {
                    return new PanelGestionCitas(userId, panelManager);
                }

                public String getPanelName() {
                    return "panelGestionCitas";
                }
            });

            panelManager.registerPanel(new PanelProvider() {
            public JPanel getPanel() {
                return new PanelJustificantesPacienteMenu(panelManager);
            }

            public String getPanelName() {
                return "justificantesPaciente";
            }
        });


            panelManager.registerPanel(new PanelReportarEmergencia());

            panelManager.registerPanel(new PanelProvider() {
                public JPanel getPanel() {
                    return new AgendaCitaFrame(userId, panelManager);
                }

                public String getPanelName() {
                    return "agendarCita";
                }
            });

            panelManager.registerPanel(new PanelProvider() {
                public JPanel getPanel() {
                    return new ModificarCitaFrame(userId, panelManager);
                }

                public String getPanelName() {
                    return "modificarCita";
                }
            });
        }
    }

    private JPanel crearTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColoresUDLAP.BLANCO);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        panel.setPreferredSize(new Dimension(0, 56));

        JLabel saludo = new JLabel("Hola, " + nombreUsuario);
        saludo.setForeground(ColoresUDLAP.VERDE_OSCURO);
        saludo.setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 12));
        left.setOpaque(false);
        left.add(saludo);

        JLabel titulo = new JLabel(
                "<html><span style='font-size:25pt;font-weight:bold;color:#006400;'>Servicios Médicos</span> <span style='font-size:25pt;font-weight:bold;color:#FF6600;'>UDLAP</span></html>",
                SwingConstants.CENTER);
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        center.setOpaque(false);
        center.add(titulo);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        right.setOpaque(false);

        notificationIcon = new JLabel(iconDefault);
        notificationIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        notificationIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarNotificaciones();
            }
        });
        right.add(notificationIcon);

        JButton cerrarSesion = new JButton("Cerrar sesión");
        cerrarSesion.setFont(new Font("Arial", Font.BOLD, 15));
        cerrarSesion.setBackground(ColoresUDLAP.NARANJA_BARRA);
        cerrarSesion.setForeground(Color.WHITE);
        cerrarSesion.setFocusPainted(false);
        cerrarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cerrarSesion.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        cerrarSesion.addActionListener(e -> {
            new InterfazLogin().setVisible(true);
            dispose();
        });
        right.add(cerrarSesion);

        panel.add(left, BorderLayout.WEST);
        panel.add(center, BorderLayout.CENTER);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    private JButton botonTransparente(String texto, Color base, Color hover, Font font) {
        JButton button = new JButton(texto) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hover : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private String fetchNombreUsuario() {
        String sql = esMedico
                ? "SELECT Nombre||' '||ApellidoPaterno FROM InformacionMedico WHERE ID=?"
                : "SELECT Nombre||' '||ApellidoPaterno FROM InformacionAlumno WHERE ID=?";
        try (Connection con = ConexionSQLite.conectar();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getString(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "Usuario";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazMedica(true, 1).setVisible(true));
    }
}
