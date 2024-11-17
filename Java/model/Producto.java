package model;

// Clase que representa un producto con sus atributos
public class Producto {
    // Asignación de atributos
    private int id;
    private String nombre;
    private int cantidad;
    private Unidad unidad;

    // Constructor
    public Producto(int id, String nombre, int cantidad, Unidad unidad) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.unidad = unidad;
    }

    // Observadores
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    @Override
    public String toString() {
        return "Producto\n" +
                "- ID: " + id + "\n" +
                "- Nombre: " + nombre + "\n" +
                "- Cantidad: " + cantidad + unidad.toString();
    }

    // Modificadores
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public void setId(int id) {
        this.id = id;
    }
}
