package gui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import servicios.ServicioStock;
import servicios.ServicioSucursal;
import servicios.ServicioLibro;
import ventas.Sucursal;
import ventas.Libro;

/**
 * Panel para la gestión de stock.
 * Permite consultar y actualizar el stock de libros por sucursal.
 */
public class GestionStockPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    private ServicioStock servicioStock;
    private ServicioSucursal servicioSucursal;
    private ServicioLibro servicioLibro;
    private JTable tablaStock;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar;
    private JButton btnDisminuir;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JComboBox<Sucursal> comboSucursales;
    private Sucursal sucursalSeleccionada;

    public GestionStockPanel() {
        super(new BorderLayout(10, 10));
        this.servicioStock = new ServicioStock();
        this.servicioSucursal = new ServicioSucursal();
        this.servicioLibro = new ServicioLibro();
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);
    }

    @Override
    public void inicializar() {
        // Panel superior con selector de sucursal
        JPanel panelSuperior = crearPanelSuperior();
        this.add(panelSuperior, BorderLayout.NORTH);

        // Panel central con tabla
        JPanel panelCentral = crearPanelCentral();
        this.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con botones de acción
        JPanel panelInferior = crearPanelInferior();
        this.add(panelInferior, BorderLayout.SOUTH);

        // Cargar sucursales
        cargarSucursales();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Gestión de Stock");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel lblSucursal = new JLabel("Sucursal:");
        comboSucursales = new JComboBox<>();
        comboSucursales.addActionListener(e -> cambiarSucursal());

        panel.add(lblTitulo);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(lblSucursal);
        panel.add(comboSucursales);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Crear modelo de tabla
        String[] columnas = {"ID Libro", "Título", "Autor", "ISBN", "Cantidad"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaStock = new JTable(modeloTabla);
        tablaStock.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaStock.setRowHeight(25);
        tablaStock.getTableHeader().setReorderingAllowed(false);

        // Scroll para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaStock);
        scrollPane.setPreferredSize(new Dimension(900, 400));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnAgregar = new JButton("Agregar Stock");
        btnAgregar.setPreferredSize(new Dimension(140, 35));
        btnAgregar.addActionListener(e -> agregarStock());

        btnDisminuir = new JButton("Disminuir Stock");
        btnDisminuir.setPreferredSize(new Dimension(140, 35));
        btnDisminuir.addActionListener(e -> disminuirStock());

        btnModificar = new JButton("Modificar");
        btnModificar.setPreferredSize(new Dimension(140, 35));
        btnModificar.addActionListener(e -> modificarStock());

        btnEliminar = new JButton("Eliminar Stock");
        btnEliminar.setPreferredSize(new Dimension(140, 35));
        btnEliminar.addActionListener(e -> eliminarStock());

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setPreferredSize(new Dimension(140, 35));
        btnRefrescar.addActionListener(e -> refrescar());

        panel.add(btnAgregar);
        panel.add(btnDisminuir);
        panel.add(btnModificar);
        panel.add(btnEliminar);
        panel.add(btnRefrescar);

        return panel;
    }

    private void cargarSucursales() {
        try {
            List<Sucursal> sucursales = servicioSucursal.obtenerTodasLasSucursales();
            comboSucursales.removeAllItems();

            for (Sucursal sucursal : sucursales) {
                comboSucursales.addItem(sucursal);
            }

            if (!sucursales.isEmpty()) {
                sucursalSeleccionada = sucursales.get(0);
                refrescar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar sucursales: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarSucursal() {
        sucursalSeleccionada = (Sucursal) comboSucursales.getSelectedItem();
        if (sucursalSeleccionada != null) {
            refrescar();
        }
    }

    @Override
    public void refrescar() {
        if (sucursalSeleccionada == null) return;
        cargarStock();
    }

    private void cargarStock() {
        try {
            modeloTabla.setRowCount(0);
            List<String> stockList = servicioStock.obtenerStockPorSucursal(sucursalSeleccionada.getId());
            List<Libro> librosConStock = servicioStock.obtenerLibrosConStock(sucursalSeleccionada.getId());

            for (Libro libro : librosConStock) {
                Integer cantidad = servicioStock.obtenerStockLibro(libro.getIdLibro(), sucursalSeleccionada.getId());
                if (cantidad != null) {
                    modeloTabla.addRow(new Object[]{
                            libro.getIdLibro(),
                            libro.getTitulo(),
                            libro.getNombreAutor() != null ? libro.getNombreAutor() : "N/A",
                            libro.getIsbn(),
                            cantidad
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar stock: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarStock() {
        if (sucursalSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione una sucursal",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Libro> libros = servicioLibro.obtenerTodos();
            if (libros.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay libros registrados",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Libro libroSeleccionado = (Libro) JOptionPane.showInputDialog(this,
                    "Seleccione un libro:",
                    "Agregar Stock",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    libros.toArray(),
                    libros.get(0));

            if (libroSeleccionado != null) {
                String cantidadStr = JOptionPane.showInputDialog(this,
                        "Ingrese la cantidad a agregar:",
                        "Agregar Stock",
                        JOptionPane.QUESTION_MESSAGE);

                if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                    try {
                        int cantidad = Integer.parseInt(cantidadStr);
                        if (cantidad <= 0) {
                            JOptionPane.showMessageDialog(this,
                                    "La cantidad debe ser mayor a 0",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        boolean resultado = servicioStock.agregarStockGUI(libroSeleccionado.getIdLibro(),
                                sucursalSeleccionada.getId(), cantidad);

                        if (resultado) {
                            JOptionPane.showMessageDialog(this,
                                    "Stock agregado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE);
                            refrescar();
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Error al agregar stock",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this,
                                "Debe ingresar un número válido",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void disminuirStock() {
        int filaSeleccionada = tablaStock.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un libro",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int libroId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            int cantidadActual = (int) modeloTabla.getValueAt(filaSeleccionada, 4);

            String cantidadStr = JOptionPane.showInputDialog(this,
                    "Ingrese la cantidad a disminuir (Cantidad actual: " + cantidadActual + "):",
                    "Disminuir Stock",
                    JOptionPane.QUESTION_MESSAGE);

            if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                try {
                    int cantidad = Integer.parseInt(cantidadStr);
                    if (cantidad <= 0) {
                        JOptionPane.showMessageDialog(this,
                                "La cantidad debe ser mayor a 0",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (cantidad > cantidadActual) {
                        JOptionPane.showMessageDialog(this,
                                "No hay suficiente stock",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean resultado = servicioStock.disminuirStockGUI(libroId,
                            sucursalSeleccionada.getId(), cantidad);

                    if (resultado) {
                        JOptionPane.showMessageDialog(this,
                                "Stock disminuido exitosamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        refrescar();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Error al disminuir stock",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Debe ingresar un número válido",
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

    private void modificarStock() {
        int filaSeleccionada = tablaStock.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un libro",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int libroId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            int cantidadActual = (int) modeloTabla.getValueAt(filaSeleccionada, 4);
            String tituloLibro = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

            String cantidadStr = JOptionPane.showInputDialog(this,
                    "Ingrese la nueva cantidad para '" + tituloLibro + "':\n(Cantidad actual: " + cantidadActual + ")",
                    "Modificar Stock",
                    JOptionPane.QUESTION_MESSAGE);

            if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                try {
                    int nuevaCantidad = Integer.parseInt(cantidadStr);
                    if (nuevaCantidad < 0) {
                        JOptionPane.showMessageDialog(this,
                                "La cantidad no puede ser negativa",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean resultado = servicioStock.modificarStockGUI(libroId,
                            sucursalSeleccionada.getId(), nuevaCantidad);

                    if (resultado) {
                        JOptionPane.showMessageDialog(this,
                                "Stock modificado exitosamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        refrescar();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Error al modificar stock",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Debe ingresar un número válido",
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

    private void eliminarStock() {
        int filaSeleccionada = tablaStock.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un libro",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int libroId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String tituloLibro = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Desea eliminar todo el stock del libro '" + tituloLibro + "' en la sucursal '" +
                            sucursalSeleccionada.getNombre() + "'?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                boolean resultado = servicioStock.eliminarStockGUI(libroId, sucursalSeleccionada.getId());

                if (resultado) {
                    JOptionPane.showMessageDialog(this,
                            "Stock eliminado exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    refrescar();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al eliminar stock",
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
}
