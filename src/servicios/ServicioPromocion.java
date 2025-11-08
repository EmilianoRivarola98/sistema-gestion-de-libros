package servicios;

import dao.PromocionDAO;
import ventas.Promocion;
import java.math.BigDecimal;
import java.util.List;

public class ServicioPromocion {
    private PromocionDAO promocionDAO;

    public ServicioPromocion() {
        this.promocionDAO = new PromocionDAO();
    }

    // Métodos GUI-compatible
    public boolean crearPromocionGUI(String nombre, String descripcion, BigDecimal descuento) {
        if (nombre == null || nombre.trim().isEmpty() || 
            descripcion == null || descripcion.trim().isEmpty() || 
            descuento == null || descuento.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        Promocion promocion = new Promocion(nombre, descripcion, descuento);
        return promocionDAO.crear(promocion);
    }

    public boolean actualizarPromocionGUI(int id, String nombre, String descripcion, BigDecimal descuento) {
        if (id <= 0 || nombre == null || nombre.trim().isEmpty() || 
            descripcion == null || descripcion.trim().isEmpty() || 
            descuento == null || descuento.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        Promocion promocion = new Promocion(id, nombre, descripcion, descuento);
        return promocionDAO.actualizar(promocion);
    }

    public boolean eliminarPromocionGUI(int id) {
        return promocionDAO.eliminar(id);
    }

    public Promocion obtenerPorId(int id) {
        return promocionDAO.obtenerPorId(id);
    }

    public List<Promocion> obtenerTodas() {
        return promocionDAO.obtenerTodas();
    }
}
