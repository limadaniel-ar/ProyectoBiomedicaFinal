package controller;

import java.util.*;
import java.util.stream.Collectors;

import model.*;
import util.DataStore;
import view.JefeProduccionView;

public class JefeProduccionController {
    private JefeProduccionView view;
    private SistemaController sistemaController;
    private Usuario usuarioActual;

    public JefeProduccionController(Usuario usuarioActual) {
        this.sistemaController = new SistemaController();
        this.usuarioActual = usuarioActual;
        this.view = new JefeProduccionView();
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
                sistemaController.registrarError("Error en JefeProduccionController", e);
            }
        }
    }

    private boolean procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                verOrdenesEnProceso();
                return true;
            case 2:
                verOrdenesDisponibles();
                return true;
            case 3:
                gestionarCategoriasParadas();
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

    // Método que muestra las ordenes activas en el momento
    private void verOrdenesEnProceso() {
        try {
            List<OrdenProduccion> ordenesEnProceso = DataStore.getOrdenesProduccion().stream()
                    .filter(orden -> orden.isIniciado() == true)
                    .collect(Collectors.toList());

            view.mostrarOrdenesEnProceso(ordenesEnProceso);

            // Si hay órdenes en proceso, mostrar detalles de operarios asignados
            if (!ordenesEnProceso.isEmpty()) {
                for (OrdenProduccion orden : ordenesEnProceso) {
                    List<OperarioEnOrden> operariosAsignados = obtenerOperariosDeOrden(orden);
                    view.mostrarDetalleOrdenEnProceso(orden, operariosAsignados);
                }
            }
        } catch (Exception e) {
            view.mostrarError("Error al mostrar órdenes en proceso: " + e.getMessage());
            sistemaController.registrarError("Error en verOrdenesEnProceso", e);
        }
    }

    // Método que muestra las oredenes que aún no se iniciaron
    private void verOrdenesDisponibles() {
        try {
            List<OrdenProduccion> ordenesDisponibles = DataStore.getOrdenesProduccion().stream()
                    .filter(orden -> orden.isIniciado() == false)
                    .collect(Collectors.toList());

            view.mostrarOrdenesDisponibles(ordenesDisponibles);
        } catch (Exception e) {
            view.mostrarError("Error al mostrar órdenes disponibles: " + e.getMessage());
            sistemaController.registrarError("Error en verOrdenesDisponibles", e);
        }
    }

    private void gestionarCategoriasParadas() {
        boolean continuar = true;
        while (continuar) {
            try {
                int opcion = view.mostrarMenuCategoriaParadas();
                switch (opcion) {
                    case 1:
                        mostrarCategoriasParadas();
                        break;
                    case 2:
                        agregarCategoriaParada();
                        break;
                    case 3:
                        modificarCategoriaParada();
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
                sistemaController.registrarError("Error en gestión de categorías de paradas", e);
            }
        }
    }

    // Método que devuelve una lista de operarios asignados a una orden de
    // produccion especifica
    private List<OperarioEnOrden> obtenerOperariosDeOrden(OrdenProduccion orden) {
        return DataStore.getOperariosEnOrden().stream()
                .filter(o -> o.getOrdenProduccion().getId() == orden.getId())
                .collect(Collectors.toList());
    }

    // Métodos de gestión de categorías de paradas
    private void mostrarCategoriasParadas() {
        List<CategoriaParada> categorias = DataStore.getCategoriasParada();
        view.mostrarCategoriasParadas(categorias);
    }

    private void agregarCategoriaParada() {
        try {
            CategoriaParada nuevaCategoria = view.solicitarDatosCategoria();
            DataStore.addCategoriaParada(nuevaCategoria);
            view.mostrarMensaje("Categoría de parada agregada exitosamente");
        } catch (Exception e) {
            view.mostrarError("Error al agregar categoría: " + e.getMessage());
            sistemaController.registrarError("Error en agregarCategoriaParada", e);
        }
    }

    private void modificarCategoriaParada() {
        try {
            mostrarCategoriasParadas();
            int id = view.solicitarIdCategoria();
            CategoriaParada categoriaExistente = DataStore.getCategoriaParadaById(id);

            if (categoriaExistente != null) {
                CategoriaParada categoriaModificada = view.solicitarDatosModificacionCategoria(categoriaExistente);
                DataStore.actualizarCategoriaParada(categoriaModificada);
                view.mostrarMensaje("Categoría modificada exitosamente");
            } else {
                view.mostrarError("Categoría no encontrada");
            }
        } catch (Exception e) {
            view.mostrarError("Error al modificar categoría: " + e.getMessage());
            sistemaController.registrarError("Error en modificarCategoriaParada", e);
        }
    }

}
