package model;

// Clase que representa un área con un identificador y un nombre
public class Area {
    // Asignación de atributos
    private int id;
    private String nombre;

    // Constructor
    public Area(int id, String nombre) {
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

    // Modificadores
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId(int id) {
        this.id = id;
    }
}
