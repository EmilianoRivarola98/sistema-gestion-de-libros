package servicios;

import dao.StockDAO;
import dao.LibroDAO;
import ventas.Libro;
import javax.swing.*;
import java.util.List;

public class ServicioStock {
    private StockDAO stockDAO;
    private LibroDAO libroDAO;

    public ServicioStock() {
        this.stockDAO = new StockDAO();
        this.libroDAO = new LibroDAO();
    }

    public void consultarStock(int idSucursal) {
        String[] opciones = {"Por ID de libro", "Ver todo el stock"};
        int opcion = JOptionPane.showOptionDialog(null,
                "Seleccione una opción",
                "Consultar stock",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (opcion == 0) {
            consultarStockPorLibro(idSucursal);
        } else if (opcion == 1) {
            listarStockSucursal(idSucursal);
        }
    }

    private void consultarStockPorLibro(int idSucursal) {
        try {
            int idLibro = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID del libro:"));
            Libro libro = libroDAO.obtenerLibroPorId(idLibro);

            if (libro == null) {
                JOptionPane.showMessageDialog(null, "Libro no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Integer cantidad = stockDAO.obtenerStockPorLibro(idLibro, idSucursal);
            if (cantidad == null) {
                JOptionPane.showMessageDialog(null, "No hay stock registrado para este libro en la sucursal.", "Sin stock", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Libro: " + libro.getTitulo() + "\n" +
                        "Stock disponible: " + cantidad,
                        "Consulta de Stock",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarStockSucursal(int idSucursal) {
        List<String> lista = stockDAO.listarStockPorSucursal(idSucursal);

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay registros de stock en esta sucursal.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Stock de la Sucursal:\n\n");
        for (String item : lista) {
            sb.append(item).append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new java.awt.Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scroll, "Stock disponible", JOptionPane.INFORMATION_MESSAGE);
    }
}
