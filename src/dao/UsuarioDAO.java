package dao;

import usuarios.Usuario;
import usuarios.Rol;
import ventas.Sucursal;
import conexion.Conexion;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private Connection conexion;
    
    public UsuarioDAO() {
        this.conexion = Conexion.getInstance().getConnection();
    }
    
    public boolean crearUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre, email, password, id_rol, id_sucursal) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPassword());
            stmt.setInt(4, usuario.getIdRol());
            stmt.setInt(5, usuario.getIdSucursal());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
        }
        return false;
    }
    
    public Usuario obtenerUsuarioPorId(int id) {
    	System.out.println("El id que busco es "+id);
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        
        try (PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearResultSetAUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por ID: " + e.getMessage());
        }
        return null;
    }
    
    public Usuario obtenerUsuarioPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        
        try (PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearResultSetAUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por email: " + e.getMessage());
        }
        return null;
    }
    
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY nombre";
        
        try (PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapearResultSetAUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
        }
        return usuarios;
    }
    
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre = ?, email = ?, password = ?, id_rol = ?, id_sucursal = ? WHERE id_usuario = ?";
        
        try (PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPassword());
            stmt.setInt(4, usuario.getIdRol());
            stmt.setInt(5, usuario.getIdSucursal());
            stmt.setInt(6, usuario.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }
        return false;
    }
    
    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        
        try (PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
        return false;
    }
    
    public List<Rol> obtenerTodosLosRoles() {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT * FROM rol ORDER BY nombre_rol";
        
        try (PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                roles.add(new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol"), rs.getString("descripcion")));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener roles: " + e.getMessage());
        }
        return roles;
    }
    
    public List<Sucursal> obtenerTodasLasSucursales() {
        List<Sucursal> sucursales = new ArrayList<>();
        String sql = "SELECT * FROM sucursal ORDER BY nombre";
        
        try (PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                sucursales.add(new Sucursal(rs.getInt("id_sucursal"), rs.getString("nombre"), 
                    rs.getString("direccion")));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener sucursales: " + e.getMessage());
        }
        return sucursales;
    }
    
    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ?";
        
        try (PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar email: " + e.getMessage());
        }
        return false;
    }
    
    public Rol obtenerRolPorId(int id) {
        String sql = "SELECT * FROM rol WHERE id_rol = ?";
        
        try (PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol"), rs.getString("descripcion"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener rol por ID: " + e.getMessage());
        }
        return null;
    }
    
    private Usuario mapearResultSetAUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id_usuario"),
            rs.getString("nombre"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("id_rol"),
            rs.getInt("id_sucursal")
        );
    }
}
