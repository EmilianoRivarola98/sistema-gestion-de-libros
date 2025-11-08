package gui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import servicios.ServicioPromocion;
import ventas.Promocion;
import gui.dialogs.PromocionFormDialog;

/**
 * Panel para la gestión de promociones de venta.
 * Permite crear, editar y eliminar promociones del sistema.
 */
public class GestionPromocionesPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    private ServicioPromocion servicioPromocion;
    private JTable tablaPromociones;
    private DefaultTableModel modeloTabla;
    private JButton btnCrear;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JButton btnBuscar;
    private JTextField campoBusqueda;

    public GestionPromocionesPanel() {
        super(new BorderLayout(10, 10));
        this.servicioPromocion = new ServicioPromocion();
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

        JLabel lblTitulo = new JLabel("Gestión de Promociones");
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
        String[] columnas = {"ID", "Nombre", "Descripción", "Descuento (%)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPromociones = new JTable(modeloTabla);
        tablaPromociones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPromociones.setRowHeight(25);
        tablaPromociones.getTableHeader().setReorderingAllowed(false);

        // Scroll para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaPromociones);
        scrollPane.setPreferredSize(new Dimension(900, 400));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnCrear = new JButton("Crear Promoción");
        btnCrear.setPreferredSize(new Dimension(140, 35));
        btnCrear.addActionListener(e -> crearPromocion());

        btnEditar = new JButton("Editar Promoción");
        btnEditar.setPreferredSize(new Dimension(140, 35));
        btnEditar.addActionListener(e -> editarPromocion());

        btnEliminar = new JButton("Eliminar Promoción");
        btnEliminar.setPreferredSize(new Dimension(140, 35));
        btnEliminar.addActionListener(e -> eliminarPromocion());

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
        cargarPromociones();
    }

    private void cargarPromociones() {
        try {
            modeloTabla.setRowCount(0);
            List<Promocion> promociones = servicioPromocion.obtenerTodas();

            for (Promocion promocion : promociones) {
                modeloTabla.addRow(new Object[]{
                        promocion.getIdPromocion(),
                        promocion.getNombre(),
                        promocion.getDescripcion(),
                        String.format("%.2f", promocion.getDescuento())
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar promociones: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearPromocion() {
        PromocionFormDialog dialog = new PromocionFormDialog(null, true, servicioPromocion);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            refrescar();
        }
    }

    private void editarPromocion() {
        int filaSeleccionada = tablaPromociones.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione una promoción para editar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int promocionId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Promocion promocion = servicioPromocion.obtenerPorId(promocionId);

            if (promocion != null) {
                PromocionFormDialog dialog = new PromocionFormDialog(promocion, true, servicioPromocion);
                dialog.setVisible(true);

                if (dialog.isConfirmado()) {
                    refrescar();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo cargar la promoción",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al editar promoción: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPromocion() {
        int filaSeleccionada = tablaPromociones.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione una promoción para eliminar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int promocionId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombrePromocion = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar la promoción '" + nombrePromocion + "'?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = servicioPromocion.eliminarPromocionGUI(promocionId);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this,
                            "Promoción eliminada exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    refrescar();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo eliminar la promoción",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar promoción: " + ex.getMessage(),
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
            List<Promocion> promociones = servicioPromocion.obtenerTodas();

            for (Promocion promocion : promociones) {
                if (promocion.getNombre().toLowerCase().contains(busqueda.toLowerCase()) ||
                    promocion.getDescripcion().toLowerCase().contains(busqueda.toLowerCase())) {
                    
                    modeloTabla.addRow(new Object[]{
                            promocion.getIdPromocion(),
                            promocion.getNombre(),
                            promocion.getDescripcion(),
                            String.format("%.2f", promocion.getDescuento())
                    });
                }
            }

            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron promociones con '" + busqueda + "'",
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
