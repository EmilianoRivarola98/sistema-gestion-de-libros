package ventas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemVenta {
    private int idDetalle;
    private int idVenta;
    private int idLibro;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuentoAplicado;
    private List<ItemVenta> items = new ArrayList<>();


    public ItemVenta(int idLibro, int cantidad, BigDecimal precioUnitario, BigDecimal descuentoAplicado) {
        this.idLibro = idLibro;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuentoAplicado = descuentoAplicado;
    }

  
    public ItemVenta(int idDetalle, int idVenta, int idLibro, int cantidad, BigDecimal precioUnitario, BigDecimal descuentoAplicado, List<ItemVenta> items) {
        this.idDetalle = idDetalle;          
        this.idVenta = idVenta;             
        this.idLibro = idLibro;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuentoAplicado = descuentoAplicado;
        this.items = items;
    }

    // Getters y Setters
    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public void setDescuentoAplicado(BigDecimal descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    public List<ItemVenta> getItems() {
        return items;
    }

    public void setItems(List<ItemVenta> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ItemVenta{" +
                "idDetalle=" + idDetalle +
                ", idVenta=" + idVenta +
                ", idLibro=" + idLibro +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", descuentoAplicado=" + descuentoAplicado +
                '}';
    }
}
