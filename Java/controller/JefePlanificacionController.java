package controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.*;
import util.DataStore;
import view.JefePlanificacionView;

public class JefePlanificacionController {
    private JefePlanificacionView view;
    private SistemaController sistemaController;
    private Usuario usuarioActual;

    public JefePlanificacionController(Usuario usuarioActual) {
        this.sistemaController = new SistemaController();
        this.usuarioActual = usuarioActual;
        this.view = new JefePlanificacionView();
    }

    public void mostrarMenu() {
        boolean continuar = true;
        while (continuar) {
            try {
                int opcion = view.mostrarMenuPrincipal();
                continuar = procesarOpcion(opcion);
            } catch (InputMismatchException e) {
                view.mostrarError("Entrada inválida. Por favor, ingrese un número.");
                view.limpiarBuffer();
            } catch (Exception e) {
                view.mostrarError("Error: " + e.getMessage());
                sistemaController.registrarError("Error en JefePlanificacionController", e);
            }
        }
    }

    private boolean procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                gestionarOrdenes();
                return true;
            case 2:
                mostrarEstadisticasTiempos();
                return true;
            case 0:
                sistemaController.cerrarSesion();
                view.mostrarMensaje("Sesión cerrada exitosamente");
                return false;
            default:
                view.mostrarError("Opción inválida");
                return true;
        }
    }

    private void gestionarOrdenes() {
        boolean continuar = true;
        while (continuar) {
            try {
                int opcion = view.mostrarMenuOrdenes();
                switch (opcion) {
                    case 1:
                        crearNuevaOrden();
                        break;
                    case 2:
                        verOrdenes();
                        break;
                    case 0:
                        continuar = false;
                        break;
                    default:
                        view.mostrarError("Opción inválida");
                }
            } catch (InputMismatchException e) {
                view.mostrarError("Entrada inválida. Por favor, ingrese un número.");
                view.limpiarBuffer();
            } catch (Exception e) {
                view.mostrarError("Error: " + e.getMessage());
                sistemaController.registrarError("Error en gestión de órdenes", e);
            }
        }
    }

    // Métodos de gestión de ordenes
    private void crearNuevaOrden() {
        try {
            List<Producto> productos = DataStore.getProductos();
            if (productos.isEmpty()) {
                view.mostrarError("No hay productos registrados en el sistema.");
                return;
            }

            List<Area> areas = DataStore.getAreas();
            if (areas.isEmpty()) {
                view.mostrarError("No hay áreas registradas en el sistema.");
                return;
            }

            OrdenProduccion nuevaOrden = view.solicitarDatosOrden(productos, areas);
            DataStore.addOrdenProduccion(nuevaOrden);
            view.mostrarMensaje("Orden de producción creada exitosamente");
        } catch (Exception e) {
            view.mostrarError("Error al crear la orden: " + e.getMessage());
            sistemaController.registrarError("Error en crearNuevaOrden", e);
        }
    }

    private void verOrdenes() {
        try {
            List<OrdenProduccion> ordenes = DataStore.getOrdenesProduccion();
            view.mostrarOrdenes(ordenes);
        } catch (Exception e) {
            view.mostrarError("Error al mostrar las órdenes: " + e.getMessage());
            sistemaController.registrarError("Error en verOrdenes", e);
        }
    }

    // Métodos de estadísticas
    private void mostrarEstadisticasTiempos() {
        try {
            Map<Producto, Duration> promediosPorProducto = calcularPromediosTiempos();
            view.mostrarEstadisticasTiempos(promediosPorProducto);
        } catch (Exception e) {
            view.mostrarError("Error al calcular estadísticas: " + e.getMessage());
            sistemaController.registrarError("Error en mostrarEstadisticasTiempos", e);
        }
    }

    private Map<Producto, Duration> calcularPromediosTiempos() {
        Map<Producto, Duration> promedios = new HashMap<>();
        Map<Producto, List<Duration>> tiemposPorProducto = new HashMap<>();

        // Recolectar todos los tiempos por producto
        for (OrdenProduccion orden : DataStore.getOrdenesProduccion()) {
            if (orden.getEstado().equals("COMPLETADA") && orden.getFin() != null) {
                Duration duracion = Duration.between(orden.getInicio(), orden.getFin());

                tiemposPorProducto.computeIfAbsent(orden.getProducto(), k -> new ArrayList<>())
                        .add(duracion);
            }
        }

        // Calcular promedios
        for (Entry<Producto, List<Duration>> entry : tiemposPorProducto.entrySet()) {
            Duration promedio = entry.getValue().stream()
                    .reduce(Duration.ZERO, Duration::plus)
                    .dividedBy(entry.getValue().size());
            promedios.put(entry.getKey(), promedio);
        }

        return promedios;
    }
}
