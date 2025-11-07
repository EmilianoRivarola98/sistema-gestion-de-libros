package gui.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import servicios.ServicioUsuario;
import servicios.ServicioSucursal;
import usuarios.Usuario;
import usuarios.Rol;
import ventas.Sucursal;

/**
 * Diálogo para crear y editar usuarios.
 * Se abre como un diálogo modal desde GestionUsuariosPanel.
 */
public class UsuarioFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField campoNombre;
    private JTextField campoEmail;
    private JPasswordField campoPassword;
    private JPasswordField campoConfirmarPassword;
    private JComboBox<String> comboRol;
    private JComboBox<String> comboSucursal;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private boolean confirmado = false;
    private Usuario usuarioEdicion = null;
    private ServicioUsuario servicioUsuario;
    private ServicioSucursal servicioSucursal;
    private List<Rol> rolesDisponibles;
    private List<Sucursal> sucursalesDisponibles;

    public UsuarioFormDialog(Usuario usuario, boolean modal) {
        super((Frame) null, "Formulario de Usuario", modal);
        this.usuarioEdicion = usuario;
        this.servicioUsuario = new ServicioUsuario();
        this.servicioSucursal = new ServicioSucursal();
        this.rolesDisponibles = servicioUsuario.obtenerRoles();
        this.sucursalesDisponibles = servicioSucursal.obtenerTodasLasSucursales();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializar();
        cargarDatos();
    }

    private void inicializar() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Título
        JLabel lblTitulo = new JLabel(usuarioEdicion == null ? "Crear Nuevo Usuario" : "Editar Usuario");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Formulario
        JPanel panelFormulario = crearFormulario();
        JScrollPane scrollPane = new JScrollPane(panelFormulario);
        scrollPane.setBorder(null);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = crearPanelBotones();
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        this.add(panelPrincipal);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 15));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        campoNombre = new JTextField();
        campoNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblNombre);
        panel.add(campoNombre);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        campoEmail = new JTextField();
        campoEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblEmail);
        panel.add(campoEmail);

        // Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        campoPassword = new JPasswordField();
        campoPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblPassword);
        panel.add(campoPassword);

        // Confirmar Contraseña
        JLabel lblConfirmar = new JLabel("Confirmar Contraseña:");
        lblConfirmar.setFont(new Font("Arial", Font.PLAIN, 12));
        campoConfirmarPassword = new JPasswordField();
        campoConfirmarPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblConfirmar);
        panel.add(campoConfirmarPassword);

        // Rol
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("Arial", Font.PLAIN, 12));
        comboRol = new JComboBox<>();
        comboRol.setFont(new Font("Arial", Font.PLAIN, 12));
        for (Rol rol : rolesDisponibles) {
            comboRol.addItem(rol.getNombre());
        }
        panel.add(lblRol);
        panel.add(comboRol);

        // Sucursal
        JLabel lblSucursal = new JLabel("Sucursal:");
        lblSucursal.setFont(new Font("Arial", Font.PLAIN, 12));
        comboSucursal = new JComboBox<>();
        comboSucursal.setFont(new Font("Arial", Font.PLAIN, 12));
        for (Sucursal sucursal : sucursalesDisponibles) {
            comboSucursal.addItem(sucursal.getNombre());
        }
        panel.add(lblSucursal);
        panel.add(comboSucursal);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(100, 35));
        btnGuardar.addActionListener(e -> guardar());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.addActionListener(e -> cancelar());

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        return panel;
    }

    private void cargarDatos() {
        if (usuarioEdicion != null) {
            campoNombre.setText(usuarioEdicion.getNombre());
            campoEmail.setText(usuarioEdicion.getEmail());

            // Seleccionar rol
            Rol rolUsuario = servicioUsuario.obtenerRolPorId(usuarioEdicion.getIdRol());
            if (rolUsuario != null) {
                comboRol.setSelectedItem(rolUsuario.getNombre());
            }

            // Seleccionar sucursal
            if (usuarioEdicion.getIdSucursal() != null) {
                Sucursal sucursal = servicioSucursal.obtenerPorId(usuarioEdicion.getIdSucursal());
                if (sucursal != null) {
                    comboSucursal.setSelectedItem(sucursal.getNombre());
                }
            }

            // Desactivar campos de contraseña para edición
            campoPassword.setEnabled(false);
            campoConfirmarPassword.setEnabled(false);
        }
    }

    private void guardar() {
        String nombre = campoNombre.getText().trim();
        String email = campoEmail.getText().trim();
        String password = new String(campoPassword.getPassword());
        String confirmar = new String(campoConfirmarPassword.getPassword());

        // Validaciones
        if (nombre.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre y email son requeridos",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!Usuario.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                    "El email no es válido",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Si es creación (no edición), validar contraseña
        if (usuarioEdicion == null) {
            if (password.isEmpty() || confirmar.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "La contraseña es requerida",
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this,
                        "La contraseña debe tener al menos 6 caracteres",
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!password.equals(confirmar)) {
                JOptionPane.showMessageDialog(this,
                        "Las contraseñas no coinciden",
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
                campoPassword.setText("");
                campoConfirmarPassword.setText("");
                return;
            }
        }

        try {
            // Obtener rol y sucursal seleccionados
            String nombreRolSeleccionado = (String) comboRol.getSelectedItem();
            Rol rolSeleccionado = rolesDisponibles.stream()
                    .filter(r -> r.getNombre().equals(nombreRolSeleccionado))
                    .findFirst()
                    .orElse(null);

            String nombreSucursalSeleccionada = (String) comboSucursal.getSelectedItem();
            Sucursal sucursalSeleccionada = sucursalesDisponibles.stream()
                    .filter(s -> s.getNombre().equals(nombreSucursalSeleccionada))
                    .findFirst()
                    .orElse(null);

            if (rolSeleccionado == null || sucursalSeleccionada == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un rol y una sucursal",
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean operacionExitosa;

            if (usuarioEdicion == null) {
                // Crear nuevo usuario
                operacionExitosa = servicioUsuario.crearUsuario(
                        nombre,
                        email,
                        password,
                        rolSeleccionado.getId(),
                        sucursalSeleccionada.getId()
                );
            } else {
                // Actualizar usuario existente
                operacionExitosa = servicioUsuario.actualizarUsuario(
                        usuarioEdicion.getId(),
                        nombre,
                        email,
                        password.isEmpty() ? null : password,
                        rolSeleccionado.getId(),
                        sucursalSeleccionada.getId()
                );
            }

            if (operacionExitosa) {
                confirmado = true;
                JOptionPane.showMessageDialog(this,
                        usuarioEdicion == null ? "Usuario creado exitosamente" : "Usuario actualizado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo completar la operación",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {
        confirmado = false;
        this.dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}
