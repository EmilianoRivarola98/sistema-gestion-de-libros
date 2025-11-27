package gui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import servicios.ServicioLibro;
import servicios.ServicioAutor;
import servicios.ServicioGenero;
import ventas.Libro;
import gui.dialogs.LibroFormDialog;

/**
 * Panel para la gestión de libros (CRUD).
 * Permite crear, leer, actualizar y eliminar libros del sistema.
 */
public class GestionLibrosPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    private ServicioLibro servicioLibro;
    private ServicioAutor servicioAutor;
    private ServicioGenero servicioGenero;
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private JButton btnCrear;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JButton btnBuscar;
    private JTextField campoBusqueda;
    private JComboBox<String> comboCriterio;
    private boolean soloLectura;

    public GestionLibrosPanel() {
        this(false);
    }

    public GestionLibrosPanel(boolean soloLectura) {
        super(new BorderLayout(10, 10));
        this.soloLectura = soloLectura;
        this.servicioLibro = new ServicioLibro();
        this.servicioAutor = new ServicioAutor();
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

        JLabel lblTitulo = new JLabel("Gestión de Libros");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitulo, BorderLayout.WEST);

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelBusqueda.setBackground(Color.WHITE);

        JLabel lblCriterio = new JLabel("Buscar por:");
        comboCriterio = new JComboBox<>(new String[]{"Todos", "Título", "Autor", "Género", "ISBN"});
        comboCriterio.setPreferredSize(new Dimension(100, 25));

        JLabel lblBuscar = new JLabel();
        campoBusqueda = new JTextField(20);
        campoBusqueda.addActionListener(this::buscar); 
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(this::buscar);

        panelBusqueda.add(lblCriterio);
        panelBusqueda.add(comboCriterio);
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
        String[] columnas = {"ID", "Título", "Autor", "Género", "ISBN", "Precio"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaLibros.setRowHeight(25);
        tablaLibros.getTableHeader().setReorderingAllowed(false);

        // Scroll para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Solo mostrar botones de edición si no es modo solo lectura
        if (!soloLectura) {
            btnCrear = new JButton("Crear Libro");
            btnCrear.setPreferredSize(new Dimension(140, 35));
            btnCrear.addActionListener(e -> crearLibro());

            btnEditar = new JButton("Editar Libro");
            btnEditar.setPreferredSize(new Dimension(140, 35));
            btnEditar.addActionListener(e -> editarLibro());

            btnEliminar = new JButton("Eliminar Libro");
            btnEliminar.setPreferredSize(new Dimension(140, 35));
            btnEliminar.addActionListener(e -> eliminarLibro());

            panel.add(btnCrear);
            panel.add(btnEditar);
            panel.add(btnEliminar);
        }

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setPreferredSize(new Dimension(140, 35));
        btnRefrescar.addActionListener(e -> refrescar());

        panel.add(btnRefrescar);

        return panel;
    }

    @Override
    public void refrescar() {
        cargarLibros();
    }

    private void cargarLibros() {
        try {
            modeloTabla.setRowCount(0);
            List<Libro> libros = servicioLibro.obtenerTodos();

            for (Libro libro : libros) {
                modeloTabla.addRow(new Object[]{
                        libro.getIdLibro(),
                        libro.getTitulo(),
                        libro.getNombreAutor() != null ? libro.getNombreAutor() : "N/A",
                        libro.getNombreGenero() != null ? libro.getNombreGenero() : "N/A",
                        libro.getIsbn(),
                        libro.getPrecio()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar libros: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearLibro() {
        LibroFormDialog dialog = new LibroFormDialog(null, true, servicioAutor, servicioGenero);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            refrescar();
        }
    }

    private void editarLibro() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un libro para editar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int libroId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Libro libro = servicioLibro.obtenerPorId(libroId);

            if (libro != null) {
                LibroFormDialog dialog = new LibroFormDialog(libro, true, servicioAutor, servicioGenero);
                dialog.setVisible(true);

                if (dialog.isConfirmado()) {
                    refrescar();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo cargar el libro",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al editar libro: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarLibro() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un libro para eliminar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int libroId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String tituloLibro = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar el libro '" + tituloLibro + "'?\nEsta acción también eliminará todo el stock asociado.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = servicioLibro.eliminarLibro(libroId);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this,
                            "Libro eliminado exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    refrescar();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo eliminar el libro",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar libro: " + ex.getMessage(),
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
            List<Libro> libros = servicioLibro.obtenerTodos();
            String criterio = (String) comboCriterio.getSelectedItem();
            boolean encontrado = false;

            for (Libro libro : libros) {
                boolean coincide = false;

                switch (criterio) {
                    case "Todos":
                        coincide = libro.getTitulo().toLowerCase().contains(busqueda.toLowerCase()) ||
                                   libro.getIsbn().toLowerCase().contains(busqueda.toLowerCase()) ||
                                   (libro.getNombreAutor() != null && libro.getNombreAutor().toLowerCase().contains(busqueda.toLowerCase())) ||
                                   (libro.getNombreGenero() != null && libro.getNombreGenero().toLowerCase().contains(busqueda.toLowerCase()));
                        break;
                    case "Título":
                        coincide = libro.getTitulo().toLowerCase().contains(busqueda.toLowerCase());
                        break;
                    case "Autor":
                        coincide = libro.getNombreAutor() != null && 
                                   libro.getNombreAutor().toLowerCase().contains(busqueda.toLowerCase());
                        break;
                    case "Género":
                        coincide = libro.getNombreGenero() != null && 
                                   libro.getNombreGenero().toLowerCase().contains(busqueda.toLowerCase());
                        break;
                    case "ISBN":
                        coincide = libro.getIsbn().toLowerCase().contains(busqueda.toLowerCase());
                        break;
                }

                if (coincide) {
                    modeloTabla.addRow(new Object[]{
                            libro.getIdLibro(),
                            libro.getTitulo(),
                            libro.getNombreAutor() != null ? libro.getNombreAutor() : "N/A",
                            libro.getNombreGenero() != null ? libro.getNombreGenero() : "N/A",
                            libro.getIsbn(),
                            libro.getPrecio()
                    });
                    encontrado = true;
                }
            }

            if (!encontrado) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron libros con el criterio '" + criterio + "' que contenga: '" + busqueda + "'",
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