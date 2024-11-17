package util;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class DataStore {

    // Usuarios
    public static List<Usuario> getUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT * FROM Usuario";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("legajo"),
                        rs.getInt("DNI"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("password"),
                        getRolById(rs.getInt("rolID")));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public static Usuario getUsuarioByLegajo(int legajo) {
        String query = "SELECT * FROM Usuario WHERE legajo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, legajo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("legajo"),
                            rs.getInt("DNI"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("password"),
                            getRolById(rs.getInt("rolID")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addUsuario(Usuario usuario) {
        String query = "INSERT INTO Usuario (legajo, DNI, nombre, apellido, password, rolID) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, usuario.getLegajo());
            pstmt.setInt(2, usuario.getDni());
            pstmt.setString(3, usuario.getNombre());
            pstmt.setString(4, usuario.getApellido());
            pstmt.setString(5, usuario.getPassword());
            pstmt.setInt(6, usuario.getRol().getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void actualizarUsuario(Usuario usuario) {
        String query = "UPDATE Usuario SET DNI = ?, nombre = ?, apellido = ?, password = ?, rolID = ? WHERE legajo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, usuario.getDni());
            pstmt.setString(2, usuario.getNombre());
            pstmt.setString(3, usuario.getApellido());
            pstmt.setString(4, usuario.getPassword());
            pstmt.setInt(5, usuario.getRol().getId());
            pstmt.setInt(6, usuario.getLegajo());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void eliminarUsuario(int legajo) {
        String query = "DELETE FROM Usuario WHERE legajo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, legajo);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Usuario con legajo " + legajo + " ha sido eliminado exitosamente.");
            } else {
                System.out.println("No se encontró un usuario con legajo " + legajo + " para eliminar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al intentar eliminar el usuario con legajo " + legajo + ": " + e.getMessage());
        }
    }

    // Roles
    private static Rol getRolById(int id) {
        String query = "SELECT * FROM Rol WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Rol(rs.getInt("ID"), rs.getString("tipo"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Rol> getRoles() {
        List<Rol> roles = new ArrayList<>();
        String query = "SELECT * FROM Rol";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                roles.add(new Rol(rs.getInt("ID"), rs.getString("tipo")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    // Productos
    public static List<Producto> getProductos() {
        List<Producto> productos = new ArrayList<>();
        String query = "SELECT * FROM Producto";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("ID"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        getUnidadById(rs.getInt("unidadID")));
                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }

    public static Producto getProductoById(int id) {
        String query = "SELECT * FROM Producto WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("ID"),
                            rs.getString("nombre"),
                            rs.getInt("cantidad"),
                            getUnidadById(rs.getInt("unidadID")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addProducto(Producto producto) {
        String query = "INSERT INTO Producto (ID, nombre, cantidad, unidadID) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, producto.getId());
            pstmt.setString(2, producto.getNombre());
            pstmt.setInt(3, producto.getCantidad());
            pstmt.setInt(4, producto.getUnidad().getId());

            pstmt.executeUpdate();
            System.out.println("Producto agregado exitosamente: " + producto.getNombre());

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Ya existe un producto con el ID " + producto.getId());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al agregar el producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void actualizarProducto(Producto nuevoProducto) throws SQLException {
        String query = "UPDATE Producto SET nombre = ? , cantidad = ?, unidadID = ? WHERE ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nuevoProducto.getNombre());
            pstmt.setInt(2, nuevoProducto.getCantidad());
            pstmt.setInt(3, nuevoProducto.getUnidad().getId());
            pstmt.setInt(4, nuevoProducto.getId());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException(
                        "No se encontró ningún producto con el ID: " + nuevoProducto.getId());
            }

        } catch (SQLException e) {
            throw e;
        }
    }

    public static boolean eliminarProducto(int id) {
        String query = "DELETE FROM Producto WHERE ID = ?";
        boolean eliminadoExitosamente = false;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Producto con ID " + id + " ha sido eliminado exitosamente.");
                eliminadoExitosamente = true;
            } else {
                System.out.println("No se encontró un Producto con ID " + id + " para eliminar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al intentar eliminar el Producto con ID " + id + ": " + e.getMessage());
        }

        return eliminadoExitosamente;
    }

    // Unidades
    private static Unidad getUnidadById(int id) {
        String query = "SELECT * FROM Unidad WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Unidad(rs.getInt("ID"), rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Unidad> getUnidades() {
        List<Unidad> unidades = new ArrayList<>();
        String query = "SELECT * FROM Unidad";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Unidad unidad = new Unidad(
                        rs.getInt("ID"),
                        rs.getString("Nombre"));
                unidades.add(unidad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return unidades;
    }

    // Órdenes de Producción
    public static List<OrdenProduccion> getOrdenesProduccion() {
        List<OrdenProduccion> ordenes = new ArrayList<>();
        String query = "SELECT * FROM OrdenProduccion";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                OrdenProduccion orden = new OrdenProduccion(
                        rs.getInt("ID"),
                        rs.getInt("lote"),
                        getProductoById(rs.getInt("ProductoID")),
                        getAreaById(rs.getInt("areaID")));
                ordenes.add(orden);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordenes;
    }

    public static void addOrdenProduccion(OrdenProduccion orden) {
        String query = "INSERT INTO OrdenProduccion (ID, lote, ProductoID, areaID, costoID, iniciado, pausado, terminado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, orden.getId());
            pstmt.setInt(2, orden.getLote());
            pstmt.setInt(3, orden.getProducto().getId());
            pstmt.setInt(4, orden.getArea().getId());
            pstmt.setBoolean(6, orden.isIniciado());
            pstmt.setBoolean(7, orden.isPausado());
            pstmt.setBoolean(8, orden.isTerminado());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Áreas
    public static List<Area> getAreas() {
        List<Area> areas = new ArrayList<>();
        String query = "SELECT * FROM Area";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Area area = new Area(
                        rs.getInt("ID"),
                        rs.getString("Nombre"));
                areas.add(area);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return areas;
    }

    private static Area getAreaById(int id) {
        String query = "SELECT * FROM Area WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Area(rs.getInt("ID"), rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Costos
    private static Costo getCostoById(int id) {
        String query = "SELECT * FROM Costo WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Costo(
                            rs.getInt("ID"),
                            rs.getInt("mes"),
                            rs.getInt("anio"),
                            rs.getDouble("valor"),
                            getUsuarioByLegajo(rs.getInt("legajoDirector")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Paradas
    public static List<Parada> getParadas() {
        List<Parada> paradas = new ArrayList<>();
        String query = "SELECT * FROM Parada";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Parada parada = new Parada(
                        rs.getInt("ID"),
                        getOrdenProduccionById(rs.getInt("OPID")),
                        rs.getTimestamp("inicio").toLocalDateTime(),
                        getCategoriaParadaById(rs.getInt("catParadaID")),
                        rs.getString("observacion"));
                paradas.add(parada);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paradas;
    }

    public static List<Parada> getParadasByOrdenProduccion(int opId) {
        List<Parada> paradas = new ArrayList<>();
        String query = "SELECT * FROM Parada WHERE OPID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, opId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Parada parada = new Parada(
                            rs.getInt("ID"),
                            getOrdenProduccionById(rs.getInt("OPID")),
                            rs.getTimestamp("inicio").toLocalDateTime(),
                            getCategoriaParadaById(rs.getInt("catParadaID")),
                            rs.getString("observacion"));
                    paradas.add(parada);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paradas;
    }

    public static void addParada(Parada parada) {
        String query = "INSERT INTO Parada (OPID, inicio, fin, catParadaID, observacion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, parada.getOrdenProduccion().getId());
            pstmt.setTimestamp(2, Timestamp.valueOf(parada.getInicio()));
            pstmt.setTimestamp(3, parada.getFin() != null ? Timestamp.valueOf(parada.getFin()) : null);
            pstmt.setInt(4, parada.getCategoriaParada().getId());
            pstmt.setString(5, parada.getObservacion());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Categorías de Parada
    public static CategoriaParada getCategoriaParadaById(int id) {
        String query = "SELECT * FROM CategoriaParada WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CategoriaParada(rs.getInt("ID"), rs.getString("descripcion"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addCategoriaParada(CategoriaParada categoriaParada) {
        String query = "INSERT INTO CategoriaParada (ID, descripcion) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, categoriaParada.getId());
            pstmt.setString(2, categoriaParada.getDescripcion());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void actualizarCategoriaParada(CategoriaParada nuevaCategoria) throws SQLException {
        String query = "UPDATE CategoriaParada SET descripcion = ? WHERE ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nuevaCategoria.getDescripcion());
            pstmt.setInt(2, nuevaCategoria.getId());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException(
                        "No se encontró ninguna categoría de parada con el ID: " + nuevaCategoria.getId());
            }

        } catch (SQLException e) {
            throw e;
        }
    }

    // Operarios en Orden

    public static List<OperarioEnOrden> getOperariosEnOrden() {
        List<OperarioEnOrden> operarios = new ArrayList<>();
        String query = "SELECT * FROM OperarioEnOrden";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                OperarioEnOrden operario = new OperarioEnOrden(
                        rs.getInt("ID"),
                        getOrdenProduccionById(rs.getInt("OPID")),
                        getUsuarioByLegajo(rs.getInt("legajoOperario")),
                        getEtapaById(rs.getInt("etapaID")),
                        rs.getTimestamp("inicio").toLocalDateTime());
                operarios.add(operario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return operarios;
    }

    public static List<OperarioEnOrden> getOperariosEnOrdenByOP(int opId) {
        List<OperarioEnOrden> operariosEnOrden = new ArrayList<>();
        String query = "SELECT * FROM OperarioEnOrden WHERE OPID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, opId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OperarioEnOrden oeo = new OperarioEnOrden(
                            rs.getInt("ID"),
                            getOrdenProduccionById(rs.getInt("OPID")),
                            getUsuarioByLegajo(rs.getInt("legajoOperario")),
                            getEtapaById(rs.getInt("etapaID")),
                            rs.getTimestamp("inicio").toLocalDateTime());
                    operariosEnOrden.add(oeo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return operariosEnOrden;
    }

    public static void addOperarioEnOrden(OperarioEnOrden oeo) {
        String query = "INSERT INTO OperarioEnOrden (OPID, legajoOperario, etapaID, inicio, fin) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, oeo.getOrdenProduccion().getId());
            pstmt.setInt(2, oeo.getUsuario().getLegajo());
            pstmt.setInt(3, oeo.getEtapa().getId());
            pstmt.setTimestamp(4, Timestamp.valueOf(oeo.getInicio()));
            pstmt.setTimestamp(5, oeo.getFin() != null ? Timestamp.valueOf(oeo.getFin()) : null);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Etapas

    public static List<Etapa> getEtapas() {
        List<Etapa> etapas = new ArrayList<>();
        String query = "SELECT * FROM Etapa";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Etapa etapa = new Etapa(
                        rs.getInt("ID"),
                        rs.getString("nombre"));
                etapas.add(etapa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return etapas;
    }

    private static Etapa getEtapaById(int id) {
        String query = "SELECT * FROM Etapa WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Etapa(rs.getInt("ID"), rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Orden de Producción (método adicional)
    private static OrdenProduccion getOrdenProduccionById(int id) {
        String query = "SELECT * FROM OrdenProduccion WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new OrdenProduccion(
                            rs.getInt("ID"),
                            rs.getInt("lote"),
                            getProductoById(rs.getInt("ProductoID")),
                            getAreaById(rs.getInt("areaID")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Métodos para actualizar estados de Orden de Producción
    public static void iniciarOrdenProduccion(int id) {
        updateOrdenProduccionStatus(id, "iniciado", true);
    }

    public static void pausarOrdenProduccion(int id) {
        updateOrdenProduccionStatus(id, "pausado", true);
    }

    public static void reanudarOrdenProduccion(int id) {
        updateOrdenProduccionStatus(id, "pausado", false);
    }

    public static void terminarOrdenProduccion(int id) {
        updateOrdenProduccionStatus(id, "terminado", true);
    }

    private static void updateOrdenProduccionStatus(int id, String campo, boolean valor) {
        String query = "UPDATE OrdenProduccion SET " + campo + " = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setBoolean(1, valor);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Métodos para obtener órdenes de producción filtradas
    public static List<OrdenProduccion> getOrdenesProduccionIniciadas() {
        return getOrdenesProduccionFiltradas("iniciado = true AND pausado = false AND terminado = false");
    }

    public static List<OrdenProduccion> getOrdenesProduccionPausadas() {
        return getOrdenesProduccionFiltradas("pausado = true");
    }

    public static List<OrdenProduccion> getOrdenesProduccionTerminadas() {
        return getOrdenesProduccionFiltradas("terminado = true");
    }

    public static List<OrdenProduccion> getOrdenesProduccionDisponibles() {
        return getOrdenesProduccionFiltradas("iniciado = false AND pausado = false AND terminado = false");
    }

    private static List<OrdenProduccion> getOrdenesProduccionFiltradas(String condicion) {
        List<OrdenProduccion> ordenes = new ArrayList<>();
        String query = "SELECT * FROM OrdenProduccion WHERE " + condicion;

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                OrdenProduccion orden = new OrdenProduccion(
                        rs.getInt("ID"),
                        rs.getInt("lote"),
                        getProductoById(rs.getInt("ProductoID")),
                        getAreaById(rs.getInt("areaID")));
                ordenes.add(orden);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordenes;
    }

    // Método para obtener órdenes de producción por área
    public static List<OrdenProduccion> getOrdenesProduccionPorArea(int areaId) {
        return getOrdenesProduccionFiltradas("areaID = " + areaId);
    }

    // Método para actualizar fin de OperarioEnOrden
    public static void finalizarOperarioEnOrden(int id, LocalDateTime fin) {
        String query = "UPDATE OperarioEnOrden SET fin = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(fin));
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener todos los Costos
    public static List<Costo> getCostos() {
        List<Costo> costos = new ArrayList<>();
        String query = "SELECT * FROM Costo";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Costo costo = new Costo(
                        rs.getInt("ID"),
                        rs.getInt("mes"),
                        rs.getInt("anio"),
                        rs.getDouble("valor"),
                        getUsuarioByLegajo(rs.getInt("legajoDirector")));
                costos.add(costo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return costos;
    }

    // Método para agregar un nuevo Costo
    public static void addCosto(Costo costo) {
        String query = "INSERT INTO Costo (mes, anio, valor, legajoDirector) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, costo.getMes());
            pstmt.setInt(2, costo.getAnio());
            pstmt.setDouble(3, costo.getValor());
            pstmt.setInt(4, costo.getUsuario().getLegajo());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener todas las Categorías de Parada
    public static List<CategoriaParada> getCategoriasParada() {
        List<CategoriaParada> categorias = new ArrayList<>();
        String query = "SELECT * FROM CategoriaParada";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                CategoriaParada categoria = new CategoriaParada(
                        rs.getInt("ID"),
                        rs.getString("descripcion"));
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categorias;
    }
}