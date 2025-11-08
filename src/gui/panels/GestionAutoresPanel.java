package gui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import servicios.ServicioAutor;
import ventas.Autor;
import gui.dialogs.AutorFormDialog;

/**
 * Panel para la gestión de autores (CRUD).
 */
public class GestionAutoresPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    private ServicioAutor servicioAutor;
    private JTable tablaAutores;
    private DefaultTableModel modeloTabla;
    private JButton btnCrear;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JButton btnBuscar;
    private JTextField campoBusqueda;

    public GestionAutoresPanel() {
        super(new BorderLayout(10, 10));
        this.servicioAutor = new ServicioAutor();
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

        JLabel lblTitulo = new JLabel("Gestión de Autores");
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

        tablaAutores = new JTable(modeloTabla);
        tablaAutores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAutores.setRowHeight(25);
        tablaAutores.getTableHeader().setReorderingAllowed(false);

        // Scroll para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaAutores);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnCrear = new JButton("Crear Autor");
        btnCrear.setPreferredSize(new Dimension(140, 35));
        btnCrear.addActionListener(e -> crearAutor());

        btnEditar = new JButton("Editar Autor");
        btnEditar.setPreferredSize(new Dimension(140, 35));
        btnEditar.addActionListener(e -> editarAutor());

        btnEliminar = new JButton("Eliminar Autor");
        btnEliminar.setPreferredSize(new Dimension(140, 35));
        btnEliminar.addActionListener(e -> eliminarAutor());

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
        cargarAutores();
    }

    private void cargarAutores() {
        try {
            modeloTabla.setRowCount(0);
            List<Autor> autores = servicioAutor.obtenerTodos();

            for (Autor autor : autores) {
                modeloTabla.addRow(new Object[]{
                        autor.getIdAutor(),
                        autor.getNombre()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar autores: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearAutor() {
        AutorFormDialog dialog = new AutorFormDialog(null, true);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            refrescar();
        }
    }

    private void editarAutor() {
        int filaSeleccionada = tablaAutores.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un autor para editar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int autorId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombreAutor = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
            Autor autor = new Autor(autorId, nombreAutor);

            AutorFormDialog dialog = new AutorFormDialog(autor, true);
            dialog.setVisible(true);

            if (dialog.isConfirmado()) {
                refrescar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al editar autor: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarAutor() {
        int filaSeleccionada = tablaAutores.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un autor para eliminar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int autorId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreAutor = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar al autor '" + nombreAutor + "'?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = servicioAutor.eliminarAutor(autorId);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this,
                            "Autor eliminado exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    refrescar();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo eliminar el autor",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar autor: " + ex.getMessage(),
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
            List<Autor> autores = servicioAutor.obtenerTodos();

            for (Autor autor : autores) {
                if (autor.getNombre().toLowerCase().contains(busqueda.toLowerCase())) {
                    modeloTabla.addRow(new Object[]{
                            autor.getIdAutor(),
                            autor.getNombre()
                    });
                }
            }

            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron autores con '" + busqueda + "'",
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
