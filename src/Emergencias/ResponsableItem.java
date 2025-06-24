package Emergencias;

/**
 * Representa un responsable médico (doctor u otro personal autorizado)
 * que puede ser seleccionado en formularios como Llamada de Emergencia.
 */
public class ResponsableItem {
    private final int id;
    private final String nombre;

    public ResponsableItem(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ResponsableItem))
            return false;
        ResponsableItem other = (ResponsableItem) obj;
        return this.id == other.id && this.nombre.equals(other.nombre);
    }

    @Override
    public int hashCode() {
        return id + nombre.hashCode();
    }
}
