package gui.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import servicios.ServicioUsuario;
import usuarios.Usuario;

/**
 * Ventana de inicio de sesión de la aplicación.
 */
public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int ANCHO = 400;
    private static final int ALTO = 300;

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
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Encabezado
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel central con formulario
        JPanel panelFormulario = crearFormulario();
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        this.add(panelPrincipal);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 15));
        panel.setBackground(new Color(240, 240, 240));

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

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
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
