import controller.LoginController;
import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // Verificar la conexión a la base de datos
            verificarConexionBD();
            
            // Crea una instancia de LoginController para manejar el proceso de inicio de sesión
            LoginController loginController = new LoginController();
            
            // Inicia el proceso de inicio de sesión
            loginController.iniciar();
            
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            System.err.println("La aplicación no puede continuar sin conexión a la base de datos.");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void verificarConexionBD() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Conexión a la base de datos establecida correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("No se pudo establecer conexión con la base de datos.");
            throw e;
        }
    }
}
