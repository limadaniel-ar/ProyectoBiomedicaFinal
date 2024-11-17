package model;

// Clase que representa un costo asociado a un usuario, con detalles como fecha y valor
public class Costo {
    private int id;
    private int mes;
    private int anio;
    private double valor;
    private Usuario usuario;

    // Constructor
    public Costo(int id, int mes, int anio, double valor, Usuario usuario) {
        this.id = id;
        this.mes = mes;
        this.anio = anio;
        this.valor = valor;
        this.usuario = usuario;
    }

    public Costo() {
        id = 0;
        mes = 0;
        anio = 0;
        valor = 0;
        usuario = null;
    }

    // Observadores
    public int getId() {
        return id;
    }

    public int getMes() {
        return mes;
    }

    public int getAnio() {
        return anio;
    }

    public double getValor() {
        return valor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public String toString() {
        return "Costo\n" +
                "- ID: " + id + "\n" +
                "- Fecha: " + mes + "/" + anio + "\n" +
                "- Valor: " + valor + "\n" +
                "- Cargado por: " + usuario.getNombre() + usuario.getApellido();
    }

    // Modificadores
    public void setMes(int mes) {
        this.mes = mes;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setId(int id) {
        this.id = id;
    }
}