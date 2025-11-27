package servicios;

import dao.LibroDAO;
import ventas.Autor;
import ventas.Genero;
import ventas.Libro;
import ventas.Sucursal;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class ServicioLibro {
	private LibroDAO libroDAO;

	public ServicioLibro() {
		this.libroDAO = new LibroDAO();
	}

	public void buscarLibro() {
		String[] opciones = {"Buscar por ID", "Buscar por título"};
		int seleccion = JOptionPane.showOptionDialog(
				null,
				"Seleccione el tipo de búsqueda:",
				"Buscar libro",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,
				opciones,
				opciones[0]
				);

		if (seleccion == 0) {
			buscarPorId();
		} else if (seleccion == 1) {
			buscarPorTitulo();
		}
	}

	private void mostrarResultado(String texto, String tituloVentana) {
		JTextArea textArea = new JTextArea(texto);
		textArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setPreferredSize(new java.awt.Dimension(500, 400));
		JOptionPane.showMessageDialog(null, scroll, tituloVentana, JOptionPane.INFORMATION_MESSAGE);
	}

	private void buscarPorId() {
		try {
			int idLibro = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del libro:"));
			Libro libro = libroDAO.obtenerLibroPorId(idLibro);

			if (libro != null) {
				mostrarResultado(libro.getDetalleFormateado(), "Resultado de búsqueda");
			} else {
				JOptionPane.showMessageDialog(null, "No se encontró un libro con ese ID.", "Sin resultados", JOptionPane.WARNING_MESSAGE);
			}

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Debe ingresar un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void buscarPorTitulo() {
		String titulo = JOptionPane.showInputDialog("Ingrese parte o todo el título del libro:");
		if (titulo == null || titulo.trim().isEmpty()) {
			return;
		}

		List<Libro> resultados = libroDAO.buscarLibrosPorTitulo(titulo.toLowerCase());

		if (resultados.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No se encontraron libros con ese título.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
		} else {
			StringBuilder sb = new StringBuilder("Detalle del libro:\n\n");
			for (Libro libro : resultados) {
				sb.append(libro.getDetalleFormateado());
			}
			mostrarResultado(sb.toString(), "Resultados de búsqueda");
		}
	}

	public List<Libro> buscarPorAutor(String nombreAutor) {
	    return libroDAO.buscarLibrosPorAutor(nombreAutor);
	}

	public List<Libro> buscarPorGenero(String nombreGenero) {
	    return libroDAO.buscarLibrosPorGenero(nombreGenero);
	}

	public List<Libro> buscarPorIsbn(String isbn) {
	    List<Libro> resultado = new ArrayList<>();
	    Libro libro = libroDAO.buscarLibroPorIsbn(isbn);
	    if (libro != null) {
	        resultado.add(libro);
	    }
	    return resultado;
	}
	public void crearLibro() {
		Libro libro = obtenerDatosLibro(null);
		if (libro != null) {
			int idLibro = libroDAO.crearLibro(libro);
			if (idLibro != -1) {
				JOptionPane.showMessageDialog(null, "Libro creado correctamente.");
				int opcion = JOptionPane.showConfirmDialog(null, "¿Desea agregar stock para este libro ahora?", "Agregar Stock", JOptionPane.YES_NO_OPTION);
				if (opcion == JOptionPane.YES_OPTION) {
					ServicioStock servicioStock = new ServicioStock();
					servicioStock.agregarStock(idLibro);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Error al crear el libro.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void actualizarLibro() {
		ServicioSucursal servicioSucursal = new ServicioSucursal();
		Sucursal sucursalSeleccionada = servicioSucursal.seleccionarSucursal();
		if (sucursalSeleccionada == null) {
			return; // User cancelled or no branches exist
		}

		try {
			int idLibro = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del libro a actualizar:"));
			Libro libroExistente = libroDAO.obtenerLibroPorId(idLibro);

			if (libroExistente == null) {
				JOptionPane.showMessageDialog(null, "Libro no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Libro libroActualizado = obtenerDatosLibro(libroExistente);
			if (libroActualizado != null) {
				libroActualizado = new Libro(idLibro, libroActualizado.getTitulo(), libroActualizado.getIdAutor(), libroActualizado.getIdGenero(), libroActualizado.getIsbn(), libroActualizado.getPrecio(), (java.sql.Date) libroActualizado.getFechaLanzamiento());
				if (libroDAO.actualizarLibro(libroActualizado)) {
					JOptionPane.showMessageDialog(null, "Libro actualizado correctamente.");
				} else {
					JOptionPane.showMessageDialog(null, "Error al actualizar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Debe ingresar un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void eliminarLibro() {
		ServicioSucursal servicioSucursal = new ServicioSucursal();
		Sucursal sucursalSeleccionada = servicioSucursal.seleccionarSucursal();
		if (sucursalSeleccionada == null) {
			return; // User cancelled or no branches exist
		}

		String titulo = JOptionPane.showInputDialog("Ingrese el título del libro a eliminar:");
		if (titulo == null || titulo.trim().isEmpty()) return;

		List<Libro> libros = libroDAO.buscarLibrosPorTitulo(titulo);

		if (libros.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No se encontraron libros con ese título.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Libro libroSeleccionado = (Libro) JOptionPane.showInputDialog(null, "Seleccione un libro para eliminar",
				"Libros Encontrados", JOptionPane.QUESTION_MESSAGE, null, libros.toArray(), libros.get(0));

		if (libroSeleccionado != null) {
			int confirmacion = JOptionPane.showConfirmDialog(null, 
					"¿Está seguro de que desea eliminar el libro '" + libroSeleccionado.getTitulo() + "'? Esta acción también eliminará todo el stock asociado.", 
					"Confirmar Eliminación", 
					JOptionPane.YES_NO_OPTION);

			if (confirmacion == JOptionPane.YES_OPTION) {
				if (libroDAO.eliminarLibro(libroSeleccionado.getIdLibro())) {
					JOptionPane.showMessageDialog(null, "Libro eliminado correctamente.");
				} else {
					JOptionPane.showMessageDialog(null, "Error al eliminar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public List<Libro> obtenerTodos() {
		return libroDAO.obtenerTodos();
	}

	public Libro obtenerPorId(int idLibro) {
		return libroDAO.obtenerLibroPorId(idLibro);
	}

	public boolean crearLibro(Libro libro) {
		try {
			if (libro == null) return false;
			int idLibro = libroDAO.crearLibro(libro);
			return idLibro != -1;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean actualizarLibro(Libro libro) {
		try {
			if (libro == null || libro.getIdLibro() == 0) return false;
			return libroDAO.actualizarLibro(libro);
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean eliminarLibro(int idLibro) {
		try {
			if (idLibro <= 0) return false;
			return libroDAO.eliminarLibro(idLibro);
		} catch (Exception ex) {
			return false;
		}
	}

	private Libro obtenerDatosLibro(Libro libro) {
		try {
			String titulo = JOptionPane.showInputDialog("Ingrese el título del libro:", libro != null ? libro.getTitulo() : "");
			if (titulo == null || titulo.trim().isEmpty()) return null;

			// Author selection
			ServicioAutor servicioAutor = new ServicioAutor();
			List<Autor> autores = servicioAutor.obtenerTodos();
			if (autores.isEmpty()) {
				int opcion = JOptionPane.showConfirmDialog(null, "No hay autores registrados. ¿Desea crear uno ahora?", "Autores", JOptionPane.YES_NO_OPTION);
				if (opcion == JOptionPane.YES_OPTION) {
					servicioAutor.crearAutor();
					autores = servicioAutor.obtenerTodos();
					if (autores.isEmpty()) {
						return null; // User cancelled or failed to create author
					}
				} else {
					return null;
				}
			}
			Autor autorSeleccionado = (Autor) JOptionPane.showInputDialog(null, "Seleccione un autor",
					"Autores", JOptionPane.QUESTION_MESSAGE, null, autores.toArray(), autores.get(0));
			if (autorSeleccionado == null) return null;

			// Genre selection
			ServicioGenero servicioGenero = new ServicioGenero();
			List<Genero> generos = servicioGenero.obtenerTodos();
			if (generos.isEmpty()) {
				int opcion = JOptionPane.showConfirmDialog(null, "No hay géneros registrados. ¿Desea crear uno ahora?", "Géneros", JOptionPane.YES_NO_OPTION);
				if (opcion == JOptionPane.YES_OPTION) {
					servicioGenero.crearGenero();
					generos = servicioGenero.obtenerTodos();
					if (generos.isEmpty()) {
						return null;
					}
				} else {
					return null;
				}
			}
			Genero generoSeleccionado = (Genero) JOptionPane.showInputDialog(null, "Seleccione un género",
					"Géneros", JOptionPane.QUESTION_MESSAGE, null, generos.toArray(), generos.get(0));
			if (generoSeleccionado == null) return null;

			String isbn = JOptionPane.showInputDialog("Ingrese el ISBN del libro:", libro != null ? libro.getIsbn() : "");
			if (isbn == null || isbn.trim().isEmpty()) return null;

			String precioStr = JOptionPane.showInputDialog("Ingrese el precio del libro:", libro != null ? String.valueOf(libro.getPrecio()) : "");
			java.math.BigDecimal precio = new java.math.BigDecimal(precioStr);

			String fechaLanzamientoStr = JOptionPane.showInputDialog("Ingrese la fecha de lanzamiento (YYYY-MM-DD):", libro != null ? String.valueOf(libro.getFechaLanzamiento()) : "");
			java.sql.Date fechaLanzamiento = java.sql.Date.valueOf(fechaLanzamientoStr);

			return new Libro(0, titulo, autorSeleccionado.getIdAutor(), generoSeleccionado.getIdGenero(), isbn, precio, fechaLanzamiento);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Debe ingresar un ID y precio válidos.", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
}

