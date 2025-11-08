package gui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import servicios.ServicioSucursal;
import ventas.Sucursal;
import gui.dialogs.SucursalFormDialog;

/**
 * Panel para la gestión de sucursales.
 * Permite crear, editar y eliminar sucursales del sistema.
 */
public class GestionSucursalesPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    private ServicioSucursal servicioSucursal;
    private JTable tablaSucursales;
    private DefaultTableModel modeloTabla;
    private JButton btnCrear;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JButton btnBuscar;
    private JTextField campoBusqueda;

    public GestionSucursalesPanel() {
        super(new BorderLayout(10, 10));
        this.servicioSucursal = new ServicioSucursal();
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

        JLabel lblTitulo = new JLabel("Gestión de Sucursales");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitulo, BorderLayout.WEST);

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelBusqueda.setBackground(Color.WHITE);

        JLabel lblBuscar = new JLabel("Buscar:");
        campoBusqueda = new JTextField(20);
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
        String[] columnas = {"ID", "Nombre", "Dirección"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaSucursales = new JTable(modeloTabla);
        tablaSucursales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaSucursales.setRowHeight(25);
        tablaSucursales.getTableHeader().setReorderingAllowed(false);

        // Scroll para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaSucursales);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnCrear = new JButton("Crear Sucursal");
        btnCrear.setPreferredSize(new Dimension(140, 35));
        btnCrear.addActionListener(e -> crearSucursal());

        btnEditar = new JButton("Editar Sucursal");
        btnEditar.setPreferredSize(new Dimension(140, 35));
        btnEditar.addActionListener(e -> editarSucursal());

        btnEliminar = new JButton("Eliminar Sucursal");
        btnEliminar.setPreferredSize(new Dimension(140, 35));
        btnEliminar.addActionListener(e -> eliminarSucursal());

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setPreferredSize(new Dimension(140, 35));
        btnRefrescar.addActionListener(e -> refrescar());

        panel.add(btnCrear);
        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnRefrescar);

        return panel;
    }

    @Override
    public void refrescar() {
        cargarSucursales();
    }

    private void cargarSucursales() {
        try {
            modeloTabla.setRowCount(0);
            List<Sucursal> sucursales = servicioSucursal.obtenerTodasLasSucursales();

            for (Sucursal sucursal : sucursales) {
                modeloTabla.addRow(new Object[]{
                        sucursal.getId(),
                        sucursal.getNombre(),
                        sucursal.getDireccion()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar sucursales: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearSucursal() {
        SucursalFormDialog dialog = new SucursalFormDialog(null, true, servicioSucursal);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            refrescar();
        }
    }

    private void editarSucursal() {
        int filaSeleccionada = tablaSucursales.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione una sucursal para editar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int sucursalId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Sucursal sucursal = servicioSucursal.obtenerPorId(sucursalId);

            if (sucursal != null) {
                SucursalFormDialog dialog = new SucursalFormDialog(sucursal, true, servicioSucursal);
                dialog.setVisible(true);

                if (dialog.isConfirmado()) {
                    refrescar();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo cargar la sucursal",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al editar sucursal: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSucursal() {
        int filaSeleccionada = tablaSucursales.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione una sucursal para eliminar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int sucursalId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreSucursal = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar la sucursal '" + nombreSucursal + "'?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = servicioSucursal.eliminarSucursalGUI(sucursalId);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this,
                            "Sucursal eliminada exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    refrescar();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo eliminar la sucursal",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar sucursal: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscar() {
        String busqueda = campoBusqueda.getText().trim();

        if (busqueda.isEmpty()) {
            refrescar();
            return;
        }

        try {
            modeloTabla.setRowCount(0);
            List<Sucursal> sucursales = servicioSucursal.obtenerTodasLasSucursales();

            for (Sucursal sucursal : sucursales) {
                if (sucursal.getNombre().toLowerCase().contains(busqueda.toLowerCase()) ||
                    sucursal.getDireccion().toLowerCase().contains(busqueda.toLowerCase())) {
                    
                    modeloTabla.addRow(new Object[]{
                            sucursal.getId(),
                            sucursal.getNombre(),
                            sucursal.getDireccion()
                    });
                }
            }

            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron sucursales con '" + busqueda + "'",
                        "Búsqueda",
                        JOptionPane.INFORMATION_MESSAGE);
                refrescar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            refrescar();
        }
    }
}
