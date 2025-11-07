package gui.dialogs;

import javax.swing.*;
import java.awt.*;
import servicios.ServicioAutor;
import ventas.Autor;

/**
 * Diálogo para crear y editar autores.
 * Se abre como un diálogo modal desde GestionAutoresPanel.
 */
public class AutorFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField campoNombre;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private boolean confirmado = false;
    private Autor autorEdicion = null;
    private ServicioAutor servicioAutor;

    public AutorFormDialog(Autor autor, boolean modal) {
        super((Frame) null, "Formulario de Autor", modal);
        this.autorEdicion = autor;
        this.servicioAutor = new ServicioAutor();

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
        JLabel lblTitulo = new JLabel(autorEdicion == null ? "Crear Nuevo Autor" : "Editar Autor");
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
        JLabel lblNombre = new JLabel("Nombre del Autor:");
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
        if (autorEdicion != null) {
            campoNombre.setText(autorEdicion.getNombre());
        }
    }

    private void guardar() {
        String nombre = campoNombre.getText().trim();

        // Validaciones
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre del autor es requerido",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean operacionExitosa;

            if (autorEdicion == null) {
                // Crear nuevo autor
                operacionExitosa = servicioAutor.crearAutor(nombre);
            } else {
                // Actualizar autor existente
                operacionExitosa = servicioAutor.actualizarAutor(autorEdicion.getIdAutor(), nombre);
            }

            if (operacionExitosa) {
                confirmado = true;
                JOptionPane.showMessageDialog(this,
                        autorEdicion == null ? "Autor creado exitosamente" : "Autor actualizado exitosamente",
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
