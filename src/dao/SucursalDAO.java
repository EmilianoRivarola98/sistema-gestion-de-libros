package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import conexion.Conexion;
import ventas.Sucursal;

public class SucursalDAO {
	private Connection conexion;

	public SucursalDAO() {
		this.conexion = Conexion.getInstance().getConnection();
	}

	public Sucursal crearSucursal(Sucursal sucursal) {
		String sql = "INSERT INTO sucursal (nombre, direccion) VALUES (?, ?)";
		try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, sucursal.getNombre());
			stmt.setString(2, sucursal.getDireccion());

			int filasAfectadas = stmt.executeUpdate();
			if (filasAfectadas > 0) {
				ResultSet generatedKeys = stmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					sucursal.setId(generatedKeys.getInt(1));
					return sucursal;
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al crear la sucursal: " + e.getMessage());
		}
		return null;
	}

	public Sucursal obtenerPorId(int id) {
		String sql = "SELECT * FROM sucursal WHERE id_sucursal = ?";
		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new Sucursal(
						rs.getInt("id_sucursal"),
						rs.getString("nombre"),
						rs.getString("direccion")
						);
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener sucursal por ID: " + e.getMessage());
		}
		return null;
	}

	public List<Sucursal> obtenerTodas() {
		List<Sucursal> sucursales = new ArrayList<>();
		String sql = "SELECT * FROM sucursal ORDER BY nombre";
		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				sucursales.add(new Sucursal(
						rs.getInt("id_sucursal"),
						rs.getString("nombre"),
						rs.getString("direccion")
						));
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener sucursales: " + e.getMessage());
		}
		return sucursales;
	}
}
