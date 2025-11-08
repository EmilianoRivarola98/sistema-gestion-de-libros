package gui.dialogs;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import servicios.ServicioPromocion;
import ventas.Promocion;

/**
 * Dialog para crear o editar una promoción.
 */
public class PromocionFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private boolean confirmado = false;
    private Promocion promocion;
    private ServicioPromocion servicioPromocion;

    private JTextField campoNombre;
    private JTextArea campoDescripcion;
    private JTextField campoDescuento;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public PromocionFormDialog(Promocion promocion, boolean modal, ServicioPromocion servicioPromocion) {
        super((Frame) null, "Formulario de Promoción", modal);
        this.promocion = promocion;
        this.servicioPromocion = servicioPromocion;

        inicializar();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void inicializar() {
        setSize(500, 350);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFormulario = crearPanelFormulario();
        this.add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = crearPanelBotones();
        this.add(panelBotones, BorderLayout.SOUTH);

        // Si es edición, cargar datos
        if (promocion != null) {
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
        gbc.weightx = 0.25;
        panel.add(lblNombre, gbc);

        campoNombre = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 0.75;
        panel.add(campoNombre, gbc);

        // Descripción
        JLabel lblDescripcion = new JLabel("Descripción:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.25;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(lblDescripcion, gbc);

        campoDescripcion = new JTextArea(3, 30);
        campoDescripcion.setLineWrap(true);
        campoDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(campoDescripcion);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.75;
        gbc.weighty = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollDescripcion, gbc);

        // Descuento
        JLabel lblDescuento = new JLabel("Descuento (%):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.25;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblDescuento, gbc);

        campoDescuento = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 0.75;
        panel.add(campoDescuento, gbc);

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
        if (promocion == null) return;

        campoNombre.setText(promocion.getNombre());
        campoDescripcion.setText(promocion.getDescripcion());
        campoDescuento.setText(promocion.getDescuento().toString());
    }

    private void guardar() {
        if (!validar()) {
            return;
        }

        try {
            String nombre = campoNombre.getText().trim();
            String descripcion = campoDescripcion.getText().trim();
            BigDecimal descuento = new BigDecimal(campoDescuento.getText().trim());

            boolean resultado;
            if (promocion != null) {
                // Actualizar
                resultado = servicioPromocion.actualizarPromocionGUI(promocion.getIdPromocion(), nombre, descripcion, descuento);
                if (resultado) {
                    JOptionPane.showMessageDialog(this,
                            "Promoción actualizada exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Crear
                resultado = servicioPromocion.crearPromocionGUI(nombre, descripcion, descuento);
                if (resultado) {
                    JOptionPane.showMessageDialog(this,
                            "Promoción creada exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }

            if (resultado) {
                confirmado = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar la promoción",
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
        String descripcion = campoDescripcion.getText().trim();
        String descuentoStr = campoDescuento.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre es obligatorio",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La descripción es obligatoria",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (descuentoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El descuento es obligatorio",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            BigDecimal descuento = new BigDecimal(descuentoStr);
            if (descuento.compareTo(BigDecimal.ZERO) <= 0 || descuento.compareTo(new BigDecimal(100)) > 0) {
                JOptionPane.showMessageDialog(this,
                        "El descuento debe estar entre 0 y 100",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El descuento debe ser un número válido",
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
