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
            
            // Seleccionar rol
            List<Rol> roles = servicioUsuario.obtenerRoles();
            if (roles.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay roles disponibles", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String[] opcionesRoles = new String[roles.size()];
            for (int i = 0; i < roles.size(); i++) {
                opcionesRoles[i] = roles.get(i).getNombre();
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
                JOptionPane.showMessageDialog(null, "No hay sucursales disponibles", "Error", JOptionPane.ERROR_MESSAGE);
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
                roles.get(rolSeleccionado).getId(),
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
        try {
            String input = JOptionPane.showInputDialog(null, "Ingrese el ID del usuario a buscar:");
            if (input == null || input.trim().isEmpty()) return;
            
            int id = Integer.parseInt(input.trim());
            Usuario usuario = servicioUsuario.obtenerUsuario(id);
            
            if (usuario != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Información del Usuario:\n\n");
                sb.append("ID: ").append(usuario.getId()).append("\n");
                sb.append("Nombre: ").append(usuario.getNombre()).append("\n");
                sb.append("Email: ").append(usuario.getEmail()).append("\n");
                sb.append("ID Rol: ").append(usuario.getIdRol()).append("\n");
                sb.append("ID Sucursal: ").append(usuario.getIdSucursal()).append("\n");
                
                JOptionPane.showMessageDialog(null, sb.toString(), "Usuario Encontrado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese un ID válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarUsuario() {
        try {
            String input = JOptionPane.showInputDialog(null, "Ingrese el ID del usuario a actualizar:");
            if (input == null || input.trim().isEmpty()) return;
            
            int id = Integer.parseInt(input.trim());
            Usuario usuario = servicioUsuario.obtenerUsuario(id);
            
            if (usuario == null) {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Mostrar datos actuales
            JOptionPane.showMessageDialog(null, 
                "Datos actuales del usuario:\n" +
                "Nombre: " + usuario.getNombre() + "\n" +
                "Email: " + usuario.getEmail() + "\n" +
                "ID Rol: " + usuario.getIdRol() + "\n" +
                "ID Sucursal: " + usuario.getIdSucursal(),
                "Datos Actuales", JOptionPane.INFORMATION_MESSAGE);
            
            // Obtener nuevos datos
            String nombre = JOptionPane.showInputDialog(null, "Ingrese el nuevo nombre (actual: " + usuario.getNombre() + "):");
            if (nombre == null || nombre.trim().isEmpty()) return;
            
            String email = JOptionPane.showInputDialog(null, "Ingrese el nuevo email (actual: " + usuario.getEmail() + "):");
            if (email == null || email.trim().isEmpty()) return;
            
            String password = JOptionPane.showInputDialog(null, "Ingrese nueva contraseña (dejar vacío para mantener la actual):");
            if (password == null) return;
            
            // Seleccionar nuevo rol
            List<Rol> roles = servicioUsuario.obtenerRoles();
            String[] opcionesRoles = new String[roles.size()];
            for (int i = 0; i < roles.size(); i++) {
                opcionesRoles[i] = roles.get(i).getNombre();
            }
            
            int rolSeleccionado = JOptionPane.showOptionDialog(
                null,
                "Seleccione el nuevo rol:",
                "Seleccionar Rol",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesRoles,
                opcionesRoles[0]
            );
            
            if (rolSeleccionado == -1) return;
            
            // Seleccionar nueva sucursal
            List<Sucursal> sucursales = servicioUsuario.obtenerSucursales();
            String[] opcionesSucursales = new String[sucursales.size()];
            for (int i = 0; i < sucursales.size(); i++) {
                opcionesSucursales[i] = sucursales.get(i).getNombre();
            }
            
            int sucursalSeleccionada = JOptionPane.showOptionDialog(
                null,
                "Seleccione la nueva sucursal:",
                "Seleccionar Sucursal",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesSucursales,
                opcionesSucursales[0]
            );
            
            if (sucursalSeleccionada == -1) return;
            
            // Actualizar usuario
            servicioUsuario.actualizarUsuario(
                id,
                nombre.trim(),
                email.trim(),
                password.trim().isEmpty() ? null : password,
                roles.get(rolSeleccionado).getId(),
                sucursales.get(sucursalSeleccionada).getId()
            );
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese un ID válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarUsuario() {
        try {
            String input = JOptionPane.showInputDialog(null, "Ingrese el ID del usuario a eliminar:");
            if (input == null || input.trim().isEmpty()) return;
            
            int id = Integer.parseInt(input.trim());
            servicioUsuario.eliminarUsuario(id);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese un ID válido", "Error", JOptionPane.ERROR_MESSAGE);
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
            if (email == null || email.trim().isEmpty()) return;
            
            String password = JOptionPane.showInputDialog(null, "Ingrese su contraseña (mínimo 6 caracteres):");
            if (password == null || password.trim().isEmpty()) return;
            
            // Seleccionar rol (asignar rol por defecto si es necesario)
            List<Rol> roles = servicioUsuario.obtenerRoles();
            if (roles.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay roles disponibles", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String[] opcionesRoles = new String[roles.size()];
            for (int i = 0; i < roles.size(); i++) {
                opcionesRoles[i] = roles.get(i).getNombre();
            }
            
            int rolSeleccionado = JOptionPane.showOptionDialog(
                null,
                "Seleccione su rol:",
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
                JOptionPane.showMessageDialog(null, "No hay sucursales disponibles", "Error", JOptionPane.ERROR_MESSAGE);
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
                roles.get(rolSeleccionado).getId(),
                sucursales.get(sucursalSeleccionada).getId()
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                
            } else {
                // Rol no reconocido, mostrar mensaje y permitir selección manual
                String[] opciones = {"Administrador", "Encargado de Ventas"};
                
                int seleccion = JOptionPane.showOptionDialog(
                    null,
                    "Rol no reconocido automáticamente: " + rol.getNombre() + "\n" +
                    "Por favor, seleccione el tipo de menú que desea acceder:",
                    "Selección de Rol",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
                );
                
                if (seleccion == 0) {
                    // Administrador
                    AdministradorGeneral admin = new AdministradorGeneral(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getEmail(),
                        usuario.getPassword(),
                        usuario.getIdRol(),
                        usuario.getIdSucursal()
                    );
                    admin.MostrarMenu();
                } else if (seleccion == 1) {
                    // Encargado de Ventas
                    EncargadodeVentas vendedor = new EncargadodeVentas(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getEmail(),
                        usuario.getPassword(),
                        usuario.getIdRol(),
                        usuario.getIdSucursal()
                    );
                    vendedor.MostrarMenu();
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al redirigir según rol: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
