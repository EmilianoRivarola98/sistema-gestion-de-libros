package gui.panels;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import servicios.ServicioLibro;
import servicios.ServicioVenta;
import servicios.ServicioUsuario;
import usuarios.Usuario;
import gui.frames.MainFrame;

/**
 * Panel de inicio/dashboard principal del sistema.
 * Muestra información general y acceso rápido a módulos.
 */
public class HomePanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    private Usuario usuarioActual;
    private ServicioLibro servicioLibro;
    private ServicioVenta servicioVenta;
    private ServicioUsuario servicioUsuario;
    private MainFrame mainFrame;

    public HomePanel(Usuario usuario) {
        this(usuario, null);
    }

    public HomePanel(Usuario usuario, MainFrame mainFrame) {
        super(new BorderLayout(15, 15));
        this.usuarioActual = usuario;
        this.mainFrame = mainFrame;
        this.servicioLibro = new ServicioLibro();
        this.servicioVenta = new ServicioVenta();
        this.servicioUsuario = new ServicioUsuario();
        
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 245, 245));
    }

    @Override
    public void inicializar() {
        // Panel superior con bienvenida
        add(crearPanelBienvenida(), BorderLayout.NORTH);

        // Panel central con información
        add(crearPanelInformacion(), BorderLayout.CENTER);

        refrescar();
    }

    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(41, 128, 185)); // Azul profesional
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setPreferredSize(new Dimension(0, 120));

        // Bienvenida
        JLabel lblBienvenida = new JLabel("¡Bienvenido, " + usuarioActual.getNombre() + "!");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
        lblBienvenida.setForeground(Color.WHITE);

        // Información del usuario
        JLabel lblInfo = new JLabel("Sistema de Gestión de Libros • " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()));
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(200, 200, 200));

        // Panel izquierdo con bienvenida
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBackground(new Color(41, 128, 185));
        panelIzquierdo.add(lblBienvenida, BorderLayout.NORTH);
        panelIzquierdo.add(lblInfo, BorderLayout.SOUTH);

        // Botón Cerrar Sesión
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCerrarSesion.setBackground(new Color(231, 76, 60));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panel.add(panelIzquierdo, BorderLayout.CENTER);
        panel.add(btnCerrarSesion, BorderLayout.EAST);

        return panel;
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Desea cerrar sesión?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            limpiar();
            if (mainFrame != null) {
                mainFrame.dispose();
            }
            // Volvemos a mostrar la ventana de login
            new gui.frames.LoginFrame();
        }
    }

    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        try {
            int totalLibros = servicioLibro.obtenerTodos().size();
            int totalVentas = servicioVenta.obtenerTodasLasVentas().size();
            int totalUsuarios = servicioUsuario.obtenerTodosLosUsuarios().size();

            panel.add(crearTarjetaEstadistica("Total de Libros", String.valueOf(totalLibros), new Color(46, 204, 113), "books.svg"));
            panel.add(crearTarjetaEstadistica("Total de Ventas", String.valueOf(totalVentas), new Color(52, 152, 219), "sales.svg"));
            panel.add(crearTarjetaEstadistica("Total de Usuarios", String.valueOf(totalUsuarios), new Color(155, 89, 182), "users.svg"));
            panel.add(crearTarjetaInformacion());

        } catch (Exception ex) {
            JLabel lblError = new JLabel("Error al cargar información");
            lblError.setForeground(Color.RED);
            panel.add(lblError);
        }

        return panel;
    }

    private JPanel crearTarjetaEstadistica(String titulo, String valor, Color color, String iconoSvg) {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 10));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createRaisedBevelBorder());
        tarjeta.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior con icono y título
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 0));
        panelSuperior.setBackground(Color.WHITE);

        JLabel lblIcono = crearIconoLabel(iconoSvg, 32);
        panelSuperior.add(lblIcono, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitulo.setForeground(color);
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 36));
        lblValor.setForeground(color);
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);

        tarjeta.add(panelSuperior, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);

        return tarjeta;
    }

    private JLabel crearIconoLabel(String nombreSvg, int tamaño) {
        try {
            String rutaIcono = "assets/icons/" + nombreSvg;
            File archivo = new File(rutaIcono);
            if (archivo.exists()) {
                ImageIcon icono = new ImageIcon(rutaIcono);
                Image img = icono.getImage().getScaledInstance(tamaño, tamaño, Image.SCALE_SMOOTH);
                return new JLabel(new ImageIcon(img));
            }
        } catch (Exception ex) {
            System.err.println("Error al cargar icono: " + nombreSvg);
        }
        // Retornar etiqueta vacía si hay error
        JLabel lblVacia = new JLabel();
        lblVacia.setPreferredSize(new Dimension(tamaño, tamaño));
        return lblVacia;
    }

    private JPanel crearTarjetaInformacion() {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createRaisedBevelBorder());
        tarjeta.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelTitulo = new JPanel(new BorderLayout(10, 0));
        panelTitulo.setBackground(Color.WHITE);

        JLabel lblIcono = crearIconoLabel("info.svg", 24);
        panelTitulo.add(lblIcono, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("Información del Sistema");
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitulo.setForeground(new Color(149, 165, 166));
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);

        JTextArea areaInfo = new JTextArea();
        areaInfo.setEditable(false);
        areaInfo.setLineWrap(true);
        areaInfo.setWrapStyleWord(true);
        areaInfo.setFont(new Font("Arial", Font.PLAIN, 11));
        areaInfo.setText(
            "Sistema de Gestión de Libros v2.0\n\n" +
            "• Módulos disponibles: Usuarios, Autores,\n" +
            "  Géneros, Libros, Stock, Sucursales,\n" +
            "  Ventas, Promociones y Reportes."
        );
        areaInfo.setBackground(new Color(236, 240, 241));

        tarjeta.add(panelTitulo, BorderLayout.NORTH);
        tarjeta.add(areaInfo, BorderLayout.CENTER);

        return tarjeta;
    }

    @Override
    public void refrescar() {
        // Actualizar datos si es necesario
    }

    @Override
    public void limpiar() {
        // Limpiar recursos si es necesario
    }
}
