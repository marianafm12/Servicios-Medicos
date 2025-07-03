package GestionEnfermedades;

import Utilidades.*;
import BaseDeDatos.PacienteDB;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FormularioGestionEnfermedades extends FormularioMedicoBase {

    private final JTextArea txtEnfermedades;
    private final JTextArea txtAlergias;
    private final JTextArea txtMedicacion;
    private final PanelBotonesFormulario botones;
    public boolean esMedico;
    private final int idUsuario;

    public FormularioGestionEnfermedades(boolean esMedico, int idUsuario) {
        super("Gestión de Enfermedades del Paciente", new String[] {
                "ID Paciente:", "Nombre del Paciente:",
                "Enfermedades Preexistentes:", "Alergias:", "Medicación:"
        });

        this.esMedico = esMedico;
        this.idUsuario = idUsuario;

        JTextField txtID = (JTextField) campos[0];
        JTextField txtNombre = (JTextField) campos[1];
        txtNombre.setEditable(false);

        if (!esMedico) {
            txtID.setText(String.valueOf(idUsuario));
            txtID.setEditable(false);
            txtNombre.setText(PacienteDB.obtenerNombrePorID(txtID.getText()));
        }

        txtID.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (esMedico) {
                    String id = txtID.getText().trim();
                    String nombre = PacienteDB.obtenerNombrePorID(id);
                    txtNombre.setText(nombre != null ? nombre : "");
                }
            }
        });

        txtEnfermedades = new JTextArea(3, 25);
        txtAlergias = new JTextArea(3, 25);
        txtMedicacion = new JTextArea(3, 25);

        campos[2] = new JScrollPane(txtEnfermedades);
        campos[3] = new JScrollPane(txtAlergias);
        campos[4] = new JScrollPane(txtMedicacion);

        if (!esMedico) {
            txtEnfermedades.setEditable(false);
            txtAlergias.setEditable(false);
            txtMedicacion.setEditable(false);
        }

        botones = new PanelBotonesFormulario(
                new PanelBotonesFormulario.BotonConfig("Guardar", PanelBotonesFormulario.BotonConfig.Tipo.PRIMARY),
                new PanelBotonesFormulario.BotonConfig("Buscar", PanelBotonesFormulario.BotonConfig.Tipo.SECONDARY),
                new PanelBotonesFormulario.BotonConfig("Limpiar", PanelBotonesFormulario.BotonConfig.Tipo.DANGER));

        add(botones, BorderLayout.SOUTH);

        if (!esMedico) {
            botones.getBotones().get(0).setEnabled(false); // Guardar
            botones.getBotones().get(2).setEnabled(false); // Limpiar
        }

        botones.setListeners(
                e -> guardarDatos(),
                e -> buscarDatos(),
                e -> limpiarCampos());

        // Cargar datos automáticamente si es paciente
        if (!esMedico)
            buscarDatos();
    }

    private void guardarDatos() {
        if (!ValidadorEnfermedades.validar(
                (JTextField) campos[0], txtEnfermedades, txtAlergias, txtMedicacion))
            return;

        boolean exito = EnfermedadesDB.guardarEnfermedades(
                ((JTextField) campos[0]).getText().trim(),
                txtEnfermedades.getText().trim(),
                txtAlergias.getText().trim(),
                txtMedicacion.getText().trim());

        if (exito) {
            JOptionPane.showMessageDialog(this, "Información médica guardada correctamente.");
            limpiarCampos();
        }
    }

    private void buscarDatos() {
        String id = ((JTextField) campos[0]).getText().trim();
        EnfermedadPaciente paciente = EnfermedadesDB.buscarPorID(id);
        if (paciente != null) {
            txtEnfermedades.setText(paciente.enfermedades());
            txtAlergias.setText(paciente.alergias());
            txtMedicacion.setText(paciente.medicacion());
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron datos para ese ID.");
        }
    }
}
