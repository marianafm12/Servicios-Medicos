package Emergencias;

public class Accidente {
    private int idEmergencia;
    private String fecha;
    private String genero;
    private String presionArterial;
    private int ritmoCardiaco;
    private int ritmoRespiratorio;
    private String consciencia;
    private String descripcion;
    private String observaciones;
    private Integer idContacto;
    private String nombreContacto;
    private String apellidosContacto;
    private String correoContacto;
    private String telefonoContacto;
    private String estado;

    public Accidente(int idEmergencia, String fecha, String genero, String presionArterial,
                     int ritmoCardiaco, int ritmoRespiratorio, String consciencia,
                     String descripcion, String observaciones, Integer idContacto,
                     String nombreContacto, String apellidosContacto,
                     String correoContacto, String telefonoContacto, String estado) {
        this.idEmergencia = idEmergencia;
        this.fecha = fecha;
        this.genero = genero;
        this.presionArterial = presionArterial;
        this.ritmoCardiaco = ritmoCardiaco;
        this.ritmoRespiratorio = ritmoRespiratorio;
        this.consciencia = consciencia;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.idContacto = idContacto;
        this.nombreContacto = nombreContacto;
        this.apellidosContacto = apellidosContacto;
        this.correoContacto = correoContacto;
        this.telefonoContacto = telefonoContacto;
        this.estado = estado;
    }

    // Getters (puedes generar setters si necesitas modificarlos luego)

    public int getIdEmergencia() { return idEmergencia; }
    public String getFecha() { return fecha; }
    public String getGenero() { return genero; }
    public String getPresionArterial() { return presionArterial; }
    public int getRitmoCardiaco() { return ritmoCardiaco; }
    public int getRitmoRespiratorio() { return ritmoRespiratorio; }
    public String getConsciencia() { return consciencia; }
    public String getDescripcion() { return descripcion; }
    public String getObservaciones() { return observaciones; }
    public Integer getIdContacto() { return idContacto; }
    public String getNombreContacto() { return nombreContacto; }
    public String getApellidosContacto() { return apellidosContacto; }
    public String getCorreoContacto() { return correoContacto; }
    public String getTelefonoContacto() { return telefonoContacto; }
    public String getEstado() { return estado; }
}
