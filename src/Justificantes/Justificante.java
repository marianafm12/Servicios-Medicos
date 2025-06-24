package Justificantes;

import java.io.File;
import java.time.LocalDate;

public class Justificante {
    private int folio;
    private String idPaciente;
    private String nombrePaciente;
    private String motivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String diagnostico;
    private File archivoReceta;

    private String estado; // Pendiente, Aprobado, Rechazado
    private String resueltoPor; // nombre médico
    private LocalDate fechaResolucion;

    
    public Justificante(String idPaciente, String nombrePaciente, String motivo,
                        LocalDate fechaInicio, LocalDate fechaFin,
                        String diagnostico, File archivoReceta) {
        this.idPaciente = idPaciente;
        this.nombrePaciente = nombrePaciente;
        this.motivo = motivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.diagnostico = diagnostico;
        this.archivoReceta = archivoReceta;
        this.estado = "Pendiente";
    }

    
    public Justificante(int folio, String idPaciente, String nombrePaciente, String motivo,
                        LocalDate fechaInicio, LocalDate fechaFin,
                        String diagnostico, File archivoReceta,
                        String estado, String resueltoPor, LocalDate fechaResolucion) {
        this(idPaciente, nombrePaciente, motivo, fechaInicio, fechaFin, diagnostico, archivoReceta);
        this.folio = folio;
        this.estado = estado;
        this.resueltoPor = resueltoPor;
        this.fechaResolucion = fechaResolucion;
    }

    

    public int getFolio() { return folio; }
    public void setFolio(int folio) { this.folio = folio; }

    public String getIdPaciente() { return idPaciente; }
    public String getNombrePaciente() { return nombrePaciente; }
    public String getMotivo() { return motivo; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public String getDiagnostico() { return diagnostico; }
    public File getArchivoReceta() { return archivoReceta; }

    public String getEstado() { return estado; }
    public String getResueltoPor() { return resueltoPor; }
    public LocalDate getFechaResolucion() { return fechaResolucion; }

    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public void setArchivoReceta(File archivoReceta) { this.archivoReceta = archivoReceta; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setResueltoPor(String resueltoPor) { this.resueltoPor = resueltoPor; }
    public void setFechaResolucion(LocalDate fechaResolucion) { this.fechaResolucion = fechaResolucion; }

    public Justificante() {
    
}
}
