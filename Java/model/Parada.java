package model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// Clase que representa una parada en el proceso de producción
public class Parada {
    private int id;
    private OrdenProduccion ordenProduccion;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private CategoriaParada categoriaParada;
    private String observacion;

    // Constructor
    public Parada(int id, OrdenProduccion ordenProduccion, LocalDateTime inicio, CategoriaParada categoriaParada, String observacion) {
        this.id = id;
        this.ordenProduccion = ordenProduccion;
        this.inicio = inicio;
        this.fin = null;
        this.categoriaParada = categoriaParada;
        this.observacion = observacion;
    }

    // Observadores
    public int getId() {
        return id;
    }

    public OrdenProduccion getOrdenProduccion() {
        return ordenProduccion;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public CategoriaParada getCategoriaParada() {
        return categoriaParada;
    }

    public String getObservacion() {
        return observacion;
    }

    @Override
    public String toString() {
        return "Parada\n" +
                "- ID: " + id + "\n" +
                "- Orden de Produccion: " + ordenProduccion.getId() + "\n" +
                "- Inicio: " + inicio + "\n" +
                "- Fin: " + fin + "\n" +
                "- Categoria de Parada: " + categoriaParada.getDescripcion() + "\n" +
                "- Observaciones: " + observacion;
    }

    // Modificadores
    public void setOrdenProduccion(OrdenProduccion ordenProduccion) {
        this.ordenProduccion = ordenProduccion;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public void setCategoriaParada(CategoriaParada categoriaParada) {
        this.categoriaParada = categoriaParada;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDuracionMinutos() {
        if (inicio == null) {
            return 0;
        }
        
        LocalDateTime fechaFinCalculo = fin != null ? fin : LocalDateTime.now();
        return ChronoUnit.MINUTES.between(inicio, fechaFinCalculo);
    }

    // Método adicional que podría ser útil para obtener la duración en formato legible
    public String getDuracionFormateada() {
        long minutos = getDuracionMinutos();
        long horas = minutos / 60;
        long minutosRestantes = minutos % 60;
        
        if (horas > 0) {
            return String.format("%d horas %d minutos", horas, minutosRestantes);
        } else {
            return String.format("%d minutos", minutosRestantes);
        }
    }

    // Método para verificar si la parada está activa
    public boolean isActiva() {
        return inicio != null && fin == null;
    }

    // Método para finalizar la parada
    public void finalizar() {
        if (this.fin == null) {
            this.fin = LocalDateTime.now();
        }
    }
}
