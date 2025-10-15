package app;

import javax.swing.JOptionPane;
import interfaces.GestionUsuarios;
import dao.UsuarioDAO;
import servicios.ServicioSucursal;
import servicios.ServicioUsuario;
import usuarios.Rol;
import usuarios.Usuario;
import ventas.Sucursal;

public class IniciarPrograma {

	private ServicioUsuario servicioUsuario;
	private ServicioSucursal servicioSucursal;

	public IniciarPrograma() {
		this.servicioUsuario = new ServicioUsuario();
		this.servicioSucursal = new ServicioSucursal();
	}

	public void iniciar() {
		if (!servicioUsuario.existenUsuarios()) {
			boolean configurado = configurarSistema();
			if (!configurado) {
				JOptionPane.showMessageDialog(null, "La configuración inicial no se completó. El programa se cerrará.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		// Bucle del menú principal
		String[] opciones = { "Iniciar sesion", "Registrarse", "Salir" };
		int seleccion = 0;
		while (seleccion != 2) {
			seleccion = JOptionPane.showOptionDialog(
					null,
					"Seleccione una opción",
					"Sistema Gestion de Libros",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					opciones,
					opciones[0]
					);

			if (seleccion == -1) {
				seleccion = 2;
			}

			switch (seleccion) {
			case 0: new GestionUsuarios().iniciarSesion(); break;
			case 1: new GestionUsuarios().registrarUsuario(); break;
			case 2: JOptionPane.showMessageDialog(null, "Gracias, vuelva prontos."); break;
			}
		}
	}

	private boolean configurarSistema() {
		JOptionPane.showMessageDialog(null, 
				"Bienvenido al Sistema de Gestión de Libros.\n" +
						"Al ser el primer inicio, procederemos con la configuración inicial del sistema.",
						"Configuración Inicial",
						JOptionPane.INFORMATION_MESSAGE);

		// --- Paso 1: Crear la Sucursal Principal ---
		JOptionPane.showMessageDialog(null, "Paso 1: Crear la sucursal principal.");
		String nombreSucursal = JOptionPane.showInputDialog(null, "Nombre de la sucursal:", "Configuración de Sucursal", JOptionPane.QUESTION_MESSAGE);
		String direccionSucursal = JOptionPane.showInputDialog(null, "Dirección de la sucursal:", "Configuración de Sucursal", JOptionPane.QUESTION_MESSAGE);

		Sucursal nuevaSucursal = servicioSucursal.crearSucursal(nombreSucursal, direccionSucursal);
		if (nuevaSucursal == null) {
			JOptionPane.showMessageDialog(null, "No se pudo crear la sucursal. No se puede continuar.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		JOptionPane.showMessageDialog(null, "Sucursal '" + nuevaSucursal.getNombre() + "' creada con éxito.", "Paso 1 Completo", JOptionPane.INFORMATION_MESSAGE);

		// --- Paso 2: Crear el Administrador General ---
		JOptionPane.showMessageDialog(null, "Paso 2: Crear la cuenta del Administrador General.");
		String nombreAdmin = JOptionPane.showInputDialog(null, "Nombre del Administrador:", "Configuración de Administrador", JOptionPane.QUESTION_MESSAGE);
		String emailAdmin = JOptionPane.showInputDialog(null, "Email del Administrador:", "Configuración de Administrador", JOptionPane.QUESTION_MESSAGE);
		String passwordAdmin = JOptionPane.showInputDialog(null, "Contraseña para el Administrador:", "Configuración de Administrador", JOptionPane.QUESTION_MESSAGE);

		Rol rolAdmin = servicioUsuario.obtenerRolPorNombre("Administrador General");
		if (rolAdmin == null) {
			JOptionPane.showMessageDialog(null, "Error crítico: El rol 'Administrador General' no existe en la base de datos.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		boolean adminCreado = servicioUsuario.crearUsuario(
				nombreAdmin, 
				emailAdmin, 
				passwordAdmin, 
				rolAdmin.getId(), 
				nuevaSucursal.getId()
				);

		if (adminCreado) {
			JOptionPane.showMessageDialog(null, "¡Configuración completada con éxito!\nEl sistema ahora está listo para usarse.", "Configuración Finalizada", JOptionPane.INFORMATION_MESSAGE);
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "No se pudo crear la cuenta de administrador. No se puede continuar.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
			// Aquí se podría intentar borrar la sucursal creada pero TODO A FUTURO
			return false;
		}
	}
}
