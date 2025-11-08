package gui.dialogs;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import servicios.ServicioAutor;
import servicios.ServicioGenero;
import servicios.ServicioLibro;
import ventas.Libro;
import ventas.Autor;
import ventas.Genero;

/**
 * Dialog para crear o editar un libro.
 */
public class LibroFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private boolean confirmado = false;
    private Libro libro;
    private ServicioAutor servicioAutor;
    private ServicioGenero servicioGenero;
    private ServicioLibro servicioLibro;

    private JTextField campoTitulo;
    private JComboBox<Autor> comboAutor;
    private JComboBox<Genero> comboGenero;
    private JTextField campoIsbn;
    private JTextField campoPrecio;
    private JTextField campoFecha;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public LibroFormDialog(Libro libro, boolean modal, ServicioAutor servicioAutor, ServicioGenero servicioGenero) {
        super((Frame) null, "Formulario de Libro", modal);
        this.libro = libro;
        this.servicioAutor = servicioAutor;
        this.servicioGenero = servicioGenero;
        this.servicioLibro = new ServicioLibro();

        inicializar();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void inicializar() {
        setSize(500, 400);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFormulario = crearPanelFormulario();
        this.add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = crearPanelBotones();
        this.add(panelBotones, BorderLayout.SOUTH);

        // Si es edición, cargar datos
        if (libro != null) {
            cargarDatos();
        }
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("Título:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panel.add(lblTitulo, gbc);

        campoTitulo = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(campoTitulo, gbc);

        // Autor
        JLabel lblAutor = new JLabel("Autor:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panel.add(lblAutor, gbc);

        comboAutor = new JComboBox<>();
        cargarAutores();
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(comboAutor, gbc);

        // Género
        JLabel lblGenero = new JLabel("Género:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        panel.add(lblGenero, gbc);

        comboGenero = new JComboBox<>();
        cargarGeneros();
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(comboGenero, gbc);

        // ISBN
        JLabel lblIsbn = new JLabel("ISBN:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        panel.add(lblIsbn, gbc);

        campoIsbn = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(campoIsbn, gbc);

        // Precio
        JLabel lblPrecio = new JLabel("Precio:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        panel.add(lblPrecio, gbc);

        campoPrecio = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(campoPrecio, gbc);

        // Fecha de Lanzamiento
        JLabel lblFecha = new JLabel("Fecha Lanzamiento (yyyy-MM-dd):");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        panel.add(lblFecha, gbc);

        campoFecha = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(campoFecha, gbc);

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

    private void cargarAutores() {
        try {
            List<Autor> autores = servicioAutor.obtenerTodos();
            for (Autor autor : autores) {
                comboAutor.addItem(autor);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar autores: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarGeneros() {
        try {
            List<Genero> generos = servicioGenero.obtenerTodos();
            for (Genero genero : generos) {
                comboGenero.addItem(genero);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar géneros: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatos() {
        if (libro == null) return;

        campoTitulo.setText(libro.getTitulo());
        campoIsbn.setText(libro.getIsbn());
        campoPrecio.setText(libro.getPrecio().toString());

        if (libro.getFechaLanzamiento() != null) {
            campoFecha.setText(libro.getFechaLanzamiento().toString());
        }

        // Seleccionar autor
        for (int i = 0; i < comboAutor.getItemCount(); i++) {
            Autor autor = comboAutor.getItemAt(i);
            if (autor.getIdAutor() == libro.getIdAutor()) {
                comboAutor.setSelectedIndex(i);
                break;
            }
        }

        // Seleccionar género
        for (int i = 0; i < comboGenero.getItemCount(); i++) {
            Genero genero = comboGenero.getItemAt(i);
            if (genero.getIdGenero() == libro.getIdGenero()) {
                comboGenero.setSelectedIndex(i);
                break;
            }
        }
    }

    private void guardar() {
        if (!validar()) {
            return;
        }

        try {
            Autor autorSeleccionado = (Autor) comboAutor.getSelectedItem();
            Genero generoSeleccionado = (Genero) comboGenero.getSelectedItem();

            if (autorSeleccionado == null || generoSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                        "Por favor seleccione un autor y un género",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Libro nuevoLibro = new Libro();
            if (libro != null) {
                nuevoLibro.setIdLibro(libro.getIdLibro());
            }
            nuevoLibro.setTitulo(campoTitulo.getText().trim());
            nuevoLibro.setIdAutor(autorSeleccionado.getIdAutor());
            nuevoLibro.setIdGenero(generoSeleccionado.getIdGenero());
            nuevoLibro.setIsbn(campoIsbn.getText().trim());
            nuevoLibro.setPrecio(new BigDecimal(campoPrecio.getText().trim()));

            String fechaStr = campoFecha.getText().trim();
            if (!fechaStr.isEmpty()) {
                nuevoLibro.setFechaLanzamiento(Date.valueOf(fechaStr));
            }

            boolean resultado;
            if (libro != null) {
                // Actualizar
                resultado = servicioLibro.actualizarLibro(nuevoLibro);
                if (resultado) {
                    JOptionPane.showMessageDialog(this,
                            "Libro actualizado exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Crear
                resultado = servicioLibro.crearLibro(nuevoLibro);
                if (resultado) {
                    JOptionPane.showMessageDialog(this,
                            "Libro creado exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }

            if (resultado) {
                confirmado = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar el libro",
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
        String titulo = campoTitulo.getText().trim();
        String isbn = campoIsbn.getText().trim();
        String precio = campoPrecio.getText().trim();

        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El título es obligatorio",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El ISBN es obligatorio",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (precio.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El precio es obligatorio",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            new BigDecimal(precio);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El precio debe ser un número válido",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String fecha = campoFecha.getText().trim();
        if (!fecha.isEmpty()) {
            try {
                Date.valueOf(fecha);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        "La fecha debe tener formato yyyy-MM-dd",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        return true;
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}
