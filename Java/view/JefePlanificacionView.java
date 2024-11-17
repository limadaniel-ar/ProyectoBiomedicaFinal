package view;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.*;
import util.DataStore;

public class JefePlanificacionView {
    private Scanner scanner = new Scanner(System.in);

    public int mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ JEFE DE PLANIFICACIÓN ===");
        System.out.println("1. Gestionar Órdenes de Producción");
        System.out.println("2. Ver Estadísticas de Tiempos de Producción");
        System.out.println("0. Cerrar Sesión");
        System.out.print("Seleccione una opción: ");
        return scanner.nextInt();
    }

    public int mostrarMenuOrdenes() {
        System.out.println("\n=== GESTIÓN DE ÓRDENES DE PRODUCCIÓN ===");
        System.out.println("1. Crear Nueva Orden");
        System.out.println("2. Ver Todas las Órdenes");
        System.out.println("0. Volver");
        System.out.print("Seleccione una opción: ");
        return scanner.nextInt();
    }

    public OrdenProduccion solicitarDatosOrden(List<Producto> productos, List<Area> areas) {
        scanner.nextLine(); // Limpiar buffer

        // Mostrar productos disponibles
        System.out.println("\n=== PRODUCTOS DISPONIBLES ===");
        System.out.printf("%-5s %-20s%n", "ID", "Nombre");
        System.out.println("-------------------------");
        for (Producto producto : productos) {
            System.out.printf("%-5d %-20s%n", producto.getId(), producto.getNombre());
        }

        // Solicitar datos de la orden
        System.out.println("\nIngrese los datos de la nueva orden:");

        System.out.print("ID del Producto: ");
        int productoId = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        System.out.print("Número de Lote: ");
        int lote = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        System.out.println("\n=== AREAS DISPONIBLES ===");
        System.out.printf("%-5s %-20s%n", "ID", "Nombre");
        System.out.println("-------------------------");
        for (Area area : areas) {
            System.out.printf("%-5d %-20s%n", area.getId(), area.getNombre());
        }

        System.out.print("ID del Area: ");
        int areaId = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        // Generar ID único para la orden
        int nuevoId = DataStore.getOrdenesProduccion().stream()
                .mapToInt(OrdenProduccion::getId)
                .max()
                .orElse(0) + 1;

        Producto productoSeleccionado = productos.stream()
                .filter(p -> p.getId() == productoId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Producto no válido"));

        Area areaSeleccionada = areas.stream()
                .filter(a -> a.getId() == areaId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Area no válida"));

        // Crear nueva orden
        return new OrdenProduccion(nuevoId, lote, productoSeleccionado, areaSeleccionada);
    }

    public void mostrarOrdenes(List<OrdenProduccion> ordenes) {
        if (ordenes.isEmpty()) {
            System.out.println("\nNo hay órdenes registradas.");
            return;
        }

        System.out.println("\n=== ÓRDENES DE PRODUCCIÓN ===");
        System.out.printf("%-5s %-20s %-15s %-20s %-20s%n",
                "ID", "Producto", "Estado", "Fecha Inicio", "Fecha Fin");
        System.out.println("------------------------------------------------------------------------");

        for (OrdenProduccion orden : ordenes) {
            System.out.printf("%-5d %-20s %-15s %-20s %-20s%n",
                    orden.getId(),
                    orden.getProducto().getNombre(),
                    orden.getEstado(),
                    orden.getInicio(),
                    orden.getFin() != null ? orden.getFin() : "En proceso");
        }
    }

    public void mostrarEstadisticasTiempos(Map<Producto, Duration> promediosPorProducto) {
        System.out.println("\n=== ESTADÍSTICAS DE TIEMPOS DE PRODUCCIÓN ===");
        System.out.printf("%-20s %-20s%n", "Producto", "Tiempo Promedio");
        System.out.println("----------------------------------------");

        for (Map.Entry<Producto, Duration> entry : promediosPorProducto.entrySet()) {
            System.out.printf("%-20s %-20s%n",
                    entry.getKey().getNombre(),
                    formatDuration(entry.getValue()));
        }
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%d horas %d minutos", hours, minutes);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("\n" + mensaje);
    }

    public void mostrarError(String error) {
        System.out.println("\nError: " + error);
    }

    public void limpiarBuffer() {
        scanner.nextLine();
    }
}
