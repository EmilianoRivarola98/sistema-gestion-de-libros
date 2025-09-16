package usuarios;

import interfaces.Menu;
import javax.swing.JOptionPane;

public class AdministradorGeneral extends Usuario implements Menu {
    public AdministradorGeneral(String nombre, String username) {
        super(nombre, username);
    }

    @Override
    public void MostrarMenu() {
        String[] opciones = {
            "Gestionar usuarios",
            "Agregar stock",
            "Modificar stock",
            "Eliminar stock",
            "Generar pedido de faltantes",
            "Gestionar promociones",
            "Salir"
        };

        int seleccion = 0;

        while (seleccion != 6) { 
            seleccion = JOptionPane.showOptionDialog(
                null,
                "Menú Administrador General",
                "Seleccione una opción",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );

            switch (seleccion) {
                case 0: gestionarUsuarios(); break;
                case 1: agregarStock(); break;
                case 2: modificarStock(); break;
                case 3: eliminarStock(); break;
                case 4: generarPedidoFaltantes(); break;
                case 5: gestionarPromociones(); break;
                case 6: JOptionPane.showMessageDialog(null, "Saliendo del menú de administrador"); break;
                default: JOptionPane.showMessageDialog(null, "Opción no válida");
            }
        }
    }

    // Métodos
    private void gestionarUsuarios() {
        JOptionPane.showMessageDialog(null, "Función Gestionar usuarios");
    }

    private void agregarStock() {
        JOptionPane.showMessageDialog(null, "Función Agregar stock");
    }

    private void modificarStock() {
        JOptionPane.showMessageDialog(null, "Función Modificar stock");
    }

    private void eliminarStock() {
        JOptionPane.showMessageDialog(null, "Función Eliminar stock");
    }

    private void generarPedidoFaltantes() {
        JOptionPane.showMessageDialog(null, "Función Generar pedido de faltantes");
    }

    private void gestionarPromociones() {
        JOptionPane.showMessageDialog(null, "Función Gestionar promociones");
    }
}