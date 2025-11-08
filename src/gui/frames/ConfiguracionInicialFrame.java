package gui.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import servicios.ServicioSucursal;
import servicios.ServicioUsuario;
import usuarios.Rol;
import ventas.Sucursal;

/**
 * Ventana para la configuración inicial del sistema.
 * Se muestra solo en el primer inicio.
 */
public class ConfiguracionInicialFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int ANCHO = 600;
    private static final int ALTO = 500;

    private JTextField campoNombreSucursal;
    private JTextField campoDireccionSucursal;
    private JTextField campoNombreAdmin;
    private JTextField campoEmailAdmin;
    private JPasswordField campoPasswordAdmin;
    private JButton btnContinuar;
    private JButton btnCancelar;
    private ServicioSucursal servicioSucursal;
    private ServicioUsuario servicioUsuario;
    private JPanel panelPrincipal;
    private int paso = 1;

    public ConfiguracionInicialFrame() {
        this.servicioSucursal = new ServicioSucursal();
        this.servicioUsuario = new ServicioUsuario();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(ANCHO, ALTO);
        setLocationRelativeTo(null);
        setTitle("Configuración Inicial del Sistema");
        setResizable(false);

        inicializar();
        setVisible(true);
    }

    private void inicializar() {
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Encabezado
        JLabel lblTitulo = new JLabel("Configuración Inicial del Sistema");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel central (se actualizará según el paso) - con nombre para poder actualizar
        JPanel panelContenido = crearPaso1();
        panelContenido.setName("panelContenido");
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        this.add(panelPrincipal);
    }

    private JPanel crearPaso1() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 240, 240));

        // Título del paso
        JLabel lblPaso = new JLabel("Paso 1 de 2: Crear Sucursal Principal");
        lblPaso.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblPaso, BorderLayout.NORTH);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 10, 15));
        panelFormulario.setBackground(new Color(240, 240, 240));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblNombre = new JLabel("Nombre de la Sucursal:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        campoNombreSucursal = new JTextField();
        campoNombreSucursal.setFont(new Font("Arial", Font.PLAIN, 12));
        panelFormulario.add(lblNombre);
        panelFormulario.add(campoNombreSucursal);

        JLabel lblDireccion = new JLabel("Dirección de la Sucursal:");
        lblDireccion.setFont(new Font("Arial", Font.PLAIN, 12));
        campoDireccionSucursal = new JTextField();
        campoDireccionSucursal.setFont(new Font("Arial", Font.PLAIN, 12));
        panelFormulario.add(lblDireccion);
        panelFormulario.add(campoDireccionSucursal);

        panel.add(panelFormulario, BorderLayout.CENTER);

        JLabel lblInfo = new JLabel("<html>Ingrese los datos de la sucursal principal de su empresa.</html>");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInfo.setForeground(new Color(100, 100, 100));
        panel.add(lblInfo, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPaso2() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 240, 240));

        // Título del paso
        JLabel lblPaso = new JLabel("Paso 2 de 2: Crear Administrador General");
        lblPaso.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblPaso, BorderLayout.NORTH);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 15));
        panelFormulario.setBackground(new Color(240, 240, 240));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblNombre = new JLabel("Nombre del Administrador:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        campoNombreAdmin = new JTextField();
        campoNombreAdmin.setFont(new Font("Arial", Font.PLAIN, 12));
        panelFormulario.add(lblNombre);
        panelFormulario.add(campoNombreAdmin);

        JLabel lblEmail = new JLabel("Email del Administrador:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        campoEmailAdmin = new JTextField();
        campoEmailAdmin.setFont(new Font("Arial", Font.PLAIN, 12));
        panelFormulario.add(lblEmail);
        panelFormulario.add(campoEmailAdmin);

        JLabel lblPassword = new JLabel("Contraseña (min. 6 caracteres):");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        campoPasswordAdmin = new JPasswordField();
        campoPasswordAdmin.setFont(new Font("Arial", Font.PLAIN, 12));
        panelFormulario.add(lblPassword);
        panelFormulario.add(campoPasswordAdmin);

        panel.add(panelFormulario, BorderLayout.CENTER);

        JLabel lblInfo = new JLabel("<html>Ingrese los datos de la cuenta del administrador general del sistema.</html>");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInfo.setForeground(new Color(100, 100, 100));
        panel.add(lblInfo, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBackground(new Color(240, 240, 240));

        btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnContinuar.setPreferredSize(new Dimension(120, 35));
        btnContinuar.addActionListener(this::procesarPaso);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCancelar.setPreferredSize(new Dimension(120, 35));
        btnCancelar.addActionListener(e -> cancelar());

        panel.add(btnContinuar);
        panel.add(btnCancelar);

        return panel;
    }

    private void procesarPaso(ActionEvent e) {
        if (paso == 1) {
            procesarPaso1();
        } else if (paso == 2) {
            procesarPaso2();
        }
    }

    private void procesarPaso1() {
        String nombre = campoNombreSucursal.getText().trim();
        String direccion = campoDireccionSucursal.getText().trim();

        if (nombre.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor complete todos los campos",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Sucursal sucursal = servicioSucursal.crearSucursal(nombre, direccion);
            if (sucursal == null) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo crear la sucursal",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Guardar el ID de la sucursal en una variable global o singleton
            SucursalConfiguracion.setSucursalId(sucursal.getId());

            JOptionPane.showMessageDialog(this,
                    "Sucursal creada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            // Avanzar al paso 2
            paso = 2;
            btnContinuar.setText("Finalizar");
            actualizarVista();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear sucursal: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarPaso2() {
        String nombre = campoNombreAdmin.getText().trim();
        String email = campoEmailAdmin.getText().trim();
        String password = new String(campoPasswordAdmin.getPassword());

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

        try {
            Rol rolAdmin = servicioUsuario.obtenerRolPorNombre("Administrador General");
            if (rolAdmin == null) {
                JOptionPane.showMessageDialog(this,
                        "Error: No se encontró el rol 'Administrador General'",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int sucursalId = SucursalConfiguracion.getSucursalId();
            boolean adminCreado = servicioUsuario.crearUsuario(nombre, email, password, rolAdmin.getId(), sucursalId);

            if (adminCreado) {
                JOptionPane.showMessageDialog(this,
                        "¡Configuración completada exitosamente!\nEl sistema está listo para usarse.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new LoginFrame();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo crear la cuenta de administrador",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear administrador: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarVista() {
        // Remover el panel anterior del BorderLayout CENTER
        Component componente = panelPrincipal.getComponent(1); // Índice 1 es CENTER después de NORTH
        if (componente != null) {
            panelPrincipal.remove(1);
        }
        
        // Agregar el nuevo panel
        JPanel panelContenido = crearPaso2();
        panelContenido.setName("panelContenido");
        panelPrincipal.add(panelContenido, BorderLayout.CENTER, 1);
        
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    private void cancelar() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea cancelar la configuración inicial?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "La configuración inicial no se completó. El programa se cerrará.",
                    "Configuración Cancelada",
                    JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Clase auxiliar para almacenar temporalmente el ID de la sucursal
     */
    private static class SucursalConfiguracion {
        private static int sucursalId;

        public static void setSucursalId(int id) {
            sucursalId = id;
        }

        public static int getSucursalId() {
            return sucursalId;
        }
    }
}
