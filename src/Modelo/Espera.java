package Modelo;

public class Espera {
    private String idPaciente;
    private String fecha;
    private String hora;
    private String servicio;

    public Espera(String idPaciente, String fecha, String hora, String servicio) {
        this.idPaciente = idPaciente;
        this.fecha = fecha;
        this.hora = hora;
        this.servicio = servicio;
    }

    // Getters opcionales
    public String getIdPaciente() { return idPaciente; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getServicio() { return servicio; }
}
