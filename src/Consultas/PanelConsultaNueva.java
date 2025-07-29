package Consultas;

import Utilidades.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Panel para generar una nueva consulta médica.
 * – Hereda la maqueta general de FormularioMedicoBase
 * – Reutiliza PanelBotonesFormulario para la barra inferior
 */
public class PanelConsultaNueva extends FormularioMedicoBase implements PanelProvider {

    /** Barra de botones reutilizable */
    private final PanelBotonesFormulario botones;
    private final MensajeErrorUDLAP mensajeInline;

    public PanelConsultaNueva(int idMedico, String nombreMedico) {
        super("Consulta Médica – Dr. " + nombreMedico, new String[] {
                "ID Paciente:", // 0
                "Nombre", // 1 ← solo lectura
                "Correo", // 2 ← solo lectura
                "Edad", // 3
                "Altura", // 4
                "Peso", // 5
                "Medicamentos Actuales", // 6
                "Síntomas", // 7 ← JTextArea
                "Medicamentos recetados", // 8 ← JTextArea
                "Diagnóstico", // 9 ← JTextArea
                "Fecha Consulta", // 10
                "Receta Médica" // 11 ← JTextArea
        });

        /* ─────────────────── 1. FECHA POR DEFECTO ────────────────────── */
        JTextField txtFecha = (JTextField) campos[10];
        txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        /* ─────────────────── 2. REFERENCIAS CLARAS ───────────────────── */
        JTextField txtId = (JTextField) campos[0];
        JTextField txtNombre = (JTextField) campos[1];
        JTextField txtCorreo = (JTextField) campos[2];
        JTextField txtEdad = (JTextField) campos[3];
        JTextField txtAltura = (JTextField) campos[4];
        JTextField txtPeso = (JTextField) campos[5];
        JTextField txtMedica = (JTextField) campos[6];

        JTextArea taSintomas = (JTextArea) campos[7];
        JTextArea taMedRec = (JTextArea) campos[8];
        JTextArea taDiag = (JTextArea) campos[9];
        JTextArea taReceta = (JTextArea) campos[11];

        /* ──────────────── 2.1. Mensaje de error inline ───────────────── */
        mensajeInline = new MensajeErrorUDLAP();

        /* ─────────────────── 3. BARRA DE BOTONES ─────────────────────── */
        botones = new PanelBotonesFormulario(
                new PanelBotonesFormulario.BotonConfig("Guardar", PanelBotonesFormulario.BotonConfig.Tipo.PRIMARY),
                new PanelBotonesFormulario.BotonConfig("Buscar", PanelBotonesFormulario.BotonConfig.Tipo.SECONDARY),
                new PanelBotonesFormulario.BotonConfig("Limpiar", PanelBotonesFormulario.BotonConfig.Tipo.DANGER));

        // Añadir mensaje de error inline en un panel nuevo
        JPanel panelSur = new JPanel(new BorderLayout());
        JPanel mensajeContenedor = new JPanel(new FlowLayout());
        mensajeContenedor.setBackground(ColoresUDLAP.BLANCO);
        mensajeContenedor.add(mensajeInline);

        panelSur.setBackground(ColoresUDLAP.BLANCO);
        panelSur.add(mensajeContenedor, BorderLayout.CENTER);
        panelSur.add(botones, BorderLayout.SOUTH);
        add(panelSur, BorderLayout.SOUTH);

        Window owner = SwingUtilities.getWindowAncestor(this);

        /* Acción Buscar: ahora SOLO por ID y rellena Edad-Altura-Peso-Medicación */
        BuscarPaciente buscarAccion = new BuscarPaciente(new JTextField[] {
                txtId, txtNombre, txtCorreo, txtEdad, txtAltura, txtPeso, txtMedica
        }, mensajeInline, owner);

        /* ────────── 4. LISTENERS: se registran en bloque ─────────────── */
        botones.setListeners(
                new GuardarConsulta(
                        idMedico, // ← se envía el ID del médico de la sesión
                        txtId, // ID Paciente
                        txtEdad, txtAltura, txtPeso,
                        txtMedica,
                        taSintomas, taMedRec, taDiag,
                        txtFecha, taReceta,
                        mensajeInline),
                buscarAccion,
                e -> {
                    limpiarCampos();
                    mensajeInline.limpiar(); // Limpiar mensajes previos
                });

        /* ─────────────────── 5. CAMPOS SOLO LECTURA ──────────────────── */
        txtNombre.setEditable(false);
        txtCorreo.setEditable(false);

        /* ───── 6. BÚSQUEDA AUTOMÁTICA AL PERDER FOCO EL ID ───── */
        txtId.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!txtId.getText().trim().isEmpty())
                    buscarAccion.actionPerformed(
                            new ActionEvent(txtId, ActionEvent.ACTION_PERFORMED, "autoBuscar"));
            }
        });
    }

    /* ───────────── Indices que renderizan JTextArea ───────────── */
    @Override
    protected boolean isTextArea(int idx) {
        return idx == 7 || idx == 8 || idx == 9 || idx == 11;
    }

    /* Implementación de PanelProvider */
    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public String getPanelName() {
        return "consultaNueva";
    }
}
