package Emergencias;

import java.sql.Timestamp;

public class Emergencia {
    private final int    id;
    private final int    idPaciente;
    private final String ubicacion;
    private final String tipoDeEmergencia;
    private final String gravedad;
    private final String descripcion;
    private final Timestamp fechaIncidente;
    private final Timestamp fechaRegistro;
    private final String estado;
    private final String telefonoContacto;
    private final String medicoResponsable;

    public Emergencia(int id,
                      int idPaciente,
                      String ubicacion,
                      String tipoDeEmergencia,
                      String gravedad,
                      String descripcion,
                      Timestamp fechaIncidente,
                      Timestamp fechaRegistro,
                      String estado,
                      String telefonoContacto,
                      String medicoResponsable) {
        this.id                 = id;
        this.idPaciente         = idPaciente;
        this.ubicacion          = ubicacion;
        this.tipoDeEmergencia   = tipoDeEmergencia;
        this.gravedad           = gravedad;
        this.descripcion        = descripcion;
        this.fechaIncidente     = fechaIncidente;
        this.fechaRegistro      = fechaRegistro;
        this.estado             = estado;
        this.telefonoContacto   = telefonoContacto;
        this.medicoResponsable  = medicoResponsable;
    }

    public int getId()                          { return id; }
    public int getIdPaciente()                  { return idPaciente; }
    public String getUbicacion()                { return ubicacion; }
    public String getTipoDeEmergencia()         { return tipoDeEmergencia; }
    public String getGravedad()                 { return gravedad; }
    public String getDescripcion()              { return descripcion; }
    public Timestamp getFechaIncidente()        { return fechaIncidente; }
    public Timestamp getFechaRegistro()         { return fechaRegistro; }
    public String getEstado()                   { return estado; }
    public String getTelefonoContacto()         { return telefonoContacto; }
    public String getMedicoResponsable()        { return medicoResponsable; }

    @Override
    public String toString() {
        return "Emergencia{" +
               "id=" + id +
               ", idPaciente=" + idPaciente +
               ", ubicacion='" + ubicacion + '\'' +
               // …
               '}';
    }
}
