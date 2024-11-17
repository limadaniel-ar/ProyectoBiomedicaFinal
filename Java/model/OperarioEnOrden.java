package model;

import java.time.LocalDateTime;

// Clase que representa la relación entre un operario y una orden de producción en una etapa específica
public class OperarioEnOrden {
    private int id;
    private OrdenProduccion ordenProduccion;
    private Usuario usuario;
    private Etapa etapa;
    private LocalDateTime inicio;
    private LocalDateTime fin;

    // Constructor
    public OperarioEnOrden(int id, OrdenProduccion ordenProduccion, Usuario usuario, Etapa etapa,
            LocalDateTime inicio) {
        this.id = id;
        this.ordenProduccion = ordenProduccion;
        this.usuario = usuario;
        this.etapa = etapa;
        this.inicio = inicio;
        this.fin = null;
    }

    // Observadores
    public int getId() {
        return id;
    }

    public OrdenProduccion getOrdenProduccion() {
        return ordenProduccion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Etapa getEtapa() {
        return etapa;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    @Override
    public String toString() {
        return "Operario en Orden" + "\n" +
                "- Orden de Produccion: " + ordenProduccion.getId() + "\n" +
                "- Operario: " + usuario.getNombre() + usuario.getApellido() + "\n" +
                "- Etapa: " + etapa.getNombre() + "\n" +
                "- Inicio:" + inicio + "\n" +
                "- Fin: " + fin;
    }

    // Modificadores
    public void setOrdenProduccion(OrdenProduccion ordenProduccion) {
        this.ordenProduccion = ordenProduccion;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
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
}
