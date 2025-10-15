package dao;

import conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ventas.Libro;

public class StockDAO {
    private Connection conexion;

    public StockDAO() {
        this.conexion = Conexion.getInstance().getConnection();
    }

    // Consultar stock por libro
    public Integer obtenerStockPorLibro(int idLibro, int idSucursal) {
        String sql = "SELECT cantidad FROM stock WHERE id_libro = ? AND id_sucursal = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idLibro);
            stmt.setInt(2, idSucursal);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cantidad");
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar stock: " + e.getMessage());
        }
        return null; 
    }

    //  stock de la sucursal
    public List<String> listarStockPorSucursal(int idSucursal) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT l.titulo, s.cantidad FROM stock s " +
                     "JOIN libro l ON s.id_libro = l.id_libro " +
                     "WHERE s.id_sucursal = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idSucursal);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add("Título: " + rs.getString("titulo") + " | Stock: " + rs.getInt("cantidad"));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar stock: " + e.getMessage());
        }
        return lista;
    }
    public boolean restarStock(int idLibro, int idSucursal, int cantidad) {
        String sql = "UPDATE stock SET cantidad = cantidad - ? WHERE id_libro = ? AND id_sucursal = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, cantidad);
            stmt.setInt(2, idLibro);
            stmt.setInt(3, idSucursal);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al restar stock: " + e.getMessage());
            return false;
        }
    }
}

