package dao;

import conexion.Conexion;
import ventas.Genero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GeneroDAO {
	private Connection conexion;

	public GeneroDAO() {
		this.conexion = Conexion.getInstance().getConnection();
	}

	public List<Genero> obtenerTodos() {
		List<Genero> generos = new ArrayList<>();
		String sql = "SELECT * FROM genero";
		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				generos.add(new Genero(rs.getInt("id_genero"), rs.getString("nombre")));
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener generos: " + e.getMessage());
		}
		return generos;
	}

	public boolean crearGenero(String nombre) {
		String sql = "INSERT INTO genero (nombre) VALUES (?)";
		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
			stmt.setString(1, nombre);
			int filas = stmt.executeUpdate();
			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al crear genero: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarGenero(int idGenero, String nombre) {
		String sql = "UPDATE genero SET nombre = ? WHERE id_genero = ?";
		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
			stmt.setString(1, nombre);
			stmt.setInt(2, idGenero);
			int filas = stmt.executeUpdate();
			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al actualizar genero: " + e.getMessage());
			return false;
		}
	}

	public boolean eliminarGenero(int idGenero) {
		String sql = "DELETE FROM genero WHERE id_genero = ?";
		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
			stmt.setInt(1, idGenero);
			int filas = stmt.executeUpdate();
			return filas > 0;
		} catch (SQLException e) {
			System.err.println("Error al eliminar genero: " + e.getMessage());
			return false;
		}
	}
}
