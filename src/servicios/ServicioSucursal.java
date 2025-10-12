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

	public Sucursal obtenerPorId(int id) {
		return sucursalDAO.obtenerPorId(id);
	}

	public List<Sucursal> obtenerTodasLasSucursales() {
		return sucursalDAO.obtenerTodas();
	}
}
