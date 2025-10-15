package usuarios;

import interfaces.GestionAutores;
import interfaces.GestionGeneros;
import interfaces.Menu;
import interfaces.GestionUsuarios;
import javax.swing.JOptionPane;
import interfaces.GestionLibros;

public class AdministradorGeneral extends Usuario implements Menu {
	private GestionUsuarios gestionUsuarios;
	private GestionLibros gestionLibros;
	private GestionAutores gestionAutores;
	private GestionGeneros gestionGeneros;

	public AdministradorGeneral(int id, String nombre, String email, String password, int idRol, int idSucursal) {
		super(id, nombre, email, password, idRol, idSucursal);
		this.gestionUsuarios = new GestionUsuarios();
		this.gestionLibros = new GestionLibros();
		this.gestionAutores = new GestionAutores();
		this.gestionGeneros = new GestionGeneros();
	}

	@Override
	public void MostrarMenu() {
		String[] opciones = {
				"Gestionar usuarios",
				"Gestionar Libros y Stock",
				"Gestionar Autores",
				"Gestionar Géneros",
				"Generar pedido de faltantes",
				"Gestionar promociones",
				"Consultar reportes",
				"Salir"
		};

		int seleccion = 0;

		while (seleccion != 7) { 
			seleccion = JOptionPane.showOptionDialog(
					null,
					"Menú Administrador General",
					"Seleccione una opción",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					opciones,
					opciones[0]
					);

			switch (seleccion) {
			case 0: gestionUsuarios.mostrarMenuGestionUsuarios(); break;
			case 1: gestionLibros.mostrarMenu(); break;
			case 2: gestionAutores.mostrarMenu(); break;
			case 3: gestionGeneros.mostrarMenu(); break;
			case 4: generarPedidoFaltantes(); break;
			case 5: gestionarPromociones(); break;
			case 6: gestionarReportes(); break;
			case 7: JOptionPane.showMessageDialog(null, "Saliendo del menú de administrador"); break;
			default: JOptionPane.showMessageDialog(null, "Opción no válida");
			}
		}
	}

	// Métodos
	private void gestionarUsuarios() {
		gestionUsuarios.mostrarMenuGestionUsuarios();
	}

	private void generarPedidoFaltantes() {
		JOptionPane.showMessageDialog(null, "Función Generar pedido de faltantes");
	}

	private void gestionarPromociones() {
		JOptionPane.showMessageDialog(null, "Función Gestionar promociones");
	}
	private void gestionarReportes() {
		JOptionPane.showMessageDialog(null, "Función Reportes");
	}
}