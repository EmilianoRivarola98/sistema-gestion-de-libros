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
		String sql = "SELECT l.*, a.nombre AS nombre_autor, g.nombre AS nombre_genero FROM libro l " +
				"JOIN autor a ON l.id_autor = a.id_autor " +
				"JOIN genero g ON l.id_genero = g.id_genero " +
				"WHERE l.id_libro = ?";
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
						rs.getDate("fecha_lanzamiento"),
						rs.getString("nombre_autor"),
						rs.getString("nombre_genero")
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
		String sql = "SELECT l.*, a.nombre AS nombre_autor, g.nombre AS nombre_genero FROM libro l " +
				"JOIN autor a ON l.id_autor = a.id_autor " +
				"JOIN genero g ON l.id_genero = g.id_genero " +
				"WHERE LOWER(l.titulo) LIKE ?";

		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
			stmt.setString(1, "%" + titulo.toLowerCase() + "%");
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
			System.err.println("Error al buscar libros por título: " + e.getMessage());
		}

		return libros;
	}

	public int crearLibro(Libro libro) {
		String sql = "INSERT INTO libro (titulo, id_autor, id_genero, isbn, precio, fecha_lanzamiento) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, libro.getTitulo());
			stmt.setInt(2, libro.getIdAutor());
			stmt.setInt(3, libro.getIdGenero());
			stmt.setString(4, libro.getIsbn());
			stmt.setBigDecimal(5, libro.getPrecio());
			stmt.setDate(6, new java.sql.Date(libro.getFechaLanzamiento().getTime()));
			int filas = stmt.executeUpdate();
			if (filas > 0) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al crear libro: " + e.getMessage());
		}
		return -1;
	}

	public boolean actualizarLibro(Libro libro) {
		String sql = "UPDATE libro SET titulo = ?, id_autor = ?, id_genero = ?, isbn = ?, precio = ?, fecha_lanzamiento = ? WHERE id_libro = ?";
		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
			stmt.setString(1, libro.getTitulo());
			stmt.setInt(2, libro.getIdAutor());
			stmt.setInt(3, libro.getIdGenero());
			stmt.setString(4, libro.getIsbn());
			stmt.setBigDecimal(5, libro.getPrecio());
			stmt.setDate(6, new java.sql.Date(libro.getFechaLanzamiento().getTime()));
			stmt.setInt(7, libro.getIdLibro());
			int filas = stmt.executeUpdate();
			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al actualizar libro: " + e.getMessage());
			return false;
		}
	}

	public boolean eliminarLibro(int idLibro) {
		String deleteStockSql = "DELETE FROM stock WHERE id_libro = ?";
		String deleteLibroSql = "DELETE FROM libro WHERE id_libro = ?";
		try {
			conexion.setAutoCommit(false);
			try (PreparedStatement deleteStockStmt = conexion.prepareStatement(deleteStockSql)) {
				deleteStockStmt.setInt(1, idLibro);
				deleteStockStmt.executeUpdate();
			}
			try (PreparedStatement deleteLibroStmt = conexion.prepareStatement(deleteLibroSql)) {
				deleteLibroStmt.setInt(1, idLibro);
				int filas = deleteLibroStmt.executeUpdate();
				conexion.commit();
				return filas > 0;
			}
		} catch (SQLException e) {
			System.err.println("Error al eliminar libro: " + e.getMessage());
			try {
				conexion.rollback();
			} catch (SQLException rollbackEx) {
				System.err.println("Error al hacer rollback: " + rollbackEx.getMessage());
			}
			return false;
		} finally {
			try {
				conexion.setAutoCommit(true);
			} catch (SQLException e) {
				System.err.println("Error al restaurar auto-commit: " + e.getMessage());
			}
		}
	}

	public List<Libro> obtenerTodos() {
		List<Libro> libros = new ArrayList<>();
		String sql = "SELECT l.*, a.nombre AS nombre_autor, g.nombre AS nombre_genero FROM libro l " +
				"JOIN autor a ON l.id_autor = a.id_autor " +
				"JOIN genero g ON l.id_genero = g.id_genero ORDER BY l.titulo";

		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
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
			System.err.println("Error al obtener todos los libros: " + e.getMessage());
		}

		return libros;
	}
}
