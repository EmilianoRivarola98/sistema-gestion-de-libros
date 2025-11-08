package gui.dialogs;

import javax.swing.*;
import java.awt.*;
import servicios.ServicioGenero;
import ventas.Genero;

/**
 * Diálogo para crear y editar géneros.
 * Se abre como un diálogo modal desde GestionGenerosPanel.
 */
public class GeneroFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField campoNombre;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private boolean confirmado = false;
    private Genero generoEdicion = null;
    private ServicioGenero servicioGenero;

    public GeneroFormDialog(Genero genero, boolean modal) {
        super((Frame) null, "Formulario de Género", modal);
        this.generoEdicion = genero;
        this.servicioGenero = new ServicioGenero();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(400, 200);
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
        JLabel lblTitulo = new JLabel(generoEdicion == null ? "Crear Nuevo Género" : "Editar Género");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Formulario
        JPanel panelFormulario = crearFormulario();
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = crearPanelBotones();
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        this.add(panelPrincipal);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 15));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Nombre
        JLabel lblNombre = new JLabel("Nombre del Género:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        campoNombre = new JTextField();
        campoNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(lblNombre);
        panel.add(campoNombre);

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
        if (generoEdicion != null) {
            campoNombre.setText(generoEdicion.getNombre());
        }
    }

    private void guardar() {
        String nombre = campoNombre.getText().trim();

        // Validaciones
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre del género es requerido",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean operacionExitosa;

            if (generoEdicion == null) {
                // Crear nuevo género
                operacionExitosa = servicioGenero.crearGenero(nombre);
            } else {
                // Actualizar género existente
                operacionExitosa = servicioGenero.actualizarGenero(generoEdicion.getIdGenero(), nombre);
            }

            if (operacionExitosa) {
                confirmado = true;
                JOptionPane.showMessageDialog(this,
                        generoEdicion == null ? "Género creado exitosamente" : "Género actualizado exitosamente",
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
