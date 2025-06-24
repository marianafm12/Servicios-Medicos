package Registro;

public class Doctor {
    private int id;
    private String nombre;

    public Doctor(int id, String nombre) {
        if (id < 2000 || id > 3000) {
            throw new IllegalArgumentException("ID de Doctor fuera de rango");
        }
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void revisarFormulario(Paciente paciente) {
        System.out.println("Revisando formulario de " + paciente.getNombre());
        System.out.println(paciente.obtenerDatos());
    }
}
