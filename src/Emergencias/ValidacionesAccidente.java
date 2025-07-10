package Emergencias;

import Utilidades.*;
import Registro.ValidadorPaciente;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
/**
 * Todas las validaciones para el FormularioAccidenteCompleto.
 * Devuelve false y muestra un diálogo en el primer error encontrado.
 */
public class ValidacionesAccidente {

    /**
     * Valida todos los campos del formulario de accidente.
     * 
     * @param owner               ventana padre para los diálogos
     * @param txtIDEmergencia     texto del campo ID Emergencia
     * @param txtMatricula        texto del campo Matrícula
     * @param nombre              Nombre(s) del estudiante
     * @param apellidoPaterno     Apellido paterno
     * @param apellidoMaterno     Apellido materno
     * @param txtEdad             texto del campo Edad
     * @param sexo                valor seleccionado de comboSexo
     * @param escuela             valor seleccionado de comboEscuela
     * @param programa            texto del campo Programa Académico
     * @param txtSemestre         texto del campo Semestre
     * @param correoUDLAP         texto del campo Correo institucional
     * @param telefonoEst         texto del campo Teléfono estudiante
     * @param fechaAccidente      fecha elegida en DatePickerUDLAP
     * @param horaAccidente       hora seleccionada en comboHoraAccidente
     * @param minutoAccidente     minuto seleccionado en comboMinutoAccidente
     * @param diaSemana           valor de comboDiaSemana
     * @param lugarOcurrencia     valor de comboLugar
     * @param ubicacionExacta     texto del campo Ubicación Exacta
     * @param enHorario           valor de comboEnHorario
     * @param lesionPrincipal     valor de comboLesionPrincipal
     * @param lesionSecundaria    texto del campo Lesión Secundaria
     * @param parteCuerpo         valor de comboParteCuerpo
     * @param gravedadTriage      valor de comboTriage
     * @param nivelConsciencia    valor de comboConsciencia
     * @param descripcion         texto del área Descripción
     * @param primerosAuxilios    texto del área Primeros Auxilios
     * @param medicamentos        texto del área Medicamentos
     * @param diagnosticoCIE10    texto del campo Diagnóstico
     * @param lesionesAtribuibles valor de comboLesionesAtribuibles
     * @param riesgoMuerte        valor de comboRiesgoMuerte
     * @param txtIncapacidad      texto del campo Incapacidad
     * @param requiereHosp        valor de comboHospitalizacion
     * @param tratamientoRec      texto del área Tratamiento
     * @param medicoTratante      texto del campo Médico tratante
     * @param cedulaProfesional   texto del campo Cédula profesional
     * @param fechaIngreso        fecha elegida en DatePickerUDLAP ingreso
     * @param horaIngreso         hora seleccionada en comboHoraIngreso
     * @param minutoIngreso       minuto seleccionado en comboMinutoIngreso
     * @param nombreContacto      texto del campo Nombre contacto de emergencia
     * @param relacionContacto    texto del campo Relación contacto
     * @param telefonoContacto    texto del campo Teléfono contacto
     * @param correoContacto      texto del campo Correo contacto
     * @param domicilioContacto   texto del área Domicilio contacto
     * @param testigo1Nombre      texto del campo Testigo1 Nombre
     * @param testigo1Telefono    texto del campo Testigo1 Teléfono
     * @param narrativaDetallada  texto del área Narrativa
     * @param fechaElaboracion    fecha elegida en DatePickerUDLAP elaboración
     * @param fotosAccidente      lista de fotos (byte[])
     * @return true si todo es válido, false en el primer error (muestra diálogo)
     */



private static final Pattern PATRON_SIGNOS = Pattern.compile(
    "^FC:\\s*\\d{1,3}\\s*LPM;\\s*PA:\\s*\\d{2,3}/\\d{2,3}\\s*mmHg;\\s*FR:\\s*\\d{1,2}\\s*RPM;\\s*T:\\s*\\d{1,2}°C;\\s*SpO2:\\s*\\d{1,3}%$"
);


