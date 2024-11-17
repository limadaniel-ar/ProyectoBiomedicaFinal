package model;

import java.time.Duration;
import java.time.LocalDateTime;

// Clase que representa una orden de producción con sus atributos y estado
public class OrdenProduccion {
    private int id;
    private int lote;
    private Producto producto;
    private Area area;
    private boolean iniciado;
    private boolean pausado;
    private boolean terminado;
    private LocalDateTime inicio;
    private LocalDateTime fin;

    // Constructor
    public OrdenProduccion(int id, int lote, Producto producto, Area area) {
        this.id = id;
        this.lote = lote;
        this.producto = producto;
        this.area = area;
        this.iniciado = false;
        this.pausado = false;
        this.terminado = false;
        this.inicio = null;
        this.fin = null;
    }

    // Observadores
    public int getId() {
        return id;
    }

    public int getLote() {
        return lote;
    }

    public Producto getProducto() {
        return producto;
    }

    public Area getArea() {
        return area;
    }

    public boolean isIniciado() {
        return iniciado;
    }

    public boolean isPausado() {
        return pausado;
    }

    public boolean isTerminado() {
        return terminado;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    @Override
    public String toString() {
        return "OrdenProduccion\n" +
                "- ID: " + id + "\n" +
                "- Lote: " + lote + "\n" +
                "- Producto: " + producto.getNombre() + "\n" +
                "- Area: " + area.getNombre() + "\n" +
                "- Iniciado: " + iniciado + "\n" +
                "- Pausado: " + pausado + "\n" +
                "- Terminado: " + terminado + "\n" +
                "- Fecha y hora de inicio: " + inicio + "\n" +
                "- Fecha y hora de fin: " + fin;
    }

    // Modificadores
    public void setLote(int lote) {
        this.lote = lote;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setIniciado(boolean iniciado) {
        this.iniciado = iniciado;
    }

    public void setPausado(boolean pausado) {
        this.pausado = pausado;
    }

    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        if (this.iniciado == true) {
            return "Iniciado";
        } else {
            return "Disponible";
        }
    }

    // Métodos propios del tipo
    // Método que devuelve la cantidad producida en la orden
    public int getCantidadProducida() {
        return producto.getCantidad();
    }

    // Método que calcula y devuelve el tiempo de produccion en formato decimal
    public long getTiempoProduccion() {
        Duration duration = Duration.between(inicio, fin);
        return (long) (duration.toHours() + (duration.toMinutes() % 60) / 60.0);
    }
}
