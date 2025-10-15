package dao;

import conexion.Conexion;
import ventas.Autor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {
	private Connection conexion;

	public AutorDAO() {
		this.conexion = Conexion.getInstance().getConnection();
	}

	public List<Autor> obtenerTodos() {
		List<Autor> autores = new ArrayList<>();
		String sql = "SELECT * FROM autor";
		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				autores.add(new Autor(rs.getInt("id_autor"), rs.getString("nombre")));
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener autores: " + e.getMessage());
		}
		return autores;
	}

	public boolean crearAutor(String nombre) {
		String sql = "INSERT INTO autor (nombre) VALUES (?)";
		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
			stmt.setString(1, nombre);
			int filas = stmt.executeUpdate();
			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al crear autor: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarAutor(int idAutor, String nombre) {
		String sql = "UPDATE autor SET nombre = ? WHERE id_autor = ?";
		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
			stmt.setString(1, nombre);
			stmt.setInt(2, idAutor);
			int filas = stmt.executeUpdate();
			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al actualizar autor: " + e.getMessage());
			return false;
		}
	}

	public boolean eliminarAutor(int idAutor) {
		String sql = "DELETE FROM autor WHERE id_autor = ?";
		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
			stmt.setInt(1, idAutor);
			int filas = stmt.executeUpdate();
			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al eliminar autor: " + e.getMessage());
			return false;
		}
	}
}