    public static boolean validarCampos(
            Window owner,
            String txtIDEmergencia,
            String txtMatricula,
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String txtEdad,
            String sexo,
            String escuela,
            String programa,
            String txtSemestre,
            String correoUDLAP,
            String telefonoEst,
            LocalDate fechaAccidente,
            String horaAccidente,
            String minutoAccidente,
            String diaSemana,
            String lugarOcurrencia,
            String ubicacionExacta,
            String enHorario,
            String lesionPrincipal,
            String lesionSecundaria,
            String parteCuerpo,
            String gravedadTriage,
            String nivelConsciencia,
            String signosVitales,
            String descripcion,
            String primerosAuxilios,
            String medicamentos,
            String diagnosticoCIE10,
            String lesionesAtribuibles,
            String riesgoMuerte,
            String txtIncapacidad,
            String requiereHosp,
            String tratamientoRec,
            String medicoTratante,
            String cedulaProfesional,
            String hospitalDestino,
            String responsableTraslado,
            String medioTransporte,
            LocalDate fechaIngreso,
            String horaIngreso,
            String minutoIngreso,
            String nombreContacto,
            String relacionContacto,
            String telefonoContacto,
            String correoContacto,
            String domicilioContacto,
            String testigo1Nombre,
            String testigo1Telefono,
            String narrativaDetallada,
            LocalDate fechaElaboracion,
            List<byte[]> fotosAccidente) {
        // I. Datos del Estudiante
        if (txtIDEmergencia == null || txtIDEmergencia.isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar el ID de Emergencia.");
            return false;
        }
        try {
            Integer.parseInt(txtIDEmergencia);
        } catch (NumberFormatException e) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "El ID de Emergencia debe ser numérico.");
            return false;
        }
        if (!ValidadorPaciente.esIDValido(txtMatricula)) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "El ID de estudiante debe ser un número entre 180000 y 999999.");
            return false;
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar el nombre del estudiante.");
            return false;
        }
        if (apellidoPaterno == null || apellidoPaterno.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar el apellido paterno.");
            return false;
        }
        if (apellidoMaterno == null || apellidoMaterno.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar el apellido materno.");
            return false;
        }
        if (txtEdad == null || txtEdad.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar la edad.");
            return false;
        }
        try {
            int edad = Integer.parseInt(txtEdad);
            if (edad < 0 || edad > 120)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "La edad debe ser un número entre 0 y 120.");
            return false;
        }
        if (sexo == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe seleccionar el sexo.");
            return false;
        }
        if (escuela == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe seleccionar la escuela.");
            return false;
        }
        if (programa == null || programa.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar el programa académico.");
            return false;
        }
        if (txtSemestre == null || txtSemestre.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar el semestre.");
            return false;
        }
        try {
            Integer.parseInt(txtSemestre);
        } catch (NumberFormatException e) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "El semestre debe ser numérico.");
            return false;
        }
        if (correoUDLAP == null || correoUDLAP.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar el correo institucional UDLAP.");
            return false;
        }
        if (!correoUDLAP.matches("^[A-Za-z0-9._%+-]+@udlap\\.mx$")) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "El correo debe pertenecer al dominio @udlap.mx.");
            return false;
        }
        if (telefonoEst == null || !telefonoEst.matches("^\\d{10}$")) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "El teléfono del estudiante debe tener 10 dígitos.");
            return false;
        }

        // II. Información del Accidente
        if (fechaAccidente == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe seleccionar la fecha del accidente.");
            return false;
        }
        if (horaAccidente == null || minutoAccidente == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe seleccionar hora y minuto del accidente.");
            return false;
        }
        if (diaSemana == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe seleccionar el día de la semana.");
            return false;
        }
        if (lugarOcurrencia == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe seleccionar el lugar de ocurrencia.");
            return false;
        }
        if (ubicacionExacta == null || ubicacionExacta.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar la ubicación exacta.");
            return false;
        }
        if (enHorario == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe indicar si fue en horario de clase.");
            return false;
        }

        // III. Lesiones y Daños
        if (lesionPrincipal == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe seleccionar el tipo de lesión principal.");
            return false;
        }
        if (parteCuerpo == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "Debe seleccionar la parte del cuerpo afectada.");
            return false;
        }
        if (gravedadTriage == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe seleccionar la gravedad.");
            return false;
        }
        if (nivelConsciencia == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe seleccionar el nivel de consciencia.");
            return false;
        }
        String placeholder = "FC:75 LPM; PA:120/80 mmHg; FR:16 RPM; T:37°C; SpO2:98%";
        if (signosVitales == null
            || signosVitales.trim().isEmpty()
            || signosVitales.equals(placeholder)) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar los signos vitales.");
            return false;
        }
        // 2) Formato de llenado
        if (!PATRON_SIGNOS.matcher(signosVitales).matches()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner,
                "Validación",
                "El campo Signos Vitales debe seguir este formato:\n" +
                "FC:75 LPM; PA:120/80 mmHg; FR:16 RPM; T:37°C; SpO2:98%");
            return false;
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "Debe ingresar la descripción detallada del accidente.");
            return false;
        }

        // IV. Evaluación Médica
        if (lesionesAtribuibles == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "Debe indicar si las lesiones son atribuibles al accidente.");
            return false;
        }
        if (riesgoMuerte == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe indicar el riesgo de muerte.");
            return false;
        }
        if (txtIncapacidad == null || txtIncapacidad.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar los días de incapacidad.");
            return false;
        }
        try {
            Integer.parseInt(txtIncapacidad);
        } catch (NumberFormatException e) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "Los días de incapacidad deben ser un número entero.");
            return false;
        }
        if (requiereHosp == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe indicar el lugar de tratamiento.");
            return false;
        }
        if (tratamientoRec == null || tratamientoRec.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe indicar el tratamiento recomendado.");
            return false;
        }
        if (medicoTratante == null || medicoTratante.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar el nombre del médico tratante.");
            return false;
        }
        if (cedulaProfesional == null || cedulaProfesional.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar la cédula profesional.");
            return false;
        }
        if (!cedulaProfesional.matches("\\d+")) {
        MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
            "La cédula profesional debe contener solo números.");
        return false;
}

        // V. Traslado y Seguimiento
        // V. Traslado y Seguimiento (parte adicional)
        if ("Hospital".equals(requiereHosp)) {
            if (hospitalDestino == null || hospitalDestino.trim().isEmpty()) {
                MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "Debe seleccionar el hospital destino.");
                return false;
            }
            if (responsableTraslado == null || responsableTraslado.trim().isEmpty()) {
                MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "Debe ingresar el responsable de traslado.");
                return false;
            }
            if (medioTransporte == null || medioTransporte.trim().isEmpty()) {
                MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "Debe ingresar el medio de transporte.");
                return false;
            }
        }
        if (fechaIngreso == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe seleccionar la fecha de ingreso.");
            return false;
        }
        if (horaIngreso == null || minutoIngreso == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe seleccionar hora y minuto de ingreso.");
            return false;
        }

        // VI. Contacto de Emergencia
        if (nombreContacto == null || nombreContacto.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "Debe ingresar el nombre del contacto de emergencia.");
            return false;
        }
        if (!nombreContacto.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+")) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "El nombre del contacto sólo debe contener letras y espacios.");
            return false;
        }
        if (relacionContacto == null || relacionContacto.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe ingresar la relación con el estudiante.");
            return false;
        }
        if (!relacionContacto.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+")) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "La relación sólo debe contener letras y espacios.");
            return false;
        }
        if (telefonoContacto == null || !telefonoContacto.matches("\\d{10}")) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "El teléfono de contacto debe tener 10 dígitos.");
            return false;
        }

        // VIII. Declaraciones y Firmas
        if (narrativaDetallada == null || narrativaDetallada.trim().isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación",
                    "Debe ingresar la narrativa detallada del accidente.");
            return false;
        }
        if (fechaElaboracion == null) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debe seleccionar la fecha de elaboración.");
            return false;
        }

        // Fotos
        if (fotosAccidente == null || fotosAccidente.isEmpty()) {
            MensajeErrorUDLAP.mostrarVentanaError(owner, "Validación", "Debes agregar al menos una foto.");
            return false;
        }

        // Todo validado correctamente
        return true;
    }
}
