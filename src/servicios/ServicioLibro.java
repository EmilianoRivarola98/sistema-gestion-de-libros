package servicios;

import dao.LibroDAO;
import ventas.Libro;

import javax.swing.*;
import java.util.List;

public class ServicioLibro {
    private LibroDAO libroDAO;

    public ServicioLibro() {
        this.libroDAO = new LibroDAO();
    }

    public void buscarLibro() {
        String[] opciones = {"Buscar por ID", "Buscar por título"};
        int seleccion = JOptionPane.showOptionDialog(
                null,
                "Seleccione el tipo de búsqueda:",
                "Buscar libro",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion == 0) {
            buscarPorId();
        } else if (seleccion == 1) {
            buscarPorTitulo();
        }
    }
    
    private void mostrarResultado(String texto, String tituloVentana) {
        JTextArea textArea = new JTextArea(texto);
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new java.awt.Dimension(500, 400));
        JOptionPane.showMessageDialog(null, scroll, tituloVentana, JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarPorId() {
        try {
            int idLibro = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del libro:"));
            Libro libro = libroDAO.obtenerLibroPorId(idLibro);

            if (libro != null) {
                mostrarResultado(libro.getDetalleFormateado(), "Resultado de búsqueda");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un libro con ese ID.", "Sin resultados", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void buscarPorTitulo() {
        String titulo = JOptionPane.showInputDialog("Ingrese parte o todo el título del libro:");
        if (titulo == null || titulo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un título.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Libro> resultados = libroDAO.buscarLibrosPorTitulo(titulo);

        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron libros con ese título.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder("Detalle del libro:\n\n");
            for (Libro libro : resultados) {
                sb.append(libro.getDetalleFormateado());
            }
            mostrarResultado(sb.toString(), "Resultados de búsqueda");
        }
    }
    
    
    
}
