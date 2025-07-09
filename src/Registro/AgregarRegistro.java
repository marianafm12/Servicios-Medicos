package Registro;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

import Utilidades.*;
import java.awt.Window;

public class AgregarRegistro implements ActionListener {
    private final JTextField[] campos;
    private final MensajeErrorUDLAP mensajeInline;
    private final Window owner;

    public AgregarRegistro(JTextField[] campos, MensajeErrorUDLAP mensajeInline, Window owner) {
        this.campos = campos;
        this.mensajeInline = mensajeInline;
        this.owner = owner;

        // Se limpia cualquier mensaje previo
        this.mensajeInline.limpiar();

        FocusAdapter cargaListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                cargarDatosPersonales();
            }
        };

        for (int i = 0; i <= 4; i++) {
            campos[i].addFocusListener(cargaListener);
        }
    }

    private void cargarDatosPersonales() {
        String idText = campos[0].getText().trim();
        String nom = campos[1].getText().trim();
        String apPat = campos[2].getText().trim();
        String apMat = campos[3].getText().trim();
        String correo = campos[4].getText().trim();

        String sql;
        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            PreparedStatement ps;

            if (!idText.isEmpty() && idText.matches("\\d+")) {
                sql = "SELECT ID, Nombre, ApellidoPaterno, ApellidoMaterno, Correo FROM InformacionAlumno WHERE ID = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(idText));
            } else if (!nom.isEmpty() && !apPat.isEmpty() && !apMat.isEmpty() && !correo.isEmpty()) {
                sql = "SELECT ID, Nombre, ApellidoPaterno, ApellidoMaterno, Correo FROM InformacionAlumno "
                        + "WHERE Nombre = ? AND ApellidoPaterno = ? AND ApellidoMaterno = ? AND Correo = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, nom);
                ps.setString(2, apPat);
                ps.setString(3, apMat);
                ps.setString(4, correo);
            } else {
                return;
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    campos[0].setText(String.valueOf(rs.getInt("ID")));
                    campos[1].setText(rs.getString("Nombre"));
                    campos[2].setText(rs.getString("ApellidoPaterno"));
                    campos[3].setText(rs.getString("ApellidoMaterno"));
                    campos[4].setText(rs.getString("Correo"));
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar datos personales:\n" + ex.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String id = campos[0].getText().trim();
        String nombre = campos[1].getText().trim();
        String apellidoP = campos[2].getText().trim();
        String apellidoM = campos[3].getText().trim();
        String correo = campos[4].getText().trim();
        String edad = campos[5].getText().trim();
        String altura = campos[6].getText().trim();
        String peso = campos[7].getText().trim();
        String enfermedades = campos[8].getText().trim();
        String medicacion = campos[9].getText().trim();
        String alergias = campos[10].getText().trim();

        if (!ValidadorPaciente.estanTodosLlenos(id, nombre, apellidoP, apellidoM, correo, edad, altura, peso,
                enfermedades, medicacion, alergias)) {
            mensajeInline.mostrarError("Todos los campos son obligatorios.");
            return;
        }

        if (!ValidadorPaciente.esIDValido(id)) {
            mensajeInline.mostrarError("El ID debe estar entre 180000 y 999999.");
            return;
        }

        if (!ValidadorPaciente.esTextoAlfabetico(nombre) ||
                !ValidadorPaciente.esTextoAlfabetico(apellidoP) ||
                !ValidadorPaciente.esTextoAlfabetico(apellidoM)) {
            mensajeInline.mostrarAdvertencia("Nombre y apellidos solo deben contener letras.");
            return;
        }

        if (!ValidadorPaciente.esCorreoValido(correo)) {
            mensajeInline.mostrarAdvertencia("El formato del correo no es válido.");
            return;
        }

        if (!ValidadorPaciente.esNumerico(edad) || !ValidadorPaciente.esNumerico(altura)
                || !ValidadorPaciente.esNumerico(peso)) {
            mensajeInline.mostrarAdvertencia("Edad, altura y peso deben ser valores numéricos.");
            return;
        }

        try (Connection conn = BaseDeDatos.ConexionSQLite.conectar()) {
            int idInt = Integer.parseInt(id);

            // 1. INFORMACION ALUMNO
            PreparedStatement checkAlumno = conn.prepareStatement("SELECT ID FROM InformacionAlumno WHERE ID = ?");
            checkAlumno.setInt(1, idInt);
            ResultSet rsAlumno = checkAlumno.executeQuery();

            if (rsAlumno.next()) {
                PreparedStatement updateAlumno = conn.prepareStatement(
                        "UPDATE InformacionAlumno SET Nombre=?, ApellidoPaterno=?, ApellidoMaterno=?, Correo=? WHERE ID=?");
                updateAlumno.setString(1, nombre);
                updateAlumno.setString(2, apellidoP);
                updateAlumno.setString(3, apellidoM);
                updateAlumno.setString(4, correo);
                updateAlumno.setInt(5, idInt);
                updateAlumno.executeUpdate();
            } else {
                PreparedStatement insertAlumno = conn.prepareStatement(
                        "INSERT INTO InformacionAlumno (ID, Nombre, ApellidoPaterno, ApellidoMaterno, Correo) VALUES (?, ?, ?, ?, ?)");
                insertAlumno.setInt(1, idInt);
                insertAlumno.setString(2, nombre);
                insertAlumno.setString(3, apellidoP);
                insertAlumno.setString(4, apellidoM);
                insertAlumno.setString(5, correo);
                insertAlumno.executeUpdate();
            }

            // 2. REGISTRO MÉDICO
            PreparedStatement checkRegistro = conn.prepareStatement("SELECT ID FROM Registro WHERE ID = ?");
            checkRegistro.setInt(1, idInt);
            ResultSet rsRegistro = checkRegistro.executeQuery();

            if (rsRegistro.next()) {
                PreparedStatement updateRegistro = conn.prepareStatement(
                        "UPDATE Registro SET Edad=?, Altura=?, Peso=?, EnfermedadesPreexistentes=?, Medicacion=?, Alergias=? WHERE ID=?");
                updateRegistro.setString(1, edad);
                updateRegistro.setString(2, altura);
                updateRegistro.setString(3, peso);
                updateRegistro.setString(4, enfermedades);
                updateRegistro.setString(5, medicacion);
                updateRegistro.setString(6, alergias);
                updateRegistro.setInt(7, idInt);
                updateRegistro.executeUpdate();
            } else {
                PreparedStatement insertRegistro = conn.prepareStatement(
                        "INSERT INTO Registro (ID, Edad, Altura, Peso, EnfermedadesPreexistentes, Medicacion, Alergias) VALUES (?, ?, ?, ?, ?, ?, ?)");
                insertRegistro.setInt(1, idInt);
                insertRegistro.setString(2, edad);
                insertRegistro.setString(3, altura);
                insertRegistro.setString(4, peso);
                insertRegistro.setString(5, enfermedades);
                insertRegistro.setString(6, medicacion);
                insertRegistro.setString(7, alergias);
                insertRegistro.executeUpdate();
            }

            mensajeInline.mostrarExito("Datos guardados correctamente.");

        } catch (SQLException ex) {
            MensajeErrorUDLAP.mostrarVentanaError(owner,
                    "Error de Base de Datos:\n" + ex.getMessage(),
                    "\nNo se pudo guardar");
        }
    }
}