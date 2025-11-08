package gui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import servicios.ServicioVenta;
import servicios.ServicioLibro;
import servicios.ServicioSucursal;
import servicios.ServicioUsuario;
import ventas.Venta;
import ventas.ItemVenta;
import ventas.Libro;
import ventas.FormaPago;
import usuarios.Usuario;
import gui.dialogs.VentaFormDialog;

/**
 * Panel para la gestión de ventas.
 * Permite crear, consultar y anular ventas del sistema.
 */
public class GestionVentasPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    private ServicioVenta servicioVenta;
    private ServicioLibro servicioLibro;
    private ServicioSucursal servicioSucursal;
    private ServicioUsuario servicioUsuario;
    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevaVenta;
    private JButton btnVerDetalles;
    private JButton btnAnularVenta;
    private JButton btnRefrescar;
    private JButton btnBuscar;
    private JTextField campoBusqueda;
    private SimpleDateFormat formatoFecha;
    private Usuario usuarioActual;

    public GestionVentasPanel(Usuario usuario) {
        super(new BorderLayout(10, 10));
        this.servicioVenta = new ServicioVenta();
        this.servicioLibro = new ServicioLibro();
        this.servicioSucursal = new ServicioSucursal();
        this.servicioUsuario = new ServicioUsuario();
        this.usuarioActual = usuario;
        this.formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);
    }

    @Override
    public void inicializar() {
        // Panel superior con búsqueda y botones
        JPanel panelSuperior = crearPanelSuperior();
        this.add(panelSuperior, BorderLayout.NORTH);

        // Panel central con tabla
        JPanel panelCentral = crearPanelCentral();
        this.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con botones de acción
        JPanel panelInferior = crearPanelInferior();
        this.add(panelInferior, BorderLayout.SOUTH);

        // Cargar datos
        refrescar();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Gestión de Ventas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitulo, BorderLayout.WEST);

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelBusqueda.setBackground(Color.WHITE);

        JLabel lblBuscar = new JLabel("Buscar ID Venta:");
        campoBusqueda = new JTextField(15);
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar());

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(campoBusqueda);
        panelBusqueda.add(btnBuscar);

        panel.add(panelBusqueda, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Crear modelo de tabla
        String[] columnas = {"ID Venta", "Fecha", "Usuario", "Forma Pago", "Subtotal", "Descuento", "Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaVentas = new JTable(modeloTabla);
        tablaVentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaVentas.setRowHeight(25);
        tablaVentas.getTableHeader().setReorderingAllowed(false);

        // Scroll para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaVentas);
        scrollPane.setPreferredSize(new Dimension(950, 400));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnNuevaVenta = new JButton("Nueva Venta");
        btnNuevaVenta.setPreferredSize(new Dimension(140, 35));
        btnNuevaVenta.addActionListener(e -> nuevaVenta());

        btnVerDetalles = new JButton("Ver Detalles");
        btnVerDetalles.setPreferredSize(new Dimension(140, 35));
        btnVerDetalles.addActionListener(e -> verDetalles());

        btnAnularVenta = new JButton("Anular Venta");
        btnAnularVenta.setPreferredSize(new Dimension(140, 35));
        btnAnularVenta.addActionListener(e -> anularVenta());

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setPreferredSize(new Dimension(140, 35));
        btnRefrescar.addActionListener(e -> refrescar());

        panel.add(btnNuevaVenta);
        panel.add(btnVerDetalles);
        panel.add(btnAnularVenta);
        panel.add(btnRefrescar);

        return panel;
    }

    @Override
    public void refrescar() {
        cargarVentas();
    }

    private void cargarVentas() {
        try {
            modeloTabla.setRowCount(0);
            List<Venta> ventas = servicioVenta.obtenerTodasLasVentas();

            for (Venta venta : ventas) {
                String fechaFormato = venta.getFecha() != null ? 
                    formatoFecha.format(venta.getFecha()) : "N/A";
                
                Usuario usuario = servicioUsuario.obtenerUsuario(venta.getIdUsuario());
                String nombreUsuario = usuario != null ? usuario.getNombre() : "N/A";
                
                List<FormaPago> formas = servicioVenta.obtenerFormasDePago();
                String formaPago = "N/A";
                for (FormaPago forma : formas) {
                    if (forma.getIdFormaPago() == venta.getIdForma()) {
                        formaPago = forma.getDescripcion();
                        break;
                    }
                }

                modeloTabla.addRow(new Object[]{
                        venta.getIdVenta(),
                        fechaFormato,
                        nombreUsuario,
                        formaPago,
                        String.format("$%.2f", venta.getSubtotal()),
                        String.format("$%.2f", venta.getDescuento()),
                        String.format("$%.2f", venta.getTotal())
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar ventas: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void nuevaVenta() {
        try {
            VentaFormDialog dialog = new VentaFormDialog(usuarioActual, servicioVenta, servicioLibro, servicioSucursal);
            dialog.setVisible(true);

            if (dialog.isConfirmado()) {
                refrescar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear venta: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDetalles() {
        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione una venta",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int ventaId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Venta venta = servicioVenta.obtenerVentaPorId(ventaId);

            if (venta != null) {
                StringBuilder detalles = new StringBuilder();
                detalles.append("ID Venta: ").append(venta.getIdVenta()).append("\n");
                detalles.append("Fecha: ").append(formatoFecha.format(venta.getFecha())).append("\n");
                detalles.append("Usuario ID: ").append(venta.getIdUsuario()).append("\n");
                detalles.append("Forma de Pago ID: ").append(venta.getIdForma()).append("\n");
                detalles.append("Subtotal: $").append(String.format("%.2f", venta.getSubtotal())).append("\n");
                detalles.append("Descuento: $").append(String.format("%.2f", venta.getDescuento())).append("\n");
                detalles.append("Total: $").append(String.format("%.2f", venta.getTotal())).append("\n");

                if (venta.getItems() != null && !venta.getItems().isEmpty()) {
                    detalles.append("\n--- Ítems de la Venta ---\n");
                    for (ItemVenta item : venta.getItems()) {
                        Libro libro = servicioLibro.obtenerPorId(item.getIdLibro());
                        String tituloLibro = libro != null ? libro.getTitulo() : "Libro ID: " + item.getIdLibro();
                        detalles.append("- ").append(tituloLibro)
                                .append(" | Cantidad: ").append(item.getCantidad())
                                .append(" | Precio: $").append(String.format("%.2f", item.getPrecioUnitario()))
                                .append("\n");
                    }
                }

                JTextArea textArea = new JTextArea(detalles.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));

                JOptionPane.showMessageDialog(this,
                        scrollPane,
                        "Detalles de la Venta",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener detalles: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void anularVenta() {
        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione una venta",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int ventaId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String total = (String) modeloTabla.getValueAt(filaSeleccionada, 6);

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Desea anular la venta ID " + ventaId + " con total " + total + "?",
                    "Confirmar Anulación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean resultado = servicioVenta.eliminarVentaGUI(ventaId);

                if (resultado) {
                    JOptionPane.showMessageDialog(this,
                            "Venta anulada exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    refrescar();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al anular la venta",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscar() {
        String busqueda = campoBusqueda.getText().trim();

        if (busqueda.isEmpty()) {
            refrescar();
            return;
        }

        try {
            int ventaId = Integer.parseInt(busqueda);
            modeloTabla.setRowCount(0);

            List<Venta> ventas = servicioVenta.obtenerTodasLasVentas();
            for (Venta venta : ventas) {
                if (venta.getIdVenta() == ventaId) {
                    String fechaFormato = venta.getFecha() != null ? 
                        formatoFecha.format(venta.getFecha()) : "N/A";
                    
                    Usuario usuario = servicioUsuario.obtenerUsuario(venta.getIdUsuario());
                    String nombreUsuario = usuario != null ? usuario.getNombre() : "N/A";
                    
                    List<FormaPago> formas = servicioVenta.obtenerFormasDePago();
                    String formaPago = "N/A";
                    for (FormaPago forma : formas) {
                        if (forma.getIdFormaPago() == venta.getIdForma()) {
                            formaPago = forma.getDescripcion();
                            break;
                        }
                    }

                    modeloTabla.addRow(new Object[]{
                            venta.getIdVenta(),
                            fechaFormato,
                            nombreUsuario,
                            formaPago,
                            String.format("$%.2f", venta.getSubtotal()),
                            String.format("$%.2f", venta.getDescuento()),
                            String.format("$%.2f", venta.getTotal())
                    });
                    break;
                }
            }

            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró venta con ID " + ventaId,
                        "Búsqueda",
                        JOptionPane.INFORMATION_MESSAGE);
                refrescar();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un ID de venta válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            refrescar();
        }
    }
}
