package gui.frames;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import servicios.ServicioUsuario;
import servicios.ServicioSucursal;
import usuarios.Rol;
import ventas.Sucursal;

/**
 * Ventana de registro de nuevos usuarios.
 */
public class RegistroFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int ANCHO = 450;
    private static final int ALTO = 350;

    private JTextField campoNombre;
    private JTextField campoEmail;
    private JPasswordField campoPassword;
    private JPasswordField campoConfirmar;
    private JComboBox<String> comboSucursal;
    private JButton btnRegistrar;
    private JButton btnVolver;
    private ServicioUsuario servicioUsuario;
    private ServicioSucursal servicioSucursal;
    private List<Sucursal> sucursales;

    public RegistroFrame() {
        this.servicioUsuario = new ServicioUsuario();
        this.servicioSucursal = new ServicioSucursal();
        this.sucursales = servicioSucursal.obtenerTodasLasSucursales();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(ANCHO, ALTO);
        setLocationRelativeTo(null);
        setTitle("Sistema de Gestión de Libros - Registro");
        setResizable(false);

        inicializar();
        setVisible(true);
    }

    private void inicializar() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Encabezado
        JLabel lblTitulo = new JLabel("Registrarse");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel central con formulario
        JPanel panelFormulario = crearFormulario();
        JScrollPane scrollPane = new JScrollPane(panelFormulario);
        scrollPane.setBorder(null);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        this.add(panelPrincipal);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 15));
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
        campoConfirmar = new JPasswordField();
        campoConfirmar.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblConfirmar);
        panel.add(campoConfirmar);

        // Sucursal
        JLabel lblSucursal = new JLabel("Sucursal:");
        lblSucursal.setFont(new Font("Arial", Font.PLAIN, 12));
        comboSucursal = new JComboBox<>();
        
        if (!sucursales.isEmpty()) {
            for (Sucursal sucursal : sucursales) {
                comboSucursal.addItem(sucursal.getNombre());
            }
        } else {
            comboSucursal.addItem("No hay sucursales disponibles");
            comboSucursal.setEnabled(false);
        }
        
        comboSucursal.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblSucursal);
        panel.add(comboSucursal);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBackground(new Color(240, 240, 240));

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRegistrar.setPreferredSize(new Dimension(120, 35));
        btnRegistrar.addActionListener(this::registrar);

        btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVolver.setPreferredSize(new Dimension(120, 35));
        btnVolver.addActionListener(e -> volverAlLogin());

        panel.add(btnRegistrar);
        panel.add(btnVolver);

        return panel;
    }

    private void registrar(ActionEvent e) {
        String nombre = campoNombre.getText().trim();
        String email = campoEmail.getText().trim();
        String password = new String(campoPassword.getPassword());
        String confirmar = new String(campoConfirmar.getPassword());

        // Validaciones
        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor complete todos los campos",
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
            campoConfirmar.setText("");
            return;
        }

        if (sucursales.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay sucursales disponibles",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Sucursal sucursalSeleccionada = sucursales.get(comboSucursal.getSelectedIndex());
            Rol rolEncargado = servicioUsuario.obtenerRolPorNombre("Encargado de Ventas");

            if (rolEncargado == null) {
                JOptionPane.showMessageDialog(this,
                        "Error: No se encontró el rol 'Encargado de Ventas'",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean registrado = servicioUsuario.crearUsuario(nombre, email, password, rolEncargado.getId(), sucursalSeleccionada.getId());

            if (registrado) {
                JOptionPane.showMessageDialog(this,
                        "Registro exitoso. Por favor inicie sesión.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new LoginFrame();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo completar el registro. Intente nuevamente.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAlLogin() {
        this.dispose();
        new LoginFrame();
    }
}
