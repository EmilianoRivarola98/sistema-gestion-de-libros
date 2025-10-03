package app;

import javax.swing.JOptionPane;
import interfaces.GestionUsuarios;

public class IniciarPrograma {

    public void iniciar() {
            String[] opciones = {
                "Iniciar sesion",
                "Registrarse",
                "Salir"
            };

            int seleccion = 0;

                while (seleccion != 2) {
                seleccion = JOptionPane.showOptionDialog(
                    null,
                    "Seleccione una opción",
                    "Sistema Gestion de Libros",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
                );

                    if (seleccion == -1) {
                        seleccion = 2;
                    }

                switch (seleccion) {
                        case 0: iniciarSesion(); break;
                        case 1: registrarse(); break;
                        case 2: JOptionPane.showMessageDialog(null, "Gracias vuelva prontos."); break;
                }
            }
    }
    private void iniciarSesion() {
            new GestionUsuarios().iniciarSesion();
    }

    private void registrarse() {
            new GestionUsuarios().registrarUsuario();
    }
}