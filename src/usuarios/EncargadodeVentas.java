package usuarios;

import interfaces.Menu;
import servicios.ServicioVenta;
import servicios.ServicioLibro;
import servicios.ServicioStock;

import javax.swing.JOptionPane;

public class EncargadodeVentas extends Usuario implements Menu {
	private ServicioVenta servicioVenta;
	private ServicioLibro servicioLibro;
	private ServicioStock servicioStock;
	
	public EncargadodeVentas(int id, String nombre, String email, String password, int idRol, int idSucursal) {
		super(id, nombre, email, password, idRol, idSucursal);
		this.servicioVenta = new ServicioVenta();
		this.servicioLibro = new ServicioLibro();
		this.servicioStock = new ServicioStock();
	}

	@Override
	public void MostrarMenu() {
		String[] opciones = {
				"Buscar libro",
				"Consultar stock",
				"Registrar venta",
				"Consultar ventas",
				"Anular venta",
				"Salir"
		};

		int seleccion = 0;

		while (seleccion != 5) {
			seleccion = JOptionPane.showOptionDialog(null,
					"Menú Encargado de Ventas",
					"Seleccione una opción",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					opciones,
					opciones[0]);

			switch (seleccion) {
			case 0:
				servicioLibro.buscarLibro();
				break;
			case 1:
				ServicioStock servicioStock = new ServicioStock();
				servicioStock.consultarStock(this.getIdSucursal());
				break;
			case 2:
				servicioVenta.crearVenta(this.getId(), this.getIdSucursal()); 
				break;
			case 3:
				servicioVenta.mostrarListaVentas(); 
				break;
			case 4:
				servicioVenta.eliminarVenta(); 
				break;
			case 5:
				JOptionPane.showMessageDialog(null, "Saliendo del menú de ventas");
				break;
			default:
				JOptionPane.showMessageDialog(null, "Opción no válida");
			}
		}
	}

}
