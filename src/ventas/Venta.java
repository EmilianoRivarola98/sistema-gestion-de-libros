package ventas;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Venta {
    private int idVenta;
    private int idUsuario;
    private int idForma;
    private Date fecha;
    private BigDecimal total;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private List<ItemVenta> items;

    // Constructor para insertar una nueva venta
    public Venta(int idUsuario, int idForma, BigDecimal total, BigDecimal subtotal, BigDecimal descuento) {
        this.idUsuario = idUsuario;
        this.idForma = idForma;
        this.total = total;
        this.subtotal = subtotal;
        this.descuento = descuento;
    }

    // Constructor  al obtener una venta de la BD
    public Venta(int idVenta, int idUsuario, int idForma, Date fecha, BigDecimal total, BigDecimal subtotal, BigDecimal descuento) {
        this.idVenta = idVenta;
        this.idUsuario = idUsuario;
        this.idForma = idForma;
        this.fecha = fecha;
        this.total = total;
        this.subtotal = subtotal;
        this.descuento = descuento;
    }

    // Getters y Setters
    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdForma() {
        return idForma;
    }

    public void setIdForma(int idForma) {
        this.idForma = idForma;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public List<ItemVenta> getItems() {
        return items;
    }

    public void setItems(List<ItemVenta> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", idUsuario=" + idUsuario +
                ", idForma=" + idForma +
                ", fecha=" + fecha +
                ", total=" + total +
                ", subtotal=" + subtotal +
                ", descuento=" + descuento +
                ", items=" + items +
                '}';
    }
}
