package controller;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.*;
import util.DataStore;
import view.DirectorView;

public class DirectorController {
    private DirectorView view;
    private SistemaController sistemaController;
    private Usuario usuarioActual;

    public DirectorController(Usuario usuario) {
        this.view = new DirectorView();
        this.sistemaController = new SistemaController();
        this.usuarioActual = usuario;
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
                sistemaController.registrarError("Error en menú director", e);
            }
        }
    }

    private boolean procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                gestionarUsuarios();
                return true;
            case 2:
                gestionarProductos();
                return true;
            case 3:
                consultarEstadisticas();
                return true;
            case 4:
                gestionarCostos();
                return true;
            case 0:
                sistemaController.cerrarSesion();
                view.mostrarMensajeDespedida();
                return false;
            default:
                view.mostrarError("Opción inválida");
                return true;
        }
    }

    private void gestionarUsuarios() {
        boolean continuar = true;
        while (continuar) {
            int opcion = view.mostrarMenuUsuarios();
            switch (opcion) {
                case 1:
                    listarUsuarios();
                    break;
                case 2:
                    crearUsuario();
                    break;
                case 3:
                    modificarUsuario();
                    break;
                case 4:
                    eliminarUsuario();
                    break;
                case 0:
                    continuar = false;
                    break;
                default:
                    view.mostrarError("Opción inválida");
            }
        }
    }

    private void gestionarProductos() {
        boolean continuar = true;
        while (continuar) {
            try {
                int opcion = view.mostrarMenuProductos();
                switch (opcion) {
                    case 1:
                        agregarProducto();
                        break;
                    case 2:
                        modificarProducto();
                        break;
                    case 3:
                        eliminarProducto();
                        break;
                    case 4:
                        listarProductos();
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
                sistemaController.registrarError("Error en gestión de productos", e);
            }
        }
    }

    private void consultarEstadisticas() {
        boolean continuar = true;
        while (continuar) {
            int opcion = view.mostrarMenuEstadisticas();
            switch (opcion) {
                case 1:
                    mostrarEstadisticasProduccion();
                    break;
                case 2:
                    mostrarEstadisticasParadas();
                    break;
                case 3:
                    mostrarEstadisticasProductividad();
                    break;
                case 0:
                    continuar = false;
                    break;
                default:
                    view.mostrarError("Opción inválida");
            }
        }
    }

    private void gestionarCostos() {
        boolean continuar = true;
        while (continuar) {
            int opcion = view.mostrarMenuCostos();
            switch (opcion) {
                case 1:
                    registrarCosto();
                    break;
                case 2:
                    consultarCostos();
                    break;
                case 0:
                    continuar = false;
                    break;
                default:
                    view.mostrarError("Opción inválida");
            }
        }
    }

    // Métodos de gestión de usuarios
    private void listarUsuarios() {
        List<Usuario> usuarios = DataStore.getUsuarios();
        view.mostrarListaUsuarios(usuarios);
    }

    private void crearUsuario() {
        try {
            Usuario nuevoUsuario = view.solicitarDatosUsuario();
            DataStore.addUsuario(nuevoUsuario);
            view.mostrarMensajeExito("Usuario creado exitosamente");
        } catch (Exception e) {
            view.mostrarError("Error al crear usuario: " + e.getMessage());
        }
    }

    private void modificarUsuario() {
        try {
            int legajo = view.solicitarLegajo();
            Usuario usuario = sistemaController.buscarUsuarioPorLegajo(legajo);
            if (usuario != null) {
                Usuario usuarioModificado = view.solicitarDatosModificacion(usuario);
                DataStore.actualizarUsuario(usuarioModificado);
                view.mostrarMensajeExito("Usuario modificado exitosamente");
            } else {
                view.mostrarError("Usuario no encontrado");
            }
        } catch (Exception e) {
            view.mostrarError("Error al modificar usuario: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        try {
            int legajo = view.solicitarLegajo();
            Usuario usuario = sistemaController.buscarUsuarioPorLegajo(legajo);
            if (usuario != null && view.confirmarEliminacion()) {
                DataStore.eliminarUsuario(usuario.getLegajo());
                view.mostrarMensajeExito("Usuario eliminado exitosamente");
            } else {
                view.mostrarError("Usuario no encontrado o operación cancelada");
            }
        } catch (Exception e) {
            view.mostrarError("Error al eliminar usuario: " + e.getMessage());
        }
    }

    // Métodos de estadísticas
    // Método que calcula el tiempo promedio de producción para cada producto
    private void mostrarEstadisticasProduccion() {
        List<OrdenProduccion> ordenes = sistemaController.obtenerOrdenesFinalizadas();
        Map<Producto, Double> tiemposPromedio = new HashMap<>();

        for (Producto producto : DataStore.getProductos()) {
            double promedio = sistemaController.calcularTiempoPromedioProduccion(producto);
            tiemposPromedio.put(producto, promedio);
        }

        view.mostrarEstadisticasProduccion(ordenes, tiemposPromedio);
    }

    // Método que obtiene una lista de todas las paradas, las agrupa por categoría y
    // cuenta cuántas paradas hay en cada categoría
    private void mostrarEstadisticasParadas() {
        List<Parada> paradas = DataStore.getParadas();
        Map<CategoriaParada, Long> paradasPorCategoria = paradas.stream()
                .collect(Collectors.groupingBy(Parada::getCategoriaParada, Collectors.counting()));

        view.mostrarEstadisticasParadas(paradasPorCategoria);
    }

    // Método que recopila datos sobre las órdenes de producción finalizadas,
    // calcula el total de productos producidos y el tiempo total de producción, y
    // luego calcula la productividad
    private void mostrarEstadisticasProductividad() {
        // Obtiene la lista de órdenes de producción finalizadas
        List<OrdenProduccion> ordenesFinalizadas = sistemaController.obtenerOrdenesFinalizadas();

        // Calcula la productividad total (ejemplo: cantidad de productos producidos)
        int totalProductosProducidos = ordenesFinalizadas.stream()
                .mapToInt(OrdenProduccion::getCantidadProducida)
                .sum();

        // Calcula el tiempo total de producción
        long tiempoTotalProduccion = ordenesFinalizadas.stream()
                .mapToLong(OrdenProduccion::getTiempoProduccion)
                .sum();

        // Calcula la productividad como productos por hora (o cualquier otra métrica
        // relevante)
        double productividad = (tiempoTotalProduccion > 0)
                ? (double) totalProductosProducidos / (tiempoTotalProduccion / 3600)
                : 0;

        // Muestra las estadísticas de productividad en la vista
        view.mostrarEstadisticasProductividad(totalProductosProducidos, tiempoTotalProduccion, productividad);
    }

    // Métodos de gestión de costos
    private void registrarCosto() {
        try {
            Costo nuevoCosto = view.solicitarDatosCosto();
            if (sistemaController.registrarCosto(nuevoCosto.getMes(),
                    nuevoCosto.getAnio(),
                    nuevoCosto.getValor())) {
                view.mostrarMensajeExito("Costo registrado exitosamente");
            } else {
                view.mostrarError("Datos de costo inválidos");
            }
        } catch (Exception e) {
            view.mostrarError("Error al registrar costo: " + e.getMessage());
        }
    }

    private void consultarCostos() {
        List<Costo> costos = DataStore.getCostos();
        view.mostrarListaCostos(costos);
    }

    // Métodos de gestión de productos
    private void agregarProducto() {
        try {
            Producto nuevoProducto = view.solicitarDatosProducto();
            DataStore.addProducto(nuevoProducto);
            view.mostrarMensajeExito("Producto agregado exitosamente");
        } catch (Exception e) {
            view.mostrarError("Error al agregar producto: " + e.getMessage());
        }
    }

    private void modificarProducto() {
        try {
            listarProductos();
            int id = view.solicitarIdProducto();
            Producto productoExistente = DataStore.getProductoById(id);

            if (productoExistente != null) {
                Producto productoModificado = view.solicitarDatosModificacionProducto(productoExistente);
                DataStore.actualizarProducto(productoModificado);
                view.mostrarMensajeExito("Producto modificado exitosamente");
            } else {
                view.mostrarError("Producto no encontrado");
            }
        } catch (Exception e) {
            view.mostrarError("Error al modificar producto: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        try {
            listarProductos();
            int id = view.solicitarIdProducto();
            if (DataStore.eliminarProducto(id)) {
                view.mostrarMensajeExito("Producto eliminado exitosamente");
            } else {
                view.mostrarError("Producto no encontrado");
            }
        } catch (Exception e) {
            view.mostrarError("Error al eliminar producto: " + e.getMessage());
        }
    }

    private void listarProductos() {
        List<Producto> productos = DataStore.getProductos();
        view.mostrarProductos(productos);
    }
}
