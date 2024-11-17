package model;

// Clase que representa un usuario del sistema
public class Usuario {
    // Asignación de atributos
    private int legajo;
    private int dni;
    private String nombre;
    private String apellido;
    private String password;
    private Rol rol;

    // Constructor
    public Usuario(int legajo, int dni, String nombre, String apellido, String password, Rol rol) {
        this.legajo = legajo;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.rol = rol;
    }

    // Observadores
    public int getLegajo() {
        return legajo;
    }

    public int getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getPassword() {
        return password;
    }

    public Rol getRol() {
        return rol;
    }

    @Override
    public String toString() {
        return "Usuario \n" +
                "- Legajo: " + legajo + "\n" +
                "- DNI: " + dni + "\n" +
                "- Nombre: " + nombre + "\n" +
                "- Apellido: " + apellido + "\n" +
                "- Rol: " + rol.getTipo();
    }

    // Modificadores
    public void setDni(int dni) {
        this.dni = dni;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setLegajo(int legajo) {
        this.legajo = legajo;
    }
}
