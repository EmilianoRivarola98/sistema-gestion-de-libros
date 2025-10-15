package interfaces;

import servicios.ServicioAutor;
import javax.swing.JOptionPane;

public class GestionAutores {
	private ServicioAutor servicioAutor;

	public GestionAutores() {
		this.servicioAutor = new ServicioAutor();
	}

	public void mostrarMenu() {
		String[] opciones = {
				"Crear Autor",
				"Listar Autores",
				"Actualizar Autor",
				"Eliminar Autor",
				"Volver"
		};

		int seleccion = 0;

		while (seleccion != 4) {
			seleccion = JOptionPane.showOptionDialog(
					null,
					"Gestión de Autores",
					"Seleccione una opción",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					opciones,
					opciones[0]
					);

			switch (seleccion) {
			case 0: servicioAutor.crearAutor(); break;
			case 1: servicioAutor.listarAutores(); break;
			case 2: servicioAutor.actualizarAutor(); break;
			case 3: servicioAutor.eliminarAutor(); break;
			case 4: break; // Volver
			default: JOptionPane.showMessageDialog(null, "Opción no válida");
			}
		}
	}
}
