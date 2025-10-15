package interfaces;

import servicios.ServicioGenero;
import javax.swing.JOptionPane;

public class GestionGeneros {
	private ServicioGenero servicioGenero;

	public GestionGeneros() {
		this.servicioGenero = new ServicioGenero();
	}

	public void mostrarMenu() {
		String[] opciones = {
				"Crear Género",
				"Listar Géneros",
				"Actualizar Género",
				"Eliminar Género",
				"Volver"
		};

		int seleccion = 0;

		while (seleccion != 4) {
			seleccion = JOptionPane.showOptionDialog(
					null,
					"Gestión de Géneros",
					"Seleccione una opción",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					opciones,
					opciones[0]
					);

			switch (seleccion) {
			case 0: servicioGenero.crearGenero(); break;
			case 1: servicioGenero.listarGeneros(); break;
			case 2: servicioGenero.actualizarGenero(); break;
			case 3: servicioGenero.eliminarGenero(); break;
			case 4: break; // Volver
			default: JOptionPane.showMessageDialog(null, "Opción no válida");
			}
		}
	}
}
