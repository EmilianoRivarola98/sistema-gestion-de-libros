package servicios;

import dao.AutorDAO;
import ventas.Autor;

import java.util.List;

import javax.swing.JOptionPane;

public class ServicioAutor {
	private AutorDAO autorDAO;

	public ServicioAutor() {
		this.autorDAO = new AutorDAO();
	}

	public List<Autor> obtenerTodos() {
		return autorDAO.obtenerTodos();
	}

	public void crearAutor() {
		String nombre = JOptionPane.showInputDialog("Ingrese el nombre del autor:");
		if (nombre != null && !nombre.trim().isEmpty()) {
			if (autorDAO.crearAutor(nombre.trim())) {
				JOptionPane.showMessageDialog(null, "Autor creado correctamente.");
			} else {
				JOptionPane.showMessageDialog(null, "Error al crear el autor.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void listarAutores() {
		List<Autor> autores = obtenerTodos();
		if (autores.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay autores registrados.");
			return;
		}
		StringBuilder sb = new StringBuilder("Lista de Autores:\n\n");
		for (Autor autor : autores) {
			sb.append(autor.getIdAutor()).append(": ").append(autor.getNombre()).append("\n");
		}
		JOptionPane.showMessageDialog(null, sb.toString());
	}

	public void actualizarAutor() {
		try {
			int idAutor = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del autor a actualizar:"));
			String nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del autor:");
			if (nombre != null && !nombre.trim().isEmpty()) {
				if (autorDAO.actualizarAutor(idAutor, nombre.trim())) {
					JOptionPane.showMessageDialog(null, "Autor actualizado correctamente.");
				} else {
					JOptionPane.showMessageDialog(null, "Error al actualizar el autor.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Debe ingresar un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void eliminarAutor() {
		try {
			int idAutor = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del autor a eliminar:"));
			int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este autor?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
			if (confirmacion == JOptionPane.YES_OPTION) {
				if (autorDAO.eliminarAutor(idAutor)) {
					JOptionPane.showMessageDialog(null, "Autor eliminado correctamente.");
				} else {
					JOptionPane.showMessageDialog(null, "Error al eliminar el autor.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Debe ingresar un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
