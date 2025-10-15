package servicios;

import dao.LibroDAO;
import dao.StockDAO;
import dao.VentaDAO;
import ventas.Venta;
import ventas.ItemVenta;
import ventas.Libro;
import ventas.FormaPago;
import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ServicioVenta {

    private VentaDAO ventaDAO;
    private LibroDAO libroDAO;
    private StockDAO stockDAO;
    
    public ServicioVenta() {
        this.ventaDAO = new VentaDAO();
        this.libroDAO = new LibroDAO();
        this.stockDAO = new StockDAO();

    }

    public void crearVenta(int idUsuarioLogueado, int idSucursal) {
        try {
            BigDecimal subtotal = BigDecimal.ZERO;
            BigDecimal descuentoTotal = BigDecimal.ZERO;
            List<ItemVenta> items = new ArrayList<>();
            boolean agregarMas = true;

            do {
                String idLibroStr = JOptionPane.showInputDialog("Ingrese ID del libro:");

                
                if (idLibroStr == null || idLibroStr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Operación cancelada.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    return; 
                }

                try {
                    int idLibro = Integer.parseInt(idLibroStr);
                    Libro libro = libroDAO.obtenerLibroPorId(idLibro);

                    if (libro == null) {
                        JOptionPane.showMessageDialog(null, "Libro no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    String mensajeLibro = "Título: " + libro.getTitulo() + "\nPrecio: " + libro.getPrecio();
                    JOptionPane.showMessageDialog(null, mensajeLibro, "Detalles del Libro", JOptionPane.INFORMATION_MESSAGE);

                    String cantidadStr = JOptionPane.showInputDialog("Ingrese cantidad:");

                    if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Operación cancelada.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        return; 
                    }

                    try {
                        int cantidad = Integer.parseInt(cantidadStr);

                        // Verificar stock en la sucursal
                        Integer stockDisponible = stockDAO.obtenerStockPorLibro(idLibro, idSucursal);
                        if (stockDisponible == null || stockDisponible < cantidad) {
                            JOptionPane.showMessageDialog(null, "No hay suficiente stock para este libro.", "Error", JOptionPane.ERROR_MESSAGE);
                            continue; 
                        }

                        BigDecimal precioUnitario = libro.getPrecio();
                        BigDecimal descuento = BigDecimal.ZERO;  // Sin descuento por ahora
                        subtotal = subtotal.add(precioUnitario.multiply(BigDecimal.valueOf(cantidad)));
                        descuentoTotal = descuentoTotal.add(descuento.multiply(BigDecimal.valueOf(cantidad)));

                        items.add(new ItemVenta(idLibro, cantidad, precioUnitario, descuento));

                        int respuesta = JOptionPane.showConfirmDialog(
                                null,
                                "¿Desea agregar otro libro?",
                                "Agregar ítem",
                                JOptionPane.YES_NO_OPTION
                        );
                        agregarMas = (respuesta == JOptionPane.YES_OPTION);

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Cantidad inválida. Debe ingresar un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "ID del libro inválido. Debe ingresar un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } while (agregarMas);

            List<FormaPago> formasDePago = ventaDAO.obtenerFormasDePago();
            String[] metodosPago = new String[formasDePago.size()];
            for (int i = 0; i < formasDePago.size(); i++) {
                metodosPago[i] = formasDePago.get(i).getDescripcion();
            }

            String metodoSeleccionado = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione el método de pago:",
                    "Método de Pago",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    metodosPago,
                    metodosPago[0]
            );

            if (metodoSeleccionado == null) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un método de pago.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idForma = 0;
            for (FormaPago forma : formasDePago) {
                if (forma.getDescripcion().equals(metodoSeleccionado)) {
                    idForma = forma.getIdFormaPago();
                    break;
                }
            }

            // Crear la venta
            BigDecimal total = subtotal.subtract(descuentoTotal);
            Venta venta = new Venta(idUsuarioLogueado, idForma, total, subtotal, descuentoTotal);

            boolean resultado = ventaDAO.crearVenta(venta, items);

            if (resultado) {
                for (ItemVenta item : items) {
                    boolean restarExito = stockDAO.restarStock(item.getIdLibro(), idSucursal, item.getCantidad());
                    if (!restarExito) {
                        JOptionPane.showMessageDialog(null, "Error al actualizar el stock del libro: " + item.getIdLibro(), "Error", JOptionPane.ERROR_MESSAGE);
                        return;  
                    }
                }
                JOptionPane.showMessageDialog(null, "Venta registrada y stock actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar la venta.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en los datos ingresados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al crear la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void mostrarListaVentas() {
        List<Venta> ventas = obtenerTodasLasVentas();
        

        if (ventas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay ventas registradas", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

     
        StringBuilder sb = new StringBuilder();
        sb.append("Lista de Ventas (Total: ").append(ventas.size()).append(")\n\n");

       
        for (Venta venta : ventas) {
            sb.append("Venta ID: ").append(venta.getIdVenta()).append("\n");
            sb.append("Usuario ID: ").append(venta.getIdUsuario()).append("\n");
            sb.append("Forma de Pago ID: ").append(venta.getIdForma()).append("\n");
            sb.append("Fecha: ").append(venta.getFecha()).append("\n");
            sb.append("Subtotal: $").append(venta.getSubtotal()).append("\n");
            sb.append("Descuento: $").append(venta.getDescuento()).append("\n");
            sb.append("Total: $").append(venta.getTotal()).append("\n");
            sb.append("----------------------------------------\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new java.awt.Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scroll, "Lista de Ventas", JOptionPane.INFORMATION_MESSAGE);
    }

    // obtener todas las ventas
    public List<Venta> obtenerTodasLasVentas() {
        return ventaDAO.obtenerTodasLasVentas();
    }
    
    public void eliminarVenta() {
        try {
            String idVentaStr = JOptionPane.showInputDialog("Ingrese el ID de la venta a anular:");
            if (idVentaStr == null || idVentaStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un ID de venta válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idVenta = Integer.parseInt(idVentaStr);
            Venta venta = ventaDAO.obtenerVentaConDetallesPorId(idVenta);

            if (venta == null) {
                JOptionPane.showMessageDialog(null, "Venta no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mostrar detalles antes de confirmar
            StringBuilder detalle = new StringBuilder();
            detalle.append("Venta ID: ").append(venta.getIdVenta()).append("\n");
            detalle.append("Usuario ID: ").append(venta.getIdUsuario()).append("\n");
            detalle.append("Forma de Pago ID: ").append(venta.getIdForma()).append("\n");
            detalle.append("Fecha: ").append(venta.getFecha()).append("\n");
            detalle.append("Subtotal: $").append(venta.getSubtotal()).append("\n");
            detalle.append("Descuento: $").append(venta.getDescuento()).append("\n");
            detalle.append("Total: $").append(venta.getTotal()).append("\n\n");
            detalle.append("¿Desea anular esta venta?");

            int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    detalle.toString(),
                    "Confirmar anulación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean exito = ventaDAO.eliminarVenta(idVenta);
                if (exito) {
                    JOptionPane.showMessageDialog(null, "Venta anulada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al anular la venta.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al anular la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }
