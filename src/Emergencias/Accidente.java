package Emergencias;
import java.util.List;
import java.util.Objects;
/**
 * Modelo de datos para un reporte de accidente completo según el formato UDLAP.
 * Contiene todos los campos de las secciones I–VIII del formulario.
 */
public class Accidente {
    // I. Datos del Estudiante
    private final int idEmergencia;
    private final int matricula;
    private final String nombreEstudiante;
    private final String apellidoPaterno;
    private final String apellidoMaterno;
    private final int edad;
    private final String sexo;
    private final String escuela;
    private final String programaAcademico;
    private final int semestre;
    private final String correoUDLAP;
    private final String telefonoEstudiante;
    private final String direccion;

    // II. Información del Accidente
    private final String fechaAccidente;       // "YYYY-MM-DD HH:MM:SS"
    private final String diaSemana;
    private final String lugarOcurrencia;
    private final String ubicacionExacta;
    private final String enHorarioClase;
    private final String tipoTrayecto;
    private final String centroAtencionInicial;

    // III. Lesiones y Daños
    private final String lesionPrincipal;
    private final String lesionSecundaria;
    private final String parteCuerpo;
    private final String gravedadTriage;
    private final String nivelConsciencia;
    private final String signosVitales;
    private final String descripcionDetallada;
    private final String primerosAuxilios;
    private final String medicamentos;
    private final String diagnosticoCIE10;

    // IV. Evaluación Médica
    private final String lesionesAtribuibles;
    private final String riesgoMuerte;
    private final int incapacidadDias;
    private final String requiereHospitalizacion;
    private final String tratamientoRecomendado;
    private final String medicoTratante;
    private final String cedulaProfesional;
    private final String fechaInformeMedico;   // "YYYY-MM-DD"

    // V. Traslado y Seguimiento
    private final String hospitalDestino;
    private final String responsableTraslado;
    private final String medioTransporte;
    private final String fechaHoraIngreso;     // "YYYY-MM-DD HH:MM:SS"

    // VI. Contacto de Emergencia
    private final String nombreContacto;
    private final String relacionContacto;
    private final String telefonoContacto;
    private final String correoContacto;
    private final String domicilioContacto;

    // VII. Testigos
    private final String testigo1Nombre;
    private final String testigo1Telefono;

    // VIII. Declaraciones y Firmas
    private final String narrativaDetallada;
    private final String fechaElaboracion;     // "YYYY-MM-DD"
     // IX. Fotos del Accidente
    private final List<byte[]> fotos;


    public Accidente(
        int idEmergencia,
        int matricula,
        String nombreEstudiante,
        String apellidoPaterno,
        String apellidoMaterno,
        int edad,
        String sexo,
        String escuela,
        String programaAcademico,
        int semestre,
        String correoUDLAP,
        String telefonoEstudiante,
        String direccion,
        String fechaAccidente,
        String diaSemana,
        String lugarOcurrencia,
        String ubicacionExacta,
        String enHorarioClase,
        String tipoTrayecto,
        String centroAtencionInicial,
        String lesionPrincipal,
        String lesionSecundaria,
        String parteCuerpo,
        String gravedadTriage,
        String nivelConsciencia,
        String signosVitales,
        String descripcionDetallada,
        String primerosAuxilios,
        String medicamentos,
        String diagnosticoCIE10,
        String lesionesAtribuibles,
        String riesgoMuerte,
        int incapacidadDias,
        String requiereHospitalizacion,
        String tratamientoRecomendado,
        String medicoTratante,
        String cedulaProfesional,
        String fechaInformeMedico,
        String hospitalDestino,
        String responsableTraslado,
        String medioTransporte,
        String fechaHoraIngreso,
        String nombreContacto,
        String relacionContacto,
        String telefonoContacto,
        String correoContacto,
        String domicilioContacto,
        String testigo1Nombre,
        String testigo1Telefono,
        String narrativaDetallada,
        String fechaElaboracion,
        List<byte[]> fotos 
    ) {
        this.idEmergencia = idEmergencia;
        this.matricula = matricula;
        this.nombreEstudiante = nombreEstudiante;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.edad = edad;
        this.sexo = sexo;
        this.escuela = escuela;
        this.programaAcademico = programaAcademico;
        this.semestre = semestre;
        this.correoUDLAP = correoUDLAP;
        this.telefonoEstudiante = telefonoEstudiante;
        this.direccion = direccion;
        this.fechaAccidente = fechaAccidente;
        this.diaSemana = diaSemana;
        this.lugarOcurrencia = lugarOcurrencia;
        this.ubicacionExacta = ubicacionExacta;
        this.enHorarioClase = enHorarioClase;
        this.tipoTrayecto = tipoTrayecto;
        this.centroAtencionInicial = centroAtencionInicial;
        this.lesionPrincipal = lesionPrincipal;
        this.lesionSecundaria = lesionSecundaria;
        this.parteCuerpo = parteCuerpo;
        this.gravedadTriage = gravedadTriage;
        this.nivelConsciencia = nivelConsciencia;
        this.signosVitales = signosVitales;
        this.descripcionDetallada = descripcionDetallada;
        this.primerosAuxilios = primerosAuxilios;
        this.medicamentos = medicamentos;
        this.diagnosticoCIE10 = diagnosticoCIE10;
        this.lesionesAtribuibles = lesionesAtribuibles;
        this.riesgoMuerte = riesgoMuerte;
        this.incapacidadDias = incapacidadDias;
        this.requiereHospitalizacion = requiereHospitalizacion;
        this.tratamientoRecomendado = tratamientoRecomendado;
        this.medicoTratante = medicoTratante;
        this.cedulaProfesional = cedulaProfesional;
        this.fechaInformeMedico = fechaInformeMedico;
        this.hospitalDestino = hospitalDestino;
        this.responsableTraslado = responsableTraslado;
        this.medioTransporte = medioTransporte;
        this.fechaHoraIngreso = fechaHoraIngreso;
        this.nombreContacto = nombreContacto;
        this.relacionContacto = relacionContacto;
        this.telefonoContacto = telefonoContacto;
        this.correoContacto = correoContacto;
        this.domicilioContacto = domicilioContacto;
        this.testigo1Nombre = testigo1Nombre;
        this.testigo1Telefono = testigo1Telefono;
        this.narrativaDetallada = narrativaDetallada;
        this.fechaElaboracion = fechaElaboracion;

        Objects.requireNonNull(fotos, "La lista de fotos no puede ser null");
        if (fotos.isEmpty()) {
            throw new IllegalArgumentException("Debe agregar al menos una foto al reporte");
        }
        this.fotos = List.copyOf(fotos); 
    }

