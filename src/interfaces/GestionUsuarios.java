package interfaces;

import servicios.ServicioUsuario;
import usuarios.Usuario;
import usuarios.Rol;
import usuarios.AdministradorGeneral;
import usuarios.EncargadodeVentas;
import ventas.Sucursal;
import javax.swing.JOptionPane;
import java.util.List;

public class GestionUsuarios {
	private ServicioUsuario servicioUsuario;

	public GestionUsuarios() {
		this.servicioUsuario = new ServicioUsuario();
	}

	public void mostrarMenuGestionUsuarios() {
		String[] opciones = {
				"Crear Usuario",
				"Listar Usuarios", 
				"Buscar Usuario",
				"Actualizar Usuario",
				"Eliminar Usuario",
				"Volver al Menú Principal"
		};

		int seleccion = 0;

		while (seleccion != 5) {
			seleccion = JOptionPane.showOptionDialog(
					null,
					"Gestión de Usuarios",
					"Seleccione una opción",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					opciones,
					opciones[0]
					);

			switch (seleccion) {
			case 0: crearUsuario(); break;
			case 1: listarUsuarios(); break;
			case 2: buscarUsuario(); break;
			case 3: actualizarUsuario(); break;
			case 4: eliminarUsuario(); break;
			case 5: break; // Volver
			default: JOptionPane.showMessageDialog(null, "Opción no válida");
			}
		}
	}

