package view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import model.*;
import util.DataStore;

public class DirectorView {
    private Scanner scanner;

    public DirectorView() {
        this.scanner = new Scanner(System.in);
    }

    public int mostrarMenuPrincipal() {
        limpiarPantalla();
        System.out.println("\n=== MENÚ DIRECTOR ===");
        System.out.println("1. Gestionar Usuarios");
        System.out.println("2. Gestionar Productos"); // Nueva opción
        System.out.println("3. Consultar Estadísticas");
        System.out.println("4. Gestionar Costos");
        System.out.println("0. Cerrar Sesión");
        System.out.print("Seleccione una opción: ");
        return scanner.nextInt();
    }

    public int mostrarMenuUsuarios() {
        System.out.println("\n=== GESTIÓN DE USUARIOS ===");
        System.out.println("1. Listar Usuarios");
        System.out.println("2. Crear Usuario");
        System.out.println("3. Modificar Usuario");
        System.out.println("4. Eliminar Usuario");
        System.out.println("0. Volver");
        System.out.print("\nSeleccione una opción: ");
        return scanner.nextInt();
    }

    public int mostrarMenuEstadisticas() {
        System.out.println("\n=== ESTADÍSTICAS ===");
        System.out.println("1. Estadísticas de Producción");
        System.out.println("2. Estadísticas de Paradas");
        System.out.println("3. Estadísticas de Productividad");
        System.out.println("0. Volver");
        System.out.print("\nSeleccione una opción: ");
        return scanner.nextInt();
    }

    public int mostrarMenuCostos() {
        System.out.println("\n=== GESTIÓN DE COSTOS ===");
        System.out.println("1. Registrar Costo");
        System.out.println("2. Consultar Costos");
        System.out.println("0. Volver");
        System.out.print("\nSeleccione una opción: ");
        return scanner.nextInt();
    }

    public void mostrarListaUsuarios(List<Usuario> usuarios) {
        System.out.println("\n=== LISTA DE USUARIOS ===");
        usuarios.forEach(u -> System.out.printf("Legajo: %d, Nombre: %s %s, Rol: %s%n",
                u.getLegajo(), u.getNombre(), u.getApellido(), u.getRol().getTipo()));
        esperarEnter();
    }

