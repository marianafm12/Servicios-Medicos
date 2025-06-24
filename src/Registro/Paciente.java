package Registro;

public class Paciente {
    private int id;
    private String nombre;
    private String enfermedades;
    private String medicamentos;
    private String alergias;
    private double altura;
    private double peso;

    public Paciente(int id, String nombre) {
        if (id < 180000 || id > 200000) {
            throw new IllegalArgumentException("ID de Paciente fuera de rango");
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

    public void setEnfermedades(String enfermedades) {
        this.enfermedades = enfermedades;
    }

    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String obtenerDatos() {
        return "ID: " + id + "\nNombre: " + nombre + "\nEnfermedades: " + enfermedades + "\nMedicamentos: "
                + medicamentos + "\nAlergias: " + alergias + "\nAltura: " + altura + "\nPeso: " + peso;
    }
}
