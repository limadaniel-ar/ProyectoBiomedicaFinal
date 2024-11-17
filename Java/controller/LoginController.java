package controller;

import java.util.InputMismatchException;
import java.util.List;

import model.Usuario;
import util.DataStore;
import view.MenuView;

public class LoginController {
    private MenuView menuView;
    private SistemaController sistemaController;
    private Usuario usuarioActual;

    public LoginController() {
        this.menuView = new MenuView();
        this.sistemaController = new SistemaController();
    }

    public void iniciar() {
        boolean continuar = true;
        while (continuar) {
            try {
                menuView.limpiarPantalla();
                menuView.mostrarMensajeBienvenida();
                listarUsuarios();
                continuar = manejarLogin();
            } catch (InputMismatchException e) {
                menuView.mostrarError("Error: Ingreso inválido. Por favor, intente nuevamente.");
                menuView.limpiarBuffer();
            } catch (Exception e) {
                menuView.mostrarError("Error inesperado: " + e.getMessage());
                menuView.limpiarBuffer();
            }
        }
    }

    private boolean manejarLogin() {
        int opcion = menuView.mostrarMenuPrincipal();

        switch (opcion) {
            case 1:
                realizarLogin();
                return true;
            case 0:
                menuView.mostrarMensajeDespedida();
                return false;
            default:
                menuView.mostrarError("Opción inválida");
                return true;
        }
    }

    private void realizarLogin() {
        int intentos = 3;
        boolean loginExitoso = false;
        while (intentos > 0 && !loginExitoso) {
            int legajo = menuView.solicitarLegajo();
            String password = menuView.solicitarPassword();

            Usuario usuario = validarCredenciales(legajo, password);

            if (usuario != null) {
                loginExitoso = true;
                this.usuarioActual = usuario;
                menuView.mostrarMensajeLoginExitoso(usuario.getNombre(), usuario.getApellido());
                redirigirSegunRol(usuario);
            } else {
                intentos--;
                if (intentos > 0) {
                    menuView.mostrarError("Credenciales inválidas. Le quedan " + intentos + " intentos.");
                } else {
                    menuView.mostrarError("Ha excedido el número máximo de intentos. El programa se cerrará.");
                }
            }
        }
    }

    private Usuario validarCredenciales(int legajo, String password) {
        Usuario usuario = DataStore.getUsuarioByLegajo(legajo);

        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }

    private void redirigirSegunRol(Usuario usuario) {
        try {
            switch (usuario.getRol().getId()) {
                case 1:
                    new OperarioController(usuario).mostrarMenu();
                    break;
                case 2:
                    new JefeProduccionController(usuario).mostrarMenu();
                    break;
                case 3:
                    new JefePlanificacionController(usuario).mostrarMenu();
                    break;
                case 4:
                    new DirectorController(usuario).mostrarMenu();
                    break;
                default:
                    menuView.mostrarError("Rol no reconocido");
                    break;
            }
        } catch (Exception e) {
            menuView.mostrarError("Error al cargar el menú: " + e.getMessage());
        }
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
        menuView.mostrarMensajeCierreSesion();
    }

    public boolean sesionActiva() {
        return usuarioActual != null;
    }

    private void listarUsuarios() {
        List<Usuario> usuarios = DataStore.getUsuarios();
        menuView.mostrarUsuariosDisponibles(usuarios);
    }
}
