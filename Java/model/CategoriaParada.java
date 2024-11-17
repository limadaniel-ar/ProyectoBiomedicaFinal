package model;

// Clase que representa una categoría de parada con un identificador y una descripción
public class CategoriaParada {
    // Asignación de atributos
    private int id;
    private String descripcion;

    // Constructor
    public CategoriaParada(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    // Observadores
    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Modificadores
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setId(int id) {
        this.id = id;
    }
}
