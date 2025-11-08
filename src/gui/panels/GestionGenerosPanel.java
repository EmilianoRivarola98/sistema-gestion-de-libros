package gui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import servicios.ServicioGenero;
import ventas.Genero;
import gui.dialogs.GeneroFormDialog;

/**
 * Panel para la gestión de géneros (CRUD).
 * Permite crear, leer, actualizar y eliminar géneros del sistema.
 */
public class GestionGenerosPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    private ServicioGenero servicioGenero;
    private JTable tablaGeneros;
    private DefaultTableModel modeloTabla;
    private JButton btnCrear;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JButton btnBuscar;
    private JTextField campoBusqueda;

    public GestionGenerosPanel() {
        super(new BorderLayout(10, 10));
        this.servicioGenero = new ServicioGenero();
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

        JLabel lblTitulo = new JLabel("Gestión de Géneros");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitulo, BorderLayout.WEST);

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelBusqueda.setBackground(Color.WHITE);

        JLabel lblBuscar = new JLabel("Buscar:");
        campoBusqueda = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(this::buscar);

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
        String[] columnas = {"ID", "Nombre"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaGeneros = new JTable(modeloTabla);
        tablaGeneros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaGeneros.setRowHeight(25);
        tablaGeneros.getTableHeader().setReorderingAllowed(false);

        // Scroll para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaGeneros);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnCrear = new JButton("Crear Género");
        btnCrear.setPreferredSize(new Dimension(140, 35));
        btnCrear.addActionListener(e -> crearGenero());

        btnEditar = new JButton("Editar Género");
        btnEditar.setPreferredSize(new Dimension(140, 35));
        btnEditar.addActionListener(e -> editarGenero());

        btnEliminar = new JButton("Eliminar Género");
        btnEliminar.setPreferredSize(new Dimension(140, 35));
        btnEliminar.addActionListener(e -> eliminarGenero());

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
        cargarGeneros();
    }

    private void cargarGeneros() {
        try {
            modeloTabla.setRowCount(0);
            List<Genero> generos = servicioGenero.obtenerTodos();

            for (Genero genero : generos) {
                modeloTabla.addRow(new Object[]{
                        genero.getIdGenero(),
                        genero.getNombre()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar géneros: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearGenero() {
        GeneroFormDialog dialog = new GeneroFormDialog(null, true);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            refrescar();
        }
    }

    private void editarGenero() {
        int filaSeleccionada = tablaGeneros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un género para editar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int generoId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombreGenero = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
            Genero genero = new Genero(generoId, nombreGenero);

            GeneroFormDialog dialog = new GeneroFormDialog(genero, true);
            dialog.setVisible(true);

            if (dialog.isConfirmado()) {
                refrescar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al editar género: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarGenero() {
        int filaSeleccionada = tablaGeneros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un género para eliminar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int generoId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreGenero = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar el género '" + nombreGenero + "'?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = servicioGenero.eliminarGenero(generoId);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this,
                            "Género eliminado exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    refrescar();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo eliminar el género",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar género: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscar(ActionEvent e) {
        String busqueda = campoBusqueda.getText().trim();

        if (busqueda.isEmpty()) {
            refrescar();
            return;
        }

        try {
            modeloTabla.setRowCount(0);
            List<Genero> generos = servicioGenero.obtenerTodos();

            for (Genero genero : generos) {
                if (genero.getNombre().toLowerCase().contains(busqueda.toLowerCase())) {
                    modeloTabla.addRow(new Object[]{
                            genero.getIdGenero(),
                            genero.getNombre()
                    });
                }
            }

            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron géneros con '" + busqueda + "'",
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
