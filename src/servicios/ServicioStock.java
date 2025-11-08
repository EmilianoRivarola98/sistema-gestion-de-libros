package servicios;

import dao.StockDAO;
import dao.LibroDAO;
import ventas.Libro;
import ventas.Sucursal;
import javax.swing.*;
import java.util.List;
import servicios.ServicioLibro;
import servicios.ServicioSucursal;

public class ServicioStock {
	private StockDAO stockDAO;
	private LibroDAO libroDAO;

	public ServicioStock() {
		this.stockDAO = new StockDAO();
		this.libroDAO = new LibroDAO();
	}

	public void consultarStock(int idSucursal) {
		String[] opciones = { "Por ID de libro", "Ver todo el stock" };
		int opcion = JOptionPane.showOptionDialog(null, "Seleccione una opción", "Consultar stock",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

		if (opcion == 0) {
			consultarStockPorLibro(idSucursal);
		} else if (opcion == 1) {
			listarStockSucursal(idSucursal);
		}
	}

	private void consultarStockPorLibro(int idSucursal) {
		try {
			int idLibro = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID del libro:"));
			Libro libro = libroDAO.obtenerLibroPorId(idLibro);

			if (libro == null) {
				JOptionPane.showMessageDialog(null, "Libro no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Integer cantidad = stockDAO.obtenerStockPorLibro(idLibro, idSucursal);
			if (cantidad == null) {
				JOptionPane.showMessageDialog(null, "No hay stock registrado para este libro en la sucursal.",
						"Sin stock", JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null,
						"Libro: " + libro.getTitulo() + "\n" + "Stock disponible: " + cantidad, "Consulta de Stock",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Debe ingresar un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void listarStockSucursal(int idSucursal) {
		List<String> lista = stockDAO.listarStockPorSucursal(idSucursal);

		if (lista.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay registros de stock en esta sucursal.", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		StringBuilder sb = new StringBuilder("Stock de la Sucursal:\n\n");
		for (String item : lista) {
			sb.append(item).append("\n");
		}

		JTextArea textArea = new JTextArea(sb.toString());
		textArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setPreferredSize(new java.awt.Dimension(400, 300));

		JOptionPane.showMessageDialog(null, scroll, "Stock disponible", JOptionPane.INFORMATION_MESSAGE);
	}

	public void consultarStock() {
		try {
			ServicioSucursal servicioSucursal = new ServicioSucursal();
			List<Sucursal> sucursales = servicioSucursal.obtenerTodasLasSucursales();
			if (sucursales.isEmpty()) {
				JOptionPane.showMessageDialog(null, "No hay sucursales registradas.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			Sucursal sucursalSeleccionada = (Sucursal) JOptionPane.showInputDialog(null, "Seleccione una sucursal",
					"Sucursales", JOptionPane.QUESTION_MESSAGE, null, sucursales.toArray(), sucursales.get(0));
			if (sucursalSeleccionada == null)
				return;

			String[] opciones = { "Ver todo el stock", "Buscar por nombre", "Buscar por ID", "Buscar por ISBN" };
			int opcion = JOptionPane.showOptionDialog(null, "¿Qué desea hacer?", "Consultar Stock",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

			switch (opcion) {
			case 0: // Ver todo el stock
				List<String> stockCompleto = stockDAO.listarStockPorSucursal(sucursalSeleccionada.getId());
				if (stockCompleto.isEmpty()) {
					JOptionPane.showMessageDialog(null, "No hay stock registrado en esta sucursal.", "Información",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JTextArea textArea = new JTextArea(String.join("\n", stockCompleto));
					JScrollPane scrollPane = new JScrollPane(textArea);
					scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));
					JOptionPane.showMessageDialog(null, scrollPane, "Stock en " + sucursalSeleccionada.getNombre(),
							JOptionPane.INFORMATION_MESSAGE);
				}
				break;
			case 1: // Buscar por nombre
				String nombre = JOptionPane.showInputDialog("Ingrese el nombre del libro:");
				if (nombre != null && !nombre.trim().isEmpty()) {
					List<String> stockPorNombre = stockDAO.buscarStockPorNombreLibroEnSucursal(nombre,
							sucursalSeleccionada.getId());
					if (stockPorNombre.isEmpty()) {
						JOptionPane.showMessageDialog(null,
								"No se encontraron libros con ese nombre en el stock de esta sucursal.", "Información",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JTextArea textArea = new JTextArea(String.join("\n", stockPorNombre));
						JScrollPane scrollPane = new JScrollPane(textArea);
						scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));
						JOptionPane.showMessageDialog(null, scrollPane, "Stock en " + sucursalSeleccionada.getNombre(),
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
				break;
			case 2: // Buscar por ID
				try {
					int idLibro = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del libro:"));
					String stockPorId = stockDAO.buscarStockPorIdLibroEnSucursal(idLibro, sucursalSeleccionada.getId());
					if (stockPorId == null) {
						JOptionPane.showMessageDialog(null,
								"No se encontró un libro con ese ID en el stock de esta sucursal.", "Información",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, stockPorId, "Stock en " + sucursalSeleccionada.getNombre(),
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Debe ingresar un ID válido.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				break;
			case 3: // Buscar por ISBN
				String isbn = JOptionPane.showInputDialog("Ingrese el ISBN del libro:");
				if (isbn != null && !isbn.trim().isEmpty()) {
					String stockPorIsbn = stockDAO.buscarStockPorIsbnLibroEnSucursal(isbn,
							sucursalSeleccionada.getId());
					if (stockPorIsbn == null) {
						JOptionPane.showMessageDialog(null,
								"No se encontró un libro con ese ISBN en el stock de esta sucursal.", "Información",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, stockPorIsbn,
								"Stock en " + sucursalSeleccionada.getNombre(), JOptionPane.INFORMATION_MESSAGE);
					}
				}
				break;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al consultar stock: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void actualizarStockGeneral() {

		try {

			// sucursal selector

			ServicioSucursal servicioSucursal = new ServicioSucursal();

			List<Sucursal> sucursales = servicioSucursal.obtenerTodasLasSucursales();

			if (sucursales.isEmpty()) {

				JOptionPane.showMessageDialog(null, "No hay sucursales registradas.", "Error",
						JOptionPane.ERROR_MESSAGE);

				return;

			}

			Sucursal sucursalSeleccionada = (Sucursal) JOptionPane.showInputDialog(null, "Seleccione una sucursal",

					"Sucursales", JOptionPane.QUESTION_MESSAGE, null, sucursales.toArray(), sucursales.get(0));

			if (sucursalSeleccionada == null)
				return;

			// Libro selection

			ServicioLibro servicioLibro = new ServicioLibro();

			List<Libro> libros = servicioLibro.obtenerTodos();

			if (libros.isEmpty()) {

				JOptionPane.showMessageDialog(null, "No hay libros registrados.", "Error", JOptionPane.ERROR_MESSAGE);

				return;

			}

			Libro libroSeleccionado = (Libro) JOptionPane.showInputDialog(null, "Seleccione un libro",

					"Libros", JOptionPane.QUESTION_MESSAGE, null, libros.toArray(), libros.get(0));

			if (libroSeleccionado == null)
				return;

			// check si hay stock

			Integer cantidadActual = stockDAO.obtenerStockPorLibro(libroSeleccionado.getIdLibro(),
					sucursalSeleccionada.getId());

			if (cantidadActual != null) {

				// si Stock exists

				String[] opciones = { "Sumar al stock", "Disminuir stock", "Establecer nuevo valor" };

				int opcion = JOptionPane.showOptionDialog(null,

						"El stock actual para '" + libroSeleccionado.getTitulo() + "' en la sucursal '"
								+ sucursalSeleccionada.getNombre() + "' es: " + cantidadActual + ". ¿Qué desea hacer?",

								"Actualizar Stock",

								JOptionPane.DEFAULT_OPTION,

								JOptionPane.INFORMATION_MESSAGE,

								null,

								opciones,

								opciones[0]);

				if (opcion == 0) { // Sumar al stock

					int cantidadASumar = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad a sumar:"));

					if (stockDAO.agregarStock(libroSeleccionado.getIdLibro(), sucursalSeleccionada.getId(),
							cantidadASumar)) {

						JOptionPane.showMessageDialog(null, "Stock actualizado correctamente.");

					} else {

						JOptionPane.showMessageDialog(null, "Error al actualizar el stock.", "Error",
								JOptionPane.ERROR_MESSAGE);

					}

				} else if (opcion == 1) { // Disminuir stock

					int cantidadADisminuir = Integer
							.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad a disminuir:"));

					if (cantidadActual >= cantidadADisminuir) {

						if (stockDAO.disminuirStock(libroSeleccionado.getIdLibro(), sucursalSeleccionada.getId(),
								cantidadADisminuir)) {

							JOptionPane.showMessageDialog(null, "Stock actualizado correctamente.");

						} else {

							JOptionPane.showMessageDialog(null, "Error al actualizar el stock.", "Error",
									JOptionPane.ERROR_MESSAGE);

						}

					} else {

						JOptionPane.showMessageDialog(null, "No hay suficiente stock para disminuir.", "Error",
								JOptionPane.ERROR_MESSAGE);

					}

				} else if (opcion == 2) { // Establecer nuevo valor

					int nuevaCantidad = Integer
							.parseInt(JOptionPane.showInputDialog("Ingrese la nueva cantidad total:"));
					if (stockDAO.modificarStock(libroSeleccionado.getIdLibro(), sucursalSeleccionada.getId(),
							nuevaCantidad)) {
						JOptionPane.showMessageDialog(null, "Stock actualizado correctamente.");
					} else {
						JOptionPane.showMessageDialog(null, "Error al actualizar el stock.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}

			} else {

				// Stock no existe

				int cantidad = Integer.parseInt(JOptionPane.showInputDialog(
						"No hay stock registrado para este libro en la sucursal seleccionada. Ingrese la cantidad inicial:"));

				if (stockDAO.agregarStock(libroSeleccionado.getIdLibro(), sucursalSeleccionada.getId(), cantidad)) {

					JOptionPane.showMessageDialog(null, "Stock agregado correctamente.");

				} else {

					JOptionPane.showMessageDialog(null, "Error al agregar el stock.", "Error",
							JOptionPane.ERROR_MESSAGE);

				}

			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad válida.", "Error",
					JOptionPane.ERROR_MESSAGE);

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Error al actualizar stock: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);

		}

	}

	public void agregarStock(int idLibro) {

		try {

			ServicioSucursal servicioSucursal = new ServicioSucursal();

			List<Sucursal> sucursales = servicioSucursal.obtenerTodasLasSucursales();

			if (sucursales.isEmpty()) {

				int opcion = JOptionPane.showConfirmDialog(null,
						"No hay sucursales registradas. ¿Desea crear una ahora?", "Sucursales",
						JOptionPane.YES_NO_OPTION);

				if (opcion == JOptionPane.YES_OPTION) {

					servicioSucursal.crearSucursal();

					sucursales = servicioSucursal.obtenerTodasLasSucursales();

					if (sucursales.isEmpty()) {
						return;
					}

				} else {

					return;

				}

			}

			Sucursal sucursalSeleccionada = (Sucursal) JOptionPane.showInputDialog(null, "Seleccione una sucursal",

					"Sucursales", JOptionPane.QUESTION_MESSAGE, null, sucursales.toArray(), sucursales.get(0));

			if (sucursalSeleccionada == null)
				return;

			int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad a agregar:"));

			if (stockDAO.agregarStock(idLibro, sucursalSeleccionada.getId(), cantidad)) {

				JOptionPane.showMessageDialog(null, "Stock agregado correctamente.");

			} else {

				JOptionPane.showMessageDialog(null, "Error al agregar stock.", "Error", JOptionPane.ERROR_MESSAGE);

			}

		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad válida.", "Error",
					JOptionPane.ERROR_MESSAGE);

		}

	}

	public void eliminarStock() {
		try {
			// sucursal selector
			ServicioSucursal servicioSucursal = new ServicioSucursal();
			List<Sucursal> sucursales = servicioSucursal.obtenerTodasLasSucursales();
			if (sucursales.isEmpty()) {
				JOptionPane.showMessageDialog(null, "No hay sucursales registradas.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			Sucursal sucursalSeleccionada = (Sucursal) JOptionPane.showInputDialog(null, "Seleccione una sucursal",
					"Sucursales", JOptionPane.QUESTION_MESSAGE, null, sucursales.toArray(), sucursales.get(0));
			if (sucursalSeleccionada == null)
				return;

			// Book selection
			List<Libro> libros = stockDAO.obtenerLibrosConStockPorSucursal(sucursalSeleccionada.getId());
			if (libros.isEmpty()) {
				JOptionPane.showMessageDialog(null, "No hay libros con stock en esta sucursal.", "Información",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			Libro libroSeleccionado = (Libro) JOptionPane.showInputDialog(null,
					"Seleccione un libro para eliminar su stock", "Libros con Stock", JOptionPane.QUESTION_MESSAGE,
					null, libros.toArray(), libros.get(0));
			if (libroSeleccionado == null)
				return;

			int confirmacion = JOptionPane.showConfirmDialog(null,
					"¿Está seguro de que desea eliminar el stock del libro '" + libroSeleccionado.getTitulo()
					+ "' en la sucursal '" + sucursalSeleccionada.getNombre() + "'?",
					"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

			if (confirmacion == JOptionPane.YES_OPTION) {
				if (stockDAO.eliminarStock(libroSeleccionado.getIdLibro(), sucursalSeleccionada.getId())) {
					JOptionPane.showMessageDialog(null, "Stock eliminado correctamente.");
				} else {
					JOptionPane.showMessageDialog(null, "Error al eliminar el stock.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al eliminar stock: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Métodos para GUI
	public List<String> obtenerStockPorSucursal(int idSucursal) {
		return stockDAO.listarStockPorSucursal(idSucursal);
	}

	public List<Libro> obtenerLibrosConStock(int idSucursal) {
		return stockDAO.obtenerLibrosConStockPorSucursal(idSucursal);
	}

	public Integer obtenerStockLibro(int idLibro, int idSucursal) {
		return stockDAO.obtenerStockPorLibro(idLibro, idSucursal);
	}

	public boolean agregarStockGUI(int idLibro, int idSucursal, int cantidad) {
		try {
			if (cantidad <= 0) {
				return false;
			}
			return stockDAO.agregarStock(idLibro, idSucursal, cantidad);
		} catch (Exception e) {
			return false;
		}
	}

	public boolean disminuirStockGUI(int idLibro, int idSucursal, int cantidad) {
		try {
			if (cantidad <= 0) {
				return false;
			}
			Integer stockActual = stockDAO.obtenerStockPorLibro(idLibro, idSucursal);
			if (stockActual == null || stockActual < cantidad) {
				return false; // No hay suficiente stock
			}
			return stockDAO.disminuirStock(idLibro, idSucursal, cantidad);
		} catch (Exception e) {
			return false;
		}
	}

	public boolean modificarStockGUI(int idLibro, int idSucursal, int nuevaCantidad) {
		try {
			if (nuevaCantidad < 0) {
				return false;
			}
			return stockDAO.modificarStock(idLibro, idSucursal, nuevaCantidad);
		} catch (Exception e) {
			return false;
		}
	}

	public boolean eliminarStockGUI(int idLibro, int idSucursal) {
		try {
			return stockDAO.eliminarStock(idLibro, idSucursal);
		} catch (Exception e) {
			return false;
		}
	}
}
