package servicios;

import dao.UsuarioDAO;
import usuarios.Usuario;
import usuarios.Rol;
import ventas.Sucursal;
import javax.swing.JOptionPane;
import java.util.List;

public class ServicioUsuario {
	private UsuarioDAO usuarioDAO;
	private ServicioSucursal servicioSucursal;

	public ServicioUsuario() {
		this.usuarioDAO = new UsuarioDAO();
		this.servicioSucursal = new ServicioSucursal();
	}

	public boolean crearUsuario(String nombre, String email, String password, int idRol, Integer idSucursal) {
		if (nombre == null || nombre.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!Usuario.isValidEmail(email)) {
			JOptionPane.showMessageDialog(null, "El formato del email no es válido", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (password == null || password.length() < 6) {
			JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 6 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		String passwordHasheado = Usuario.hashPassword(password);
		Usuario usuario = new Usuario(nombre, email, passwordHasheado, idRol, idSucursal);
		boolean resultado = usuarioDAO.crearUsuario(usuario);
		if (resultado) {
			JOptionPane.showMessageDialog(null, "Usuario creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Error al crear el usuario", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return resultado;
	}

	public Usuario obtenerUsuario(int id) {
		return usuarioDAO.obtenerUsuarioPorId(id);
	}

	public Usuario obtenerUsuarioPorEmail(String email) {

		return usuarioDAO.obtenerUsuarioPorEmail(email);

	}



	public boolean emailExiste(String email) {

		return usuarioDAO.emailExiste(email);

	}


	public List<Usuario> obtenerTodosLosUsuarios() {
		return usuarioDAO.obtenerTodosLosUsuarios();
	}

	public boolean actualizarUsuario(int id, String nombre, String email, String password, int idRol, Integer idSucursal) {
		Usuario usuario = usuarioDAO.obtenerUsuarioPorId(id);
		if (usuario == null) {
			JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (nombre == null || nombre.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!Usuario.isValidEmail(email)) {
			JOptionPane.showMessageDialog(null, "El formato del email no es válido", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		Usuario usuarioConEmail = usuarioDAO.obtenerUsuarioPorEmail(email);
		if (usuarioConEmail != null && usuarioConEmail.getId() != id) {
			JOptionPane.showMessageDialog(null, "Ya existe otro usuario con ese email", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		usuario.setNombre(nombre);
		usuario.setEmail(email);
		usuario.setIdRol(idRol);
		usuario.setIdSucursal(idSucursal);
		if (password != null && !password.trim().isEmpty()) {
			if (password.length() < 6) {
				JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 6 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			usuario.setPassword(Usuario.hashPassword(password));
		}
		boolean resultado = usuarioDAO.actualizarUsuario(usuario);
		if (resultado) {
			JOptionPane.showMessageDialog(null, "Usuario actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Error al actualizar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return resultado;
	}

	public boolean eliminarUsuario(int id) {
		Usuario usuario = usuarioDAO.obtenerUsuarioPorId(id);
		if (usuario == null) {
			JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar al usuario: " + usuario.getNombre() + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION) {
			boolean resultado = usuarioDAO.eliminarUsuario(id);
			if (resultado) {
				JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Error al eliminar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
			}
			return resultado;
		}
		return false;
	}

	public List<Rol> obtenerRoles() {
		return usuarioDAO.obtenerTodosLosRoles();
	}

	public List<Sucursal> obtenerSucursales() {
		return usuarioDAO.obtenerTodasLasSucursales();
	}

	public boolean existenUsuarios() {
		return usuarioDAO.existenUsuarios();
	}

	public Rol obtenerRolPorId(int idRol) {
		return usuarioDAO.obtenerRolPorId(idRol);
	}

	public Rol obtenerRolPorNombre(String nombreRol) {
		return usuarioDAO.obtenerRolPorNombre(nombreRol);
	}

	public void mostrarListaUsuarios() {
		List<Usuario> usuarios = obtenerTodosLosUsuarios();
		if (usuarios.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay usuarios registrados", "Información", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Lista de Usuarios (Total: ").append(usuarios.size()).append(")\n\n");
		for (Usuario usuario : usuarios) {
			Rol rol = obtenerRolPorId(usuario.getIdRol());
			String nombreRol = (rol != null) ? rol.getNombre() : "Rol no encontrado";
			String nombreSucursal = "N/A";
			if (usuario.getIdSucursal() != null) {
				Sucursal sucursal = servicioSucursal.obtenerPorId(usuario.getIdSucursal());
				if (sucursal != null) {
					nombreSucursal = sucursal.getNombre();
				}
			}
			sb.append("ID: ").append(usuario.getId()).append("\n");
			sb.append("Nombre: ").append(usuario.getNombre()).append("\n");
			sb.append("Email: ").append(usuario.getEmail()).append("\n");
			sb.append("Rol: ").append(nombreRol).append("\n");
			sb.append("Sucursal: ").append(nombreSucursal).append("\n");
			sb.append("----------------------------------------\n");
		}
		javax.swing.JTextArea textArea = new javax.swing.JTextArea(sb.toString());
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea);
		textArea.setEditable(false);
		scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));
		JOptionPane.showMessageDialog(null, scrollPane, "Lista de Usuarios", JOptionPane.INFORMATION_MESSAGE);
	}

	public Usuario login(String email, String password) {
		if (!Usuario.isValidEmail(email)) {
			JOptionPane.showMessageDialog(null, "El formato del email no es válido", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		Usuario usuario = usuarioDAO.obtenerUsuarioPorEmail(email);
		if (usuario == null) {
			JOptionPane.showMessageDialog(null, "No existe un usuario con ese email", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (!usuario.verifyPassword(password)) {
			JOptionPane.showMessageDialog(null, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return usuario;
	}
}