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

    public List<Libro> obtenerLibrosConStockPorSucursal(int idSucursal) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT l.*, a.nombre AS nombre_autor, g.nombre AS nombre_genero FROM libro l " +
                     "JOIN autor a ON l.id_autor = a.id_autor " +
                     "JOIN genero g ON l.id_genero = g.id_genero " +
                     "JOIN stock s ON l.id_libro = s.id_libro " +
                     "WHERE s.id_sucursal = ? ORDER BY l.titulo";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idSucursal);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                libros.add(new Libro(
                    rs.getInt("id_libro"),
                    rs.getString("titulo"),
                    rs.getInt("id_autor"),
                    rs.getInt("id_genero"),
                    rs.getString("isbn"),
                    rs.getBigDecimal("precio"),
                    rs.getDate("fecha_lanzamiento"),
                    rs.getString("nombre_autor"),
                    rs.getString("nombre_genero")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libros con stock: " + e.getMessage());
        }

                return libros;

            }

        

            public List<String> buscarStockPorNombreLibroEnSucursal(String nombre, int idSucursal) {

                List<String> lista = new ArrayList<>();

                String sql = "SELECT l.titulo, s.cantidad FROM stock s " +

                             "JOIN libro l ON s.id_libro = l.id_libro " +

                             "WHERE s.id_sucursal = ? AND LOWER(l.titulo) LIKE ?";

                try (PreparedStatement stmt = conexion.prepareStatement(sql)) {

                    stmt.setInt(1, idSucursal);

                    stmt.setString(2, "%" + nombre.toLowerCase() + "%");

                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {

                        lista.add("Título: " + rs.getString("titulo") + " | Stock: " + rs.getInt("cantidad"));

                    }

                } catch (SQLException e) {

                    System.err.println("Error al buscar stock por nombre: " + e.getMessage());

                }

                return lista;

            }

        

            public String buscarStockPorIdLibroEnSucursal(int idLibro, int idSucursal) {

                String sql = "SELECT l.titulo, s.cantidad FROM stock s " +

                             "JOIN libro l ON s.id_libro = l.id_libro " +

                             "WHERE s.id_sucursal = ? AND s.id_libro = ?";

                try (PreparedStatement stmt = conexion.prepareStatement(sql)) {

                    stmt.setInt(1, idSucursal);

                    stmt.setInt(2, idLibro);

                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {

                        return "Título: " + rs.getString("titulo") + " | Stock: " + rs.getInt("cantidad");

                    }

                } catch (SQLException e) {

                    System.err.println("Error al buscar stock por ID: " + e.getMessage());

                }

                return null;

            }

        

            public String buscarStockPorIsbnLibroEnSucursal(String isbn, int idSucursal) {

                String sql = "SELECT l.titulo, s.cantidad FROM stock s " +

                             "JOIN libro l ON s.id_libro = l.id_libro " +

                             "WHERE s.id_sucursal = ? AND l.isbn = ?";

                try (PreparedStatement stmt = conexion.prepareStatement(sql)) {

                    stmt.setInt(1, idSucursal);

                    stmt.setString(2, isbn);

                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {

                        return "Título: " + rs.getString("titulo") + " | Stock: " + rs.getInt("cantidad");

                    }

                } catch (SQLException e) {

                    System.err.println("Error al buscar stock por ISBN: " + e.getMessage());

                }

                return null;

            }

        

            public boolean disminuirStock(int idLibro, int idSucursal, int cantidad) {
        String sql = "UPDATE stock SET cantidad = cantidad - ? WHERE id_libro = ? AND id_sucursal = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, cantidad);
            stmt.setInt(2, idLibro);
            stmt.setInt(3, idSucursal);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al disminuir stock: " + e.getMessage());
            return false;
        }
    }
    
    public boolean agregarStock(int idLibro, int idSucursal, int cantidad) {
        Integer stockActual = obtenerStockPorLibro(idLibro, idSucursal);
        if (stockActual != null) {
            // Stock exists, update it
            String sql = "UPDATE stock SET cantidad = cantidad + ? WHERE id_libro = ? AND id_sucursal = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setInt(1, cantidad);
                stmt.setInt(2, idLibro);
                stmt.setInt(3, idSucursal);
                int filas = stmt.executeUpdate();
                return filas > 0;
            } catch (SQLException e) {
                System.err.println("Error al agregar stock: " + e.getMessage());
                return false;
            }
        } else {
            // Stock does not exist, insert it
            String sql = "INSERT INTO stock (id_libro, id_sucursal, cantidad) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setInt(1, idLibro);
                stmt.setInt(2, idSucursal);
                stmt.setInt(3, cantidad);
                int filas = stmt.executeUpdate();
                return filas > 0;
            } catch (SQLException e) {
                System.err.println("Error al agregar stock: " + e.getMessage());
                return false;
            }
        }
    }

    public boolean modificarStock(int idLibro, int idSucursal, int cantidad) {
        String sql = "UPDATE stock SET cantidad = ? WHERE id_libro = ? AND id_sucursal = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, cantidad);
            stmt.setInt(2, idLibro);
            stmt.setInt(3, idSucursal);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar stock: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarStock(int idLibro, int idSucursal) {
        String sql = "DELETE FROM stock WHERE id_libro = ? AND id_sucursal = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idLibro);
            stmt.setInt(2, idSucursal);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar stock: " + e.getMessage());
            return false;
        }
    }
}

