package gui.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import gui.panels.BasePanel;
import gui.panels.GestionUsuariosPanel;
import usuarios.Usuario;
import usuarios.Rol;
import servicios.ServicioUsuario;

/**
 * Ventana principal de la aplicación.
 * Gestiona la navegación entre los diferentes módulos.
 */
public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int ANCHO = 1000;
    private static final int ALTO = 700;

    private JPanel contentPanel;
    private JMenuBar menuBar;
    private JPanel panelPrincipal;
    private Usuario usuarioActual;
    private BasePanel panelActual;
    private Rol rolUsuario;
    private ServicioUsuario servicioUsuario;

    public MainFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        this.servicioUsuario = new ServicioUsuario();
        this.rolUsuario = servicioUsuario.obtenerRolPorId(usuario.getIdRol());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(ANCHO, ALTO);
        setLocationRelativeTo(null);
        setTitle("Sistema de Gestión de Libros - " + usuario.getNombre());
        setResizable(true);

        inicializar();
        setVisible(true);
    }

    private void inicializar() {
        // Panel principal con CardLayout para cambiar entre vistas
        panelPrincipal = new JPanel(new BorderLayout());
        this.contentPanel = new JPanel(new BorderLayout());

        // Panel de contenido (se reemplazará según la opción seleccionada)
        contentPanel.setBackground(Color.WHITE);
        panelPrincipal.add(contentPanel, BorderLayout.CENTER);

        // Crear menú
        crearMenuBar();

        this.setJMenuBar(menuBar);
        this.add(panelPrincipal);
    }

    private void crearMenuBar() {
        menuBar = new JMenuBar();

        // Menú Gestión
        JMenu menuGestion = new JMenu("Gestión");
        menuGestion.setMnemonic('G');

        // Submenús según el rol del usuario
        if (rolUsuario != null && rolUsuario.getNombre().equalsIgnoreCase("Administrador General")) {
            agregarMenuItemConAccion(menuGestion, "Usuarios", 'U', e -> mostrarPanelUsuarios());
            agregarMenuItemConAccion(menuGestion, "Autores", 'A', e -> mostrarPanelAutores());
            agregarMenuItemConAccion(menuGestion, "Géneros", 'G', e -> mostrarPanelGeneros());
            agregarMenuItemConAccion(menuGestion, "Libros", 'L', e -> mostrarPanelLibros());
            agregarMenuItemConAccion(menuGestion, "Stock", 'S', e -> mostrarPanelStock());
            menuGestion.addSeparator();
            agregarMenuItemConAccion(menuGestion, "Ventas", 'V', e -> mostrarPanelVentas());
        } else if (rolUsuario != null && rolUsuario.getNombre().equalsIgnoreCase("Encargado de Ventas")) {
            agregarMenuItemConAccion(menuGestion, "Libros", 'L', e -> mostrarPanelLibros());
            agregarMenuItemConAccion(menuGestion, "Stock", 'S', e -> mostrarPanelStock());
            agregarMenuItemConAccion(menuGestion, "Ventas", 'V', e -> mostrarPanelVentas());
        }

        menuBar.add(menuGestion);

        // Menú Usuario
        JMenu menuUsuario = new JMenu("Usuario");
        menuUsuario.setMnemonic('U');
        agregarMenuItemConAccion(menuUsuario, "Mi Perfil", 'P', e -> mostrarPerfil());
        menuUsuario.addSeparator();
        agregarMenuItemConAccion(menuUsuario, "Cerrar Sesión", 'C', e -> cerrarSesion());

        menuBar.add(menuUsuario);

        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        menuAyuda.setMnemonic('A');
        agregarMenuItemConAccion(menuAyuda, "Acerca de...", 'A', e -> mostrarAcercaDe());

        menuBar.add(menuAyuda);
    }

    private void agregarMenuItemConAccion(JMenu menu, String texto, char mnemonic, ActionListener accion) {
        JMenuItem item = new JMenuItem(texto);
        item.setMnemonic(mnemonic);
        item.addActionListener(accion);
        menu.add(item);
    }

    /**
     * Muestra un panel en el área de contenido
     */
    public void mostrarPanel(BasePanel panel) {
        if (panelActual != null) {
            panelActual.limpiar();
        }
        contentPanel.removeAll();
        panel.inicializar();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
        this.panelActual = panel;
    }

    // Métodos para mostrar cada panel
    private void mostrarPanelUsuarios() {
        GestionUsuariosPanel panel = new GestionUsuariosPanel();
        mostrarPanel(panel);
    }

    private void mostrarPanelAutores() {
        JOptionPane.showMessageDialog(this, "Panel de Autores - Próximamente", "En Desarrollo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarPanelGeneros() {
        JOptionPane.showMessageDialog(this, "Panel de Géneros - Próximamente", "En Desarrollo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarPanelLibros() {
        JOptionPane.showMessageDialog(this, "Panel de Libros - Próximamente", "En Desarrollo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarPanelStock() {
        JOptionPane.showMessageDialog(this, "Panel de Stock - Próximamente", "En Desarrollo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarPanelVentas() {
        JOptionPane.showMessageDialog(this, "Panel de Ventas - Próximamente", "En Desarrollo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarPerfil() {
        JOptionPane.showMessageDialog(this, "Perfil del usuario: " + usuarioActual.getNombre(), "Mi Perfil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            if (panelActual != null) {
                panelActual.limpiar();
            }
            this.dispose();
            // Volvemos a mostrar la ventana de login
            new LoginFrame();
        }
    }

    private void mostrarAcercaDe() {
        JOptionPane.showMessageDialog(this,
                "Sistema de Gestión de Libros v2.0\n\n" +
                        "© 2025 Universidad Davinci\n\n" +
                        "Desarrollado por: Estudiantes de Programación Avanzada",
                "Acerca de",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}
