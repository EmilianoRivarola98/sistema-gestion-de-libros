package dao;

import conexion.Conexion;
import ventas.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    private Connection conexion;

    public LibroDAO() {
        this.conexion = Conexion.getInstance().getConnection();
    }

    // libro por ID
    public Libro obtenerLibroPorId(int idLibro) {
        String sql = "SELECT * FROM libro WHERE id_libro = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idLibro);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Libro(
                    rs.getInt("id_libro"),
                    rs.getString("titulo"),
                    rs.getInt("id_autor"),
                    rs.getInt("id_genero"),
                    rs.getString("isbn"),
                    rs.getBigDecimal("precio"),
                    rs.getDate("fecha_lanzamiento")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libro por ID: " + e.getMessage());
        }
        return null;
    }

    // libros por título
    public List<Libro> buscarLibrosPorTitulo(String titulo) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libro WHERE titulo LIKE ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + titulo + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                libros.add(new Libro(
                    rs.getInt("id_libro"),
                    rs.getString("titulo"),
                    rs.getInt("id_autor"),
                    rs.getInt("id_genero"),
                    rs.getString("isbn"),
                    rs.getBigDecimal("precio"),
                    rs.getDate("fecha_lanzamiento")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar libros por título: " + e.getMessage());
        }

        return libros;
    }
}
