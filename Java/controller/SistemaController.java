package controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import model.*;
import util.DataStore;

public class SistemaController {
    private static Usuario usuarioActual;
    private static boolean sistemaActivo = false;

    public SistemaController() {
        // Constructor vacío
    }

    // Métodos de control del sistema
    public void iniciarSistema() {
        if (!sistemaActivo) {
            sistemaActivo = true;
        }
    }

    public void detenerSistema() {
        sistemaActivo = false;
        usuarioActual = null;
    }

    public boolean isSistemaActivo() {
        return sistemaActivo;
    }

    // Métodos de gestión de sesión
    public void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }

    public boolean hayUsuarioActivo() {
        return usuarioActual != null;
    }

    // Métodos de validación
    public boolean tienePermiso(String... rolesPermitidos) {
        if (usuarioActual == null || usuarioActual.getRol() == null) {
            return false;
        }
        
        String rolUsuario = usuarioActual.getRol().getTipo().toUpperCase();
        return Arrays.asList(rolesPermitidos).stream()
                .map(String::toUpperCase)
                .anyMatch(rol -> rol.equals(rolUsuario));
    }

    // Métodos de utilidad para fechas y tiempo
    public LocalDateTime obtenerFechaHoraActual() {
        return LocalDateTime.now();
    }

    public String formatearFechaHora(LocalDateTime fechaHora) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return fechaHora.format(formatter);
    }

    // Métodos de validación de datos
    public boolean validarNumeroPositivo(int numero) {
        return numero > 0;
    }

    public boolean validarTextoNoVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    // Métodos de gestión de órdenes de producción
    public List<OrdenProduccion> obtenerOrdenesEnProceso() {
        return DataStore.getOrdenesProduccion().stream()
                .filter(orden -> orden.isIniciado() && !orden.isTerminado())
                .collect(Collectors.toList());
    }

    public List<OrdenProduccion> obtenerOrdenesDisponibles() {
        return DataStore.getOrdenesProduccion().stream()
                .filter(orden -> !orden.isIniciado())
                .collect(Collectors.toList());
    }

    public List<OrdenProduccion> obtenerOrdenesFinalizadas() {
        return DataStore.getOrdenesProduccion().stream()
                .filter(OrdenProduccion::isTerminado)
                .collect(Collectors.toList());
    }

    // Métodos de cálculo y estadísticas
    public double calcularTiempoPromedioProduccion(Producto producto) {
        List<OrdenProduccion> ordenesFinalizadas = DataStore.getOrdenesProduccion().stream()
                .filter(orden -> orden.getProducto().equals(producto) && orden.isTerminado())
                .collect(Collectors.toList());

        if (ordenesFinalizadas.isEmpty()) {
            return 0.0;
        }

        double tiempoTotal = ordenesFinalizadas.stream()
                .mapToDouble(orden -> {
                    Duration duracion = Duration.between(orden.getInicio(), orden.getFin());
                    return duracion.toMinutes();
                })
                .sum();

        return tiempoTotal / ordenesFinalizadas.size();
    }

    // Métodos de búsqueda
    public Usuario buscarUsuarioPorLegajo(int legajo) {
        return DataStore.getUsuarioByLegajo(legajo);
    }

    public Producto buscarProductoPorId(int id) {
        return DataStore.getProductos().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public OrdenProduccion buscarOrdenProduccionPorId(int id) {
        return DataStore.getOrdenesProduccion().stream()
                .filter(o -> o.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Métodos de validación de operaciones
    public boolean puedeIniciarOrden(OrdenProduccion orden) {
        return !orden.isIniciado() && !orden.isTerminado();
    }

    public boolean puedeFinalizarOrden(OrdenProduccion orden) {
        return orden.isIniciado() && !orden.isTerminado();
    }

    public boolean puedeRegistrarParada(OrdenProduccion orden) {
        return orden.isIniciado() && !orden.isTerminado();
    }

    // Métodos de gestión de errores
    public void registrarError(String mensaje, Exception e) {
        // Aquí podrías implementar un sistema de logging
        System.err.println("Error: " + mensaje);
        System.err.println("Detalles: " + e.getMessage());
        e.printStackTrace();
    }

    // Métodos de validación de seguridad
    public boolean validarPassword(String password) {
        // Implementar reglas de validación de contraseña
        return password != null && password.length() >= 6;
    }

    public boolean validarLegajo(int legajo) {
        // Implementar reglas de validación de legajo
        return legajo > 0 && String.valueOf(legajo).length() == 8;
    }

    // Métodos de gestión de categorías de parada
    public List<CategoriaParada> obtenerCategoriasParada() {
        return new ArrayList<>(DataStore.getCategoriasParada());
    }

    // Métodos de gestión de costos
    public boolean registrarCosto(int mes, int anio, double monto) {
        if (mes < 1 || mes > 12 || anio < 2000 || monto <= 0) {
            return false;
        }
        
        Costo nuevoCosto = new Costo();
        nuevoCosto.setMes(mes);
        nuevoCosto.setAnio(anio);
        nuevoCosto.setValor(monto);
        DataStore.addCosto(nuevoCosto);
        return true;
    }
}
