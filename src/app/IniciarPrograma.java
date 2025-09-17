package app;

import javax.swing.JOptionPane;
import usuarios.EncargadodeVentas;
import usuarios.AdministradorGeneral;

public class IniciarPrograma {

    public void iniciar() {
        String[] roles = { "Vendedor", "Administrador" };

        String rolSeleccionado = (String) JOptionPane.showInputDialog(
                null,
                "Bienvenido. Seleccione su rol:",
                "Inicio de sesión",
                JOptionPane.QUESTION_MESSAGE,
                null,
                roles,
                roles[0]
        );

        if (rolSeleccionado == null) {
            JOptionPane.showMessageDialog(null, "Saliendo del sistema");
            System.exit(0);
        }

        String username = JOptionPane.showInputDialog("Ingrese su usuario:");
        String nombre = JOptionPane.showInputDialog("Ingrese su nombre:");

        if (rolSeleccionado.equals("Vendedor")) {
            EncargadodeVentas vendedor = new EncargadodeVentas(nombre, username);
            vendedor.MostrarMenu();
        } else if (rolSeleccionado.equals("Administrador")) {
            AdministradorGeneral admin = new AdministradorGeneral(nombre, username);
            admin.MostrarMenu();
        }
    }
}