	private void crearUsuario() {
		try {
			// Obtener datos del usuario
			String nombre = JOptionPane.showInputDialog(null, "Ingrese el nombre del usuario:");
			if (nombre == null || nombre.trim().isEmpty()) return;

			String email = JOptionPane.showInputDialog(null, "Ingrese el email del usuario:");
			if (email == null || email.trim().isEmpty()) return;

			String password = JOptionPane.showInputDialog(null, "Ingrese la contraseña (mínimo 6 caracteres):");
			if (password == null || password.trim().isEmpty()) return;

			// Seleccionar rol (restringido para admin)
			List<Rol> rolesDisponibles = servicioUsuario.obtenerRoles();
			List<Rol> rolesPermitidos = new java.util.ArrayList<>();
			for (Rol rol : rolesDisponibles) {
				if (rol.getNombre().equalsIgnoreCase("Encargado de Ventas")) {
					rolesPermitidos.add(rol);
				}
			}

			if (rolesPermitidos.isEmpty()) {
				JOptionPane.showMessageDialog(null, "No se encontró el rol 'Encargado de Ventas'.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String[] opcionesRoles = new String[rolesPermitidos.size()];
			for (int i = 0; i < rolesPermitidos.size(); i++) {
				opcionesRoles[i] = rolesPermitidos.get(i).getNombre();
			}

			int rolSeleccionado = JOptionPane.showOptionDialog(
					null,
					"Seleccione el rol:",
					"Seleccionar Rol",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					opcionesRoles,
					opcionesRoles[0]
					);

			if (rolSeleccionado == -1) return;

			// Seleccionar sucursal
			List<Sucursal> sucursales = servicioUsuario.obtenerSucursales();
			if (sucursales.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Debe crear al menos una sucursal antes de asignar un encargado.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String[] opcionesSucursales = new String[sucursales.size()];
			for (int i = 0; i < sucursales.size(); i++) {
				opcionesSucursales[i] = sucursales.get(i).getNombre();
			}

			int sucursalSeleccionada = JOptionPane.showOptionDialog(
					null,
					"Seleccione la sucursal:",
					"Seleccionar Sucursal",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					opcionesSucursales,
					opcionesSucursales[0]
					);

			if (sucursalSeleccionada == -1) return;

			// Crear usuario
			servicioUsuario.crearUsuario(
					nombre.trim(),
					email.trim(),
					password,
					rolesPermitidos.get(rolSeleccionado).getId(),
					sucursales.get(sucursalSeleccionada).getId()
					);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al crear usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void listarUsuarios() {
		servicioUsuario.mostrarListaUsuarios();
	}

	private void buscarUsuario() {
		Usuario usuario = buscarUsuarioInteractivamente();
		if (usuario != null) {
			mostrarInformacionUsuario(usuario);
		}
	}
	// Método de búsqueda reutilizable
	private Usuario buscarUsuarioInteractivamente() {
		String[] opcionesBusqueda = {"Buscar por ID", "Buscar por Email"};
		int seleccion = JOptionPane.showOptionDialog(
				null,
				"¿Cómo desea buscar al usuario?",
				"Buscar Usuario",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				opcionesBusqueda,
				opcionesBusqueda[0]
				);

		if (seleccion == -1) return null;

		Usuario usuario = null;
		try {
			if (seleccion == 0) { // Buscar por ID
				String input = JOptionPane.showInputDialog(null, "Ingrese el ID del usuario a buscar:");
				if (input == null || input.trim().isEmpty()) return null;
				int id = Integer.parseInt(input.trim());
				usuario = servicioUsuario.obtenerUsuario(id);
			} else { // Buscar por Email
				String email = JOptionPane.showInputDialog(null, "Ingrese el Email del usuario a buscar:");
				if (email == null || email.trim().isEmpty()) return null;
				usuario = servicioUsuario.obtenerUsuarioPorEmail(email.trim());
			}

			if (usuario == null) {
				JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Por favor ingrese un ID válido", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al buscar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		return usuario;
	}

	private void mostrarInformacionUsuario(Usuario usuario) {Rol rol = servicioUsuario.obtenerRolPorId(usuario.getIdRol());
	String nombreRol = (rol != null) ? rol.getNombre() : "Rol no encontrado";

	String nombreSucursal = "N/A";
	if (usuario.getIdSucursal() != null) {
		// Necesitamos una instancia de ServicioSucursal para esto.
		// Por simplicidad, la creamos aquí, pero idealmente se inyectaría.
		Sucursal sucursal = new servicios.ServicioSucursal().obtenerPorId(usuario.getIdSucursal());
		if (sucursal != null) {
			nombreSucursal = sucursal.getNombre();
		}
	}

	StringBuilder sb = new StringBuilder();
	sb.append("Información del Usuario:\n\n");
	sb.append("ID: ").append(usuario.getId()).append("\n");
	sb.append("Nombre: ").append(usuario.getNombre()).append("\n");
	sb.append("Email: ").append(usuario.getEmail()).append("\n");
	sb.append("Rol: ").append(nombreRol).append("\n");
	sb.append("Sucursal: ").append(nombreSucursal).append("\n");

	JOptionPane.showMessageDialog(null, sb.toString(), "Usuario Encontrado", JOptionPane.INFORMATION_MESSAGE);
	}
	private void actualizarUsuario() {
		Usuario usuario = buscarUsuarioInteractivamente();
		if (usuario == null) {
			return; // Mensaje de error ya mostrado en el helper
		}

		try {
			// Mostrar datos actuales de forma más limpia
			mostrarInformacionUsuario(usuario);

			// Obtener nuevos datos
			String nombre = JOptionPane.showInputDialog(null, "Ingrese el nuevo nombre:", usuario.getNombre());
			if (nombre == null) return;

			String email = JOptionPane.showInputDialog(null, "Ingrese el nuevo email:", usuario.getEmail());
			if (email == null) return;

			String password = JOptionPane.showInputDialog(null, "Ingrese nueva contraseña (dejar vacío para no cambiar):");
			if (password == null) return;

			// Lógica para seleccionar rol (restringido)
			List<Rol> rolesDisponibles = servicioUsuario.obtenerRoles();
			List<Rol> rolesPermitidos = new java.util.ArrayList<>();
			for (Rol rol : rolesDisponibles) {
				if (rol.getNombre().equalsIgnoreCase("Encargado de Ventas") || rol.getNombre().equalsIgnoreCase("Usuario")) {
					rolesPermitidos.add(rol);
				}
			}
			String[] opcionesRoles = rolesPermitidos.stream().map(Rol::getNombre).toArray(String[]::new);
			int rolSeleccionadoIdx = JOptionPane.showOptionDialog(null, "Seleccione el nuevo rol:", "Seleccionar Rol", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcionesRoles, opcionesRoles[0]);
			if (rolSeleccionadoIdx == -1) return;
			int nuevoRolId = rolesPermitidos.get(rolSeleccionadoIdx).getId();

			// Lógica para seleccionar sucursal
			List<Sucursal> sucursales = servicioUsuario.obtenerSucursales();
			Integer nuevaSucursalId = usuario.getIdSucursal(); // Mantener la actual por defecto
			if (!sucursales.isEmpty()) {
				String[] opcionesSucursales = sucursales.stream().map(Sucursal::getNombre).toArray(String[]::new);
				int sucursalSeleccionadaIdx = JOptionPane.showOptionDialog(null, "Seleccione la nueva sucursal:", "Seleccionar Sucursal", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcionesSucursales, opcionesSucursales[0]);
				if (sucursalSeleccionadaIdx != -1) {
					nuevaSucursalId = sucursales.get(sucursalSeleccionadaIdx).getId();
				}
			}

			servicioUsuario.actualizarUsuario(
					usuario.getId(),
					nombre.trim(),
					email.trim(),
					password.trim().isEmpty() ? null : password,
							nuevoRolId,
							nuevaSucursalId
					);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void eliminarUsuario() {
		Usuario usuario = buscarUsuarioInteractivamente();
		if (usuario == null) {
			return; // Mensaje de error ya mostrado en el helper
		}

		try {
			int confirmacion = JOptionPane.showConfirmDialog(
					null, 
					"¿Está seguro de que desea eliminar al usuario: " + usuario.getNombre() + "?", 
					"Confirmar eliminación", 
					JOptionPane.YES_NO_OPTION
					);

			if (confirmacion == JOptionPane.YES_OPTION) {
				servicioUsuario.eliminarUsuario(usuario.getId());
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al eliminar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void iniciarSesion() {
		try {
			String email = JOptionPane.showInputDialog(null, "Ingrese su email:");
			if (email == null || email.trim().isEmpty()) return;

			String password = JOptionPane.showInputDialog(null, "Ingrese su contraseña:");
			if (password == null || password.trim().isEmpty()) return;

			Usuario usuario = servicioUsuario.login(email.trim(), password);
			if (usuario != null) {
				JOptionPane.showMessageDialog(null, "Login exitoso. Bienvenido " + usuario.getNombre() + "!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

				// Redirigir según el rol del usuario
				redirigirSegunRol(usuario);
			} else {
				JOptionPane.showMessageDialog(null, "Email o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error en el login: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void registrarUsuario() {
		try {
			// Obtener datos del usuario
			String nombre = JOptionPane.showInputDialog(null, "Ingrese su nombre:");
			if (nombre == null || nombre.trim().isEmpty()) return;

			            String email = JOptionPane.showInputDialog(null, "Ingrese su email:");
			            if (email == null || !Usuario.isValidEmail(email.trim())) {
			                JOptionPane.showMessageDialog(null, "Email inválido.");
			                return;
			            }
			            if (servicioUsuario.emailExiste(email.trim())) {
			                JOptionPane.showMessageDialog(null, "Ya existe un usuario con ese email.", "Error", JOptionPane.ERROR_MESSAGE);
			                return;
			            }
			String password = JOptionPane.showInputDialog(null, "Ingrese su contraseña:");
			if (password == null || password.trim().isEmpty()) return;

			// Asignar rol de "Usuario" por defecto
			Rol rolUsuario = servicioUsuario.obtenerRolPorNombre("Usuario");
			if (rolUsuario == null) {
				JOptionPane.showMessageDialog(null, "Error crítico: El rol por defecto 'Usuario' no se encuentra.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Seleccionar sucursal
			List<Sucursal> sucursales = servicioUsuario.obtenerSucursales();
			if (sucursales.isEmpty()) {
				JOptionPane.showMessageDialog(null, "No hay sucursales disponibles para el registro. Contacte a un administrador.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String[] opcionesSucursales = new String[sucursales.size()];
			for (int i = 0; i < sucursales.size(); i++) {
				opcionesSucursales[i] = sucursales.get(i).getNombre();
			}

			int sucursalSeleccionada = JOptionPane.showOptionDialog(
					null,
					"Seleccione su sucursal:",
					"Seleccionar Sucursal",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					opcionesSucursales,
					opcionesSucursales[0]
					);

			if (sucursalSeleccionada == -1) return;

			// Crear usuario
			            servicioUsuario.crearUsuario(
			                nombre.trim(),
			                email.trim(),
			                password,
			                rolUsuario.getId(),
			                sucursales.get(sucursalSeleccionada).getId()
			            );
			
			        } catch (Exception e) {			JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Método para redirigir según el rol del usuario
	private void redirigirSegunRol(Usuario usuario) {
		try {
			Rol rol = servicioUsuario.obtenerRolPorId(usuario.getIdRol());

			if (rol == null) {
				JOptionPane.showMessageDialog(null, "Error: No se pudo determinar el rol del usuario", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String nombreRol = rol.getNombre().toLowerCase();

			if (nombreRol.contains("administrador") || nombreRol.contains("admin")) {
				// Crear instancia de AdministradorGeneral y mostrar su menú
				AdministradorGeneral admin = new AdministradorGeneral(
						usuario.getId(),
						usuario.getNombre(),
						usuario.getEmail(),
						usuario.getPassword(),
						usuario.getIdRol(),
						usuario.getIdSucursal()
						);
				admin.MostrarMenu();

			} else if (nombreRol.contains("ventas") || nombreRol.contains("vendedor") || nombreRol.contains("encargado")) {
				// Crear instancia de EncargadodeVentas y mostrar su menú
				EncargadodeVentas vendedor = new EncargadodeVentas(
						usuario.getId(),
						usuario.getNombre(),
						usuario.getEmail(),
						usuario.getPassword(),
						usuario.getIdRol(),
						usuario.getIdSucursal()
						);
				vendedor.MostrarMenu();

			} else if (nombreRol.contains("usuario")) {
				// Menú para el rol de Usuario/Cliente
				String[] opcionesUsuario = {"Ver mi perfil", "Ver libros", "Salir"};
				int seleccionUsuario = -1;
				while (seleccionUsuario != 2) {
					seleccionUsuario = JOptionPane.showOptionDialog(
							null,
							"Bienvenido, " + usuario.getNombre(),
							"Menú de Usuario",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE,
							null,
							opcionesUsuario,
							opcionesUsuario[0]
							);

					switch (seleccionUsuario) {
					case 0: JOptionPane.showMessageDialog(null, "Función 'Ver mi perfil' no implementada."); break;
					case 1: JOptionPane.showMessageDialog(null, "Función 'Ver libros' no implementada."); break;
					case 2: break; // Salir
					}
				}

			} else {
				// Rol no reconocido, mostrar mensaje de error simple
				JOptionPane.showMessageDialog(null, "Su rol ('" + rol.getNombre() + "') no tiene un menú asignado.", "Error de Rol", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al redirigir según rol: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
