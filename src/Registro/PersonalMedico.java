//Personal Médico
package Registro;

public class PersonalMedico {
    private int id;
    private String nombre;

    public PersonalMedico(int id, String nombre) {
        if (id < 5000 || id > 6000) {
            throw new IllegalArgumentException("ID de Personal Médico fuera de rango");
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

    public void llenarFormulario(Paciente paciente, String enfermedades, String medicamentos, String alergias,
            double altura, double peso) {
        paciente.setEnfermedades(enfermedades);
        paciente.setMedicamentos(medicamentos);
        paciente.setAlergias(alergias);
        paciente.setAltura(altura);
        paciente.setPeso(peso);
    }
}
