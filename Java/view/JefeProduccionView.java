package view;

import java.util.*;

import model.*;
import util.DataStore;

public class JefeProduccionView {
    private Scanner scanner = new Scanner(System.in);

    public int mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ JEFE DE PRODUCCIÓN ===");
        System.out.println("1. Ver Órdenes en Proceso");
        System.out.println("2. Ver Órdenes Disponibles");
        System.out.println("3. Gestionar Categorías de Paradas");
        System.out.println("0. Cerrar Sesión");
        System.out.print("Seleccione una opción: ");
        return scanner.nextInt();
    }

    public void mostrarOrdenesEnProceso(List<OrdenProduccion> ordenes) {
        if (ordenes.isEmpty()) {
            System.out.println("\nNo hay órdenes en proceso actualmente.");
            return;
        }

        System.out.println("\n=== ÓRDENES EN PROCESO ===");
        System.out.printf("%-10s %-20s %-20s %-20s %-15s%n", 
            "ID", "Producto", "Fecha Inicio", "Etapa Actual", "Estado");
        System.out.println("--------------------------------------------------------------------------------");
        
        for (OrdenProduccion orden : ordenes) {
            // Obtener la etapa actual
            OperarioEnOrden etapaActual = DataStore.getOperariosEnOrden().stream()
                .filter(oeo -> oeo.getOrdenProduccion().getId() == orden.getId())
                .filter(oeo -> oeo.getInicio() != null)  // Filtrar entradas con fecha null
                .sorted((o1, o2) -> o2.getInicio().compareTo(o1.getInicio()))  // Ordenar por fecha descendente
                .findFirst()  // Obtener la más reciente
                .orElse(null);

            String etapaNombre = etapaActual != null ? etapaActual.getEtapa().getNombre() : "Sin etapa";
            String estado = orden.isTerminado() ? "Finalizada" : 
                            (orden.isPausado() ? "Pausada" : "En proceso");

            System.out.printf("%-10d %-20s %-20s %-20s %-15s%n",
                orden.getId(),
                orden.getProducto().getNombre(),
                orden.getInicio(),
                etapaNombre,
                estado);
        }
    }
    
    

    public void mostrarDetalleOrdenEnProceso(OrdenProduccion orden, List<OperarioEnOrden> operarios) {
        System.out.println("\n=== DETALLE DE ORDEN #" + orden.getId() + " ===");
        System.out.println("Producto: " + orden.getProducto().getNombre());
        System.out.println("Estado: " + orden.getEstado());

        System.out.println("\nOperarios Asignados:");
        if (operarios.isEmpty()) {
            System.out.println("No hay operarios asignados a esta orden.");
        } else {
            System.out.printf("%-10s %-20s %-20s%n", "Legajo", "Nombre", "Apellido");
            System.out.println("--------------------------------------------------");
            for (OperarioEnOrden operario : operarios) {
                System.out.printf("%-10d %-20s %-20s%n",
                        operario.getUsuario().getLegajo(),
                        operario.getUsuario().getNombre(),
                        operario.getUsuario().getApellido());
            }
        }
    }

    public void mostrarOrdenesDisponibles(List<OrdenProduccion> ordenes) {
        if (ordenes.isEmpty()) {
            System.out.println("\nNo hay órdenes disponibles para iniciar.");
            return;
        }

        System.out.println("\n=== ÓRDENES DISPONIBLES ===");
        System.out.printf("%-5s %-20s %-15s%n",
                "ID", "Producto", "Estado");
        System.out.println("----------------------------------------");

        for (OrdenProduccion orden : ordenes) {
            System.out.printf("%-5d %-20s %-15s%n",
                    orden.getId(),
                    orden.getProducto().getNombre(),
                    orden.getEstado());
        }
    }

    public int mostrarMenuCategoriaParadas() {
        System.out.println("\n=== GESTIÓN DE CATEGORÍAS DE PARADAS ===");
        System.out.println("1. Ver Categorías");
        System.out.println("2. Agregar Nueva Categoría");
        System.out.println("3. Modificar Categoría");
        System.out.println("0. Volver");
        System.out.print("Seleccione una opción: ");
        return scanner.nextInt();
    }

    public void mostrarCategoriasParadas(List<CategoriaParada> categorias) {
        if (categorias.isEmpty()) {
            System.out.println("\nNo hay categorías de paradas registradas.");
            return;
        }

        System.out.println("\n=== CATEGORÍAS DE PARADAS ===");
        System.out.printf("%-5s %-30s %-30s%n",
                "ID", "Nombre", "Descripción");
        System.out.println("----------------------------------------------------");

        for (CategoriaParada categoria : categorias) {
            System.out.printf("%-5d %-30s%n",
                    categoria.getId(),
                    categoria.getDescripcion());
        }
    }

    public CategoriaParada solicitarDatosCategoria() {
        scanner.nextLine(); // Limpiar buffer
        System.out.println("\nIngrese los datos de la nueva categoría:");

        // Generar nuevo ID
        int nuevoId = DataStore.getCategoriasParada().size() + 1;

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        return new CategoriaParada(nuevoId, descripcion);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("\n" + mensaje);
    }

    public void mostrarError(String error) {
        System.out.println("\nError: " + error);
    }

    public int solicitarIdCategoria() {
        System.out.print("\nIngrese el ID de la categoría: ");
        return scanner.nextInt();
    }

    public CategoriaParada solicitarDatosModificacionCategoria(CategoriaParada categoriaExistente) {
        scanner.nextLine(); // Limpiar buffer
        System.out.println("\nIngrese los nuevos datos (Enter para mantener el valor actual):");

        System.out.print("Descripción actual [" + categoriaExistente.getDescripcion() + "]: ");
        String descripcion = scanner.nextLine();
        descripcion = descripcion.isEmpty() ? categoriaExistente.getDescripcion() : descripcion;

        return new CategoriaParada(categoriaExistente.getId(), descripcion);
    }

    public void limpiarBuffer() {
        scanner.nextLine(); // Limpiar buffer
    }
}