 // Método para solicitar datos de usuario
    public Usuario solicitarDatosUsuario() {
        scanner.nextLine(); // Limpiar buffer
        System.out.println("\nIngrese los datos del nuevo usuario:");
        System.out.print("DNI: ");
        int dni = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        // Mostrar roles disponibles
        List<Rol> roles = DataStore.getRoles();
        System.out.println("\nRoles disponibles:");
        roles.forEach(r -> System.out.println(r.getId() + ". " + r.getTipo()));
        System.out.print("Seleccione el ID del rol: ");
        int rolId = scanner.nextInt();

        Rol rolSeleccionado = roles.stream()
                .filter(r -> r.getId() == rolId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rol no válido"));
        int nuevoLegajo = DataStore.getUsuarios().size() + 1;

        return new Usuario(nuevoLegajo, dni, nombre, apellido, password, rolSeleccionado);
    }

    public void mostrarEstadisticasProduccion(List<OrdenProduccion> ordenes,
            Map<Producto, Double> tiemposPromedio) {
        System.out.println("\n=== ESTADÍSTICAS DE PRODUCCIÓN ===");
        System.out.println("Órdenes completadas: " + ordenes.size());

        System.out.println("\nTiempos promedio por producto:");
        tiemposPromedio
                .forEach((producto, tiempo) -> System.out.printf("%s: %.2f minutos%n", producto.getNombre(), tiempo));

        esperarEnter();
    }

    public void mostrarEstadisticasParadas(Map<CategoriaParada, Long> paradasPorCategoria) {
        System.out.println("\n=== ESTADÍSTICAS DE PARADAS ===");
        paradasPorCategoria.forEach(
                (categoria, cantidad) -> System.out.printf("%s: %d paradas%n", categoria.getDescripcion(), cantidad));
        esperarEnter();
    }

    public void mostrarEstadisticasProductividad(/* parámetros necesarios */) {
        System.out.println("\n=== ESTADÍSTICAS DE PRODUCTIVIDAD ===");
        // Mostrar datos de productividad
        esperarEnter();
    }

    public Costo solicitarDatosCosto() {
        System.out.println("\nIngrese los datos del costo:");
        System.out.print("Mes (1-12): ");
        int mes = scanner.nextInt();
        System.out.print("Año: ");
        int anio = scanner.nextInt();
        System.out.print("Monto: ");
        double monto = scanner.nextDouble();

        Costo costo = new Costo();
        costo.setMes(mes);
        costo.setAnio(anio);
        costo.setValor(monto);
        return costo;
    }

    public void mostrarListaCostos(List<Costo> costos) {
        System.out.println("\n=== LISTA DE COSTOS ===");
        costos.forEach(c -> System.out.printf("Mes: %d/%d, Monto: $%.2f%n",
                c.getMes(), c.getAnio(), c.getValor()));
        esperarEnter();
    }

    public void mostrarOrdenesProduccion(List<OrdenProduccion> ordenesEnProceso,
            List<OrdenProduccion> ordenesFinalizadas) {
        System.out.println("\n=== ÓRDENES EN PROCESO ===");
        ordenesEnProceso.forEach(this::mostrarDetalleOrden);

        System.out.println("\n=== ÓRDENES FINALIZADAS ===");
        ordenesFinalizadas.forEach(this::mostrarDetalleOrden);

        esperarEnter();
    }

    private void mostrarDetalleOrden(OrdenProduccion orden) {
        System.out.printf("ID: %d, Producto: %s, Inicio: %s, Estado: %s%n",
                orden.getId(),
                orden.getProducto().getNombre(),
                formatearFecha(orden.getInicio()),
                obtenerEstadoOrden(orden));
    }

    public void mostrarListaParadas(List<Parada> paradas) {
        System.out.println("\n=== LISTA DE PARADAS ===");
        paradas.forEach(p -> System.out.printf(
                "Orden #%d, Categoría: %s, Inicio: %s, Duración: %d minutos%n",
                p.getOrdenProduccion().getId(),
                p.getCategoriaParada().getDescripcion(),
                formatearFecha(p.getInicio()),
                p.getDuracionMinutos()));
        esperarEnter();
    }

    public int solicitarLegajo() {
        System.out.print("\nIngrese el legajo del usuario: ");
        return scanner.nextInt();
    }

    public Usuario solicitarDatosModificacion(Usuario usuario) {
        scanner.nextLine(); // Limpiar buffer
        System.out.println("\nModificando usuario (deje en blanco para mantener el valor actual)");

        System.out.printf("Nombre actual: %s%n", usuario.getNombre());
        System.out.print("Nuevo nombre: ");
        String nombre = scanner.nextLine();
        if (!nombre.trim().isEmpty()) {
            usuario.setNombre(nombre);
        }

        System.out.printf("Apellido actual: %s%n", usuario.getApellido());
        System.out.print("Nuevo apellido: ");
        String apellido = scanner.nextLine();
        if (!apellido.trim().isEmpty()) {
            usuario.setApellido(apellido);
        }

        System.out.print("Nueva contraseña (Enter para mantener la actual): ");
        String password = scanner.nextLine();
        if (!password.trim().isEmpty()) {
            usuario.setPassword(password);
        }

        System.out.printf("Rol actual: %s%n", usuario.getRol().getTipo());
        System.out.print("¿Desea cambiar el rol? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            List<Rol> roles = DataStore.getRoles();
            System.out.println("\nRoles disponibles:");
            roles.forEach(r -> System.out.println(r.getId() + ". " + r.getTipo()));
            System.out.print("Seleccione el ID del nuevo rol: ");
            int rolId = scanner.nextInt();

            Rol nuevoRol = roles.stream()
                    .filter(r -> r.getId() == rolId)
                    .findFirst()
                    .orElse(usuario.getRol());

            usuario.setRol(nuevoRol);
        }

        return usuario;
    }

    public boolean confirmarEliminacion() {
        System.out.print("¿Está seguro que desea eliminar este usuario? (S/N): ");
        scanner.nextLine(); // Limpiar buffer
        return scanner.nextLine().equalsIgnoreCase("S");
    }

    public void mostrarMensajeExito(String mensaje) {
        System.out.println("\n✓ " + mensaje);
        esperarEnter();
    }

    public void mostrarError(String mensaje) {
        System.out.println("\n✗ Error: " + mensaje);
        esperarEnter();
    }

    public void mostrarMensajeDespedida() {
        System.out.println("\nCerrando sesión...");
        esperarEnter();
    }

    public void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void limpiarBuffer() {
        scanner.nextLine();
    }

    private void esperarEnter() {
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }

    private String formatearFecha(LocalDateTime fecha) {
        if (fecha == null)
            return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fecha.format(formatter);
    }

    private String obtenerEstadoOrden(OrdenProduccion orden) {
        if (orden.isTerminado())
            return "Finalizada";
        if (orden.isIniciado())
            return "En Proceso";
        return "Pendiente";
    }

    public void cerrar() {
        if (scanner != null) {
            scanner.close();
        }
    }

    public int mostrarMenuProductos() {
        System.out.println("\n=== GESTIÓN DE PRODUCTOS ===");
        System.out.println("1. Agregar Producto");
        System.out.println("2. Modificar Producto");
        System.out.println("3. Eliminar Producto");
        System.out.println("4. Listar Productos");
        System.out.println("0. Volver");
        System.out.print("Seleccione una opción: ");
        return scanner.nextInt();
    }

    public Producto solicitarDatosProducto() {
        scanner.nextLine(); // Limpiar buffer
        System.out.println("\nIngrese los datos del nuevo producto:");

        // Generar nuevo ID basado en el máximo existente
        int nuevoId = DataStore.getProductos().size() + 1;

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Cantidad: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        List<Unidad> unidades = DataStore.getUnidades();
        System.out.println("\nUnidades disponibles:");
        unidades.forEach(u -> System.out.println(u.getId() + ". " + u.getNombre()));
        System.out.print("Seleccione el ID de la unidad: ");
        int unidadId = scanner.nextInt();

        Unidad unidadSeleccionada = unidades.stream()
                .filter(u -> u.getId() == unidadId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unidad no válida"));

        return new Producto(nuevoId, nombre, cantidad, unidadSeleccionada);
    }

    public void mostrarProductos(List<Producto> productos) {
        if (productos.isEmpty()) {
            System.out.println("\nNo hay productos registrados.");
            return;
        }

        System.out.println("\n=== LISTA DE PRODUCTOS ===");
        System.out.printf("%-5s %-20s%n", "ID", "Nombre");
        System.out.println("--------------------------------------------------");
        for (Producto producto : productos) {
            System.out.printf("%-5d %-20s%n",
                    producto.getId(),
                    producto.getNombre());
        }
    }

    public int solicitarIdProducto() {
        System.out.print("\nIngrese el ID del producto: ");
        return scanner.nextInt();
    }

    public Producto solicitarDatosModificacionProducto(Producto productoExistente) {
        scanner.nextLine(); // Limpiar buffer
        System.out.println("\nIngrese los nuevos datos del producto (Enter para mantener el valor actual):");

        System.out.print("Nombre actual [" + productoExistente.getNombre() + "]: ");
        String nombre = scanner.nextLine();
        nombre = nombre.isEmpty() ? productoExistente.getNombre() : nombre;

        System.out.print("Cantidad actual [" + productoExistente.getCantidad()
                + productoExistente.getUnidad().getNombre() + "]: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();
        cantidad = cantidad == 0 ? productoExistente.getCantidad() : cantidad;

        return new Producto(productoExistente.getId(), nombre, cantidad, productoExistente.getUnidad());
    }

    public void mostrarEstadisticasProductividad(int totalProductosProducidos, long tiempoTotalProduccion,
            double productividad) {
        // Convertir tiempo total de producción a horas y minutos
        long horas = tiempoTotalProduccion / 3600;
        long minutos = (tiempoTotalProduccion % 3600) / 60;

        // Imprimir las estadísticas en la consola
        System.out.println("Estadísticas de Productividad:");
        System.out.printf("Total Productos Producidos: %d%n", totalProductosProducidos);
        System.out.printf("Tiempo Total de Producción: %d horas y %d minutos%n", horas, minutos);
        System.out.printf("Productividad: %.2f productos por hora%n", productividad);
    }
}