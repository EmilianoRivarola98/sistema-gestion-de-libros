package servicios;

import dao.UsuarioDAO;
import usuarios.Usuario;
import usuarios.Rol;
import ventas.Sucursal;
import javax.swing.JOptionPane;
import java.util.List;

public class ServicioUsuario {
    private UsuarioDAO usuarioDAO;
    
    public ServicioUsuario() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    // Crear nuevo usuario con validaciones
    public boolean crearUsuario(String nombre, String email, String password, int idRol, int idSucursal) {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!Usuario.isValidEmail(email)) {
            JOptionPane.showMessageDialog(null, "El formato del email no es válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (usuarioDAO.emailExiste(email)) {
            JOptionPane.showMessageDialog(null, "Ya existe un usuario con ese email", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (password == null || password.length() < 6) {
            JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 6 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Crear usuario con password hasheado
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
    
    // Obtener usuario por ID
    public Usuario obtenerUsuario(int id) {
        return usuarioDAO.obtenerUsuarioPorId(id);
    }
    
    // Obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioDAO.obtenerTodosLosUsuarios();
    }
    
    // Actualizar usuario
    public boolean actualizarUsuario(int id, String nombre, String email, String password, int idRol, int idSucursal) {
        Usuario usuario = usuarioDAO.obtenerUsuarioPorId(id);
        if (usuario == null) {
            JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!Usuario.isValidEmail(email)) {
            JOptionPane.showMessageDialog(null, "El formato del email no es válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Verificar si el email ya existe en otro usuario
        Usuario usuarioConEmail = usuarioDAO.obtenerUsuarioPorEmail(email);
        if (usuarioConEmail != null && usuarioConEmail.getId() != id) {
            JOptionPane.showMessageDialog(null, "Ya existe otro usuario con ese email", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Actualizar datos
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setIdRol(idRol);
        usuario.setIdSucursal(idSucursal);
        
        // Solo actualizar password si se proporciona uno nuevo
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
    
    // Eliminar usuario
    public boolean eliminarUsuario(int id) {
        Usuario usuario = usuarioDAO.obtenerUsuarioPorId(id);
        if (usuario == null) {
            JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(
            null, 
            "¿Está seguro de que desea eliminar al usuario: " + usuario.getNombre() + "?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION
        );
        
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
    
    // Obtener roles para mostrar en select
    public List<Rol> obtenerRoles() {
        return usuarioDAO.obtenerTodosLosRoles();
    }
    
    // Obtener sucursales para mostrar en select
    public List<Sucursal> obtenerSucursales() {
        return usuarioDAO.obtenerTodasLasSucursales();
    }
    
    // Obtener rol por ID
    public Rol obtenerRolPorId(int idRol) {
        return usuarioDAO.obtenerRolPorId(idRol);
    }
    
    // Método auxiliar para mostrar lista de usuarios
    public void mostrarListaUsuarios() {
        List<Usuario> usuarios = obtenerTodosLosUsuarios();
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay usuarios registrados", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Lista de Usuarios:\n\n");
        
        for (Usuario usuario : usuarios) {
            sb.append("ID: ").append(usuario.getId()).append("\n");
            sb.append("Nombre: ").append(usuario.getNombre()).append("\n");
            sb.append("Email: ").append(usuario.getEmail()).append("\n");
            sb.append("ID Rol: ").append(usuario.getIdRol()).append("\n");
            sb.append("ID Sucursal: ").append(usuario.getIdSucursal()).append("\n");
            sb.append("----------------------------------------\n");
        }
        
        JOptionPane.showMessageDialog(null, sb.toString(), "Lista de Usuarios", JOptionPane.INFORMATION_MESSAGE);
    }

    //Metodo de login
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
