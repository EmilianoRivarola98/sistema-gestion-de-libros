package dao;

import conexion.Conexion;
import ventas.Promocion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromocionDAO {
    private Connection conexion;

    public PromocionDAO() {
        this.conexion = Conexion.getInstance().getConnection();
    }

    public boolean crear(Promocion promocion) {
        String sql = "INSERT INTO promocion (nombre, descripcion, descuento) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, promocion.getNombre());
            stmt.setString(2, promocion.getDescripcion());
            stmt.setBigDecimal(3, promocion.getDescuento());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                promocion.setIdPromocion(rs.getInt(1));
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al crear promoción: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Promocion promocion) {
        String sql = "UPDATE promocion SET nombre = ?, descripcion = ?, descuento = ? WHERE id_promocion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, promocion.getNombre());
            stmt.setString(2, promocion.getDescripcion());
            stmt.setBigDecimal(3, promocion.getDescuento());
            stmt.setInt(4, promocion.getIdPromocion());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar promoción: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM promocion WHERE id_promocion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar promoción: " + e.getMessage());
            return false;
        }
    }

    public Promocion obtenerPorId(int id) {
        String sql = "SELECT id_promocion, nombre, descripcion, descuento FROM promocion WHERE id_promocion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Promocion(
                        rs.getInt("id_promocion"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBigDecimal("descuento")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener promoción: " + e.getMessage());
        }
        return null;
    }

    public List<Promocion> obtenerTodas() {
        List<Promocion> promociones = new ArrayList<>();
        String sql = "SELECT id_promocion, nombre, descripcion, descuento FROM promocion ORDER BY nombre";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                promociones.add(new Promocion(
                        rs.getInt("id_promocion"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBigDecimal("descuento")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener promociones: " + e.getMessage());
        }
        return promociones;
    }
}
