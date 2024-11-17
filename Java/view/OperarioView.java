package view;

import java.util.List;
import java.util.Scanner;

import model.*;

public class OperarioView {
    private Scanner scanner = new Scanner(System.in);

    public int mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ OPERARIO ===");
        System.out.println("1. Iniciar Orden de Producción");
        System.out.println("2. Gestionar Orden Activa");
        System.out.println("3. Ver Mi Orden Actual");
        System.out.println("0. Cerrar Sesión");
        System.out.print("Seleccione una opción: ");
        return scanner.nextInt();
    }

    public int mostrarMenuGestionOrden() {
        System.out.println("\n=== GESTIÓN DE ORDEN ACTIVA ===");
        System.out.println("1. Iniciar Nueva Etapa");
        System.out.println("2. Finalizar Etapa Actual");
        System.out.println("3. Registrar Parada");
        System.out.println("4. Finalizar Parada Actual");
        System.out.println("5. Finalizar Orden de Producción");
        System.out.println("0. Volver");
        System.out.print("Seleccione una opción: ");
        return scanner.nextInt();
    }

    public void mostrarOrdenesDisponibles(List<OrdenProduccion> ordenes) {
        if (ordenes.isEmpty()) {
            System.out.println("\nNo hay órdenes disponibles para iniciar.");
            return;
        }

        System.out.println("\n=== ÓRDENES DISPONIBLES ===");
        System.out.printf("%-5s %-20s %-30s %-15s%n",
                "ID", "Producto", "Lote", "Estado");
        System.out.println("----------------------------------------------------------");

        for (OrdenProduccion orden : ordenes) {
            System.out.printf("%-5d %-20s %-30s %-15s%n",
                    orden.getId(),
                    orden.getProducto().getNombre(),
                    orden.getLote(),
                    orden.getEstado());
        }
    }

    public void mostrarOrdenActual(OrdenProduccion orden, OperarioEnOrden etapaActual, String estado) {
        System.out.println("\n=== ORDEN ACTUAL ===");
        System.out.println("ID: " + orden.getId());
        System.out.println("Producto: " + orden.getProducto().getNombre());
        System.out.println("Estado: " + estado);
        if (orden.getInicio() != null) {
            System.out.println("Fecha Inicio: " + orden.getInicio());
        }
        if (orden.getFin() != null) {
            System.out.println("Fecha Fin: " + orden.getFin());
        }
        
        if (etapaActual != null) {
            System.out.println("\nEtapa Actual:");
            System.out.println("Descripción: " + etapaActual.getEtapa().getNombre());
            System.out.println("Inicio: " + etapaActual.getInicio());
        }
    }

    public void mostrarCategoriasParada(List<CategoriaParada> categorias) {
        System.out.println("\n=== CATEGORÍAS DE PARADA ===");
        System.out.printf("%-5s %-30s%n", "ID", "Descripción");
        System.out.println("------------------------------------");

        for (CategoriaParada categoria : categorias) {
            System.out.printf("%-5d %-30s%n",
                    categoria.getId(),
                    categoria.getDescripcion());
        }
    }

    public int solicitarIdOrden() {
        System.out.print("\nIngrese el ID de la orden: ");
        return scanner.nextInt();
    }

    public String solicitarDescripcionEtapa() {
        scanner.nextLine(); // Limpiar buffer
        System.out.print("\nIngrese descripción de la etapa: ");
        return scanner.nextLine();
    }

    public int solicitarCategoriaParada() {
        System.out.print("\nIngrese el ID de la categoría de parada: ");
        return scanner.nextInt();
    }

    public String solicitarDescripcionParada() {
        scanner.nextLine(); // Limpiar buffer
        System.out.print("\nIngrese descripción de la parada: ");
        return scanner.nextLine();
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

    public void mostrarHistorialEtapas(List<OperarioEnOrden> etapasOrden) {
        if (etapasOrden.isEmpty()) {
            System.out.println("\nNo hay etapas registradas.");
            return;
        }

        System.out.println("\n=== HISTORIAL DE ETAPAS ===");
        System.out.printf("%-20s %-15s %-20s %-20s%n", 
            "Operario", "Etapa", "Fecha Inicio", "Fecha Fin");
        System.out.println("-------------------------------------------------------------------------");
        
        for (OperarioEnOrden oeo : etapasOrden) {
            System.out.printf("%-20s %-15s %-20s %-20s%n",
                oeo.getUsuario().getNombre(),
                oeo.getEtapa().getNombre(),
                oeo.getInicio(),
                oeo.getFin() != null ? oeo.getFin() : "En proceso");
        }
    }
}
