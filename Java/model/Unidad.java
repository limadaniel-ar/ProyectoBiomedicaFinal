package model;

// Clase que representa una unidad de medida
public class Unidad {
    // Asignación de atributos
    private int id;
    private String nombre;

    // Constructor
    public Unidad(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Observadores
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Unidad\n" +
                "- ID: " + id + "\n" +
                "- Nombre: " + nombre;
    }

    // Modificadores
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