    // Getters
    public int getIdEmergencia() { return idEmergencia; }
    public int getMatricula() { return matricula; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public String getApellidoPaterno() { return apellidoPaterno; }
    public String getApellidoMaterno() { return apellidoMaterno; }
    public int getEdad() { return edad; }
    public String getSexo() { return sexo; }
    public String getEscuela() { return escuela; }
    public String getProgramaAcademico() { return programaAcademico; }
    public int getSemestre() { return semestre; }
    public String getCorreoUDLAP() { return correoUDLAP; }
    public String getTelefonoEstudiante() { return telefonoEstudiante; }
    public String getDireccion() { return direccion; }
    public String getFechaAccidente() { return fechaAccidente; }
    public String getDiaSemana() { return diaSemana; }
    public String getLugarOcurrencia() { return lugarOcurrencia; }
    public String getUbicacionExacta() { return ubicacionExacta; }
    public String getEnHorarioClase() { return enHorarioClase; }
    public String getTipoTrayecto() { return tipoTrayecto; }
    public String getCentroAtencionInicial() { return centroAtencionInicial; }
    public String getLesionPrincipal() { return lesionPrincipal; }
    public String getLesionSecundaria() { return lesionSecundaria; }
    public String getParteCuerpo() { return parteCuerpo; }
    public String getGravedadTriage() { return gravedadTriage; }
    public String getNivelConsciencia() { return nivelConsciencia; }
    public String getSignosVitales() { return signosVitales; }
    public String getDescripcionDetallada() { return descripcionDetallada; }
    public String getPrimerosAuxilios() { return primerosAuxilios; }
    public String getMedicamentos() { return medicamentos; }
    public String getDiagnosticoCIE10() { return diagnosticoCIE10; }
    public String getLesionesAtribuibles() { return lesionesAtribuibles; }
    public String getRiesgoMuerte() { return riesgoMuerte; }
    public int getIncapacidadDias() { return incapacidadDias; }
    public String getRequiereHospitalizacion() { return requiereHospitalizacion; }
    public String getTratamientoRecomendado() { return tratamientoRecomendado; }
    public String getMedicoTratante() { return medicoTratante; }
    public String getCedulaProfesional() { return cedulaProfesional; }
    public String getFechaInformeMedico() { return fechaInformeMedico; }
    public String getHospitalDestino() { return hospitalDestino; }
    public String getResponsableTraslado() { return responsableTraslado; }
    public String getMedioTransporte() { return medioTransporte; }
    public String getFechaHoraIngreso() { return fechaHoraIngreso; }
    public String getNombreContacto() { return nombreContacto; }
    public String getRelacionContacto() { return relacionContacto; }
    public String getTelefonoContacto() { return telefonoContacto; }
    public String getCorreoContacto() { return correoContacto; }
    public String getDomicilioContacto() { return domicilioContacto; }
    public String getTestigo1Nombre() { return testigo1Nombre; }
    public String getTestigo1Telefono() { return testigo1Telefono; }
    public String getNarrativaDetallada() { return narrativaDetallada; }
    public String getFechaElaboracion() { return fechaElaboracion; }
    public List<byte[]> getFotos() { return fotos; }
}
