package view;

import java.util.List;
import java.util.Scanner;

import model.Usuario;
import util.DataStore;

public class MenuView {
    private Scanner scanner;

    public MenuView() {
        this.scanner = new Scanner(System.in);
    }

    public void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void mostrarMensajeBienvenida() {
        System.out.println("=================================");
        System.out.println("  SISTEMA DE GESTIÓN DE PRODUCCIÓN  ");
        System.out.println("=================================");
    }

    public int mostrarMenuPrincipal() {
        System.out.println("\n1. Iniciar sesión");
        System.out.println("0. Salir");
        System.out.print("\nSeleccione una opción: ");
        return scanner.nextInt();
    }

    public int solicitarLegajo() {        
        System.out.print("Ingrese su número de legajo: ");
        return scanner.nextInt();
    }

    public String solicitarPassword() {
        System.out.print("Ingrese su contraseña: ");
        return scanner.next();
    }

    public void mostrarMensajeLoginExitoso(String nombre, String apellido) {
        System.out.println("\n¡Bienvenido/a " + nombre + " " + apellido + "!");
        System.out.println("Iniciando sesión...");
        esperarSegundos(2);
    }

    public void mostrarError(String mensaje) {
        System.out.println("\n¡ERROR! " + mensaje);
        esperarSegundos(2);
    }

    public void mostrarMensajeDespedida() {
        System.out.println("\nGracias por usar el sistema. ¡Hasta pronto!");
        esperarSegundos(1);
    }

    public void mostrarMensajeCierreSesion() {
        System.out.println("\nCerrando sesión...");
        esperarSegundos(1);
        System.out.println("Sesión cerrada exitosamente.");
        esperarSegundos(1);
    }

    public void limpiarBuffer() {
        scanner.nextLine();
    }

    private void esperarSegundos(int segundos) {
        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void mostrarUsuariosDisponibles(List<Usuario> usuarios) {
        System.out.println("\n=== USUARIOS Y CONTRASEÑAS ===");
        usuarios.forEach(u -> System.out.printf("Legajo: %d, Contraseña: %s, Rol: %s%n",
                u.getLegajo(), u.getPassword(), u.getRol().getTipo()));
    }
}

