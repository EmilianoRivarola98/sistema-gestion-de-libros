package gui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import servicios.ServicioUsuario;
import servicios.ServicioSucursal;
import usuarios.Usuario;
import usuarios.Rol;
import ventas.Sucursal;
import gui.dialogs.UsuarioFormDialog;

/**
 * Panel para la gestión de usuarios (CRUD).
 */
public class GestionUsuariosPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    private ServicioUsuario servicioUsuario;
    private ServicioSucursal servicioSucursal;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JButton btnCrear;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JButton btnBuscar;
    private JTextField campoBusqueda;

    public GestionUsuariosPanel() {
        super(new BorderLayout(10, 10));
        this.servicioUsuario = new ServicioUsuario();
        this.servicioSucursal = new ServicioSucursal();
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);
    }

    @Override
    public void inicializar() {
        // Panel superior con búsqueda y botones
        JPanel panelSuperior = crearPanelSuperior();
        this.add(panelSuperior, BorderLayout.NORTH);

        // Panel central con tabla
        JPanel panelCentral = crearPanelCentral();
        this.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con botones de acción
        JPanel panelInferior = crearPanelInferior();
        this.add(panelInferior, BorderLayout.SOUTH);

        // Cargar datos
        refrescar();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Gestión de Usuarios");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitulo, BorderLayout.WEST);

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelBusqueda.setBackground(Color.WHITE);

        JLabel lblBuscar = new JLabel("Buscar:");
        campoBusqueda = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(this::buscar);

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(campoBusqueda);
        panelBusqueda.add(btnBuscar);

        panel.add(panelBusqueda, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Crear modelo de tabla
        String[] columnas = {"ID", "Nombre", "Email", "Rol", "Sucursal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.getTableHeader().setReorderingAllowed(false);

        // Scroll para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnCrear = new JButton("Crear Usuario");
        btnCrear.setPreferredSize(new Dimension(140, 35));
        btnCrear.addActionListener(e -> crearUsuario());

        btnEditar = new JButton("Editar Usuario");
        btnEditar.setPreferredSize(new Dimension(140, 35));
        btnEditar.addActionListener(e -> editarUsuario());

        btnEliminar = new JButton("Eliminar Usuario");
        btnEliminar.setPreferredSize(new Dimension(140, 35));
        btnEliminar.addActionListener(e -> eliminarUsuario());

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setPreferredSize(new Dimension(140, 35));
        btnRefrescar.addActionListener(e -> refrescar());

        panel.add(btnCrear);
        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnRefrescar);

        return panel;
    }

    @Override
    public void refrescar() {
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        try {
            modeloTabla.setRowCount(0);
            List<Usuario> usuarios = servicioUsuario.obtenerTodosLosUsuarios();

            for (Usuario usuario : usuarios) {
                Rol rol = servicioUsuario.obtenerRolPorId(usuario.getIdRol());
                Sucursal sucursal = null;
                if (usuario.getIdSucursal() != null) {
                    sucursal = servicioSucursal.obtenerPorId(usuario.getIdSucursal());
                }

                String nombreRol = (rol != null) ? rol.getNombre() : "N/A";
                String nombreSucursal = (sucursal != null) ? sucursal.getNombre() : "N/A";

                modeloTabla.addRow(new Object[]{
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getEmail(),
                        nombreRol,
                        nombreSucursal
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar usuarios: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearUsuario() {
        UsuarioFormDialog dialog = new UsuarioFormDialog(null, true);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            refrescar();
        }
    }

    private void editarUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un usuario para editar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int usuarioId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Usuario usuario = servicioUsuario.obtenerUsuario(usuarioId);

            if (usuario != null) {
                UsuarioFormDialog dialog = new UsuarioFormDialog(usuario, true);
                dialog.setVisible(true);

                if (dialog.isConfirmado()) {
                    refrescar();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo cargar el usuario",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al editar usuario: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un usuario para eliminar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int usuarioId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreUsuario = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar al usuario '" + nombreUsuario + "'?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = servicioUsuario.eliminarUsuario(usuarioId);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this,
                            "Usuario eliminado exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    refrescar();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo eliminar el usuario",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar usuario: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscar(ActionEvent e) {
        String busqueda = campoBusqueda.getText().trim();

        if (busqueda.isEmpty()) {
            refrescar();
            return;
        }

        try {
            modeloTabla.setRowCount(0);
            List<Usuario> usuarios = servicioUsuario.obtenerTodosLosUsuarios();

            for (Usuario usuario : usuarios) {
                if (usuario.getNombre().toLowerCase().contains(busqueda.toLowerCase()) ||
                    usuario.getEmail().toLowerCase().contains(busqueda.toLowerCase())) {
                    
                    Rol rol = servicioUsuario.obtenerRolPorId(usuario.getIdRol());
                    Sucursal sucursal = null;
                    if (usuario.getIdSucursal() != null) {
                        sucursal = servicioSucursal.obtenerPorId(usuario.getIdSucursal());
                    }

                    String nombreRol = (rol != null) ? rol.getNombre() : "N/A";
                    String nombreSucursal = (sucursal != null) ? sucursal.getNombre() : "N/A";

                    modeloTabla.addRow(new Object[]{
                            usuario.getId(),
                            usuario.getNombre(),
                            usuario.getEmail(),
                            nombreRol,
                            nombreSucursal
                    });
                }
            }

            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron usuarios con '" + busqueda + "'",
                        "Búsqueda",
                        JOptionPane.INFORMATION_MESSAGE);
                refrescar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            refrescar();
        }
    }
}
