package ventas;

import java.math.BigDecimal;

public class Promocion {
    private int idPromocion;
    private String nombre;
    private String descripcion;
    private BigDecimal descuento;

    public Promocion(int idPromocion, String nombre, String descripcion, BigDecimal descuento) {
        this.idPromocion = idPromocion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
    }

    public Promocion(String nombre, String descripcion, BigDecimal descuento) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
    }

    // Getters y Setters
    public int getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(int idPromocion) {
        this.idPromocion = idPromocion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    @Override
    public String toString() {
        return nombre + " (" + String.format("%.2f%%", descuento) + ")";
    }
}
