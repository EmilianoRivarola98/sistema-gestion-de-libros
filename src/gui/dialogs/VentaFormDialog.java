package gui.dialogs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import servicios.ServicioVenta;
import servicios.ServicioLibro;
import servicios.ServicioSucursal;
import ventas.Venta;
import ventas.ItemVenta;
import ventas.Libro;
import ventas.FormaPago;
import ventas.Sucursal;
import usuarios.Usuario;

/**
 * Dialog para crear una nueva venta.
 */
public class VentaFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private boolean confirmado = false;
    private Usuario usuarioActual;
    private ServicioVenta servicioVenta;
    private ServicioLibro servicioLibro;
    private ServicioSucursal servicioSucursal;
    private List<ItemVenta> items;

    private JComboBox<Sucursal> comboSucursales;
    private JComboBox<Libro> comboLibros;
    private JSpinner spinnerCantidad;
    private JTable tablaItems;
    private DefaultTableModel modeloTabla;
    private JLabel lblSubtotal;
    private JLabel lblDescuento;
    private JLabel lblTotal;
    private JComboBox<FormaPago> comboFormaPago;
    private JButton btnAgregarItem;
    private JButton btnEliminarItem;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public VentaFormDialog(Usuario usuario, ServicioVenta servicioVenta, ServicioLibro servicioLibro, 
                          ServicioSucursal servicioSucursal) {
        super((Frame) null, "Nueva Venta", true);
        this.usuarioActual = usuario;
        this.servicioVenta = servicioVenta;
        this.servicioLibro = servicioLibro;
        this.servicioSucursal = servicioSucursal;
        this.items = new ArrayList<>();

        inicializar();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void inicializar() {
        setSize(900, 650);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de selección de sucursal
        JPanel panelSucursal = crearPanelSucursal();
        this.add(panelSucursal, BorderLayout.NORTH);

        // Panel de agregar items
        JPanel panelItems = crearPanelItems();
        this.add(panelItems, BorderLayout.CENTER);

        // Panel de totales
        JPanel panelTotales = crearPanelTotales();
        this.add(panelTotales, BorderLayout.SOUTH);

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        this.add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearPanelSucursal() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Seleccionar Sucursal"));

        JLabel lblSucursal = new JLabel("Sucursal:");
        comboSucursales = new JComboBox<>();
        
        try {
            List<Sucursal> sucursales = servicioSucursal.obtenerTodasLasSucursales();
            for (Sucursal sucursal : sucursales) {
                comboSucursales.addItem(sucursal);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar sucursales: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        panel.add(lblSucursal);
        panel.add(comboSucursales);

        return panel;
    }

    private JPanel crearPanelItems() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        // Panel de entrada de items
        JPanel panelEntrada = new JPanel(new GridBagLayout());
        panelEntrada.setBackground(Color.WHITE);
        panelEntrada.setBorder(BorderFactory.createTitledBorder("Agregar Ítems"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Seleccionar libro
        JLabel lblLibro = new JLabel("Libro:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        panelEntrada.add(lblLibro, gbc);

        comboLibros = new JComboBox<>();
        try {
            List<Libro> libros = servicioLibro.obtenerTodos();
            for (Libro libro : libros) {
                comboLibros.addItem(libro);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        panelEntrada.add(comboLibros, gbc);

        // Cantidad
        JLabel lblCantidad = new JLabel("Cantidad:");
        gbc.gridx = 2;
        gbc.weightx = 0.1;
        panelEntrada.add(lblCantidad, gbc);

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        gbc.gridx = 3;
        gbc.weightx = 0.1;
        panelEntrada.add(spinnerCantidad, gbc);

        // Botón agregar
        btnAgregarItem = new JButton("Agregar");
        btnAgregarItem.addActionListener(e -> agregarItem());
        gbc.gridx = 4;
        gbc.weightx = 0.1;
        panelEntrada.add(btnAgregarItem, gbc);

        panel.add(panelEntrada, BorderLayout.NORTH);

        // Tabla de items
        String[] columnas = {"ID Libro", "Título", "Cantidad", "Precio Unit.", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaItems = new JTable(modeloTabla);
        tablaItems.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tablaItems);
        scrollPane.setPreferredSize(new Dimension(850, 250));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Botón eliminar
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAcciones.setBackground(Color.WHITE);
        btnEliminarItem = new JButton("Eliminar Seleccionado");
        btnEliminarItem.addActionListener(e -> eliminarItem());
        panelAcciones.add(btnEliminarItem);

        panel.add(panelAcciones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelTotales() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Resumen"));

        lblSubtotal = new JLabel("Subtotal: $0.00");
        lblSubtotal.setFont(new Font("Arial", Font.BOLD, 12));

        lblDescuento = new JLabel("Descuento: $0.00");
        lblDescuento.setFont(new Font("Arial", Font.BOLD, 12));

        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotal.setForeground(new Color(0, 100, 0));

        JLabel lblForma = new JLabel("Forma de Pago:");
        comboFormaPago = new JComboBox<>();
        try {
            List<FormaPago> formas = servicioVenta.obtenerFormasDePago();
            for (FormaPago forma : formas) {
                comboFormaPago.addItem(forma);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar formas de pago: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        panel.add(lblSubtotal);
        panel.add(lblDescuento);
        panel.add(lblTotal);
        panel.add(new JSeparator(JSeparator.VERTICAL));
        panel.add(lblForma);
        panel.add(comboFormaPago);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);

        btnGuardar = new JButton("Guardar Venta");
        btnGuardar.setPreferredSize(new Dimension(120, 35));
        btnGuardar.addActionListener(e -> guardarVenta());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(120, 35));
        btnCancelar.addActionListener(e -> dispose());

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        return panel;
    }

    private void agregarItem() {
        if (comboLibros.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un libro", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Libro libro = (Libro) comboLibros.getSelectedItem();
        Sucursal sucursal = (Sucursal) comboSucursales.getSelectedItem();
        int cantidad = (Integer) spinnerCantidad.getValue();

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar stock
        try {
            Integer stockDisponible = servicioVenta.obtenerStockLibro(libro.getIdLibro(), sucursal.getId());
            if (stockDisponible == null || stockDisponible < cantidad) {
                JOptionPane.showMessageDialog(this, 
                        "No hay suficiente stock. Disponible: " + (stockDisponible != null ? stockDisponible : 0), 
                        "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Agregar a la tabla y a la lista
            ItemVenta item = new ItemVenta(libro.getIdLibro(), cantidad, libro.getPrecio(), BigDecimal.ZERO);
            items.add(item);

            BigDecimal subtotal = libro.getPrecio().multiply(BigDecimal.valueOf(cantidad));
            modeloTabla.addRow(new Object[]{
                    libro.getIdLibro(),
                    libro.getTitulo(),
                    cantidad,
                    String.format("$%.2f", libro.getPrecio()),
                    String.format("$%.2f", subtotal)
            });

            actualizarTotales();
            spinnerCantidad.setValue(1);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarItem() {
        int filaSeleccionada = tablaItems.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un item para eliminar", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        items.remove(filaSeleccionada);
        modeloTabla.removeRow(filaSeleccionada);
        actualizarTotales();
    }

    private void actualizarTotales() {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal descuento = BigDecimal.ZERO;

        for (ItemVenta item : items) {
            BigDecimal precioItem = item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad()));
            subtotal = subtotal.add(precioItem);
            descuento = descuento.add(item.getDescuentoAplicado().multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        BigDecimal total = subtotal.subtract(descuento);

        lblSubtotal.setText(String.format("Subtotal: $%.2f", subtotal));
        lblDescuento.setText(String.format("Descuento: $%.2f", descuento));
        lblTotal.setText(String.format("Total: $%.2f", total));
    }

    private void guardarVenta() {
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor agregue al menos un item", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (comboFormaPago.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione una forma de pago", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Calcular totales
            BigDecimal subtotal = BigDecimal.ZERO;
            BigDecimal descuento = BigDecimal.ZERO;

            for (ItemVenta item : items) {
                BigDecimal precioItem = item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad()));
                subtotal = subtotal.add(precioItem);
                descuento = descuento.add(item.getDescuentoAplicado().multiply(BigDecimal.valueOf(item.getCantidad())));
            }

            BigDecimal total = subtotal.subtract(descuento);
            FormaPago forma = (FormaPago) comboFormaPago.getSelectedItem();

            Venta venta = new Venta(usuarioActual.getId(), forma.getIdFormaPago(), total, subtotal, descuento);
            Sucursal sucursal = (Sucursal) comboSucursales.getSelectedItem();

            boolean resultado = servicioVenta.crearVentaGUI(venta, items, sucursal.getId());

            if (resultado) {
                JOptionPane.showMessageDialog(this, "Venta registrada exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                confirmado = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la venta", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}
