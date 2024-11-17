package controller;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

import model.CategoriaParada;
import model.Etapa;
import model.OperarioEnOrden;
import model.OrdenProduccion;
import model.Parada;
import model.Usuario;
import util.DataStore;
import view.OperarioView;

public class OperarioController {
    private OperarioView view;
    private SistemaController sistemaController;
    private Usuario usuarioActual;
    private OrdenProduccion ordenActiva;
    private OperarioEnOrden etapaActual;
    private Parada paradaActual;

    public OperarioController(Usuario usuarioActual) {
        this.sistemaController = new SistemaController();
        this.usuarioActual = usuarioActual;
        this.view = new OperarioView();
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
                sistemaController.registrarError("Error en OperarioController", e);
            }
        }
    }

    private boolean procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                iniciarOrdenProduccion();
                return true;
            case 2:
                gestionarOrdenActiva();
                return true;
            case 3:
                verOrdenActual();
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

    // Método para seleccionar e iniciar una orden de producción que aún no fue
    // iniciada
    private void iniciarOrdenProduccion() {
        // Obtener órdenes que no han sido iniciadas (iniciado = false)
        List<OrdenProduccion> ordenesDisponibles = DataStore.getOrdenesProduccion().stream()
                .filter(orden -> !orden.isIniciado() && !orden.isTerminado())
                .collect(Collectors.toList());

        view.mostrarOrdenesDisponibles(ordenesDisponibles);

        if (ordenesDisponibles.isEmpty()) {
            view.mostrarError("No hay órdenes disponibles para iniciar");
            return;
        }

        int idOrden = view.solicitarIdOrden();
        ordenActiva = ordenesDisponibles.stream()
                .filter(orden -> orden.getId() == idOrden)
                .findFirst()
                .orElse(null);

        if (ordenActiva == null) {
            view.mostrarError("Orden no encontrada");
            return;
        }

        ordenActiva.setIniciado(true);
        ordenActiva.setPausado(false);
        ordenActiva.setTerminado(false);

        view.mostrarMensaje("Orden de producción seleccionada exitosamente");
    }

    private void gestionarOrdenActiva() {
        if (ordenActiva == null) {
            view.mostrarError("No tiene ninguna orden activa");
            return;
        }

        boolean continuar = true;
        while (continuar) {
            try {
                int opcion = view.mostrarMenuGestionOrden();
                switch (opcion) {
                    case 1:
                        iniciarEtapa();
                        break;
                    case 2:
                        finalizarEtapa();
                        break;
                    case 3:
                        registrarParada();
                        break;
                    case 4:
                        finalizarParada();
                        break;
                    case 5:
                        finalizarOrden();
                        continuar = false;
                        break;
                    case 0:
                        continuar = false;
                        break;
                    default:
                        view.mostrarError("Opción inválida");
                }
            } catch (Exception e) {
                view.mostrarError("Error: " + e.getMessage());
            }
        }
    }

    // Métodos de gestión de etapas
    private void iniciarEtapa() {
        if (etapaActual != null) {
            view.mostrarError("Ya tiene una etapa activa. Debe finalizarla antes de iniciar una nueva.");
            return;
        }

        // Obtener la última etapa registrada para esta orden (si existe)
        OperarioEnOrden ultimaEtapa = DataStore.getOperariosEnOrden().stream()
                .filter(oeo -> oeo.getOrdenProduccion().getId() == ordenActiva.getId())
                .max(Comparator.comparing(oeo -> oeo.getEtapa().getId()))
                .orElse(null);

        // Determinar qué etapa corresponde iniciar
        Etapa etapaAIniciar;
        if (ultimaEtapa == null) {
            // Si no hay etapas, debe iniciar Preparación
            etapaAIniciar = DataStore.getEtapas().stream()
                    .filter(e -> e.getId() == 1)
                    .findFirst()
                    .orElse(null);

            // Registrar el inicio de la orden
            ordenActiva.setInicio(LocalDateTime.now());

        } else if (ultimaEtapa.getFin() == null) {
            view.mostrarError("Debe finalizar la etapa actual antes de iniciar una nueva");
            return;
        } else {
            // Buscar la siguiente etapa según el ID
            int siguienteId = ultimaEtapa.getEtapa().getId() + 1;
            if (siguienteId > 3) {
                view.mostrarError("La orden ya ha completado todas sus etapas");
                return;
            }
            etapaAIniciar = DataStore.getEtapas().stream()
                    .filter(e -> e.getId() == siguienteId)
                    .findFirst()
                    .orElse(null);
        }
        int idOpEnOrden = DataStore.getOperariosEnOrden().size() + 1;

        // Crear nuevo registro de OperarioEnOrden
        etapaActual = new OperarioEnOrden(idOpEnOrden, ordenActiva, usuarioActual, etapaAIniciar, LocalDateTime.now());

        DataStore.getOperariosEnOrden().add(etapaActual);
        view.mostrarMensaje("Etapa de " + etapaAIniciar.getNombre() + " iniciada exitosamente");
    }

    private void finalizarEtapa() {
        if (etapaActual == null) {
            view.mostrarError("No hay etapa activa para finalizar");
            return;
        }

        LocalDateTime ahora = LocalDateTime.now();
        etapaActual.setFin(ahora);

        // Si es la etapa de Finalización (ID = 3)
        if (etapaActual.getEtapa().getId() == 3) {
            ordenActiva.setFin(ahora);
            ordenActiva.setTerminado(true);
            ordenActiva = null;
        }

        etapaActual = null;
        view.mostrarMensaje("Etapa finalizada exitosamente");
    }

    // Métodos de gestión de paradas
    private void registrarParada() {
        if (paradaActual != null) {
            view.mostrarError("Ya tiene una parada activa");
            return;
        }

        List<CategoriaParada> categorias = DataStore.getCategoriasParada();
        view.mostrarCategoriasParada(categorias);

        try {
            int idCategoria = view.solicitarCategoriaParada();
            CategoriaParada categoria = categorias.stream()
                    .filter(c -> c.getId() == idCategoria)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

            String descripcion = view.solicitarDescripcionParada();
            int idParada = DataStore.getParadas().size() + 1;
            paradaActual = new Parada(idParada, ordenActiva, LocalDateTime.now(), categoria, descripcion);
            DataStore.addParada(paradaActual);
            view.mostrarMensaje("Parada registrada exitosamente");
        } catch (Exception e) {
            view.mostrarError("Error al registrar la parada: " + e.getMessage());
        }
    }

    private void finalizarParada() {
        if (paradaActual == null) {
            view.mostrarError("No hay parada activa para finalizar");
            return;
        }

        paradaActual.setFin(LocalDateTime.now());
        paradaActual = null;
        view.mostrarMensaje("Parada finalizada exitosamente");
    }

    private void finalizarOrden() {
        if (etapaActual != null) {
            view.mostrarError("Debe finalizar la etapa actual antes de finalizar la orden");
            return;
        }

        if (paradaActual != null) {
            view.mostrarError("Debe finalizar la parada actual antes de finalizar la orden");
            return;
        }

        ordenActiva.setTerminado(true);
        ordenActiva.setFin(LocalDateTime.now());
        ordenActiva = null;
        view.mostrarMensaje("Orden finalizada exitosamente");
    }

    private void verOrdenActual() {
        if (ordenActiva == null) {
            view.mostrarMensaje("No hay orden activa");
            return;
        }

        String estado;
        if (ordenActiva.isTerminado()) {
            estado = "Finalizada";
        } else if (ordenActiva.isIniciado()) {
            if (ordenActiva.isPausado()) {
                estado = "Pausada";
            } else {
                estado = "En proceso";
            }
        } else {
            estado = "Pendiente";
        }

        view.mostrarOrdenActual(ordenActiva, etapaActual, estado);
    }
}
