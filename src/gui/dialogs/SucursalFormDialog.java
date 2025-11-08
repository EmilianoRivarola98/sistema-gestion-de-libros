package gui.dialogs;

import javax.swing.*;
import java.awt.*;
import servicios.ServicioSucursal;
import ventas.Sucursal;

/**
 * Dialog para crear o editar una sucursal.
 */
public class SucursalFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private boolean confirmado = false;
    private Sucursal sucursal;
    private ServicioSucursal servicioSucursal;

    private JTextField campoNombre;
    private JTextField campoDireccion;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public SucursalFormDialog(Sucursal sucursal, boolean modal, ServicioSucursal servicioSucursal) {
        super((Frame) null, "Formulario de Sucursal", modal);
        this.sucursal = sucursal;
        this.servicioSucursal = servicioSucursal;

        inicializar();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void inicializar() {
        setSize(450, 250);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFormulario = crearPanelFormulario();
        this.add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = crearPanelBotones();
        this.add(panelBotones, BorderLayout.SOUTH);

        // Si es edición, cargar datos
        if (sucursal != null) {
            cargarDatos();
        }
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panel.add(lblNombre, gbc);

        campoNombre = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(campoNombre, gbc);

        // Dirección
        JLabel lblDireccion = new JLabel("Dirección:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panel.add(lblDireccion, gbc);

        campoDireccion = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(campoDireccion, gbc);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(100, 35));
        btnGuardar.addActionListener(e -> guardar());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.addActionListener(e -> dispose());

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        return panel;
    }

    private void cargarDatos() {
        if (sucursal == null) return;

        campoNombre.setText(sucursal.getNombre());
        campoDireccion.setText(sucursal.getDireccion());
    }

    private void guardar() {
        if (!validar()) {
            return;
        }

        try {
            String nombre = campoNombre.getText().trim();
            String direccion = campoDireccion.getText().trim();

            boolean resultado;
            if (sucursal != null) {
                // Actualizar
                resultado = servicioSucursal.actualizarSucursalGUI(sucursal.getId(), nombre, direccion);
                if (resultado) {
                    JOptionPane.showMessageDialog(this,
                            "Sucursal actualizada exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Crear
                resultado = servicioSucursal.crearSucursalGUI(nombre, direccion);
                if (resultado) {
                    JOptionPane.showMessageDialog(this,
                            "Sucursal creada exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }

            if (resultado) {
                confirmado = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar la sucursal",
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

    private boolean validar() {
        String nombre = campoNombre.getText().trim();
        String direccion = campoDireccion.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre es obligatorio",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La dirección es obligatoria",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}
