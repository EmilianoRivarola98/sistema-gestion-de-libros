package gui.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import servicios.ServicioUsuario;
import usuarios.Usuario;

/**
 * Ventana de inicio de sesión de la aplicación.
 */
public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int ANCHO = 380;
    private static final int ALTO = 420;

    private JTextField campoEmail;
    private JPasswordField campoPassword;
    private JButton btnIngresar;
    private JButton btnRegistro;
    private ServicioUsuario servicioUsuario;

    public LoginFrame() {
        this.servicioUsuario = new ServicioUsuario();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(ANCHO, ALTO);
        setLocationRelativeTo(null);
        setTitle("Sistema de Gestión de Libros - Login");
        setResizable(false);

        inicializar();
        setVisible(true);
    }

    private void inicializar() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        JPanel panelSuperior = crearPanelSuperior();
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelFormulario = crearFormulario();
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = crearPanelBotones();
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        this.add(panelPrincipal);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(240, 240, 240));

        JLabel lblImagen = cargarImagen();
        if (lblImagen != null) {
            panel.add(lblImagen, BorderLayout.CENTER);
        }

        // Título
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitulo, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel cargarImagen() {
        try {
            String[] posiblesRutas = {
                "assets/img/logo.png",
                "assets/img/logo.jpg",
                "assets/img/logo.jpeg"
            };

            for (String ruta : posiblesRutas) {
                File archivo = new File(ruta);
                if (archivo.exists()) {
                    ImageIcon icono = new ImageIcon(ruta);
                    Image img = icono.getImage();
                    Image imgEscalada = img.getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING);
                    JLabel lblImagen = new JLabel(new ImageIcon(imgEscalada));
                    lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
                    return lblImagen;
                }
            }
        } catch (Exception ex) {
            System.err.println("Error al cargar imagen: " + ex.getMessage());
        }

        // Si no hay imagen, no mostrar nada
        return null;
    }

    private JPanel crearFormulario() {
        JPanel panelContenedor = new JPanel(new GridBagLayout());
        panelContenedor.setBackground(new Color(240, 240, 240));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panelContenedor.add(lblEmail, gbc);

        campoEmail = new JTextField();
        campoEmail.setPreferredSize(new Dimension(300, 34));
        campoEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoEmail.setBackground(Color.WHITE);
        campoEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        panelContenedor.add(campoEmail, gbc);

        // Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelContenedor.add(lblPassword, gbc);

        campoPassword = new JPasswordField();
        campoPassword.setPreferredSize(new Dimension(300, 34));
        campoPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoPassword.setBackground(Color.WHITE);
        campoPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        panelContenedor.add(campoPassword, gbc);

        // Enter para login
        campoPassword.addActionListener(this::ingresar);

        return panelContenedor;
    }


    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(240, 240, 240));

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnIngresar.setPreferredSize(new Dimension(120, 35));
        btnIngresar.addActionListener(this::ingresar);

        btnRegistro = new JButton("Registrarse");
        btnRegistro.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRegistro.setPreferredSize(new Dimension(120, 35));
        btnRegistro.addActionListener(e -> abrirRegistro());

        panel.add(btnIngresar);
        panel.add(btnRegistro);

        return panel;
    }

    private void ingresar(ActionEvent e) {
        String email = campoEmail.getText().trim();
        String password = new String(campoPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor complete todos los campos",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = servicioUsuario.login(email, password);
            if (usuario != null) {
                JOptionPane.showMessageDialog(this,
                        "Bienvenido " + usuario.getNombre(),
                        "Login Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new MainFrame(usuario);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Email o contraseña incorrectos",
                        "Error de Autenticación",
                        JOptionPane.ERROR_MESSAGE);
                campoPassword.setText("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al autenticar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirRegistro() {
        this.dispose();
        new RegistroFrame();
    }
}