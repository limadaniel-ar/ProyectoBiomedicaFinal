package model;

// Clase que representa un rol de usuario dentro del sistema
public class Rol {
    // Asignación de atributos
    private int id;
    private String tipo;

    // Constructor
    public Rol(int iD, String tipo) {
        this.id = iD;
        this.tipo = tipo;
    }

    // Observadores (getters)
    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "Rol\n" +
                "- ID: " + id + "\n" +
                "- Tipo: " + tipo;
    }

    // Modificadores
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
