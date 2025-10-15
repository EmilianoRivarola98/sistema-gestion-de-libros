package servicios;

import dao.GeneroDAO;
import ventas.Genero;
import javax.swing.JOptionPane;
import java.util.List;

public class ServicioGenero {
	private GeneroDAO generoDAO;

	public ServicioGenero() {
		this.generoDAO = new GeneroDAO();
	}

	public List<Genero> obtenerTodos() {
		return generoDAO.obtenerTodos();
	}

	public void crearGenero() {
		String nombre = JOptionPane.showInputDialog("Ingrese el nombre del género:");
		if (nombre != null && !nombre.trim().isEmpty()) {
			if (generoDAO.crearGenero(nombre.trim())) {
				JOptionPane.showMessageDialog(null, "Género creado correctamente.");
			} else {
				JOptionPane.showMessageDialog(null, "Error al crear el género.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void listarGeneros() {
		List<Genero> generos = obtenerTodos();
		if (generos.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay géneros registrados.");
			return;
		}
		StringBuilder sb = new StringBuilder("Lista de Géneros:\n\n");
		for (Genero genero : generos) {
			sb.append(genero.getIdGenero()).append(": ").append(genero.getNombre()).append("\n");
		}
		JOptionPane.showMessageDialog(null, sb.toString());
	}

	public void actualizarGenero() {
		try {
			int idGenero = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del género a actualizar:"));
			String nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del género:");
			if (nombre != null && !nombre.trim().isEmpty()) {
				if (generoDAO.actualizarGenero(idGenero, nombre.trim())) {
					JOptionPane.showMessageDialog(null, "Género actualizado correctamente.");
				} else {
					JOptionPane.showMessageDialog(null, "Error al actualizar el género.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Debe ingresar un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void eliminarGenero() {
		try {
			int idGenero = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del género a eliminar:"));
			int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este género?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
			if (confirmacion == JOptionPane.YES_OPTION) {
				if (generoDAO.eliminarGenero(idGenero)) {
					JOptionPane.showMessageDialog(null, "Género eliminado correctamente.");
				} else {
					JOptionPane.showMessageDialog(null, "Error al eliminar el género.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Debe ingresar un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
