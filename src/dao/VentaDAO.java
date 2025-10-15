package dao;

import conexion.Conexion;
import ventas.Venta;
import ventas.FormaPago;
import ventas.ItemVenta;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {
    private Connection conexion;

    public VentaDAO() {
        this.conexion = Conexion.getInstance().getConnection();
    }

    
    // Crear venta

    public boolean crearVenta(Venta venta, List<ItemVenta> items) {
        String sqlVenta = "INSERT INTO venta (id_usuario, id_forma, fecha, total, subtotal, descuento) VALUES (?, ?, NOW(), ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_libro, cantidad, precio_unitario, descuento_aplicado) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmtVenta = conexion.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtDetalle = conexion.prepareStatement(sqlDetalle)) {

            
        	 stmtVenta.setInt(1, venta.getIdUsuario());
             stmtVenta.setInt(2, venta.getIdForma());
             stmtVenta.setBigDecimal(3, venta.getTotal());
             stmtVenta.setBigDecimal(4, venta.getSubtotal());
             stmtVenta.setBigDecimal(5, venta.getDescuento());
             stmtVenta.executeUpdate();

            ResultSet rs = stmtVenta.getGeneratedKeys();
            int idVenta = 0;
            if (rs.next()) idVenta = rs.getInt(1);
            venta.setIdVenta(idVenta);

            // agrega el detalle_venta
            for (ItemVenta item : items) {
                stmtDetalle.setInt(1, idVenta);
                stmtDetalle.setInt(2, item.getIdLibro());
                stmtDetalle.setInt(3, item.getCantidad());
                stmtDetalle.setBigDecimal(4, item.getPrecioUnitario());
                stmtDetalle.setBigDecimal(5, item.getDescuentoAplicado());
                stmtDetalle.addBatch();
            }

            stmtDetalle.executeBatch();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al crear venta: " + e.getMessage());
            return false;
        }
    }

  
    // Listar ventas
    
    public List<Venta> obtenerTodasLasVentas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM venta ORDER BY fecha DESC";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ventas.add(mapearResultSetAVenta(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas: " + e.getMessage());
        }
        return ventas;
    }
    

    // Obtener detalle de una venta

    public List<ItemVenta> obtenerDetallesPorVenta(int idVenta) {
        List<ItemVenta> items = new ArrayList<>();
        String sql = "SELECT * FROM detalle_venta WHERE id_venta = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ItemVenta item = new ItemVenta(
                    rs.getInt("id_libro"),
                    rs.getInt("cantidad"),
                    rs.getBigDecimal("precio_unitario"),
                    rs.getBigDecimal("descuento_aplicado")
                );
                items.add(item);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener detalles de venta: " + e.getMessage());
        }

        return items;
    }

	


    public boolean eliminarVenta(int id) {
        String sqlDetalleVenta = "DELETE FROM detalle_venta WHERE id_venta = ?";
        String sqlVenta = "DELETE FROM venta WHERE id_venta = ?";

        try (
           
            PreparedStatement stmtDetalle = conexion.prepareStatement(sqlDetalleVenta);
            PreparedStatement stmtVenta = conexion.prepareStatement(sqlVenta)
        ) {

            stmtDetalle.setInt(1, id);
            stmtDetalle.executeUpdate();


            stmtVenta.setInt(1, id);
            int ventaEliminada = stmtVenta.executeUpdate();

            if (ventaEliminada > 0) {
                System.out.println("Venta eliminada correctamente (id=" + id + ")");
                return true;
            } else {
                System.err.println("No se encontró la venta con id=" + id);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar venta: " + e.getMessage());
            return false;
        }
    }

	
	
    public List<FormaPago> obtenerFormasDePago() {
        List<FormaPago> formasDePago = new ArrayList<>();
        String query = "SELECT id_forma, descripcion FROM forma_pago"; 

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idFormaPago = rs.getInt("id_forma");
                String descripcion = rs.getString("descripcion");
                formasDePago.add(new FormaPago(idFormaPago, descripcion));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return formasDePago;
    }
    
    public Venta obtenerVentaConDetallesPorId(int idVenta) {
        String sql = "SELECT v.id_venta, v.id_usuario, v.id_forma, v.fecha, v.subtotal, v.descuento, v.total, " +
                     "d.id_libro, d.cantidad, d.precio_unitario, d.descuento_aplicado " +
                     "FROM venta v " +
                     "LEFT JOIN detalle_venta d ON v.id_venta = d.id_venta " +
                     "WHERE v.id_venta = ?";

        List<ItemVenta> items = new ArrayList<>();
        Venta venta = null; 

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);
            ResultSet rs = stmt.executeQuery();

            boolean ventaEncontrada = false;

            while (rs.next()) {
                if (!ventaEncontrada) {
                    venta = mapearResultSetAVenta(rs); // 
                    ventaEncontrada = true;
                }

                int idLibro = rs.getInt("id_libro");
                if (idLibro != 0) {
                    ItemVenta item = new ItemVenta(
                        idLibro,
                        rs.getInt("cantidad"),
                        rs.getBigDecimal("precio_unitario"),
                        rs.getBigDecimal("descuento_aplicado")
                    );
                    items.add(item);
                }
            }

            if (venta != null) {
                venta.setItems(items);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener venta con detalles: " + e.getMessage());
        }

        return venta;
    }

    private Venta mapearResultSetAVenta(ResultSet rs) throws SQLException {
        return new Venta(
            rs.getInt("id_venta"),
            rs.getInt("id_usuario"),
            rs.getInt("id_forma"),
            rs.getTimestamp("fecha"),
            rs.getBigDecimal("total"),
            rs.getBigDecimal("subtotal"),
            rs.getBigDecimal("descuento")
        );
    }
 
}



