package servicios;

import dao.SucursalDAO;
import ventas.Sucursal;
import javax.swing.JOptionPane;
import java.util.List;

public class ServicioSucursal {
	private SucursalDAO sucursalDAO;

	public ServicioSucursal() {
		this.sucursalDAO = new SucursalDAO();
	}
	// Métodos GUI-compatible
	public boolean crearSucursalGUI(String nombre, String direccion) {
		if (nombre == null || nombre.trim().isEmpty() || direccion == null || direccion.trim().isEmpty()) {
			return false;
		}
		Sucursal sucursal = new Sucursal(nombre, direccion);
		Sucursal resultado = sucursalDAO.crearSucursal(sucursal);
		return resultado != null;
	}

	public boolean actualizarSucursalGUI(int id, String nombre, String direccion) {
		if (id <= 0 || nombre == null || nombre.trim().isEmpty() || direccion == null || direccion.trim().isEmpty()) {
			return false;
		}
		Sucursal sucursal = new Sucursal(id, nombre, direccion);
		return sucursalDAO.actualizar(sucursal);
	}

	public boolean eliminarSucursalGUI(int id) {
		return sucursalDAO.eliminar(id);
	}

	public Sucursal crearSucursal(String nombre, String direccion) {
		if (nombre == null || nombre.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre de la sucursal no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (direccion == null || direccion.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "La dirección de la sucursal no puede estar vacía.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		Sucursal nuevaSucursal = new Sucursal(nombre, direccion);
		return sucursalDAO.crearSucursal(nuevaSucursal);
	}

	public void crearSucursal() {
		String nombre = JOptionPane.showInputDialog("Ingrese el nombre de la sucursal:");
		String direccion = JOptionPane.showInputDialog("Ingrese la dirección de la sucursal:");
		crearSucursal(nombre, direccion);
	}

	public void listarSucursales() {
		List<Sucursal> sucursales = obtenerTodasLasSucursales();
		if (sucursales.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay sucursales registradas.");
			return;
		}
		StringBuilder sb = new StringBuilder("Lista de Sucursales:\n\n");
		for (Sucursal sucursal : sucursales) {
			sb.append(sucursal.getId()).append(": ").append(sucursal.getNombre()).append(" - ").append(sucursal.getDireccion()).append("\n");
		}
		JOptionPane.showMessageDialog(null, sb.toString());
	}

	public Sucursal obtenerPorId(int id) {
		return sucursalDAO.obtenerPorId(id);
	}

	public List<Sucursal> obtenerTodasLasSucursales() {
		return sucursalDAO.obtenerTodas();
	}

	public Sucursal seleccionarSucursal() {
		List<Sucursal> sucursales = obtenerTodasLasSucursales();
		if (sucursales.isEmpty()) {
			int opcion = JOptionPane.showConfirmDialog(null, "No hay sucursales registradas. ¿Desea crear una ahora?", "Sucursales", JOptionPane.YES_NO_OPTION);
			if (opcion == JOptionPane.YES_OPTION) {
				crearSucursal();
				sucursales = obtenerTodasLasSucursales();
				if (sucursales.isEmpty()) {
					return null;
				}
			} else {
				return null;
			}
		}
		return (Sucursal) JOptionPane.showInputDialog(null, "Seleccione una sucursal",
				"Sucursales", JOptionPane.QUESTION_MESSAGE, null, sucursales.toArray(), sucursales.get(0));
	}
}
