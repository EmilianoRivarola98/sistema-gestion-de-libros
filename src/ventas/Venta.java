package ventas;

import usuarios.EncargadodeVentas;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Venta {
    private int id;
    private Date fecha;
    private List<ItemVenta> itemsVenta = new ArrayList<>(); 
    private EncargadodeVentas encargado;
    private Sucursal sucursal;

    public Venta(int id, Date fecha, EncargadodeVentas encargado, Sucursal sucursal) {
        this.id = id;
        this.fecha = fecha;
        this.encargado = encargado;
        this.sucursal = sucursal;
    }
    
    public int getId() { return id; }
    public Date getFecha() { return fecha; }
    public List<ItemVenta> getItemsVenta() { return itemsVenta; }
    public EncargadodeVentas getEncargado() { return encargado; }
    public Sucursal getSucursal() { return sucursal; };
    
    public void agregarItem(ItemVenta item) {
        this.itemsVenta.add(item);
    }
    
    public BigDecimal calcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        
        for(ItemVenta item : itemsVenta) {
        	total = total.add(item.calcularSubtotal());
        }
        
        return total;
    }

}
