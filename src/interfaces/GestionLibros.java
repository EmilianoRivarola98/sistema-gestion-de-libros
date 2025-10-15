package interfaces;

import servicios.ServicioLibro;
import servicios.ServicioStock;
import javax.swing.JOptionPane;

public class GestionLibros {
	private ServicioLibro servicioLibro;
	private ServicioStock servicioStock;

	public GestionLibros() {
		this.servicioLibro = new ServicioLibro();
		this.servicioStock = new ServicioStock();
	}

	public void mostrarMenu() {
		String[] opciones = {
				"Crear Libro",
				"Buscar Libro",
				"Actualizar Libro",
				"Eliminar Libro",
				"Actualizar Stock",
				"Eliminar Stock",
				"Consultar Stock",
				"Volver"
		};

		int seleccion = 0;

		while (seleccion != 7) {
			seleccion = JOptionPane.showOptionDialog(
					null,
					"Gestión de Libros y Stock",
					"Seleccione una opción",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					opciones,
					opciones[0]
					);

			switch (seleccion) {
			case 0: servicioLibro.crearLibro(); break;
			case 1: servicioLibro.buscarLibro(); break;
			case 2: servicioLibro.actualizarLibro(); break;
			case 3: servicioLibro.eliminarLibro(); break;
			case 4: servicioStock.actualizarStockGeneral(); break;
			case 5: servicioStock.eliminarStock(); break;
			case 6: servicioStock.consultarStock(); break;
			case 7: break; // Volver
			default: JOptionPane.showMessageDialog(null, "Opción no válida");
			}
		}
	}
}
